/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vBoxJwsTools;

/**
 *
 * @author Admin
 */
public interface IDeviceHost 
{
    public String           getIDeviceHostName();
    public int              getIDeviceHostId();
    public String           getIDeviceHostURL();
    public IDeviceProgress   startIDevice        (IDevice _transientIDevice, int _pauseTillCheckSeconds);
    public IDeviceProgress   stopIDevice         (IDevice _transientIDevice, int _pauseTillCheckSeconds);
    public IDeviceProgress   exportIDevice       (IDevice _transientIDevice, String _filePath);
    public int              persistIDevice      (IDevice _iDevice);
}

