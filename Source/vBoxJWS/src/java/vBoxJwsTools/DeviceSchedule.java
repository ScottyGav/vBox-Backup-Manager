/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vBoxJwsTools;

import java.util.ArrayList;
import java.util.Date;
import org.joda.time.DateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *Description   -no implementation details, EG sql exceptions, implementations required for return types, use case(problem to solve)
 * Class        -What and instance of this class represents, simple clear name
 * Methods      -True Before, True After return,Side effects, split if complicated
 * Parameters   -Form,units, ownership/state space/when legal to use
 * @author ScottyGav
 * @param  name description, simple clear name
 * @return      -Dont return null
 * @throws      -Fail fast
 */
public class DeviceSchedule implements Runnable, IDeviceSchedule
{
    public transient DateTime scheduleDateStamp = null;
    public int scheduleId = 0;
    //public transient IDevice  IDevice  = null;
    public transient IDevice  iDevice  = null;
    public int fkScheduleEventTypeId = 0;
    public int fkScheduleEventFrequencyId = 0;
    public String scheduleEventType = "";
    public String scheduleEventFrequency = "";
    public String scheduleEventStatus = "";
    public transient DateTime startDateTime = null;
    public String startDateTimeString = "";
    public String scheduleConfigText = "";
    public int statusId = 0;
    
    private transient ScheduleManager  scheduleManager = null;
    public transient ScheduledFuture scheduledFuture  = null;
    //private transient SQL sql = null;
    private boolean dieWithServer = true;//will this continue to run if the webserver goes down?
    public transient DateTime taskSessionGlobal = DateTime.now();  
    ArrayList scheduleReciepts = new ArrayList();

    /**
 *Description   -no implementation details, EG sql exceptions, implementations required for return types, use case(problem to solve)
 * Class        -What and instance of this class represents, simple clear name
 * Methods      -True Before, True After return,Side effects, split if complicated
 * Parameters   -Form,units, ownership/state space/when legal to use
 * @author ScottyGav
 * @param  name description, simple clear name
 * @return      -Dont return null
 * @throws      -Fail fast
 */
    public DeviceSchedule(int _machineScheduleId, IDevice  _iDevice , int _fkScheduleEventTypeId, DateTime _startDateTime, int _fkScheduleEventFrequencyId, String _scheduleConfigText, ScheduleManager _scheduleManager, String _scheduleEventType, String _scheduleEventFrequency, ArrayList _scheduleReciepts  )
    {
        //A schedule could come from the BD or from the user 
        scheduleDateStamp = DateTime.now();
        scheduleId = _machineScheduleId;
        iDevice  =  _iDevice ;
        fkScheduleEventTypeId = _fkScheduleEventTypeId;
        startDateTime = _startDateTime;
        startDateTimeString = _startDateTime.toString("dd/MM/yyyy HH:mm");
        fkScheduleEventFrequencyId = _fkScheduleEventFrequencyId;
        scheduleConfigText = _scheduleConfigText;
        scheduleManager = _scheduleManager ;
        scheduleEventType =  _scheduleEventType;
        scheduleEventFrequency = _scheduleEventFrequency; 
        scheduleReciepts = _scheduleReciepts;
        
        getStatus();
    }
    
    /**
 *Description   -no implementation details, EG sql exceptions, implementations required for return types, use case(problem to solve)
 * Class        -What and instance of this class represents, simple clear name
 * Methods      -True Before, True After return,Side effects, split if complicated
 * Parameters   -Form,units, ownership/state space/when legal to use
 * @author ScottyGav
 * @param  name description, simple clear name
 * @return      -Dont return null
 * @throws      -Fail fast
 */
    @Override
    public void run()
    {
        Logger.getLogger(DeviceSchedule.class.getName()).log(Level.INFO,"Scheduled Executor Service Task Running");
        task();
    }
    
    /**
 *Description   -no implementation details, EG sql exceptions, implementations required for return types, use case(problem to solve)
 * Class        -What and instance of this class represents, simple clear name
 * Methods      -True Before, True After return,Side effects, split if complicated
 * Parameters   -Form,units, ownership/state space/when legal to use
 * @author ScottyGav
 * @param  name description, simple clear name
 * @return      -Dont return null
 * @throws      -Fail fast
 */
    public void activateSchedule()
    {
         scheduleManager.activateSchedule(this);
    }
    
