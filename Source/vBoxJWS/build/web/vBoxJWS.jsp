<%-- 
    Document   : vBoxJWS
    Created on : 27/07/2014, 3:25:41 PM
    Author     : admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="org.joda.time.DateTime"%>
<%@page import="org.virtualbox_4_2.IProgress"%>
<%@page import="java.lang.reflect.Type"%>
<%@page import="com.google.gson.reflect.TypeToken"%>
<%@page import="java.util.ArrayList"%>
<%@page language="java" import="com.google.gson.*" %>
<%@page language="java" import="vBoxJwsTools.*" %>


<%
VBoxHostMannager vBoxHostMannager = (VBoxHostMannager)getServletContext().getAttribute("vBoxHostMannager");
ScheduleManager scheduleManager = (ScheduleManager)getServletContext().getAttribute("scheduleManager");
MannagedMachine currentMannagedMachine = (MannagedMachine) session.getAttribute("currentMannagedMachine");

if( currentMannagedMachine == null)
{
    currentMannagedMachine = new MannagedMachine(0, "", null, null);
}

String data = request.getParameter("data");

if(data != null)
{
    
    String requestURL = request.getRequestURL().toString();
   
    if(data.equals("getMachineDetails"))
    {
        String machineName = request.getParameter("machineName");
        MannagedMachine tempMannagedMachine = (MannagedMachine)vBoxHostMannager.getIDeviceByName(machineName);
        
        Gson gson = new Gson();
        session.setAttribute("currentMannagedMachine", tempMannagedMachine);
        
        tempMannagedMachine.getUpdatedProgresses();//updates the progress, but dosnt use the returned value here, it is sent via gson

        String gsontxt = gson.toJson(tempMannagedMachine);

        if(gsontxt != null)
        {
            response.setContentType("text/plain");
            response.getWriter().write(gsontxt);
            response.getWriter().flush();
        }
        
        return;
    }
    else if (data.equals("stopCurrentMachine") )
    {
        MannagedMachine tempMannagedMachine = (MannagedMachine) session.getAttribute("currentMannagedMachine");
        
        final DateTime taskSession = DateTime.now();
        if(tempMannagedMachine.startIDeviceSession(taskSession))
        {
            tempMannagedMachine.performTask("Stop",5,taskSession);
        }
        
        tempMannagedMachine.endIDeviceSession(taskSession);
        
        response.sendRedirect(requestURL);
    }
    else if (data.equals("startCurrentMachine") )
    {
       final DateTime taskSession = DateTime.now();
        if(currentMannagedMachine.startIDeviceSession(taskSession))
        {
            session.setAttribute("Progresses", currentMannagedMachine.performTask("Start",5,taskSession));
        }
        
        currentMannagedMachine.endIDeviceSession(taskSession);
        response.sendRedirect(requestURL);
    }
    else if(data.equals("getListOfMachines"))
   {
        ArrayList listOfMannagedMachines = getListOfMannagedMachines(vBoxHostMannager);
        Gson gson = new Gson();
        
        Type listType = new TypeToken<ArrayList<MannagedMachine>>(){}.getType();
        String gsontxt = gson.toJson(listOfMannagedMachines,listType );
        
        if(gsontxt != null)
        {
            response.setContentType("text/plain");
            response.getWriter().write(gsontxt);
            response.getWriter().flush();
        }
        
        return;
        
    }
    else if(data.equals("exportCurrentMachine")) 
    {
        MannagedMachine tempMannagedMachine = (MannagedMachine) session.getAttribute("currentMannagedMachine");
        
        final DateTime taskSession = DateTime.now();
        if(tempMannagedMachine.startIDeviceSession(taskSession))
        {
            session.setAttribute("Progresses", tempMannagedMachine.performTask("Export",5,taskSession));
        }
        
        tempMannagedMachine.endIDeviceSession(taskSession);
        
    }
    else if(data.equals("getCurrentMachineProgress")) 
    {
       getProgressLastItem(currentMannagedMachine.progresses);
    }
    else if(data.equals("rebuildManagerList"))
    {
        //rebuild the JWS server
         vBoxHostMannager.refreshMannagedMachines();//probably should stop all schedules and create new, incase a new host is found (the reason for user refreshing)
   }           
    ////////////////////////////////////////////////////////////////////////////
    
        else if(data.equals("deleteMachineSchedule"))
        {
            String machineScheduleId = request.getParameter("MachineScheduleId");
            scheduleManager.deleteMachineSchedule(currentMannagedMachine,Integer.parseInt(machineScheduleId));
            
            Gson gson = new Gson();
            String gsontxt = gson.toJson(currentMannagedMachine);
            
            if(gsontxt != null)
            {
                response.setContentType("text/plain");
                response.getWriter().write(gsontxt);
                response.getWriter().flush();
            }
            
            return;
        }
        else if(data.equals("cancelMachineSchedule"))
        {
            String machineScheduleId = request.getParameter("MachineScheduleId");
            currentMannagedMachine.cancelIDeviceSchedule(Integer.parseInt(machineScheduleId));
            
            Gson gson = new Gson();
            String gsontxt = gson.toJson(currentMannagedMachine);
            
            if(gsontxt != null)
            {
                response.setContentType("text/plain");
                response.getWriter().write(gsontxt);
                response.getWriter().flush();
            }
            
            return;
       }
        else if(data.equals("startMachineSchedule"))
        {
            String machineScheduleId = request.getParameter("MachineScheduleId");
            currentMannagedMachine.startIDeviceSchedule(Integer.parseInt(machineScheduleId));
            
            Gson gson = new Gson();
            String gsontxt = gson.toJson(currentMannagedMachine);
            
            if(gsontxt != null)
            {
                response.setContentType("text/plain");
                response.getWriter().write(gsontxt);
                response.getWriter().flush();
            }
            
            return;
        }
        else if(data.equals("scheduleHistory"))
        {
            String machineScheduleId = request.getParameter("scheduleId"); 
          
            ArrayList tempScheduleHistoryList = currentMannagedMachine.getScheduleHistory(Integer.parseInt(machineScheduleId));                   
            Gson gson = new Gson();
            String gsontxt = gson.toJson(tempScheduleHistoryList);
            
            if(gsontxt != null)
            {
              response.setContentType("text/plain");
              response.getWriter().write(gsontxt);
              response.getWriter().flush();
            }
            return;
        }
        else if(data.equals("deleteScheduleHistory"))
        {
            String machineScheduleId = request.getParameter("MachineScheduleId"); 
            currentMannagedMachine.deleteScheduleHistory(Integer.parseInt(machineScheduleId));
            
            Gson gson = new Gson();
            String gsontxt = gson.toJson(currentMannagedMachine);
            
            if(gsontxt != null)
            {
                response.setContentType("text/plain");
                response.getWriter().write(gsontxt);
                response.getWriter().flush();
            }
            
            return;
        }
        else if(data.equals("addMachineSchedule"))
        {
            String startDate = request.getParameter("startDate");
            String path = request.getParameter("backupPath");
            String ScheduleEventType = request.getParameter("scheduleEventType");
            String ScheduleEventFrequency = request.getParameter("scheduleEventFrequency"); 
            
            String[] dateTiemValues = startDate.split("/|:| ");
            DateTime scheduledDate = new DateTime( Integer.parseInt(dateTiemValues[2]) ,Integer.parseInt(dateTiemValues[1]),Integer.parseInt(dateTiemValues[0]),Integer.parseInt(dateTiemValues[3]),Integer.parseInt(dateTiemValues[4]));
        
            vBoxHostMannager.scheduleManager.addMachineSchedule(currentMannagedMachine, Integer.parseInt(ScheduleEventType) , scheduledDate  , Integer.parseInt(ScheduleEventFrequency), path);
            
            Gson gson = new Gson();
            String gsontxt = gson.toJson(currentMannagedMachine);
            
            if(gsontxt != null)
            {
                response.setContentType("text/plain");
                response.getWriter().write(gsontxt);
                response.getWriter().flush();
            }
            return;
        }
}
%>



