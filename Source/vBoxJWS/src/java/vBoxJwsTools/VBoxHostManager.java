/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vBoxJwsTools;

import java.util.ArrayList;
import java.util.List;
import org.virtualbox_4_2.IMachine;
import org.virtualbox_4_2.ISession;
import org.virtualbox_4_2.VirtualBoxManager;
//import com.google.gson.*;
import java.text.DateFormat;
import java.util.Date;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
import org.virtualbox_4_2.IVirtualBox;

//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

//import org.joda.time.Chronology;
//import org.joda.time.DateTime;
//import org.joda.time.DateTimeZone;
//import org.joda.time.DurationField;
//import org.joda.time.chrono.ISOChronology;
import org.virtualbox_4_2.IAppliance;
import org.virtualbox_4_2.IConsole;
import org.virtualbox_4_2.IProgress;
import org.virtualbox_4_2.IVirtualBoxErrorInfo;
import org.virtualbox_4_2.LockType;
import org.virtualbox_4_2.MachineState;
import org.virtualbox_4_2.SessionState;
import org.virtualbox_4_2.SessionType;
import org.virtualbox_4_2.VBoxException;


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

// when running VBoxWebSrv, run with -H (ip Address) By default it will bind to 127.0.0.1 and not the LAN Ip

public class VBoxHostManager  implements IDeviceManager
{
    private ArrayList ManagedHostsList= null;
    private ArrayList scheduleList = null;
    SQL sql = null;
    public ScheduleManager scheduleManager = null;
    long schedulerTimeToExecute = 0;
    long schedulerDelayToExecution = 0;
    
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
    public VBoxHostManager(SQL _sql, ScheduleManager _scheduleManager)
    {
         ManagedHostsList= new ArrayList();
         scheduleList = new ArrayList();
         sql = _sql;
         scheduleManager = _scheduleManager;
         
        //1 populate host list
        //2 within each host populate machine list
        //3 within each machine populate schedules
        populateManagedHostsListFromDB();
        //2 Discover from these details what machines were found/ not found
    }
    
    public int getIDeviceHostId(IDevice _iDevice)
    {
      return getIDeviceHost(_iDevice).getIDeviceHostId();
    }

    public String getIDeviceHostURL(IDevice _iDevice)
    {
         return getIDeviceHost(_iDevice).getIDeviceHostURL();
    }
    
    @Override
    public void deleteBackupPath(String _backupPathId)
    {
        sql.deleteBackupPath(_backupPathId);
    }
     
   public String getIDeviceHostName(IDevice _iDevice)
   {
       return getIDeviceHost(_iDevice).getIDeviceHostName();
   }
    
    @Override
    public DeviceProgress exportIDevice(IDevice _iDevice, String _optionOne)
    {
       DeviceProgress  DeviceProgress = null;
       return DeviceProgress; 
    }
    
    @Override
    public String getIDeviceState(IDevice _iDevice)
    {
        IMachine tempImachine = ((ManagedMachine)_iDevice).getImachine();

        if(tempImachine != null)
        {
           return tempImachine.getState().toString();
        }
        else
        {
           return "iMachine State Not Found";
        }
    }
     
    @Override
    public void addBackupPath(String _backupPath)
    {
        sql.addBackupPath(_backupPath);
    }
    
    @Override
    public ArrayList getBackupPaths()
    {
        return sql.getBackupPaths();
    }
     
    @Override
    public ArrayList getPersistantIDevicesForHostId(int _ManagedBoxHostId)
    {
        return sql.getPersistantIDevicesForHostId(_ManagedBoxHostId);
    }  
     
    @Override
    public void loadIDeviceSchedules(IDevice _transientIDevice, boolean _activateSchedules)
    {
        scheduleManager.loadMachineSchedules(_transientIDevice,_activateSchedules);
    }
   
    @Override
    public IDeviceProgress startIDevice(IDevice _transientIDevice, int _pauseTillCheckSeconds) 
    {
       return getIDeviceHost(_transientIDevice).startIDevice(_transientIDevice,_pauseTillCheckSeconds);
    } 
     
    @Override
    public IDeviceProgress stopIDevice(IDevice _transientIDevice,int _pauseTillCheckSeconds)
    {
       return getIDeviceHost(_transientIDevice).stopIDevice(_transientIDevice,_pauseTillCheckSeconds);
    }
     
