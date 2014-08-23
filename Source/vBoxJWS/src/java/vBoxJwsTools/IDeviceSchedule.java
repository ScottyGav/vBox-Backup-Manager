/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vBoxJwsTools;

/**
 *
 * @author admin
 */
public interface IDeviceSchedule 
{
    public void activateSchedule();
    public void addReciept(ScheduleReciept _ScheduleReciept);
    public void cancelSchedule();
    public void deleteMachineSchedule();
    public void task();
    public String getStatus();
    public boolean isCancelled();
    public boolean isDone();
}
