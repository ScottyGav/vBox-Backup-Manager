package vBoxJwsTools;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;


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


public class ServletListner implements javax.servlet.ServletContextListener
{
    @Resource(lookup="vBoxJWSResource")private javax.sql.DataSource vBoxJWSResource;
   
    @Override public void contextInitialized(ServletContextEvent event)
    {
        ServletContext sc = event.getServletContext();
        Logger.getLogger(ServletListner.class.getName()).log(Level.FINE,"Servlet Context Initialized....");
        SQL sql = new SQL(vBoxJWSResource);
        ScheduleManager scheduleManager = new ScheduleManager(sql);
        VBoxHostMannager vBoxHostMannager = new VBoxHostMannager(sql,scheduleManager);
        sc.setAttribute("scheduleManager", scheduleManager);
        sc.setAttribute("vBoxHostMannager",  vBoxHostMannager);
        sc.setAttribute("sql",  sql);
    }
    
    @Override  public void contextDestroyed(ServletContextEvent event)
    {
        Logger.getLogger(ServletListner.class.getName()).log(Level.FINE,"Servlet Context Destroyed....");
    }
}