    public void addManagedBoxHost(int _managedBoxHostId, String _hostURL , String _hostAlternateName, String _hostUserName , String _hostPassword)
    {
        ManagedBoxHost tempManagedBoxHost = new ManagedBoxHost( this, _managedBoxHostId,_hostURL ,_hostAlternateName, _hostUserName , _hostPassword);
        tempManagedBoxHost.populateManagedIMachinesList(true); 
        ManagedHostsList.add(tempManagedBoxHost);
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
    //good to do before rebuilding a serverlist
    public void clearManagedLists()
    {
        clearManagedHostList();
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
    public void clearManagedHostList()
    {
        ManagedHostsList.clear();
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
    public ArrayList getManagedBoxHostsList()
    {
        return ManagedHostsList;
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
    public void activateSchedule(DeviceSchedule _machineSchedule)
    {
       scheduleManager.activateSchedule(_machineSchedule);
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
    public void activateManagedMachineSchedules()
    {
        //get a list of all the schedules
        ManagedBoxHost tempManagedBoxHost = null;
        ArrayList tempManagedMachineList = null;
        
        for(Object ManagedHostObject : ManagedHostsList)
        {
            tempManagedBoxHost = (ManagedBoxHost)ManagedHostObject;
            tempManagedMachineList =  tempManagedBoxHost.managedMachines; 
            
            scheduleManager.activateSchedules(tempManagedMachineList);
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
    
    public IDeviceHost getIDeviceHost(IDevice _iDevice)
    {
        ManagedMachine tempManagedMachine = null;
        ManagedBoxHost tempManagedBoxHost = null;
        
        for(Object ManagedHostObject : ManagedHostsList)
        {
            tempManagedBoxHost = (ManagedBoxHost)ManagedHostObject;
            
            tempManagedMachine =  tempManagedBoxHost.getIDeviceByName(_iDevice.getIDeviceName());
            
            if(tempManagedMachine != null)
            {
                    return tempManagedBoxHost;
            }
       }
       
        return null;
    }
            
    public IDevice getIDeviceByName(String _name)
    {
        ManagedMachine tempManagedMachine = null;
        ManagedBoxHost tempManagedBoxHost = null;
        
        for(Object ManagedHostObject : ManagedHostsList)
        {
            tempManagedBoxHost = (ManagedBoxHost)ManagedHostObject;
            
            tempManagedMachine =  tempManagedBoxHost.getIDeviceByName(_name);
            
            if(tempManagedMachine != null)
            {
                    return tempManagedMachine;
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
    
    
 
    public ArrayList getIDevices()
    {
        ArrayList tempArrayList = new ArrayList();
        ManagedBoxHost tempManagedBoxHost = null;
        ArrayList tempManagedMachineList = null;
        
        for(Object ManagedHostObject : ManagedHostsList)
        {
            tempManagedBoxHost = (ManagedBoxHost)ManagedHostObject;
            tempManagedMachineList =  tempManagedBoxHost.managedMachines; 
            tempArrayList.addAll(tempManagedMachineList );
        }
        
        return tempArrayList;
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
 * 
 * 
 */
    @Override
    public void insertIDeviceHost(String _hostURL, String  _hostAlternateName, String  _hostUserName, String  _hostPassword) 
    {
        sql.insertManagedBoxHost(_hostURL, _hostAlternateName, _hostUserName, _hostPassword);
        refreshManagedMachines();
    }

    @Override
    public void deleteIDeviceHost(String _hostURL)
    {
      sql.deleteHost(_hostURL);
      refreshManagedMachines();
    }
    
    private void populateManagedHostsListFromDB()
    {
        //will get all records from DB and then connect to the hosts, creating objects for each machine found.
        sql.populateManagedBoxHostList(this,scheduleManager);
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
    public void refreshManagedMachines() 
    {
        clearManagedLists();
        populateManagedHostsListFromDB();
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
   
    
public class ManagedBoxHost implements IDeviceHost
{
    public int managedBoxHostId = 0;
    public String hostUrl = "";
    public String hostName = "";
    public String user = "";
    public String password = "";
    private IVirtualBox iVirtualBox = null;
    public transient  VBoxHostManager vBoxHostManager = null;
    public ArrayList managedMachines = new ArrayList();

    VirtualBoxManager virtualBoxManager = null;

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
    private ManagedBoxHost( VBoxHostManager _vBoxHostManager, int _managedBoxHostId, String _hostUrl,String _hostName, String _user, String _password)
    {
        //NOTE
        // when running VBoxWebSrv, run with -H (ip Address) By default it will bind to 127.0.0.1 and not the LAN Ip

        //will connect and find all virtual machines
        managedBoxHostId = _managedBoxHostId;
        hostUrl = _hostUrl;
        hostName = _hostName;
        user = _user ;
        password = _password;
        setup();//vBoxJWS setup
        vBoxHostManager = _vBoxHostManager;
        managedMachines =  getPersistantIDevicesForHostId(_managedBoxHostId);//load initial list of machines previously saved to the DB
    }
      
    public ArrayList<IDevice> getPersistantIDevicesForHostId(int _ManagedBoxHostId)
    {
        ArrayList persistantManagedMachines = sql.getPersistantIDevicesForHostId(_ManagedBoxHostId); 

        for (Object ManagedMachineObject : persistantManagedMachines)
        {
            ManagedMachine managedMachine = (ManagedMachine)ManagedMachineObject; 
            managedMachine.setIDeviceHost(this);
            managedMachine.setScheduleManager(scheduleManager);
        }

        populateManagedIMachinesList(true);         
        return persistantManagedMachines;
    }
       
    public int persistIDevice(IDevice _iDevice)
    {
        return sql.insertManagedMachine(_iDevice);
    }

    public String   getIDeviceHostName()
    {
        return hostName;
    }

    public int      getIDeviceHostId()
    {
        return managedBoxHostId;
    }

    public String   getIDeviceHostURL()
    {
        return hostUrl;
    }
        
       
        
    public DeviceProgress exportIDevice(IDevice _transientIDevice, String _filePath)
    {
        ManagedMachine tempManagedMachine = (ManagedMachine)_transientIDevice;//getManagedMachineForName(_machineName,false);//Get a fresh Managed Machine with new IObjects
        IVirtualBox iVirtualBox = getIVirtualBox();

        IAppliance iAppliance = iVirtualBox.createAppliance();
        int tempResultCode = 0;
        String description = "";
        String operationDescription = "";
        String errorInfo = "";
        IVirtualBoxErrorInfo iVirtualBoxErrorInfo = null;
        IProgress prog = null;
        IMachine tempImachine = tempManagedMachine.getImachine(); 
        if(tempImachine!= null)
        {
            tempImachine.getName();

            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
            String dateTimeString = dateFormat.format(new Date());
            String filePath = _filePath + dateTimeString + " "+tempImachine.getName()+".ova";
            filePath = filePath.replace('/', '-');
            filePath = filePath.replace(':', '-');
            filePath = filePath.replace(' ', '_');

            System.out.println("filePath: " + filePath );
            tempImachine.export(iAppliance, filePath ); 
            prog = iAppliance.write("ovf-2.0", Boolean.TRUE, filePath);

           if(prog.getCompleted())
           {
                tempResultCode = prog.getResultCode();
                description = prog.getDescription();

                iVirtualBoxErrorInfo = prog.getErrorInfo();
                errorInfo = iVirtualBoxErrorInfo.getText();
           }

            return new DeviceProgress(prog);
        }
        else
        {
            return null;
        }
    }
        
    public DeviceProgress startIDevice(IDevice _transientIDevice, int _pauseTillCheckSeconds)
    {

        ManagedMachine tempManagedMachine = (ManagedMachine)_transientIDevice;
        SessionType sessionType = null;
        SessionState sessionState = null;
        ISession tempISession = null;
        IProgress prog = null;
        IConsole tempIConsole = null;

        VirtualBoxManager tempVirtualBoxManager = null;

        try
        {
           tempISession = getISession(tempManagedMachine.iMachine);

            if(tempISession != null)
            {
                tempIConsole = getIConsole(tempISession, tempManagedMachine.getImachine());
                if(tempIConsole == null)
                {
                    return null;
                }
                tempManagedMachine.machineState = tempIConsole.getState();

                if( tempManagedMachine.machineState != tempManagedMachine.machineState.Running)
                {
                    sessionState = tempISession.getState();
                    int sesionStateInt = sessionState.value();
                    if(sesionStateInt == 2 )//Locked
                    {
                        tempISession.unlockMachine();
                    }

                    IMachine tempIMachine = tempManagedMachine.getIMachine();

                    try
                    {
                         prog  = tempIMachine.launchVMProcess(tempISession,"gui",null );
                    }
                    catch (VBoxException e)
                    {
                        Logger.getLogger(vBoxJwsTools.VBoxHostManager.ManagedBoxHost.class.getName()).log(Level.SEVERE,"EXCEPTION startManagedMachine: {0}" + e.toString());
                    }

                    prog.waitForCompletion(_pauseTillCheckSeconds * 1000); // give the process 10 secs

                    if (prog.getResultCode() != 0) // check success
                    {
                        Logger.getLogger(vBoxJwsTools.VBoxHostManager.ManagedBoxHost.class.getName()).log(Level.INFO,"ERROR: ManagedMachine::start failed on iMachine.launchVMProcess IProgress:ResultCode: " + prog.getResultCode());
                    }
                    else
                    {
                        Logger.getLogger(vBoxJwsTools.VBoxHostManager.ManagedBoxHost.class.getName()).log(Level.INFO,"ManagedMachine::start successful on iMachine.launchVMProcess IProgress:ResultCode: " + prog.getResultCode());
                    }

                    sessionState = tempISession.getState();
                    sesionStateInt = sessionState.value();

                    if(sesionStateInt == 2 )//Locked
                    {
                        tempISession.unlockMachine();
                    }
                    return new DeviceProgress(prog);
                }
                else
                {
                    Logger.getLogger(vBoxJwsTools.VBoxHostManager.ManagedBoxHost.class.getName()).log(Level.INFO,"ManagedMachine::start failed because the Machine State was already Running");
                }
            }
            else //tempISession == null
            {
                System.out.println();
                Logger.getLogger(vBoxJwsTools.VBoxHostManager.ManagedBoxHost.class.getName()).log(Level.INFO,"ManagedMachine::start failed because the Machine State not unlocked");
            }

        }
        catch (VBoxException e)
        {
            Logger.getLogger(vBoxJwsTools.VBoxHostManager.ManagedBoxHost.class.getName()).log(Level.SEVERE,"EXCEPTION startManagedMachine: " + e.toString());
        }
        finally
        {
            sessionState = tempISession.getState();

            int sesionStateInt = sessionState.value();
            if(sesionStateInt == 2 )//Locked
            {   
                tempISession.unlockMachine();
            }
       }

        return null;
    }
        
       
    public DeviceProgress stopIDevice(IDevice _transientIDevice, int _pauseTillCheckSeconds)
    {
         ManagedMachine tempManagedMachine = (ManagedMachine)_transientIDevice;

        SessionType sessionType = null;
        ISession tempISession = null;
        SessionState sessionState = null;
        IConsole tempIConsole = null;
        IProgress prog = null;

        ManagedMachine tempManagedMAchine = null;

       try
       {
            tempISession = getISession(tempManagedMachine.iMachine);

            if(tempISession != null)
            {
                tempIConsole = getIConsole(tempISession, tempManagedMachine.getImachine());
                if(tempIConsole == null)
                {
                    return null;
                }

                tempManagedMachine.machineState = tempIConsole.getState();

                if( tempManagedMachine.machineState == tempManagedMachine.machineState.Running)
                {
                    sessionState = tempISession.getState();

                    int sesionStateInt = sessionState.value();
                    if(sesionStateInt == 2 )//Locked
                    {
                        tempISession.unlockMachine();// it is recommended to unlock all machines explicitly before terminating the application
                    }   

                    Logger.getLogger(vBoxJwsTools.VBoxHostManager.ManagedBoxHost.class.getName()).log(Level.INFO,"Power Down called on machine. waiting 5 seconds until status check.");
                    prog = tempIConsole.powerDown();
                    prog.waitForCompletion(_pauseTillCheckSeconds * 5000); // give the process 10 secs

                    if (prog.getResultCode() != 0) // check success
                    {
                        Logger.getLogger(vBoxJwsTools.VBoxHostManager.ManagedBoxHost.class.getName()).log(Level.INFO,"ERROR: ManagedMachine::stop failed on iConsole.powerDown(); IProgress:ResultCode: " + prog.getResultCode());
                    }
                    else
                    {
                        Logger.getLogger(vBoxJwsTools.VBoxHostManager.ManagedBoxHost.class.getName()).log(Level.INFO,"ManagedMachine::stop successful on iConsole.powerDown(); IProgress:ResultCode: " + prog.getResultCode());
                    }

                    sessionState = tempISession.getState();
                    sesionStateInt = sessionState.value();

                    if(sesionStateInt == 2 )//Locked
                    {
                        tempISession.unlockMachine();
                    }

                    return new DeviceProgress(prog);
                }
                else
                {
                    Logger.getLogger(vBoxJwsTools.VBoxHostManager.ManagedBoxHost.class.getName()).log(Level.INFO,"ManagedMachine::stop failed because the Machine State was not Running");
                }
            }
            else
            {
                Logger.getLogger(vBoxJwsTools.VBoxHostManager.ManagedBoxHost.class.getName()).log(Level.INFO,"ManagedMachine::stop failed because the Machine State was not unlocked");
            }
       }
        catch (VBoxException e)
        {
            Logger.getLogger(vBoxJwsTools.VBoxHostManager.ManagedBoxHost.class.getName()).log(Level.SEVERE,"EXCEPTION stopManagedMachine: " + e.getCause());
        }
        finally
        {
            sessionState = tempISession.getState();
            int sesionStateInt = sessionState.value();

            if(sesionStateInt == 2 )//Locked
            {
                sessionType = tempISession.getType();
                tempISession.unlockMachine();
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
    private void setup()
    {
        if(iVirtualBox == null)
        {
            if(virtualBoxManager == null)
            {
                virtualBoxManager = VirtualBoxManager.createInstance(null);
                connectHost(virtualBoxManager);//unless a condition can be checked, or possibly breaking other connections
            }

            iVirtualBox = virtualBoxManager.getVBox(); 
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
    private IVirtualBox getIVirtualBox()
    {
        return iVirtualBox;
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
    private boolean connectHost(VirtualBoxManager _virtualBoxManager)
    {
        try 
        {
            _virtualBoxManager.connect(hostUrl, user, password);
            Logger.getLogger(vBoxJwsTools.VBoxHostManager.ManagedBoxHost.class.getName()).log(Level.CONFIG,"Connected to " + hostName);
            return true;
        } 
        catch (VBoxException e) 
        {
            Logger.getLogger(vBoxJwsTools.VBoxHostManager.ManagedBoxHost.class.getName()).log(Level.CONFIG,"Cannot connect to "+hostUrl+", start webserver VBoxWebSrv -H <ip Address>");
            _virtualBoxManager.cleanup();
            return false;
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
    private ISession getISession(IMachine _imachine)
    {
        _imachine = getIMachineByName(_imachine.getName());
        ISession tempISession = null;

        try 
        {
            SessionState sessionState = _imachine.getSessionState();
            int sesionStateInt = sessionState.value();
            if(sesionStateInt == 2 )//Locked
            {
                 tempISession = virtualBoxManager.getSessionObject();
                Logger.getLogger(vBoxJwsTools.VBoxHostManager.ManagedBoxHost.class.getName()).log(Level.FINE,"ManagedMachine::getISession returned a Locked SessionState");
            }

            if(SessionState.Unlocked == sessionState) 
            {
                tempISession = virtualBoxManager.openMachineSession(_imachine);
                Logger.getLogger(vBoxJwsTools.VBoxHostManager.ManagedBoxHost.class.getName()).log(Level.FINE,"ManagedMachine::getISession returned a Unlocked SessionState");
            }
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(vBoxJwsTools.VBoxHostManager.ManagedBoxHost.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tempISession;
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
    private void startManagedIMachineByName(String _machineName,boolean _activateSchedules)
    {
        ManagedMachine ManagedMachine = getManagedMachineForName(_machineName,_activateSchedules);
        ManagedMachine.performTask("Start",10);
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
    private IMachine getIMachineByName(String _name)
    {
        IVirtualBox tempIVirtualBox = getIVirtualBox();
        List<IMachine> machs = tempIVirtualBox.getMachines();

        for (IMachine iMachine : machs)
        {
            if(iMachine.getName().equals(_name))
            {
                return iMachine;
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
      public String getHostURL()
      {
          return hostUrl;
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
    private void populateManagedIMachinesList(boolean _activateSchedules)//Does not start the machines
    {
        SessionState sessionState = null;
        MachineState machineState = null;

        IVirtualBox iVirtualBox = getIVirtualBox();

        if(iVirtualBox != null)
        {
            List<IMachine> iMachineList = iVirtualBox.getMachines();

            for (IMachine iMachine : iMachineList)
            {
                ManagedMachine tempManagedMachine = getManagedMachine(iMachine, VBoxHostManager.this, iVirtualBox, _activateSchedules);

                Logger.getLogger(vBoxJwsTools.VBoxHostManager.ManagedBoxHost.class.getName()).log(Level.CONFIG,hostName + " addedd '"+ tempManagedMachine.iDeviceName  +  " (" + hostUrl + ")");
            }
        }
        else
        {
            Logger.getLogger(vBoxJwsTools.VBoxHostManager.ManagedBoxHost.class.getName()).log(Level.CONFIG,"iVirtualBox in BoxHost Failed. Is VBoxWebSrv Running and setup correctly on the host "+ hostName +"("+ hostUrl +") ?");
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
    private ArrayList getIDevices(boolean _activateSchedules )
    {
            Logger.getLogger(vBoxJwsTools.VBoxHostManager.ManagedBoxHost.class.getName()).log(Level.INFO,"Rebuilding Managed machine list for: " + hostName);
            //will return a list of managed machines with current IObjects
            IVirtualBox iVirtualBox = getIVirtualBox();
            ManagedMachine ManagedMachineCreated = null;

            //this method will add actual machines
            if(iVirtualBox != null)
            {
                List<IMachine> iMachineList = iVirtualBox.getMachines();

                for (IMachine iMachine : iMachineList)
                {
                    getManagedMachine(iMachine, VBoxHostManager.this, iVirtualBox, _activateSchedules);
                }
            }

            return managedMachines;

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
    private ManagedMachine getManagedMachineForName(String _machineName,boolean _activateSchedules)//Does not start the machines
    {
        //searches the network for an IMachine of the same name and returns a Managed machine, newly refreshed with the iObjects.
        //the iObjects become invalid if held for a period of time so this will refreshing
        IVirtualBox tempIVirtualBox  = getIVirtualBox();
        ManagedMachine transientIDevice= null;

        if(tempIVirtualBox  != null)
        {
            List<IMachine> iMachineList = tempIVirtualBox .getMachines();

            for (IMachine iMachine : iMachineList)
            {
                String iMachineName = iMachine.getName();

                if(iMachineName.equals(_machineName))
                {
                    transientIDevice = getManagedMachine(iMachine, VBoxHostManager.this, tempIVirtualBox ,_activateSchedules);
                    Logger.getLogger(vBoxJwsTools.VBoxHostManager.ManagedBoxHost.class.getName()).log(Level.INFO,hostName + " addedd '"+iMachine.getName()+  " (" + hostUrl + ")");
                }
            }
        }
        else
        {
            Logger.getLogger(vBoxJwsTools.VBoxHostManager.ManagedBoxHost.class.getName()).log(Level.CONFIG,"iVirtualBox in BoxHost Failed. Is VBoxWebSrv Running and setup correctly on the host "+ hostName +"("+ hostUrl +") ?");
        }

        return transientIDevice;
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
    private IDevice populateManagedMachineFromDB(IDevice _transientIDevice, boolean _activateSchedules)
    {
        //this method is to get machines frm the DB
        //for each machine found on the network, update the network object with machine ID
        //for machines not found, add to list

        String tempManagedMachineName = null;
        ManagedMachine tempPersistantIDevice = null;
        ManagedMachine tempManagedMachineOnNetwork = null;

        ArrayList managedMachineNameListDB  = vBoxHostManager.getPersistantIDevicesForHostId(this.managedBoxHostId);

        for(Object ManagedMachineObject: managedMachineNameListDB )
        {
            tempPersistantIDevice = (ManagedMachine)ManagedMachineObject;

            if(_transientIDevice.getIDeviceName().equals(tempPersistantIDevice.iDeviceName))
            {
                _transientIDevice.setIDeviceId(tempPersistantIDevice.iDeviceId);

                vBoxHostManager.loadIDeviceSchedules(_transientIDevice, _activateSchedules);
            }
        }

        return _transientIDevice;
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
        //getManagedMachine needs to be an inteface method of vBoxHostMnanger, then calling this method
        //private ManagedMachine getManagedMachine(IMachine _iMachine,  vBoxJwsTools.VBoxHostManager.ManagedBoxHost  _ManagedBoxHost,  IVirtualBox  _iVirtualBox,boolean _activateSchedules)
    private ManagedMachine getManagedMachine(IMachine _iMachine,  IDeviceManager  _iDeviceManager,  IVirtualBox  _iVirtualBox,boolean _activateSchedules)
    {
        //Returns a Managed machine object populated with the iObjects, if there is a persistant Managed machine of the same name, those details will also be populated in th ManagedMachine.
        ManagedMachine transientIDevice = null;
        String tempMachineName = _iMachine.getName();
        Boolean machineIsAlreadyManaged = false;

        for(Object ManagedMachineObject : managedMachines )
        {
            //find this existing machine 
            if(((ManagedMachine)ManagedMachineObject).getIDeviceName().equals(tempMachineName))
            {
                Logger.getLogger(vBoxJwsTools.VBoxHostManager.ManagedBoxHost.class.getName()).log(Level.INFO, "A Managed machine matched to a iMachine: {0}", tempMachineName);

                //add the iMachine if it is found
                machineIsAlreadyManaged = true;
                ((ManagedMachine)ManagedMachineObject).setIMachine(_iMachine);
                ((ManagedMachine)ManagedMachineObject).setIVirtualBox(_iVirtualBox);
                transientIDevice = (ManagedMachine)ManagedMachineObject;

                //now populate it with persistant details from DB
                 transientIDevice = (ManagedMachine)populateManagedMachineFromDB(transientIDevice,_activateSchedules);
            }
        }

        if(!machineIsAlreadyManaged)//will add it to the list but not the DB
        {
            Logger.getLogger(vBoxJwsTools.VBoxHostManager.ManagedBoxHost.class.getName()).log(Level.INFO, "A new iMachine found and added: {0}", tempMachineName);
            int iDeviceId = 0;
            transientIDevice = new ManagedMachine(_iMachine, iDeviceId , tempMachineName, this);
            transientIDevice.setIMachine(_iMachine);
            transientIDevice.setIVirtualBox(_iVirtualBox);
            managedMachines.add(transientIDevice);
        }

        return transientIDevice;
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
        private IConsole getIConsole(ISession _iSession, IMachine _iMachine)
        {
            IConsole tempIConsole = null;
            SessionType tempSessionType = null;
            SessionState sessionState = _iSession.getState(); 
            int sesionStateInt = sessionState.value();

            if(sesionStateInt == 2 )//Locked
            {
                 tempSessionType = _iSession.getType();
                _iSession.unlockMachine();
            }

            try
            {
              _iMachine.lockMachine(_iSession, LockType.Shared);//the API eventually obtains a Write lock
            }
            catch (VBoxException e)
            {
                Logger.getLogger(vBoxJwsTools.VBoxHostManager.ManagedBoxHost.class.getName()).log(Level.SEVERE,"EXCEPTION ManagedBoxHost:getIConsole " + e.toString());
            }

            sessionState = _iSession.getState();

            if( sessionState != sessionState.Unlocked)//the machine needs to be running to get the following, and must be locked
            {
                sesionStateInt = sessionState.value();
                if(sesionStateInt == 2 )//Locked
                {
                   tempSessionType = _iSession.getType();

                    try
                    {
                        tempIConsole  = _iSession.getConsole();
                    }
                    catch (VBoxException e)
                    {
                        Logger.getLogger(vBoxJwsTools.VBoxHostManager.ManagedBoxHost.class.getName()).log(Level.SEVERE,"EXCEPTION ManagedBoxHost:getIConsole " + e.toString());
                    }
                }
            }
            else
            {
                Logger.getLogger(vBoxJwsTools.VBoxHostManager.ManagedBoxHost.class.getName()).log(Level.FINE,"ManagedMachine::obtainIConsole failed because the Session State was Unlocked");
            }

            return tempIConsole;
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
        
        public ManagedMachine getIDeviceByName(String _name)
        {
            ManagedMachine  tempManagedMachine = null;

            for(Object ManagedMachineObject : managedMachines)
            {
                tempManagedMachine =  (ManagedMachine)ManagedMachineObject;

                if(tempManagedMachine.getIDeviceName().equals( _name ))
                {
                   tempManagedMachine.refreshDetails();
                    return tempManagedMachine;
                }
            }

            return tempManagedMachine;
        }
    }
}