<%!

public ArrayList getListOfMannagedMachines(VBoxHostMannager _vBoxHostMannager)
{
   return _vBoxHostMannager.getIDevices();
}

public String getProgressLastItem(ArrayList progresses_)
{
  IProgress iProgress =null;
  iProgress = (IProgress)progresses_.get(progresses_.size()-1);
  String htmlResult = "";
  htmlResult += "<div id='iprogresses>";
   htmlResult += "<div id='iprogress>";
   
  htmlResult += "<h1>" + iProgress.getPercent() +"% </h1>";
  
  htmlResult += "</div><!--div id='iprogress-->";
  htmlResult +="</div><!--div id='iprogresses'-->";
  return "";  
}

////////////////////////////////////////////////////////////////////////////////

public String printScheduleEventTypes()
{
    ScheduleManager scheduleManager = (ScheduleManager)getServletContext().getAttribute("scheduleManager");
    
    String htmlResponce = "";
    ArrayList scheduledEventTypesList = scheduleManager.getScheduleEventTypes();
   
    int listLength = scheduledEventTypesList.size();
     htmlResponce += "<select name=\"ScheduleEventType\" id=\"ScheduleEventType\">";
    
    for(int count = 0; count < listLength; count++ )
    {
        String[] eventPair = (String[]) scheduledEventTypesList.get(count);
        String id = eventPair[0];
        String event = eventPair[1];
        
        htmlResponce += "<option value=\""+id+"\">"+event +"</option>";
    }
    htmlResponce += "</select>";

    return htmlResponce;
}

