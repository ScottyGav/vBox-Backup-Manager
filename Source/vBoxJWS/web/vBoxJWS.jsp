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
VBoxHostManager vBoxHostManager = (VBoxHostManager)getServletContext().getAttribute("vBoxHostManager");
ScheduleManager scheduleManager = (ScheduleManager)getServletContext().getAttribute("scheduleManager");
ManagedMachine currentManagedMachine = (ManagedMachine) session.getAttribute("currentManagedMachine");

if( currentManagedMachine == null)
{
    currentManagedMachine = new ManagedMachine(0, "", null, null);
}

String data = request.getParameter("data");

if(data != null)
{
    
    String requestURL = request.getRequestURL().toString();
   
    if(data.equals("getMachineDetails"))
    {
        String machineName = request.getParameter("machineName");
        ManagedMachine tempManagedMachine = (ManagedMachine)vBoxHostManager.getIDeviceByName(machineName);
        
        Gson gson = new Gson();
        session.setAttribute("currentManagedMachine", tempManagedMachine);
        
        tempManagedMachine.getUpdatedProgresses();//updates the progress, but dosnt use the returned value here, it is sent via gson

        String gsontxt = gson.toJson(tempManagedMachine);

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
        ManagedMachine tempManagedMachine = (ManagedMachine) session.getAttribute("currentManagedMachine");
        
        final DateTime taskSession = DateTime.now();
        if(tempManagedMachine.startIDeviceSession(taskSession))
        {
            tempManagedMachine.performTask("Stop",5,taskSession);
        }
        
        tempManagedMachine.endIDeviceSession(taskSession);
        
        response.sendRedirect(requestURL);
    }
    else if (data.equals("startCurrentMachine") )
    {
       final DateTime taskSession = DateTime.now();
        if(currentManagedMachine.startIDeviceSession(taskSession))
        {
            session.setAttribute("Progresses", currentManagedMachine.performTask("Start",5,taskSession));
        }
        
        currentManagedMachine.endIDeviceSession(taskSession);
        response.sendRedirect(requestURL);
    }
    else if(data.equals("getListOfMachines"))
   {
        ArrayList listOfManagedMachines = getListOfManagedMachines(vBoxHostManager);
        Gson gson = new Gson();
        
        Type listType = new TypeToken<ArrayList<ManagedMachine>>(){}.getType();
        String gsontxt = gson.toJson(listOfManagedMachines,listType );
        
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
        ManagedMachine tempManagedMachine = (ManagedMachine) session.getAttribute("currentManagedMachine");
        
        final DateTime taskSession = DateTime.now();
        if(tempManagedMachine.startIDeviceSession(taskSession))
        {
            session.setAttribute("Progresses", tempManagedMachine.performTask("Export",5,taskSession));
        }
        
        tempManagedMachine.endIDeviceSession(taskSession);
        
    }
    else if(data.equals("getCurrentMachineProgress")) 
    {
       getProgressLastItem(currentManagedMachine.progresses);
    }
    else if(data.equals("rebuildManagerList"))
    {
        //rebuild the JWS server
         vBoxHostManager.refreshManagedMachines();//probably should stop all schedules and create new, incase a new host is found (the reason for user refreshing)
   }           
    ////////////////////////////////////////////////////////////////////////////
    
        else if(data.equals("deleteMachineSchedule"))
        {
            String machineScheduleId = request.getParameter("MachineScheduleId");
            scheduleManager.deleteMachineSchedule(currentManagedMachine,Integer.parseInt(machineScheduleId));
            
            Gson gson = new Gson();
            String gsontxt = gson.toJson(currentManagedMachine);
            
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
            currentManagedMachine.cancelIDeviceSchedule(Integer.parseInt(machineScheduleId));
            
            Gson gson = new Gson();
            String gsontxt = gson.toJson(currentManagedMachine);
            
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
            currentManagedMachine.startIDeviceSchedule(Integer.parseInt(machineScheduleId));
            
            Gson gson = new Gson();
            String gsontxt = gson.toJson(currentManagedMachine);
            
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
          
            ArrayList tempScheduleHistoryList = currentManagedMachine.getScheduleHistory(Integer.parseInt(machineScheduleId));                   
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
            currentManagedMachine.deleteScheduleHistory(Integer.parseInt(machineScheduleId));
            
            Gson gson = new Gson();
            String gsontxt = gson.toJson(currentManagedMachine);
            
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
        
            vBoxHostManager.scheduleManager.addMachineSchedule(currentManagedMachine, Integer.parseInt(ScheduleEventType) , scheduledDate  , Integer.parseInt(ScheduleEventFrequency), path);
            
            Gson gson = new Gson();
            String gsontxt = gson.toJson(currentManagedMachine);
            
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

public ArrayList getListOfManagedMachines(VBoxHostManager _vBoxHostManager)
{
   return _vBoxHostManager.getIDevices();
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
    VBoxHostManager vBoxHostManager = (VBoxHostManager)getServletContext().getAttribute("vBoxHostManager");
    String htmlResponce = "";
    ArrayList BackupPathList = vBoxHostManager.getBackupPaths();
   
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
        
        <link rel="stylesheet" href="http://code.jquery.com/mobile/1.4.3/jquery.mobile.structure-1.4.3.min.css" />
        <script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
        <script src="http://code.jquery.com/mobile/1.4.3/jquery.mobile-1.4.3.min.js"></script>

        <script type="text/javascript">
       
            var Global_SelectedMachineName = "";
            var Global_CurrentManagedMachine = null;

            $(document).on('pageshow', "#indexPageId", function()
            { 
               printManagedMachineList("machineListDiv_IndexPage");
            });

            $(document).on("pageshow","#machineSchedulePageId", function()
            {
                $("#scheduleRecieptDivId").html("");
                printManagedMachineList("machineListDiv_SchedulePage");
                printMachineSchedulePage(Global_CurrentManagedMachine);
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
                var ajax = $.ajax(
                {
                    url:"vBoxJWS.jsp?data=stopCurrentMachine",
                    dataType: "json",
                    type: "POST",
                    async: false,//wait for the response
                    data:
                    {
                        
                    }
                });    
        
                 ajax.done(function()
                {
                    mainMachinePage(_machineName);
                    printManagedMachineList();
                }); 
            }

            function exportCurrentMachine()
            {
                var ajax = $.ajax(
                {
                    url:"vBoxJWS.jsp?data=exportCurrentMachine",
                    dataType: "json",
                    type: "POST",
                    //async: false,//wait for the response
                    data:
                    {
                        
                    }
                });
        
                 ajax.done(function(ManagedMachineList)
                {
                    alert("Export Done");
                });    
            }

            function startCurrentMachine(_machineName)
            {
                var ajax = $.ajax(
                {
                    url:"vBoxJWS.jsp?data=startCurrentMachine",
                    dataType: "json",
                    type: "POST",
                    //async: false,//wait for the response
                    data:
                    {
                        
                    }
                });
                
                 ajax.done(function()
                {
                    mainMachinePage(_machineName);
                    printManagedMachineList();
                });
            }

            function printManagedMachineList(_destinationDiv)
            {
                var ajax = $.ajax(
                {
                    url:"vBoxJWS.jsp?data=getListOfMachines",
                    dataType: "json",
                    type: "GET",
                    //async: false,//wait for the response
                    data:
                    {
                        
                    }
                });
                
                ajax.done(function(ManagedMachineList)
                {
                    var responseHTML = "";

                    var numberOfManagedMachines = ManagedMachineList.length;
                    responseHTML += "<ul data-role=\"listview\" >";
                    responseHTML += "<li data-role=\"list-divider\" data-theme=\"a\">Virtual Boxes</li>";  

                    for(var ManagedMachineCount = 0; ManagedMachineCount < numberOfManagedMachines; ManagedMachineCount++)
                    {
                        responseHTML +="<li><a onclick='mainMachinePage(\""+ ManagedMachineList[ManagedMachineCount].iDeviceName+ "\")' href=\"\" >"  + ManagedMachineList[ManagedMachineCount].iDeviceName +": <div style='float:right'>"+ManagedMachineList[ManagedMachineCount].iDeviceState +"</div></a></li>";
                    }
                    responseHTML += "</ul>";

                    $("#" + _destinationDiv).html(responseHTML);
                    $("#" + _destinationDiv).trigger("create");
                });
            }

            function mainMachinePage(_machineName)
            {
                var responseHTML = "";
                var responseProgressesHTML = "";
                var responseText = "";
                var ManagedMachine = null;
                
                $.mobile.changePage("#indexPageId");
                
                var ajax = $.ajax(
                {
                    url:"vBoxJWS.jsp?data=getMachineDetails",
                    dataType: "json",
                    type: "GET",
                    //async: false,//wait for the response
                    data:
                    {
                        machineName:_machineName
                    }
                });
                   
                ajax.done(function(ManagedMachine)
                {
                        Global_CurrentManagedMachine = ManagedMachine;

                        responseHTML += "<h1>" + ManagedMachine.ManagedBoxHostName +" : "+ ManagedMachine.iDeviceName+ "</h1>";

                        responseHTML += "<table data-role=\"table\"  id=\"machineDetailTableId\" class=\"ui-responsive table-stroke\" style=\"display:table;\" ><thead><tr>";

                            responseHTML += "<th >Current State</th>";
                            responseHTML += "<th >Host URL</th>";
                            responseHTML += "<th >RAM</th>";

                        responseHTML += "</tr></thead><tbody><tr>";

                            responseHTML += "<td>" + ManagedMachine.iDeviceState + "</td>";
                            responseHTML += "<td>" + ManagedMachine.ManagedBoxHostURL + "</td>";
                            responseHTML += "<td>" + ManagedMachine.ManagedBoxRam + "</td>";

                        responseHTML += "</tr></tbody></table>";

                        var numberOfProgresses = ManagedMachine.progresses.length;

                        for(var numberOfProgressesCount = numberOfProgresses-1; 0 < numberOfProgressesCount; numberOfProgressesCount--)
                        {
                           responseProgressesHTML +=  "<h2>" + ManagedMachine.progresses[numberOfProgressesCount].dateCreatedString + " " + ManagedMachine.progresses[numberOfProgressesCount].operationDescription + "</h2>";
                        }

                        document.getElementById("contentDiv").innerHTML = responseHTML + getMachineOptions(ManagedMachine) + responseProgressesHTML + "<p></p>";
                        
                        $("#machineDetailTableId").table();
                        $("#machineOptionsGroupId").controlgroup();
                });  
            }

            function getMachineOptions(_ManagedMachine)
            {
                var responseHTML = "";
                responseHTML += "<div data-role='controlgroup'  id=\"machineOptionsGroupId\">";

                if(_ManagedMachine.iDeviceState  === "Running")
                {
                  responseHTML += "<a onclick='stopCurrentMachine(\""+ _ManagedMachine.iDeviceName+"\")' data-role='button' >Stop</a>";
                  responseHTML += "<a onclick=\"getMachineSchedule('"+ _ManagedMachine.iDeviceName+"')\" data-role='button' >Schedule</a>";
                }

                if(_ManagedMachine.iDeviceState === "PoweredOff" || _ManagedMachine.iDeviceState  === "Aborted")
                {
                    responseHTML += "<a onclick='startCurrentMachine(\""+ _ManagedMachine.iDeviceName+"\")' data-role='button' >Start</a>";
                    responseHTML += "<a onclick=\"getMachineSchedule('"+ _ManagedMachine.iDeviceName+"')\" data-role='button' >Schedule</a>";
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
                
                ajax.done(function(ManagedMachine)
                {
                    $("#scheduleRecieptDivId").html("");
                    Global_CurrentManagedMachine = ManagedMachine; 
                });
            }

            function printMachineSchedulePage(_Global_CurrentManagedMachine)
            {
                if(_Global_CurrentManagedMachine === null)
                {
                    return "";
                }
                 var responseHTML = "<h1>" + _Global_CurrentManagedMachine.ManagedBoxHostName +" : "+ _Global_CurrentManagedMachine.iDeviceName+ "</h1>";
                $("#pageHeaddingDivId").html(responseHTML);
                responseHTML = printMachineSchedules(_Global_CurrentManagedMachine);
                $("#machineScheduleDivId").html(responseHTML);
                $("#machineScheduleDivId").trigger("create");
            }

            function printMachineSchedules(_Global_CurrentManagedMachine)
            {
                if(_Global_CurrentManagedMachine === null)
                {
                    return"";
                }

                var machineSchedules = _Global_CurrentManagedMachine.iDeviceSchedules;

                if(machineSchedules === null)
                {
                    return "";
                }

                if(machineSchedules.length < 1)
                {
                  return "";  
                }

                var htmlResponce = "";
                htmlResponce += "<div data-role=\"collapsible\" data-inset=\"false\" data-collapsed=\"false\" data-theme=\"a\" data-content-theme=\"c\">";
                htmlResponce += "<h3>"+  _Global_CurrentManagedMachine.iDeviceName + "'s Schedules</h3>";
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
                            + "<a class=\"ui-btn ui-mini\" style=\"float:left; width:50px;\" onclick=\"deleteMachineSchedule("+ scheduleId +")\"  > Delete </a>"
                            + "<a class=\"ui-btn ui-mini\" style=\"float:left; width:50px;\" onclick=\"cancelMachineSchedule("+ scheduleId +")\"  > Cancel </a>"
                            + "<a class=\"ui-btn ui-mini\" style=\"float:left; width:50px;\" onclick=\"startMachineSchedule("+ scheduleId +")\"  > Start </a>"
                            + "<a class=\"ui-btn ui-mini\" style=\"float:left; width:50px;\" onclick=\"showScheduleHistory("+ scheduleId +")\"  > History </a>"
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
                
                 ajax.done(function(ManagedMachine)
                {
                    Global_CurrentManagedMachine = ManagedMachine; 
                    var responseHTML = printMachineSchedules(Global_CurrentManagedMachine);
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
                
                ajax.done(function(ManagedMachine)
                {
                    Global_CurrentManagedMachine = ManagedMachine; 
                    var responseHTML = printMachineSchedules(Global_CurrentManagedMachine);
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

                ajax.done(function(ManagedMachine)
                {
                    Global_CurrentManagedMachine = ManagedMachine; 
                    var responseHTML = printMachineSchedules(Global_CurrentManagedMachine);
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
                
                ajax.done(function(ManagedMachine)
                {
                    Global_CurrentManagedMachine = ManagedMachine; 
                    var responseHTML = printMachineSchedules(Global_CurrentManagedMachine);
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
            <h1>vBoxManager</h1>
            <a href="vBoxJWS.jsp" rel="external" data-icon="home" data-iconpos="notext" data-direction="reverse">Home</a>
        </div><!-- /header -->

	<div data-role="content" class="ui-content" >
            <div style="width:20%; float:left;">
                <ul data-role="listview" data-insert="true" >
                    <!--li data-role="list-divider" data-theme="z">Pages</li-->
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
           <p></p>
        </div>
         
    </div><!-- /page -->

<!-- Start of second page -->
<div data-role="page" id="machineSchedulePageId" data-theme="c">

	
    <div data-role="header" data-theme="a">
        <h1>Add Host</h1>
        <a href="vBoxJWS.jsp" rel="external" data-icon="home" data-iconpos="notext" data-direction="reverse">Home</a>
    </div><!-- /header -->

    <div role="main" class="ui-content">
        <div style="width:20%; float:left;">
            <ul data-role="listview" data-insert="true" data-divider-theme="a">
                <!--li data-role="list-divider">Pages</li-->
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
                <div data-role="collapsible" data-inset="false" data-theme="a" data-content-theme="c">
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
       <p></p>
    </div>
</div><!-- /page -->
</body>
</html>
