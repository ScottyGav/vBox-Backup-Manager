
package vBoxJwsTools;

import org.virtualbox_4_2.IProgress;
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
public class DeviceProgress implements IDeviceProgress 
{
    
private transient IProgress iProgress = null; 
public long progressPercent = 0;
public String description = "";
public String operationDescription = "";
public transient DateTime dateCreated = new DateTime();
public String dateCreatedString = null;
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
    public DeviceProgress(IProgress iProgress_)
    {
        iProgress = iProgress_;
        progressPercent  = iProgress.getPercent();
        description = iProgress.getDescription();
        operationDescription = iProgress.getOperationDescription();
        dateCreatedString = dateCreated.toString("yyyy-MM-dd HH:mm:ss");
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
    //will update and return 
    public long getPercent()
    {
        if(progressPercent != 100)//old progress objects will be invalid after an amount of time
        {
            description = iProgress.getDescription();
            operationDescription = iProgress.getOperationDescription();
            progressPercent = iProgress.getPercent();
            
            if(progressPercent == 100)
            {
                iProgress = null;
            }
            
            return progressPercent;
        }
        else
        {   
            iProgress = null;
            return progressPercent;
        }
    }
}