public String printScheduledEventFrequencies()
{
    ScheduleManager scheduleManager = (ScheduleManager)getServletContext().getAttribute("scheduleManager");
    String htmlResponce = "";
    ArrayList scheduledEventFrequenciesList = scheduleManager.getScheduledEventFrequencies();
    
    int listLength = scheduledEventFrequenciesList.size();
    htmlResponce += "<select name=\"ScheduleEventFrequency\" id=\"ScheduleEventFrequency\">";
    
    for(int count = 0; count < listLength; count++ )
    {
        String[] frequencyPair = (String[]) scheduledEventFrequenciesList.get(count);
        String id = frequencyPair[0];
        String frequency = frequencyPair[1];
        
        htmlResponce += "<option value=\""+id+"\">"+frequency +"</option>";
    }
    htmlResponce += "</select>";

    return htmlResponce;
}

public String printBackupPaths()
{
    VBoxHostMannager vBoxHostMannager = (VBoxHostMannager)getServletContext().getAttribute("vBoxHostMannager");
    String htmlResponce = "";
    ArrayList BackupPathList = vBoxHostMannager.getBackupPaths();
   
    int listLength = BackupPathList.size();
    htmlResponce += "<select name=\"BackupPath\" id=\"BackupPath\" >";
    
    for(int count = 0; count < listLength; count++ )
    {
        String[] eventPair = (String[]) BackupPathList.get(count);
        String id = eventPair[0];
        String event = eventPair[1];
        
        htmlResponce += "<option value=\""+id+"\">"+event +"</option>";
    }
    htmlResponce += "</select>";

    return htmlResponce;
}

%>

<!DOCTYPE html>
<html>
    
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>

        <link rel="stylesheet" href="css/themes/vBoxJWS.min.css" />
        <link rel="stylesheet" href="css/themes/jquery.mobile.icons.min.css" />
        
        <!--link rel="stylesheet" href="jquery/jquery.mobile-1.4.2.min.css" />
        <script src="jquery/jquery-1.9.1.min.js"></script>
        <script src="jquery/jquery.mobile-1.4.2.min.js"></script-->

        <link rel="stylesheet" href="http://code.jquery.com/mobile/1.4.3/jquery.mobile.structure-1.4.3.min.css" />
