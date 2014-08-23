/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vBoxJwsTools;

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
public class ScheduleReciept implements IScheduleReciept 
{
    public int pkScheduleRecieptId = 0;
    public int fkMachineScheduleId  = 0;
    public int fkScheduleRecieptStatusId  = 0;
    public String completedDateTime = null;
    public String description = "";
    public String scheduleRecieptStatus = "";
    
    public ScheduleReciept (int _pkScheduleRecieptId, int _fkMachineScheduleId, int _fkScheduleRecieptStatusId, DateTime _completedDateTime, String _description, String _scheduleRecieptStatus )
    {
        pkScheduleRecieptId = _pkScheduleRecieptId;
        fkMachineScheduleId  = _fkMachineScheduleId;
        fkScheduleRecieptStatusId  = _fkScheduleRecieptStatusId;
        completedDateTime = _completedDateTime.toString("yyyy-MM-dd HH:mm:ss");
        description = _description;
        scheduleRecieptStatus = _scheduleRecieptStatus; 
        
    }
}