    /**
 *Description   -no implementation details, EG sql exceptions, implementations required for return types, use case(problem to solve)
 * Class        -What and instance of this class represents, simple clear name
 * Methods      -True Before, True After return,Side effects, split if complicated
 * Parameters   -Form,units, ownership/state space/when legal to use
 * @author ScottyGav
 * @param  name description, simple clear name
 * @return      -Dont return null
 * @throws      -Fail fast
 */
    public void task() 
    {
        final DateTime taskSession = DateTime.now(); 
        taskSessionGlobal = taskSession;
         
        try
        {    
            Logger.getLogger(DeviceSchedule.class.getName()).log(Level.FINE,"Schedule Event: "+scheduleDateStamp.toString()+", Type: " + scheduleEventType + " schedule Event Frequency: " + scheduleEventFrequency);
          
            if (fkScheduleEventTypeId  == 3)
            {
                System.out.println("Scheduled Task: Shut Down");
                if(iDevice.performTask("Stop",0) != null)
                {
                    scheduleManager.insertScheduleReciept(scheduleId, 2, "Scheduled Task: 'Shut Down' succeeded");
                }
                else
                {
                    scheduleManager.insertScheduleReciept(scheduleId, 1, "Scheduled Task: 'Shut Down' failed");
                }
            }
             else if (fkScheduleEventTypeId  == 4)
            {
                System.out.println();
                Logger.getLogger(DeviceSchedule.class.getName()).log(Level.INFO,"Scheduled Task: Start Up");
                
                if(iDevice.performTask("Start",0) == null)
                {
                    Logger.getLogger(DeviceSchedule.class.getName()).log(Level.INFO,"Scheduled Task: Start Up was not executed as machine already in running state");
                    scheduleManager.insertScheduleReciept(scheduleId, 1, "Scheduled Task: 'Start Up' failed");
                }
                else
                {
                    scheduleManager.insertScheduleReciept(scheduleId, 2, "Scheduled Task: 'Start Up' succeeded");
                }
            }
             else if (fkScheduleEventTypeId  == 2)
            {
                Logger.getLogger(DeviceSchedule.class.getName()).log(Level.INFO,"Scheduled Task: Restart");
                
                if(!iDevice.startIDeviceSession(taskSession))
                {
                    return;//could not begin a session
                }

                final IDeviceProgress  DeviceProgressStop  = iDevice.performTask("Stop",5,taskSession);//send the stop command and get an object to review Progress 

                if(DeviceProgressStop  != null)//Stop was not ran, possibly already stopped or some other problem, skip over the monitoring of the stopping progres
                {
                    Thread threadStop = new Thread()
                      {
                          public void run()
                          {
                              while( DeviceProgressStop.getPercent()!= 100)
                               {
                                  try 
                                  {
                                    Thread.sleep(1000);
                                  }
                                  catch (InterruptedException e)
                                  {
                                      e.printStackTrace();
                                  }
                               }
                              scheduleManager.insertScheduleReciept(scheduleId, 2, "Scheduled Task: 'Restart' succeeded in shutting down.");
                          }
                      };

                     threadStop.start();
                }
                 else
                 {
                     Logger.getLogger(DeviceSchedule.class.getName()).log(Level.INFO,"Stop was not ran");
                     scheduleManager.insertScheduleReciept(scheduleId, 1, "Scheduled Task: 'Restart' failed to stop the machine. It was possibly already shut down.");
                 }

                if(iDevice.performTask("Start",5,taskSession)!= null)
                {
                    scheduleManager.insertScheduleReciept(scheduleId, 2, "Scheduled Task: 'Restart' succeeded in starting up.");
                }
                
                iDevice.endIDeviceSession(taskSession);
            }
             else if (fkScheduleEventTypeId  == 1)
            {
                Logger.getLogger(DeviceSchedule.class.getName()).log(Level.INFO,"Scheduled Task: Backup (Export Allpiance)");
                
                if(iDevice.startIDeviceSession(taskSession))
                {
                    return;
                }

                Thread threadStop = new Thread()
                 {
                     public void run()
                     {
                         //Stop the machine
                         final IDeviceProgress  DeviceProgressStop = iDevice.performTask("Stop",5, taskSession);

                         if(DeviceProgressStop != null)//Stop was not ran, possibly already stopped or some other problem, skip over the monitoring of the stopping progres
                         {
                             while( DeviceProgressStop.getPercent()!= 100)
                             {
                                try 
                                {
                                    Logger.getLogger(DeviceSchedule.class.getName()).log(Level.INFO,"Stop Process at " + DeviceProgressStop.getPercent() + "%");
                                    
                                    Thread.sleep(1000);
                                }
                                catch (InterruptedException e)
                                {
                                    e.printStackTrace();
                                }
                             }

                             Logger.getLogger(DeviceSchedule.class.getName()).log(Level.INFO,"Stop Process at " + DeviceProgressStop.getPercent() + "%");
                             scheduleManager.insertScheduleReciept(scheduleId, 2, "Scheduled Task: 'Backup (Export Allpiance)' succeeded in shutting down.");
                             
                         //Export the machine
                         final IDeviceProgress  DeviceProgressExport = iDevice.performTask("Export",scheduleConfigText,taskSession);

                         if(DeviceProgressExport != null)
                         {
                              while(DeviceProgressExport.getPercent() != 100)
                              {
                                 try 
                                 {
                                     Logger.getLogger(DeviceSchedule.class.getName()).log(Level.INFO,"Export Appliance Process at " + DeviceProgressExport.getPercent()+ "%");
                                     Thread.sleep(5000);
                                 }
                                 catch (InterruptedException e)
                                 {
                                     e.printStackTrace();
                                 }
                              }

                             scheduleManager.insertScheduleReciept(scheduleId, 2, "Scheduled Task: 'Backup (Export Allpiance)' succeeded to export.");     
                             Logger.getLogger(DeviceSchedule.class.getName()).log(Level.INFO,"Export Appliance Process at " + DeviceProgressExport.getPercent()+ "%");
                         }
                          else
                         {
                             Logger.getLogger(DeviceSchedule.class.getName()).log(Level.INFO,"Export Appliance was not ran");
                             scheduleManager.insertScheduleReciept(scheduleId, 1, "Scheduled Task: 'Backup (Export Allpiance)' failed to export.");
                         }

                            //Start the machine
                            if(iDevice.performTask("Start",5,taskSession) != null)
                            {
                                scheduleManager.insertScheduleReciept(scheduleId, 2, "Scheduled Task: 'Backup (Export Allpiance)' succeeded to start.");
                            }
                            else
                            {
                                scheduleManager.insertScheduleReciept(scheduleId, 1, "Scheduled Task: 'Backup (Export Allpiance)' failed to start.");
                            }
                         }
                         else
                         {
                             Logger.getLogger(DeviceSchedule.class.getName()).log(Level.INFO,"Stop was not ran for backup, backup was not run");
                             scheduleManager.insertScheduleReciept(scheduleId, 1, "Scheduled Task: 'Backup (Export Allpiance)' failed to stop the machine. It was possibly already shut down.");
                         }
                    }
                 };

                threadStop.start();
                iDevice.endIDeviceSession(taskSession);
            }
        }
        catch (Exception e)
        {
            Logger.getLogger(DeviceSchedule.class.getName()).log(Level.SEVERE,"Exception: DeviceSchedule:task " + e.toString());
            iDevice.endIDeviceSession(taskSession);
        }
        finally
        {
            
        }
        
        getStatus();
    }
    