<script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
<script src="http://code.jquery.com/mobile/1.4.3/jquery.mobile-1.4.3.min.js"></script>


        
        <script src="centerfire.js"></script>
            <script type="text/javascript">
       
            var Global_SelectedMachineName = "";
            var Global_CurrentMannagedMachine = null;

            $(document).on('pageshow', "#indexPageId", function()
            { 
               printMannagedMachineList("machineListDiv_IndexPage");
            });

            $(document).on("pageshow","#machineSchedulePageId", function()
            {
                $("#scheduleRecieptDivId").html("");
                printMannagedMachineList("machineListDiv_SchedulePage");
                printMachineSchedulePage(Global_CurrentMannagedMachine);
                $("#ScheduleEventType").on("change", function()
                {
                   if( getComboSelectedText("ScheduleEventType") === "Backup (Export Allpiance)")
                    {
                        $('#BackupPathSelectDiv').show('slow');  
                    }
                    else
                    {
                        $('#BackupPathSelectDiv').hide('slow');
                    }
                });
            });

            function stopCurrentMachine(_machineName)
            {
                var url = "vBoxJWS.jsp?data=stopCurrentMachine"; 

                if(req !== null)
                {
                    document.close();
                    return;
                }

                var req = initRequest();

                req.open("POST", url, false);
                req.send(null);//no parameters

                if (req.readyState === 4)
                {
                    if (req.status === 200)
                    {
                        if(req.responseText === "")
                        {
                            return "";
                        }

                        mainMachinePage(_machineName);
                        printMannagedMachineList();
                    }
                    else
                    {
                    }
                }
                document.close();   
            }

            function exportCurrentMachine()
            {
                var url = "vBoxJWS.jsp?data=exportCurrentMachine"; 
                var req = initRequest();

                req.open("POST", url, false);
                req.send(null);//no parameters

                if (req.readyState === 4)
                {
                    if (req.status === 200)
                    {
                        if(req.responseText === "")
                        {
                            return "";
                        }
                        alert("Export Done");
                    }
                    else
                    {
                    }
                }
                document.close();   
            }

            function startCurrentMachine(_machineName)
            {
                var url = "vBoxJWS.jsp?data=startCurrentMachine"; 
                var req = initRequest();
                req.open("POST", url, false);
                req.send(null);//no parameters

                if (req.readyState === 4)
                {
                    if (req.status === 200)
                    {
                        if(req.responseText === "")
                        {
                            return "";
                        }
                        mainMachinePage(_machineName);
                        printMannagedMachineList();
                    }
                    else
                    {
                    }
                }
                document.close();   
            }

            function printMannagedMachineList(_destinationDiv)
            {
                var url = "vBoxJWS.jsp?data=getListOfMachines";
                var req = initRequest();
                var responseText = "";

                req.open("POST", url, false);
                req.send(null);//no parameters

                if (req.readyState === 4)
                {
                    if (req.status === 200)
                    {
                        if(req.responseText === "")
                        {
                           return "";
                        }

                        var responseHTML = "";
                        responseText = '(' + req.responseText + ')';

                        var mannagedMachineList= eval(responseText);
                        var numberOfMannagedMachines = mannagedMachineList.length;
                        responseHTML += "<ul data-role=\"listview\" >";
                        responseHTML += "<li data-role=\"list-divider\" data-theme=\"z\">Pages</li>";  

                        for(var mannagedMachineCount = 0; mannagedMachineCount < numberOfMannagedMachines; mannagedMachineCount++)
                        {
                            responseHTML +="<li><a onclick='mainMachinePage(\""+ mannagedMachineList[mannagedMachineCount].iDeviceName+ "\")' href=\"\" >"  + mannagedMachineList[mannagedMachineCount].iDeviceName +": <div style='float:right'>"+mannagedMachineList[mannagedMachineCount].iDeviceState +"</div></a></li>";
                        }
                        responseHTML += "</ul>";

                        $("#" + _destinationDiv).html(responseHTML);
                        $("#" + _destinationDiv).trigger("create");
                    }
                }
            }

            function mainMachinePage(_machineName)
            {
                $.mobile.changePage("#indexPageId");
                
                var url = "vBoxJWS.jsp?data=getMachineDetails&machineName=" + _machineName; 
                var req = initRequest();
                    
                var responseHTML = "";
                var responseProgressesHTML = "";
                var responseText = "";
                var mannagedMachine = null;
                        
                req.open("POST", url, false);
                req.send(null);//no parameters

                if (req.readyState === 4)
                {
                    if (req.status === 200)
                    {
                        if(req.responseText === "")
                        {
                            return "";
                        }

                        responseText = '(' + req.responseText + ')';
                        mannagedMachine = eval( responseText );
                        
                        Global_CurrentMannagedMachine = mannagedMachine;

                        responseHTML += "<h1>" + mannagedMachine.mannagedBoxHostName +" : "+ mannagedMachine.iDeviceName+ "</h1>";

                        responseHTML += "<table data-role=\"table\"  id=\"machineDetailTableId\" class=\"ui-responsive table-stroke\" style=\"display:table;\" ><thead><tr>";

                            responseHTML += "<th >Current State</th>";
                            responseHTML += "<th >Host URL</th>";
                            responseHTML += "<th >RAM</th>";

                        responseHTML += "</tr></thead><tbody><tr>";

                            responseHTML += "<td>" + mannagedMachine.iDeviceState + "</td>";
                            responseHTML += "<td>" + mannagedMachine.mannagedBoxHostURL + "</td>";
                            responseHTML += "<td>" + mannagedMachine.mannagedBoxRam + "</td>";

                        responseHTML += "</tr></tbody></table>";

                        var numberOfProgresses = mannagedMachine.progresses.length;

                        for(var numberOfProgressesCount = numberOfProgresses-1; 0 < numberOfProgressesCount; numberOfProgressesCount--)
                        {
                           responseProgressesHTML +=  "<h2>" + mannagedMachine.progresses[numberOfProgressesCount].dateCreatedString + " " + mannagedMachine.progresses[numberOfProgressesCount].operationDescription + "</h2>";
                        }

                        document.getElementById("contentDiv").innerHTML = responseHTML + getMachineOptions(mannagedMachine) + responseProgressesHTML + "<p></p>";
                        
                        $("#machineDetailTableId").table();
                        $("#machineOptionsGroupId").controlgroup();
                    }
                    else
                    {
                     }
                }
                
                document.close();
            }

            function getMachineOptions(_mannagedMachine)
            {
                var responseHTML = "";
                responseHTML += "<div data-role='controlgroup'  id=\"machineOptionsGroupId\">";

                if(_mannagedMachine.iDeviceState  === "Running")
                {
                  responseHTML += "<a onclick='stopCurrentMachine(\""+ _mannagedMachine.iDeviceName+"\")' data-role='button' >Stop</a>";
                  responseHTML += "<a onclick=\"getMachineSchedule('"+ _mannagedMachine.iDeviceName+"')\" data-role='button' >Schedule</a>";
                }

                if(_mannagedMachine.iDeviceState === "PoweredOff" || _mannagedMachine.iDeviceState  === "Aborted")
                {
                    responseHTML += "<a onclick='startCurrentMachine(\""+ _mannagedMachine.iDeviceName+"\")' data-role='button' >Start</a>";
                    responseHTML += "<a onclick=\"getMachineSchedule('"+ _mannagedMachine.iDeviceName+"')\" data-role='button' >Schedule</a>";
                    responseHTML += "<a onclick='exportCurrentMachine()' data-role='button' >Export</a>";
                }

               responseHTML += "</div>";
               return responseHTML;
            }

            function getMachineSchedule(_machineName)
            {
                //the <a href > that this is called from was not loading the target page correctly, not all scripts were loaded, this is a workaround.
                Global_SelectedMachineName = _machineName; 
                $.mobile.changePage("#machineSchedulePageId"); 
            }

            /////////////////////////////////////////////////////////////////////////////
            function showScheduleHistory(_scheduleId)
            {
                var ajax = $.ajax(
                {
                    url:"vBoxJWS.jsp?data=scheduleHistory",
                    dataType: "json",
                    type: "GET",
                    //async: false,//wait for the response
                    data:
                    {
                        scheduleId:_scheduleId
                    }
                });

                ajax.done(function(scheduleHistoryList)
                {

                    var responseHTML = "";
                    responseHTML += "<div data-role=\"collapsible\" data-inset=\"false\" data-collapsed=\"false\">";
                    responseHTML += "<h3>Schedule History</h3>";

                    responseHTML += "<a class=\"ui-btn ui-mini\" style=\"float:left; width:150px;\" onclick=\"deleteScheduleHistory("+ _scheduleId +")\"  > Delete History </a>";

                    responseHTML += "<table data-role=\"table\"  id=\"machineDetailTableId\" class=\"ui-responsive table-stroke\" style=\"display:table;\" ><thead><tr>";

                        responseHTML += "<th>Completed</th>";
                        responseHTML += "<th>Description </th>";
                        responseHTML += "<th>Status</th>";

                    responseHTML += "</tr></thead><tbody>";

                    $.each(scheduleHistoryList, function(i,scheduleReciept)
                    {
                        responseHTML += "<tr><td>" + scheduleReciept.completedDateTime + "</td>";
                        responseHTML += "<td>" + scheduleReciept.description + "</td>";
                        responseHTML += "<td>" + scheduleReciept.scheduleRecieptStatus + "</td></tr>";
                    });

                    responseHTML += "</tbody></table>";
                    responseHTML += "</div>";
                    $("#scheduleRecieptDivId").html(responseHTML);
                    $("#scheduleRecieptDivId").trigger("create");
                });
            }

            function deleteScheduleHistory(_scheduleId)
            {
                
               var ajax = $.ajax(
                {
                    url:"vBoxJWS.jsp?data=deleteScheduleHistory",
                    dataType: "json",
                    type: "GET",
                    //async: false,//wait for the response
                    data:
                    {
                        MachineScheduleId :            _scheduleId
                    }
                });
                
                ajax.done(function(mannagedMachine)
                {
                    $("#scheduleRecieptDivId").html("");
                    Global_CurrentMannagedMachine = mannagedMachine; 
                });
            }

            function printMachineSchedulePage(_Global_CurrentMannagedMachine)
            {
                if(_Global_CurrentMannagedMachine === null)
                {
                    return "";
                }
                 var responseHTML = "<h1>" + _Global_CurrentMannagedMachine.mannagedBoxHostName +" : "+ _Global_CurrentMannagedMachine.iDeviceName+ "</h1>";
                $("#pageHeaddingDivId").html(responseHTML);
                responseHTML = printMachineSchedules(_Global_CurrentMannagedMachine);
                $("#machineScheduleDivId").html(responseHTML);
                $("#machineScheduleDivId").trigger("create");
            }

            function printMachineSchedules(_Global_CurrentMannagedMachine)
            {
                if(_Global_CurrentMannagedMachine === null)
                {
                    return"";
                }

                var machineSchedules = _Global_CurrentMannagedMachine.iDeviceSchedules;

                if(machineSchedules === null)
                {
                    return "";
                }

                if(machineSchedules.length < 1)
                {
                  return "";  
                }

                var htmlResponce = "";
                htmlResponce += "<div data-role=\"collapsible\" data-inset=\"false\" data-collapsed=\"false\">";
                htmlResponce += "<h3>"+  _Global_CurrentMannagedMachine.iDeviceName + "'s Schedules</h3>";
                htmlResponce += "<table data-role=\"table\"  id=\"scheduleListId\" class=\"ui-responsive table-stroke\" style=\"display:table;\" ><thead><tr>";
                htmlResponce += "<thead>";
                htmlResponce += "<tr>";
                htmlResponce += "<th>Scheduduled Event</th>";
               
                htmlResponce += "<th>Frequency</th>";
                htmlResponce += "<th>Status</th>";
                htmlResponce += "<th></th>";
                htmlResponce += "</tr>";
                htmlResponce += "</thead>";
                htmlResponce += "<tbody>";

               $.each(machineSchedules, function(i,DeviceSchedule)
                {
                    var scheduleId = DeviceSchedule.scheduleId;
                    var scheduduleEventType = DeviceSchedule.scheduleEventType;
                    var  startDateTime = DeviceSchedule.startDateTimeString;
                    var scheduleEventFrequency = DeviceSchedule.scheduleEventFrequency;
                    var scheduleStatus = DeviceSchedule.scheduleEventStatus;

                    htmlResponce += "<tr>"
                            + "<td>" + scheduduleEventType +" </td>"
                            + "<td>" + scheduleEventFrequency +" </td>"
                            + "<td>" + scheduleStatus +" </td>"
                            + "<td>"
                            + "<a class=\"ui-btn ui-mini\" style=\"float:left; width:50px;\" onclick=\"deleteMachineSchedule("+ scheduleId +")\"> Delete </a>"
                            + "<a class=\"ui-btn ui-mini\" style=\"float:left; width:50px;\" onclick=\"cancelMachineSchedule("+ scheduleId +")\"> Cancel </a>"
                            + "<a class=\"ui-btn ui-mini\" style=\"float:left; width:50px;\" onclick=\"startMachineSchedule("+ scheduleId +")\"> Start </a>"
                            + "<a class=\"ui-btn ui-mini\" style=\"float:left; width:50px;\" onclick=\"showScheduleHistory("+ scheduleId +")\"> History </a>"
                            + "</td></tr>" ;
                });

                htmlResponce += "</tbody>";
                htmlResponce+= "</table>";
                return htmlResponce += "</div>";
            }

            function cancelMachineSchedule(_scheduleId)
            {
                var ajax = $.ajax(
                {
                    url:"vBoxJWS.jsp?data=cancelMachineSchedule",
                    dataType: "json",
                    type: "GET",
                    //async: false,//wait for the response
                    data:
                    {
                        MachineScheduleId :            _scheduleId
                    }
                });
                
                 ajax.done(function(mannagedMachine)
                {
                    Global_CurrentMannagedMachine = mannagedMachine; 
                    var responseHTML = printMachineSchedules(Global_CurrentMannagedMachine);
                    $("#machineScheduleDivId").html(responseHTML);
                    $("#machineScheduleDivId").trigger("create");
                });
                
            }

            function  startMachineSchedule(_scheduleId)
            {
               var ajax = $.ajax(
                {
                    url:"vBoxJWS.jsp?data=startMachineSchedule",
                    dataType: "json",
                    type: "GET",
                    //async: false,//wait for the response
                    data:
                    {
                        MachineScheduleId :            _scheduleId
                    }
                }); 
                
                ajax.done(function(mannagedMachine)
                {
                    Global_CurrentMannagedMachine = mannagedMachine; 
                    var responseHTML = printMachineSchedules(Global_CurrentMannagedMachine);
                    $("#machineScheduleDivId").html(responseHTML);
                    $("#machineScheduleDivId").trigger("create");
                });
            }

            function addMachineSchedule()
            {

                var scheduleEventType =            $("#ScheduleEventType option:selected").val();
                var ScheduleEventFrequency =       $("#ScheduleEventFrequency option:selected").val();
                var BackupPath =                   $("#BackupPath option:selected").val();
                var startDate =                    $("#startDate").val();

                var ajax = $.ajax(
                {
                    url:"vBoxJWS.jsp?data=addMachineSchedule",
                    dataType: "json",
                    type: "GET",
                    //async: false,//wait for the response
                    data:
                    {
                        scheduleEventType :            $("#ScheduleEventType option:selected").val(),
                        scheduleEventFrequency :       $("#ScheduleEventFrequency option:selected").val(),
                        backupPath :                   $("#BackupPath option:selected").val(),
                        startDate :                    $("#startDate").val()
                    }
                });

                ajax.done(function(mannagedMachine)
                {
                    Global_CurrentMannagedMachine = mannagedMachine; 
                    var responseHTML = printMachineSchedules(Global_CurrentMannagedMachine);
                    $("#machineScheduleDivId").html(responseHTML);
                    $("#machineScheduleDivId").trigger("create");
                });
            }

            function deleteMachineSchedule(_scheduleId )
            {
                var ajax = $.ajax(
                {
                    url:"vBoxJWS.jsp?data=deleteMachineSchedule",
                    dataType: "json",
                    type: "GET",
                    //async: false,//wait for the response
                    data:
                    {
                        MachineScheduleId :            _scheduleId
                    }
                });
                
                ajax.done(function(mannagedMachine)
                {
                    Global_CurrentMannagedMachine = mannagedMachine; 
                    var responseHTML = printMachineSchedules(Global_CurrentMannagedMachine);
                    $("#machineScheduleDivId").html(responseHTML);
                    $("#machineScheduleDivId").trigger("create");
                });
                
            }
        </script>
    </head>
    <body>
    <!-- Start of first page -->
    <div data-role="page" id="indexPageId" data-theme="c">

	<div data-role="header" data-theme="a">
            <h1>Navigation System</h1>
            <a href="../../" data-icon="home" data-iconpos="notext" data-direction="reverse">Home</a>
            <a href="../nav.html" data-icon="search" data-iconpos="notext" data-rel="dialog" data-transition="fade">Search</a>
        </div><!-- /header -->

	<div data-role="content" class="ui-content" >
            <div style="width:20%; float:left;">
                <ul data-role="listview" data-insert="true" >
                    <li data-role="list-divider" data-theme="z">Pages</li>
                    <li><a href="admin.jsp" >Admin</a></li>
                </ul>
                <p></p>
                <div id="machineListDiv_IndexPage" >
                </div>
            </div>
            <div style="width:78%; float:left; margin-left:2%;">
                <div id="contentDiv"></div>
            </div>
         </div>         

        <div data-role="footer" class="footer-docs" data-theme="a">
            <p>&copy; 2012 jQuery Foundation and other contributors</p>
        </div>
         
    </div><!-- /page -->

