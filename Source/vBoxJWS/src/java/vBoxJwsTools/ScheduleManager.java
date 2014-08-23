/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vBoxJwsTools;

import java.util.ArrayList;
//import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
//import static java.util.concurrent.TimeUnit.*;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import org.joda.time.DateTime;

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
public class ScheduleManager 
{
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(15); 
    private SQL sql = null;
    ArrayList iDeviceSchedules = new ArrayList();
    
    public ScheduleManager(SQL _sql)
    {
        sql = _sql;
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
    public void activateSchedules(ArrayList _iDeviceList)
    {
       IDevice tempIDevice = null;
       DeviceSchedule tempMachineSchedule = null;
       
        for(Object iDeviceObject : _iDeviceList)
        {
            tempIDevice =  (IDevice)iDeviceObject;

            for(Object machineScheduleObject : tempIDevice.getIDeviceSchedules())
            {
                tempMachineSchedule = (DeviceSchedule) machineScheduleObject;
                iDeviceSchedules.add(tempMachineSchedule);
                activateSchedule(tempMachineSchedule);
            }   
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
    public void activateSchedule(DeviceSchedule _machineSchedule)
    {
        if(_machineSchedule.fkScheduleEventFrequencyId == 1)//once off 
        {
           _machineSchedule.scheduledFuture = scheduler.schedule( _machineSchedule, _machineSchedule.startDateTime.getMillis()- System.currentTimeMillis() , TimeUnit.MILLISECONDS);
        }
        else if(_machineSchedule.fkScheduleEventFrequencyId == 2)//Every 12 hours 
        {
            _machineSchedule.scheduledFuture = scheduler.scheduleAtFixedRate(_machineSchedule,_machineSchedule.startDateTime.getMillis()- System.currentTimeMillis(), 12, TimeUnit.HOURS);
        }
        else if(_machineSchedule.fkScheduleEventFrequencyId == 3)//Every 24 hours
        {
            _machineSchedule.scheduledFuture = scheduler.scheduleAtFixedRate(_machineSchedule,_machineSchedule.startDateTime.getMillis()- System.currentTimeMillis(), 24, TimeUnit.HOURS);
        }
        else if(_machineSchedule.fkScheduleEventFrequencyId == 4)//Every 7 days
        {
            _machineSchedule.scheduledFuture = scheduler.scheduleAtFixedRate(_machineSchedule,_machineSchedule.startDateTime.getMillis()- System.currentTimeMillis(), 7, TimeUnit.DAYS);
        }
        else if(_machineSchedule.fkScheduleEventFrequencyId == 5)//Every 14 days
        {
            _machineSchedule.scheduledFuture = scheduler.scheduleAtFixedRate(_machineSchedule,_machineSchedule.startDateTime.getMillis()- System.currentTimeMillis(), 14, TimeUnit.DAYS);
        }
        else if(_machineSchedule.fkScheduleEventFrequencyId == 6)//Every 28 days
        {
            _machineSchedule.scheduledFuture = scheduler.scheduleAtFixedRate(_machineSchedule,_machineSchedule.startDateTime.getMillis()- System.currentTimeMillis(), 28, TimeUnit.DAYS);
        }
        else if(_machineSchedule.fkScheduleEventFrequencyId == 7)//Every 10
        {
            _machineSchedule.scheduledFuture = scheduler.scheduleAtFixedRate(_machineSchedule,_machineSchedule.startDateTime.getMillis()- System.currentTimeMillis(), 10, TimeUnit.MINUTES);
        }
        else if(_machineSchedule.fkScheduleEventFrequencyId == 8)//Every 5
        {
            _machineSchedule.scheduledFuture = scheduler.scheduleAtFixedRate(_machineSchedule,_machineSchedule.startDateTime.getMillis()- System.currentTimeMillis(), 5, TimeUnit.MINUTES);
        }
        else if(_machineSchedule.fkScheduleEventFrequencyId == 9)//Every 1 
        {
            _machineSchedule.scheduledFuture = scheduler.scheduleAtFixedRate(_machineSchedule,_machineSchedule.startDateTime.getMillis()- System.currentTimeMillis(), 1, TimeUnit.MINUTES);
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
    public int addIDeviceScheduleToDB(DeviceSchedule _machineSchedule)
    {
        //save schedule to db
        int fkManagedMachineId = _machineSchedule.iDevice.getIDeviceId();
        int fkScheduleEventTypeId = _machineSchedule.fkScheduleEventTypeId;
        DateTime startDateTime = _machineSchedule.startDateTime;
        int fkScheduleEventFrequencyId = _machineSchedule.fkScheduleEventFrequencyId;
        String scheduleConfigText = _machineSchedule.scheduleConfigText;
        
        return sql.addIDeviceSchedule(fkManagedMachineId,
                    fkScheduleEventTypeId,
                    startDateTime,
                    fkScheduleEventFrequencyId,
                    scheduleConfigText);
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
    public ArrayList getMachineSchedules(IDevice _iDevice)
    {
        return _iDevice.getIDeviceSchedules();
    }
    
    public ArrayList getMachineSchedulesDB(IDevice _iDevice)
    {
        return sql.getMachineSchedules(_iDevice, this);
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
    public void addMachineSchedule(IDevice _iDevice, int _fkScheduleEventTypeId, DateTime _startDateTime, int _fkScheduleEventFrequencyId, String _scheduleConfigText)
    {
        //not all ManagedMachies exist in the DB as they are created when they are discoverd on the network.
        //A schedule can only be saved if the IDevice is in the DB, because whe the server is restarted and there is no ManagedMachine,
        // in the DB, then there is no schedule found.
        _iDevice.ensureIDevicePersistant();
        
        String scheduleEventType =  sql.getScheduleEventTypeForId(_fkScheduleEventTypeId);
        String scheduleEventFrequency =  sql.getScheduledEventFrequencyForId(_fkScheduleEventFrequencyId);
        ArrayList scheduleReciepts = new ArrayList();
        
        DeviceSchedule DeviceSchedule = new DeviceSchedule ( 0,_iDevice, _fkScheduleEventTypeId , _startDateTime  , _fkScheduleEventFrequencyId, _scheduleConfigText, this, scheduleEventType, scheduleEventFrequency, scheduleReciepts);
        activateSchedule(DeviceSchedule);
        iDeviceSchedules.add(DeviceSchedule);
        DeviceSchedule.scheduleId = addIDeviceScheduleToDB(DeviceSchedule);
        _iDevice.addIDeviceSchedule(DeviceSchedule);
    }
    
    public void deleteMachineSchedule(IDevice _iDevice, int _scheduleId )
    {
        int index = 0;
        //remove schedule from DB
        sql.deleteMachineSchedule(_scheduleId );
        
        //now delete the schedule from the managers list
        for(Object machineScheculeObject: iDeviceSchedules)
        {
            if( ((DeviceSchedule)machineScheculeObject).scheduleId == _scheduleId)
            {
                iDeviceSchedules.remove(index);
                _iDevice.deleteIDeviceSchedule(_scheduleId);
                return;
            }
            
            index++;
        }      
    }
    
    public void insertScheduleReciept(int _scheduleId, int _scheduleRecieptStatusId, String _message)
    {
        int index = 0;
        
        sql.insertScheduleReciept(_scheduleId, _scheduleRecieptStatusId, _message);
       
        
        for(Object machineScheculeObject: iDeviceSchedules)
        {
            if( ((DeviceSchedule)machineScheculeObject).scheduleId == _scheduleId)
            {
                ((DeviceSchedule)machineScheculeObject).scheduleReciepts.clear();
                ((DeviceSchedule)machineScheculeObject).scheduleReciepts.addAll(sql.getScheduleReciepts(_scheduleId)) ;
                return;
            }
            index++;
        }      
    }
    
    public void deleteScheduleHistory(int _machineScheduleId)
    {
        sql.deleteScheduleHistory(_machineScheduleId);
    }
    
    public void loadMachineSchedules(IDevice _transientIDevice, boolean _activateSchedules)
    {
        if(!_transientIDevice.hasSchedules())//get everything form the DB, probably only first time this is machine is created.
        {
            ArrayList tempScheduleList = getMachineSchedulesDB(_transientIDevice);
            for(Object machineScheculeObject: tempScheduleList)
            {
                iDeviceSchedules.add((DeviceSchedule)machineScheculeObject);
                _transientIDevice.addIDeviceSchedule((DeviceSchedule)machineScheculeObject);
            }
        }
        
        if(_activateSchedules)
        {
            if(!_transientIDevice.hasSchedules())
            {
                //now start all the schedules for that Machine
                for(Object machineScheculeObject: _transientIDevice.getIDeviceSchedules())
                {
                    activateSchedule((DeviceSchedule)machineScheculeObject);
                }
            }
        }
    }
    
    public ArrayList getScheduleEventTypes()
    {
        return sql.getScheduleEventTypes();
    }
    
    public ArrayList getScheduledEventFrequencies()
    {
        return sql.getScheduledEventFrequencies();
    }
}


