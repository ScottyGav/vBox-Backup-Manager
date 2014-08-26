/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vBoxJwsTools;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
//import java.util.Date;
//import javax.annotation.Resource;

//import javax.naming.Context;
//import javax.naming.InitialContext;
//import javax.naming.NamingException;
//import javax.sql.DataSource;
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
public class SQL
{
   
   public Connection connection = null;

    private String DB_CONN_STRING = "jdbc:mysql://localhost:3306/vboxjws";
    private String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";//org.gjt.mm.mysql.Driver";
    private String USER_NAME = "root";
    private String PASSWORD = "root";

    public  ArrayList tableUpdates = new ArrayList();//
  

   public  SQL(javax.sql.DataSource _vBoxJWSResource)
    {
        System.out.println("SQL Constructor - Default");
        
        try 
        {
           connection = _vBoxJWSResource.getConnection();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
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
   public  SQL(String DB_CONN_STRING_,String DRIVER_CLASS_NAME_,String USER_NAME_,String PASSWORD_)
    {
        Logger.getLogger(SQL.class.getName()).log(Level.INFO,"SQL Constructor - With arguments");
        
        connection = getConnection(DB_CONN_STRING_, DRIVER_CLASS_NAME_, USER_NAME_, PASSWORD_);
        DB_CONN_STRING = DB_CONN_STRING_;
        DRIVER_CLASS_NAME = DRIVER_CLASS_NAME_;
        USER_NAME = USER_NAME_;
        PASSWORD = PASSWORD_;

        connection = getConnection(DB_CONN_STRING_,DRIVER_CLASS_NAME_,USER_NAME_,PASSWORD_);
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
   @Override
    protected void finalize () throws Throwable
    {
        if (connection != null)
        {
                try
                {
                    connection.commit();
                    connection.close();
                }
                catch (SQLException ex)
                {
                    Logger.getLogger(SQL.class.getName()).log(Level.SEVERE, null, ex);
                }
                connection = null;
        }

        super.finalize();
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
    private Connection getConnection(String DB_CONN_STRING_,String DRIVER_CLASS_NAME_,String USER_NAME_,String PASSWORD_)
    {
        Connection result = null;
        try
        {
            Class.forName(DRIVER_CLASS_NAME_).newInstance();
        }
        catch (Exception ex)
        {
            Logger.getLogger(SQL.class.getName()).log(Level.CONFIG,"Check classpath. Cannot load db driver: " + DRIVER_CLASS_NAME_);
        }

        try
        {
            result = DriverManager.getConnection(DB_CONN_STRING_, USER_NAME_, PASSWORD_);
        }
        catch (SQLException e)
        {
            Logger.getLogger(SQL.class.getName()).log(Level.CONFIG,"Driver loaded, but cannot connect to db: " + DB_CONN_STRING_);
        }
        Logger.getLogger(SQL.class.getName()).log(Level.INFO,"DB Connection Created Successfully");
        return result;
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
 public ResultSet callStoredProcRS(String strProc_)
 {
    Logger.getLogger(SQL.class.getName()).log(Level.FINE,"callStoredProcRS: " + strProc_);

    CallableStatement proc = null;

    try
    {
       proc = connection.prepareCall(strProc_);

       return proc.executeQuery();
    }
    catch (SQLException ex)
    {
        Logger.getLogger(SQL.class.getName()).log(Level.SEVERE,"SQLException in callStoredProcRS: " + ex.getMessage());
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
 private void callStoredProc(String strProc_)
 {

System.out.println("callStoredProc: " + strProc_);
    CallableStatement proc = null;

    try
    {
       proc = connection.prepareCall(strProc_);
       proc.executeQuery();
       proc.close();
    }
    catch (SQLException ex)
    {
        Logger.getLogger(SQL.class.getName()).log(Level.SEVERE,"SQLException in callStoredProcRS: " + ex.getMessage());
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
 private int getAffectedRows()
{
    try
    {
        Statement stmt = null;
        ResultSet rs = null;
        stmt = connection.createStatement();
        rs = stmt.executeQuery("SELECT ROW_COUNT()");

        if (rs.next())
         {
                try 
                {
                    return rs.getInt(1);
                }
                catch (SQLException ex) 
                {
                   Logger.getLogger(SQL.class.getName()).log(Level.SEVERE,"SQLException in getAffectedRows: " + ex.getMessage());
                }
         }
         else
         {
            // throw an exception from here
         }

        return 0;
    }
    catch (SQLException ex)
    {
        Logger.getLogger(SQL.class.getName()).log(Level.SEVERE,"SQLException in getAffectedRows: " + ex.getMessage());
    }

    return 0;
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
    private int returnLastInsertAsInt()
    {
        try
        {
            Statement stmt = null;
            ResultSet rs = null;
            stmt = connection.createStatement();
            rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");

             if (rs.next())
             {
                    try {
                        return rs.getInt(1);
                    }
                    catch (SQLException ex) {
                       Logger.getLogger(SQL.class.getName()).log(Level.SEVERE,"SQLException in returnLastInsertAsInt: " + ex.getMessage());
                    }
             }
             else
             {
                // throw an exception from here
             }

            return 0;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(SQL.class.getName()).log(Level.SEVERE,"SQLException in returnLastInsertAsInt: " + ex.getMessage());
        }
        return 0;
    }
 ///////////////////////////////////////////////////////////////////////////////
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
    public void populateManagedBoxHostList(VBoxHostManager _vBoxHostManager, ScheduleManager _scheduleManager)
    {
       ArrayList hostList = new ArrayList();
       ResultSet rs = null;
       rs = callStoredProcRS("{call spSelectManagedBoxHosts()}");

       if(rs!= null)
       {
           try
           {
               while (rs.next())
               {
                   int managedBoxHostId =  rs.getInt(1);
                   String hostURL =  rs.getString(2);
                   String hostAlternateName =  rs.getString(3);
                   String hostUserName =  rs.getString(4);
                   String hostPassword =  rs.getString(5);

                   _vBoxHostManager.addManagedBoxHost(managedBoxHostId,hostURL ,hostAlternateName, hostUserName , hostPassword);
               }
           }
           catch (SQLException ex)
           {
               Logger.getLogger(SQL.class.getName()).log(Level.SEVERE,"SQLException in spSelectManagedBoxHosts: " + ex.getMessage());
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
    public ArrayList getPersistantIDevicesForHostId(int  _ManagedBoxHostId)
    {
        ArrayList managedMachines = new ArrayList();
        ResultSet rs = null;
        rs = callStoredProcRS("{call spSelectManagedMachinesForHostId("+ _ManagedBoxHostId +")}");

        if(rs!= null)
        {
            try
            {
                while (rs.next())
                {
                    int managedMachineId =  rs.getInt(1);
                    String machineName =  rs.getString(2);
                    managedMachines.add(new ManagedMachine(null,managedMachineId , machineName, null));
                }
            }
            catch (SQLException ex)
            {
                Logger.getLogger(SQL.class.getName()).log(Level.SEVERE,"SQLException in spSelectManagedMachinesForHostId: " + ex.getMessage());
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
    public int insertManagedMachine(IDevice _managedMachine)
    {
       callStoredProc("{call spInsertManagedMachine("+_managedMachine.getIDeviceHostId()+",'"+_managedMachine.getIDeviceName() +"')}"); 
        
       return  returnLastInsertAsInt();
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
    public void insertManagedBoxHost(String _hostURL,String  _hostAlternateName,String  _hostUserName,String  _hostPassword)
    {
        callStoredProc("{call spInsertManagedBoxHost('"+_hostURL+"','"+_hostAlternateName+"','"+_hostUserName+"','"+_hostPassword+"')}");
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
    public void deleteHost(String _hostURL)
    {
           callStoredProc("{call spDeleteManagedBoxHost('"+_hostURL+"')}");
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
    public ArrayList getScheduleEventTypes()
    {
        ArrayList scheduleEventTypes = new ArrayList();
        
        ResultSet rs = null;
        rs = callStoredProcRS("{call spSelectScheduleEventTypes()}");
        int eventId = 0;
        String event =  "";
        String[]eventPair = null;

        if(rs!= null)
        {
            try
            {
                while (rs.next())
                {
                    eventId =  rs.getInt(1);
                    event =  rs.getString(2);

                    eventPair = new String[]{Integer.toString(eventId),event};

                    scheduleEventTypes.add(eventPair);

                }
            }
            catch (SQLException ex)
            {
                Logger.getLogger(SQL.class.getName()).log(Level.SEVERE,"SQLException in spSelectScheduleEventTypes: " + ex.getMessage());
            }
        }
        
        return scheduleEventTypes; 
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
    public String getScheduleEventTypeForId(int _eventTypeId)
    {
        ResultSet rs = null;
        rs = callStoredProcRS("{call spSelectScheduleEventTypeForId("+_eventTypeId+")}");
        
        String event =  "";
        if(rs!= null)
        {
            try
            {
                while (rs.next())
                {
                    event =  rs.getString(2);
                }
            }
            catch (SQLException ex)
            {
                Logger.getLogger(SQL.class.getName()).log(Level.SEVERE,"SQLException in spSelectScheduleEventTypeForId: " + ex.getMessage());
            }
        }
        return event; 
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
    public ArrayList getScheduledEventFrequencies()
    {
        ArrayList scheduledEventFrequencies = new ArrayList();
        ResultSet rs = null;
        rs = callStoredProcRS("{call spSelectScheduleFrequencies()}");
        int frequencyId =  0;
        String frequency =  "";
        String[]frequencyPair = null;

        if(rs!= null)
        {
            try
            {
                while (rs.next())
                {
                    frequencyId =  rs.getInt(1);
                    frequency =  rs.getString(2);
                    frequencyPair = new String[]{Integer.toString(frequencyId),frequency};
                    scheduledEventFrequencies.add(frequencyPair);
                }
            }
            catch (SQLException ex)
            {
                Logger.getLogger(SQL.class.getName()).log(Level.SEVERE,"SQLException in spSelectScheduleFrequencies: " + ex.getMessage());
            }
        }

        return scheduledEventFrequencies;
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
    public String getScheduledEventFrequencyForId(int eventFrequencyId)
    {
        ResultSet rs = null;
        rs = callStoredProcRS("{call spSelectScheduleFrequencyForId("+eventFrequencyId+")}");
        String frequency =  "";
        
        if(rs!= null)
        {
            try
            {
                while (rs.next())
                {
                    frequency =  rs.getString(2);
                }
            }
            catch (SQLException ex)
            {
                Logger.getLogger(SQL.class.getName()).log(Level.SEVERE,"SQLException in spSelectScheduleFrequencyForId: " + ex.getMessage());
            }
        }

        return frequency;
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
    
    public void addBackupPath(String _backupPath)
    {
        callStoredProc("{call spInsertBackupPath('"+_backupPath+"')}");
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
    public void deleteBackupPath(String _backupPathId)
    {
        callStoredProc("{call spDeleteBackupPathId("+_backupPathId+")}");
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
    public ArrayList getBackupPaths()
    {
        ArrayList backupPaths = new ArrayList();
        ResultSet rs = null;
        rs = callStoredProcRS("{call spSelectBackupPaths()}");
        int backupPathId =  0;
        String backupPath =  "";
        String[]backupPathPair = null;

        if(rs!= null)
        {
            try
            {
                while (rs.next())
                {
                    backupPathId =  rs.getInt(1);
                    backupPath =  rs.getString(2);

                    backupPathPair = new String[]{Integer.toString(backupPathId),backupPath};

                    backupPaths.add(backupPathPair);

                }
            }
            catch (SQLException ex)
            {
                Logger.getLogger(SQL.class.getName()).log(Level.SEVERE,"SQLException in spSelectBackupPaths: " + ex.getMessage());
            }
        }

        return backupPaths;
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
    public int addIDeviceSchedule(int _fkManagedMachineId,int _fkScheduleEventTypeId,DateTime _startDateTime,int _fkScheduleEventFrequencyId,String _scheduleConfigText)
    {
        callStoredProc( "{call spInsertMachineSchedule(" +_fkManagedMachineId+ "," +_fkScheduleEventTypeId+ ",'" + _startDateTime.toString("yyyy-MM-dd HH:mm:ss") + "'," +_fkScheduleEventFrequencyId +",'" +_scheduleConfigText+ "')}" );
        return  returnLastInsertAsInt();
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
    public ArrayList getMachineSchedules(IDevice _ManagedMachine, ScheduleManager _scheduleManager)
    {
        ArrayList machineSchedules = null;
        ResultSet rs = null;
        rs = callStoredProcRS("{call spSelectMachineSchedules("+_ManagedMachine.getIDeviceId()+")}");
        
        int machineScheduleId = 0;
        int managedMachineId = 0;
        int scheduleEventTypeId = 0;
        DateTime startDateTime = null;
        int scheduleEventFrequencyId = 0;
        String scheduleConfigText = null;
        int statusId = 0;
        
        String[]DeviceSchedule = null;

        if(rs!= null)
        {
            machineSchedules = new ArrayList();
            
            try
            {
                while (rs.next())
                {
                    machineScheduleId = rs.getInt(1);
                    managedMachineId = rs.getInt(2);
                    scheduleEventTypeId = rs.getInt(3);
                    startDateTime = new DateTime(rs.getDate(4)); //convert to joda?
                    scheduleEventFrequencyId = rs.getInt(5);
                    scheduleConfigText = rs.getString(6);
                    String scheduleEventType =  getScheduleEventTypeForId(scheduleEventTypeId);
                    String scheduleEventFrequency =  getScheduledEventFrequencyForId(scheduleEventFrequencyId);
                    ArrayList scheduleReciepts = getScheduleReciepts(machineScheduleId);
        
        
                    DeviceSchedule tempMachineSchedule = new DeviceSchedule( machineScheduleId,_ManagedMachine, scheduleEventTypeId, startDateTime, scheduleEventFrequencyId, scheduleConfigText, _scheduleManager, scheduleEventType, scheduleEventFrequency, scheduleReciepts);
                    machineSchedules.add(tempMachineSchedule);
                 }
            }
            catch (SQLException ex)
            {
                Logger.getLogger(SQL.class.getName()).log(Level.SEVERE,"SQLException in spSelectMachineSchedules: " + ex.getMessage());
            }
        }
        
        return machineSchedules;
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
    public void deleteMachineSchedule(int _machineScheduleId)
    {
        callStoredProc( "{call spDeleteMachineSchedule(" + _machineScheduleId + ")}" );
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
    public void insertScheduleReciept(int _fkMachineScheduleId, int _fkScheduleRecieptStatusId, String _description)
    {
        _description = _description.replace("'", "\\'");
        callStoredProc( "{call spInsertScheduleReciept(" + _fkMachineScheduleId +", "+ _fkScheduleRecieptStatusId + ", '"+ _description + "')}" );
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
    public ArrayList getScheduleReciepts(int _scheduleId)
    {
        int pkScheduleRecieptId = 0;
        int fkMachineScheduleId  = 0;
        int fkScheduleRecieptStatusId  = 0;
        DateTime completedDateTime = null;
        String description = "";
        String scheduleRecieptStatus = "";

        ResultSet rs = null;
        ArrayList scheduleReciepts = null;
        ScheduleReciept tempScheduleReciept = null;
        rs = callStoredProcRS( "{call spSelectScheduleRecieptsForScheduleId(" + _scheduleId +")}" );
        
        if(rs!= null)
        {
            scheduleReciepts = new ArrayList();
            
            try
            {
                while (rs.next())
                {
                    pkScheduleRecieptId = rs.getInt(1);
                    fkMachineScheduleId  = rs.getInt(2);
                    fkScheduleRecieptStatusId  = rs.getInt(3);
                    completedDateTime = new DateTime(rs.getTimestamp(4));
                    description = rs.getString(5);
                    scheduleRecieptStatus = rs.getString(6);
                    
                    tempScheduleReciept = new ScheduleReciept (pkScheduleRecieptId, fkMachineScheduleId, fkScheduleRecieptStatusId, completedDateTime, description, scheduleRecieptStatus );
                    scheduleReciepts.add(tempScheduleReciept);    
                }
            }
            catch (SQLException ex)
            {
                Logger.getLogger(SQL.class.getName()).log(Level.SEVERE,"SQLException in spSelectScheduleRecieptsForScheduleId: " + ex.getMessage());
            }
        }
        return scheduleReciepts;
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
        callStoredProc( "{call spDeleteScheduleReciepts(" + _machineScheduleId + ")}" );
    }
}