<!-- Start of second page -->
<div data-role="page" id="machineSchedulePageId" data-theme="c">

	
    <div data-role="header" data-theme="a">
        <h1>Add Host</h1>
        <a href="../../" data-icon="home" data-iconpos="notext" data-direction="reverse">Home</a>
        <a href="../nav.html" data-icon="search" data-iconpos="notext" data-rel="dialog" data-transition="fade">Search</a>
    </div><!-- /header -->

    <div role="main" class="ui-content">
        <div style="width:20%; float:left;">
            <ul data-role="listview" data-insert="true" data-divider-theme="a">
                <li data-role="list-divider">Pages</li>
                <li><a href="admin.jsp">Admin</a></li>
                <li><a href="#indexPageId">Machines</a></li>
            </ul>
            <p></p>
            <div id="machineListDiv_SchedulePage" >
            </div>
        </div>

        <div style="width:78%; float:left; margin-left:2%;">
            <div id="contentDiv">
                <div id="pageHeaddingDivId"></div>
                <div data-role="collapsible" data-inset="false">
                <h3>Add a new Schedule</h3>   
                    
                        <div data-role="fieldcontain">
                            <label for="startDate">Start Date</label>
                            <%
                            DateTime now = new DateTime();
                            out.println("<input type=\"datetime\" name=\"startDate\" id=\"startDate\" value=\""+ now.toString("dd/MM/yyyy HH:mm") +"\" />");
                            %>
                        </div>

                       <div data-role="fieldcontain">
                           <label for="ScheduleEventType">Schedule Event Type</label>
                           <%
                               out.println(printScheduleEventTypes());
                           %>
                       </div>

                       <div id="BackupPathSelectDiv">
                           <div data-role="fieldcontain">
                               <label for="BackupPath">Backup Path</label>
                               <%
                                   out.println(printBackupPaths());
                               %>
                           </div>
                       </div>

                       <div data-role="fieldcontain">
                           <label for="ScheduleEventFrequency">Schedule Event Frequency</label>
                           <%
                               out.println(printScheduledEventFrequencies());
                           %>
                       </div>

                      <a class="ui-btn " style="width:100%;" href="" onclick="addMachineSchedule()"> Add Schedule </a>
                </div>
               
                <div id="machineScheduleDivId"></div>
                <div id="scheduleRecieptDivId"></div>
                
            </div><!--contentDiv-->
        </div>
    </div>
    <div data-role="footer" class="footer-docs" data-theme="a">
        <p>&copy; 2012 jQuery Foundation and other contributors</p>
    </div>
</div><!-- /page -->
</body>
</html>
