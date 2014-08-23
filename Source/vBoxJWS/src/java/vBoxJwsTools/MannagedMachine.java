
package vBoxJwsTools;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;

import org.virtualbox_4_2.*;

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
public class MannagedMachine implements IDevice  
{
    int iDeviceId = 0;
    public String iDeviceName = null;
    public ArrayList progresses = new ArrayList();
    public ArrayList iDeviceSchedules = null;//new ArrayList();
    public String mannagedBoxHostURL = "";
    public String mannagedBoxHostName = "";
    public String iDeviceState = "";
    public String mannagedBoxRam = "";
    public transient IMachine iMachine = null;
    public MachineState machineState = null;
    private transient IDeviceHost iDeviceHost = null;
    //private transient IDeviceManager iDeviceManager = null;
    public transient DateTime taskSession = null;
    public transient IVirtualBox iVirtualBox = null;
    private transient ScheduleManager scheduleManager = null;
    
    
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
    //public MannagedMachine(SQL _sql,int _iDeviceId, String _iDeviceName, MannagedBoxHost  _mannagedBoxHost)
    //public MannagedMachine(int _iDeviceId, String _iDeviceName, IDeviceManager _iDeviceManager, ScheduleManager _scheduleManager )
    public MannagedMachine(int _iDeviceId, String _iDeviceName, IDeviceHost _iDeviceHost, ScheduleManager _scheduleManager )
    {
        iDeviceId = _iDeviceId;
        iDeviceName= _iDeviceName;
        iDeviceHost = _iDeviceHost;
        scheduleManager = _scheduleManager;
        iDeviceSchedules = new ArrayList();
        mannagedBoxHostURL = getHostURL();
        mannagedBoxHostName = getIDeviceName();
        iDeviceState = getIDeviceState();
        mannagedBoxRam = getRAM();
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
    public MannagedMachine(IMachine _iMachine,int _iDeviceId, String _iDeviceName, IDeviceHost _iDeviceHost)
    {
        iDeviceId = _iDeviceId;
        iDeviceName= _iDeviceName;
        iMachine = _iMachine;
        iDeviceHost = _iDeviceHost;
        iDeviceSchedules = new ArrayList();
         
        if(_iDeviceHost != null)
        {
            mannagedBoxHostURL = _iDeviceHost.getIDeviceHostURL();
            mannagedBoxHostName = _iDeviceHost.getIDeviceHostName();
        }
        
        iDeviceState = getIDeviceState();
        mannagedBoxRam = getRAM();
    }
    
    public void refreshDetails()
    {
        mannagedBoxHostURL = getHostURL();
        mannagedBoxHostName = getIDeviceHostName();
        iDeviceState = getIDeviceState();
        mannagedBoxRam = getRAM();
    }
    
    public void setIDeviceHost(IDeviceHost _iDeviceHost)
    {
         iDeviceHost = _iDeviceHost;
         
         if(iDeviceHost != null)
        {
            mannagedBoxHostURL = iDeviceHost.getIDeviceHostURL();
            mannagedBoxHostName = iDeviceHost.getIDeviceHostName();
        }
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
    
    public ArrayList<DeviceSchedule> getIDeviceSchedules()
    {
        return iDeviceSchedules;
    }

    public IMachine getIMachine()
    {
        return iMachine;
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
    public void ensureIDevicePersistant()
    {
        //saves this machine to the DB if not alrady
        
        if(iDeviceId == 0)//This ManagedMachine was created from the network, not from the DB
        {
            iDeviceId = iDeviceHost.persistIDevice(this);
            Logger.getLogger(MannagedMachine.class.getName()).log(Level.FINE,"ManagedMachine::ensureIDevicePersistant() - Network machine made persistant: "+iDeviceName);
        }
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
    public boolean startIDeviceSession(DateTime _taskSession)
    {
        if(taskSession == null)
        {
            taskSession = _taskSession;
            Logger.getLogger(MannagedMachine.class.getName()).log(Level.FINE,"MannagedMachine: startIDeviceSession approved >>>>>>>>> : " + _taskSession.toString());
            return true;
        }
        
        if (taskSession == _taskSession)
        {
            Logger.getLogger(MannagedMachine.class.getName()).log(Level.FINE,"MannagedMachine: startIDeviceSession again ???????????? : " + _taskSession.toString());
        }
        
        Logger.getLogger(MannagedMachine.class.getName()).log(Level.FINE,"MannagedMachine: startIDeviceSession rejected >->->->-> : " + taskSession.toString());
        Logger.getLogger(MannagedMachine.class.getName()).log(Level.FINE,"MannagedMachine: startIDeviceSession rejected >->->->-> : " + _taskSession.toString() );
        return false;
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
    public boolean endIDeviceSession(DateTime _taskSession)
    {
        if(taskSession == _taskSession)
        {
            taskSession = null;
            Logger.getLogger("MannagedMachine: endIDeviceSession approved <<<<<<<<<<< : " + _taskSession.toString());
            return true;
        }
        
        if(taskSession == null)
        {
            Logger.getLogger(MannagedMachine.class.getName()).log(Level.FINE,"MannagedMachine: endIDeviceSession Again ?????????????? : " + _taskSession.toString());
        }
        
        Logger.getLogger(MannagedMachine.class.getName()).log(Level.FINE,"MannagedMachine: endIDeviceSession rejected <-<-<-<-<-< : " + _taskSession.toString());
        return false;
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
    public IDeviceProgress performTask(String _task,int _pauseTillCheckSeconds)
    {
        return performTask( _task,_pauseTillCheckSeconds,null,null);
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
    public IDeviceProgress Export(String _task,String _optionOne)
    {
        return performTask(_task,0,_optionOne, null);
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
    public IDeviceProgress performTask(String _task,int _pauseTillCheckSeconds, DateTime _taskSession)
    {
        return performTask( _task,_pauseTillCheckSeconds,null,_taskSession);
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
    public IDeviceProgress performTask(String _task,String _optionOne, DateTime _taskSession)
    {
        return performTask(_task,0,_optionOne,_taskSession);
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
    public IDeviceProgress performTask(String _task,int _pauseTillCheckSeconds,String _optionOne, DateTime _taskSession)
    {
        if(taskSession != null)//if there is a session, because several events need to be performed
        {
            if(_taskSession != null)//and its not the session id passesd in (owner of the lock)
            {
                if(taskSession != _taskSession)
                {
                   Logger.getLogger(MannagedMachine.class.getName()).log(Level.FINE,"Machine is in session and the atempted "+_task+" was not performed as part of another Session");
                    return null; 
                }
            }    
            else
            {
                Logger.getLogger(MannagedMachine.class.getName()).log(Level.FINE,"Machine is in session and the atempted "+_task+" was not performed");
                return null;
            }
        }
        
        IDeviceProgress tempDeviceProgress = null;
        
        if(_task.equals("Start"))
        {
            if(!isIDeviceProcessingTasks())
            {
                tempDeviceProgress = iDeviceHost.startIDevice(this,_pauseTillCheckSeconds);
                
                if(tempDeviceProgress != null)
                {
                        this.progresses.add(tempDeviceProgress );
                }
            }
            else
            {
               Logger.getLogger(MannagedMachine.class.getName()).log(Level.FINE,"Could not Start Mannaged Machine as was locked");
            }

            return tempDeviceProgress;
        }
        else if(_task.equals("Stop"))
        {
            
            if(!isIDeviceProcessingTasks())
            {
                tempDeviceProgress = iDeviceHost.stopIDevice(this,_pauseTillCheckSeconds);
                
                if(tempDeviceProgress != null)
                {
                        this.progresses.add(tempDeviceProgress ); 
                }
            }
            else
            {
                Logger.getLogger(MannagedMachine.class.getName()).log(Level.FINE,"Could not Stop Mannaged Machine as was locked");
            }

            return tempDeviceProgress;
            
        }
        else if(_task.equals("Export"))
        {
            if(!isIDeviceProcessingTasks())
            {
                tempDeviceProgress = iDeviceHost.exportIDevice(this, _optionOne);
                
                if(tempDeviceProgress != null)
                {
                        this.progresses.add(tempDeviceProgress );
                }
            }
            else
            {
               Logger.getLogger(MannagedMachine.class.getName()).log(Level.FINE,"Could not export Mannaged Machine as was locked");
            }

            return tempDeviceProgress;
        }
        
        return null;
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
    public String getGuestOS()
    {
        if(iMachine != null)
        {
            return iMachine.getOSTypeId();
        }
        else
        {
            return "Machine Not Found";
        }
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
    
        
    public String getIDeviceState()
    {
        if(iMachine!= null)
        {
            iDeviceState = iMachine.getState().toString();
            return iDeviceState;
        }
        return "Machine Not Found";
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
    public String getIDeviceName()
    {
        return iDeviceName;
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
    public String getRAM()
    {
        if(iMachine != null)
        {
            mannagedBoxRam = iMachine.getMemorySize().toString();
            return  mannagedBoxRam ;
        }
        else
        {
            return "Machine Not Found";
        }
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
    public void setIMachine(IMachine _iMachine)
    {
        iMachine = _iMachine;
        iDeviceState = getIDeviceState();
        mannagedBoxRam = getRAM();
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
    public void setIVirtualBox(IVirtualBox _iVirtualBox)
    {
        iVirtualBox = _iVirtualBox;
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
    public IMachine getImachine()
    {
        return iMachine;
    }
    
    /*private void setIDeviceManager(IDeviceManager  _iDeviceManager)
    {
        iDeviceManager = _iDeviceManager;
    }*/
    
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
    public String getIDeviceHostName()
    {
        if(iDeviceHost != null)
        {
            mannagedBoxHostName = iDeviceHost.getIDeviceHostName();
        }
        return mannagedBoxHostName; 
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
    public String getHostURL()
    {
        if(iDeviceHost != null)
        {
            mannagedBoxHostURL = iDeviceHost.getIDeviceHostURL();
        }
                
        return mannagedBoxHostURL;
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
    public ArrayList<IDeviceProgress> getUpdatedProgresses()
    {
        //will update the progress values of all within the list of this machine
        int numberOfProgresses = progresses.size();
        
        for(int numberOfProgressesCount = 0; numberOfProgressesCount < numberOfProgresses; numberOfProgressesCount++) 
        {
            ((DeviceProgress)progresses.get(numberOfProgressesCount)).getPercent();
        }
        return progresses;
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
    public boolean isIDeviceProcessingTasks()
    {
        //check that all progress items have finished
        DeviceProgress tempDeviceProgress = null;
        int numberOfProgresses = progresses.size();
        
        for(int numberOfProgressesCount = 0; numberOfProgressesCount < numberOfProgresses; numberOfProgressesCount++) 
        {
            tempDeviceProgress = (DeviceProgress)progresses.get(numberOfProgressesCount);
                    
            if(tempDeviceProgress.getPercent() != 100)
            {
                Logger.getLogger(MannagedMachine.class.getName()).log(Level.FINE,iDeviceName + " is locked with the progress: " + tempDeviceProgress);
                return true;
            }
        }
        
        return false;
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
    public void addIDeviceSchedule(DeviceSchedule _machineSchedule)
    {
       iDeviceSchedules.add(_machineSchedule);
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
    public int getIDeviceId()
    {
        return iDeviceId; 
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
    private DeviceSchedule getMachineScheduleForId(int _machineScheduleId)
    {
        int count = 0;
        
        for(Object machineScheduleObject : iDeviceSchedules )
        {
            DeviceSchedule DeviceSchedule = (DeviceSchedule)machineScheduleObject;
            
            if(_machineScheduleId == DeviceSchedule.scheduleId)
            {
                return DeviceSchedule;
            }
        }
        
        return null;
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
    public void startIDeviceSchedule(int _machineScheduleId)
    {
        getMachineScheduleForId(_machineScheduleId).activateSchedule();
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
    public void cancelIDeviceSchedule(int _machineScheduleId)
    {
        getMachineScheduleForId(_machineScheduleId).cancelSchedule();
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
    public void deleteIDeviceSchedule(int _machineScheduleId)
    {
        //now remove from object collection
        int count = 0;
        
        for(Object machineScheduleObject : iDeviceSchedules )
        {
            DeviceSchedule DeviceSchedule = (DeviceSchedule)machineScheduleObject;
            
            if(_machineScheduleId == DeviceSchedule.scheduleId)
            {
                DeviceSchedule.cancelSchedule();
                DeviceSchedule.deleteMachineSchedule();
                iDeviceSchedules.remove(count);
                break;
            }
            
            count++;
        }
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
    public ArrayList<ScheduleReciept> getScheduleHistory(int _machineScheduleId)
    {
        for(Object machineScheduleObject : iDeviceSchedules )
        {
            DeviceSchedule DeviceSchedule = (DeviceSchedule)machineScheduleObject;
            
            if(_machineScheduleId == DeviceSchedule.scheduleId)
            {
                return DeviceSchedule.scheduleReciepts;
            }
        }
        return null;
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
    public void deleteScheduleHistory(int _machineScheduleId)
    {
        scheduleManager.deleteScheduleHistory(_machineScheduleId);
        
        for(Object machineScheduleObject : iDeviceSchedules )
        {
            DeviceSchedule DeviceSchedule = (DeviceSchedule)machineScheduleObject;
            
            if(_machineScheduleId == DeviceSchedule.scheduleId)
            {
                DeviceSchedule.scheduleReciepts.clear();
            }
        }
    }

    public void setIDeviceId(int _iDeviceId)
    {
        iDeviceId = _iDeviceId; 
    }
    
    public void setScheduleManager(ScheduleManager _scheduleManager)
    {
        scheduleManager = _scheduleManager;
    }
    
    public boolean hasSchedules()
    {
        return !iDeviceSchedules.isEmpty();
    }
    
    public int getIDeviceHostId()
    {
        return iDeviceHost.getIDeviceHostId();
    }
}