    /**
 *Description   -no implementation details, EG sql exceptions, implementations required for return types, use case(problem to solve)
 * Class        -What and instance of this class represents, simple clear name
 * Methods      -True Before, True After return,Side effects, split if complicated
 * Parameters   -Form,units, ownership/state space/when legal to use
 * @author ScottyGav
 * @param  name description, simple clear name
 * @return      -Dont return null
 * @throws      -Fail fast
 */
    public String getStatus()
    {
        String status = "";
        
        if(scheduledFuture == null)
        {
            status += "Not Running ";
        }
        else
        {
            if(isCancelled())
            {
                status += "Canceled ";
            }
            else if(isDone())
            {
                status += "Done ";
            }
            else
            {
                status += "Active ";
            }
        }
        scheduleEventStatus = status;
        return status;
     }
    
    /**
 *Description   -no implementation details, EG sql exceptions, implementations required for return types, use case(problem to solve)
 * Class        -What and instance of this class represents, simple clear name
 * Methods      -True Before, True After return,Side effects, split if complicated
 * Parameters   -Form,units, ownership/state space/when legal to use
 * @author ScottyGav
 * @param  name description, simple clear name
 * @return      -Dont return null
 * @throws      -Fail fast
 */
    public boolean isCancelled()
    {
        if(scheduledFuture != null)
        {
            return scheduledFuture.isCancelled();
        }
        return false;//not sure if it has been cancled.. should get last status from DB, maybe bad idea to rely on that
    }
    
    /**
 *Description   -no implementation details, EG sql exceptions, implementations required for return types, use case(problem to solve)
 * Class        -What and instance of this class represents, simple clear name
 * Methods      -True Before, True After return,Side effects, split if complicated
 * Parameters   -Form,units, ownership/state space/when legal to use
 * @author ScottyGav
 * @param  name description, simple clear name
 * @return      -Dont return null
 * @throws      -Fail fast
 */
    public boolean isDone()
    {
        return scheduledFuture.isDone();
    }
    
    /**
 *Description   -no implementation details, EG sql exceptions, implementations required for return types, use case(problem to solve)
 * Class        -What and instance of this class represents, simple clear name
 * Methods      -True Before, True After return,Side effects, split if complicated
 * Parameters   -Form,units, ownership/state space/when legal to use
 * @author ScottyGav
 * @param  name description, simple clear name
 * @return      -Dont return null
 * @throws      -Fail fast
 */
    public void cancelSchedule()
    {
        if(scheduledFuture != null)//could have been saved in db and not actually recreated as an object yet.
        {
            scheduledFuture.cancel(true);
        }
        
        iDevice.endIDeviceSession(taskSessionGlobal);
    }
    
    public void deleteMachineSchedule()
    {
        scheduleManager.deleteMachineSchedule( iDevice, scheduleId );
    }
    
    public void addReciept(ScheduleReciept _ScheduleReciept)
    {
        scheduleReciepts.add(_ScheduleReciept);
    }
}
