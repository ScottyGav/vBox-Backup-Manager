/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vBoxJwsTools;

import java.util.ArrayList;

/**
 *
 * @author Admin
 */
public interface IDeviceManager
{
    public IDevice      getIDeviceByName(String _name);
    public ArrayList    getIDevices();
    public void         insertIDeviceHost(String _hostURL, String  _hostAlternateName, String  _hostUserName, String  _hostPassword);
    public void         deleteIDeviceHost(String _hostURL);
    public void         addBackupPath(String _backupPath);
    public void         deleteBackupPath(String _backupPathId);
    public ArrayList    getBackupPaths();
    public ArrayList<IDevice>    getPersistantIDevicesForHostId(int _ManagedBoxHostId);
    public void loadIDeviceSchedules(IDevice _transientIDevice, boolean _activateSchedules);
    
    public IDeviceProgress startIDevice(IDevice _iDevice, int _pauseTillCheckSeconds);
    public IDeviceProgress stopIDevice(IDevice _iDevice,int _pauseTillCheckSeconds);
    public IDeviceProgress exportIDevice(IDevice _iDevice, String _optionOne);
    public String           getIDeviceState(IDevice _iDevice);
    public IDeviceHost      getIDeviceHost(IDevice _iDevice);
    public String           getIDeviceHostName(IDevice _iDevice);
    public String           getIDeviceHostURL(IDevice _iDevice);
    
    
    
}
