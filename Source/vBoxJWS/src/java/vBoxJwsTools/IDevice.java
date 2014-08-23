/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vBoxJwsTools;

import java.util.ArrayList;
import org.joda.time.DateTime;

/**
 *
 * @author Admin
 */
public interface IDevice 
{
    public int      getIDeviceId();
    public void     setIDeviceId(int _iDeviceId);
    public String   getIDeviceState();
    public String   getIDeviceName();
    public String   getIDeviceHostName();
    public int      getIDeviceHostId();
    
    public boolean      hasSchedules();
    public ArrayList<DeviceSchedule>    getIDeviceSchedules();
    public void         addIDeviceSchedule(DeviceSchedule _machineSchedule);
    public void         deleteIDeviceSchedule(int _machineScheduleId);
    public void         startIDeviceSchedule(int _machineScheduleId);
    public void         cancelIDeviceSchedule(int _machineScheduleId);
    
    public ArrayList<ScheduleReciept>    getScheduleHistory(int _machineScheduleId);
    public void         deleteScheduleHistory(int _machineScheduleId);
    
    public void         ensureIDevicePersistant();
    public ArrayList<IDeviceProgress>    getUpdatedProgresses();
    public boolean      isIDeviceProcessingTasks();
    
    public boolean  startIDeviceSession(DateTime _taskSession);
    public boolean  endIDeviceSession(DateTime _taskSession);
    
    public IDeviceProgress performTask(String _task,int _pauseTillCheckSeconds);
    public IDeviceProgress performTask(String _task,int _pauseTillCheckSeconds, DateTime _taskSession);
    public IDeviceProgress performTask(String _task,int _pauseTillCheckSeconds, String _optionOne, DateTime _taskSession);
    public IDeviceProgress performTask(String _task,String _optionOne, DateTime _taskSession);

}
