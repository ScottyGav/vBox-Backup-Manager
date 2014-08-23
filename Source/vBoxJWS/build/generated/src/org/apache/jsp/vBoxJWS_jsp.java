package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.joda.time.DateTime;
import org.virtualbox_4_2.IProgress;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import com.google.gson.*;
import vBoxJwsTools.*;

public final class vBoxJWS_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {



public ArrayList getListOfMannagedMachines(VBoxHostMannager _vBoxHostMannager)
{
   /*ArrayList mannagedBoxHostsList = _vBoxHostMannager.getMannagedBoxHostsList();
   ArrayList mannagedMachineList = new ArrayList();
   
    for(Object mannagedBoxHostObject : mannagedBoxHostsList)
    {
        VBoxHostMannager.MannagedBoxHost mannagedBoxHost = (VBoxHostMannager.MannagedBoxHost)mannagedBoxHostObject;
        ArrayList mannagedMachines = mannagedBoxHost.getMannagedMachines(false);//false = dont rebuild list of objects, just return the list
        
        for(Object mannagedMachineObject : mannagedMachines)
        {
            MannagedMachine mannagedMachine = (MannagedMachine)mannagedMachineObject;
            

            if(mannagedMachine.getIDeviceHostName() != null)
            {
               mannagedMachineList.add(mannagedMachine);
            }
        }
    }
    
  return mannagedMachineList;*/
    return _vBoxHostMannager.getMannagedMachines();
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

public String getProgressesAll(ArrayList progresses_)
{
    for( Object iProgressObject : progresses_)
    {
        
    }
    return "";  
}
       

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public java.util.List<String> getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.glassfish.jsp.api.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");

VBoxHostMannager vBoxHostMannager = (VBoxHostMannager)getServletContext().getAttribute("vBoxHostMannager");



String data = request.getParameter("data");

if(data != null)
{
    
    String requestURL = request.getRequestURL().toString();
   
    if(data.equals("getMachineDetails"))
    {
        String machineName = request.getParameter("machineName");
        MannagedMachine tempMannagedMachine = (MannagedMachine)vBoxHostMannager.getManagedMachineByName(machineName);
        
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
    }
    else if (data.equals("stopCurrentMachine") )
    {
        MannagedMachine tempMannagedMachine = (MannagedMachine) session.getAttribute("currentMannagedMachine");
        
        final DateTime taskSession = DateTime.now();
        if(tempMannagedMachine.startIDeviceSession(taskSession))
        {
            //session.setAttribute("Progresses", tempMannagedMachine.stop(5));
            tempMannagedMachine.performTask("Stop",5,taskSession);
        }
        
        tempMannagedMachine.endIDeviceSession(taskSession);
        
        response.sendRedirect(requestURL);
    }
    else if (data.equals("startCurrentMachine") )
    {
        MannagedMachine currentMannagedMachine = (MannagedMachine) session.getAttribute("currentMannagedMachine");
        //session.setAttribute("Progresses", currentMannagedMachine.start(5));
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
        //String gsontxt = gson.toJson(listOfMannagedMachines.get(1) );

        if(gsontxt != null)
        {
            response.setContentType("text/plain");
            response.getWriter().write(gsontxt);
            response.getWriter().flush();
        }
        
    }
    else if(data.equals("exportCurrentMachine")) 
    {
        MannagedMachine tempMannagedMachine = (MannagedMachine) session.getAttribute("currentMannagedMachine");
        
        //tempMannagedMachine.exportAppliance("\\\VBoxBackups\\testMachine3.ova");
        //tempMannagedMachine.exportAppliance("\\\\GENESISCUBE\\Users\\admin\\sharedFolder\\testMachine3.ova");
        //session.setAttribute("Progresses", tempMannagedMachine.exportAppliance("\\\\GENESISCUBE\\Users\\admin\\sharedFolder\\"));
        final DateTime taskSession = DateTime.now();
        if(tempMannagedMachine.startIDeviceSession(taskSession))
        {
            session.setAttribute("Progresses", tempMannagedMachine.performTask("Export",5,taskSession));
        }
        
        tempMannagedMachine.endIDeviceSession(taskSession);
        
    }
    else if(data.equals("getCurrentMachineProgress")) 
    {
        MannagedMachine currentMannagedMachine = (MannagedMachine) session.getAttribute("currentMannagedMachine");
        getProgressLastItem(currentMannagedMachine.progresses);
        
    }
    else if(data.equals("rebuildManagerList"))
    {
        //rebuild the JWS server
         vBoxHostMannager.refreshMannagedMachines();//probably should stop all schedules and create new, incase a new host is found (the reason for user refreshing)
   }           
    
  return;   
}

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("<!DOCTYPE html>\n");
      out.write("<html>\n");
      out.write("    \n");
      out.write("    <head>\n");
      out.write("        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
      out.write("        <title>JSP Page</title>\n");
      out.write("\n");
      out.write("        <link rel=\"stylesheet\" href=\"jquery/jquery.mobile-1.4.2.min.css\" />\n");
      out.write("        <script src=\"jquery/jquery-1.9.1.min.js\"></script>\n");
      out.write("        <script src=\"jquery/jquery.mobile-1.4.2.min.js\"></script>\n");
      out.write("\n");
      out.write("        <script src=\"centerfire.js\"></script>\n");
      out.write("        \n");
      out.write("        <script type=\"text/javascript\">\n");
      out.write("       //put all script before the </body> tag!!!!!!!!!!!\n");
      out.write("        \n");
      out.write("    $(document).ready(function() \n");
      out.write("    {\n");
      out.write("        //alert(\"4. ready\");\n");
      out.write("       printMannagedMachineList();\n");
      out.write("    });\n");
      out.write("\n");
      out.write("   $(document).on('pageshow', \"#indexPageId\", function()\n");
      out.write("    { \n");
      out.write("        //alert(\"3. pageshow : indexPageId\");\n");
      out.write("        printMannagedMachineList();\n");
      out.write("    });\n");
      out.write("    \n");
      out.write("    $( document ).delegate(\"#indexPageId\", \"pageinit\", function() {\n");
      out.write("        //alert('2. #indexPageId : pageinit');\n");
      out.write("       printMannagedMachineList();\n");
      out.write("    });\n");
      out.write("    \n");
      out.write("    $( document ).delegate(\"#indexPageId\", \"pagebeforecreate\", function() {\n");
      out.write("        //alert('1. #indexPageId : pagebeforecreate');\n");
      out.write("        printMannagedMachineList();\n");
      out.write("    });\n");
      out.write("    \n");
      out.write("     $( document ).delegate(\"#indexPageId\", \"pagecreate\", function() {\n");
      out.write("        //alert('1. #indexPageId : pagecreate');\n");
      out.write("        printMannagedMachineList();\n");
      out.write("    });\n");
      out.write("    \n");
      out.write("    //-----\n");
      out.write("    \n");
      out.write("   \n");
      out.write("\n");
      out.write("   $(document).on('pageshow', \"#machineSchedulePageId\", function()\n");
      out.write("    { \n");
      out.write("        //alert(\"3. pageshow : machineSchedulePageId\");\n");
      out.write("        //printMannagedMachineList();\n");
      out.write("    });\n");
      out.write("    \n");
      out.write("    $( document ).delegate(\"#machineSchedulePageId\", \"pageinit\", function() {\n");
      out.write("        //alert('2. #machineSchedulePageId : pageinit');\n");
      out.write("       //printMannagedMachineList();\n");
      out.write("    });\n");
      out.write("    \n");
      out.write("    $( document ).delegate(\"#machineSchedulePageId\", \"pagebeforecreate\", function() {\n");
      out.write("        //alert('1. #machineSchedulePageId : pagebeforecreate');\n");
      out.write("        //printMannagedMachineList();\n");
      out.write("    });\n");
      out.write("    \n");
      out.write("     $( document ).delegate(\"#machineSchedulePageId\", \"pagecreate\", function() {\n");
      out.write("        //alert('1. #machineSchedulePageId : pagecreate');\n");
      out.write("        //printMannagedMachineList();\n");
      out.write("    });\n");
      out.write("    \n");
      out.write("    \n");
      out.write("    \n");
      out.write("    \n");
      out.write("    var Global_SelectedMachineName = \"\";\n");
      out.write("        \n");
      out.write("    function stopCurrentMachine(_machineName)\n");
      out.write("    {\n");
      out.write("        \n");
      out.write("     var url = \"vBovJWS.jsp?data=stopCurrentMachine\"; \n");
      out.write("        \n");
      out.write("        if(req!= null)\n");
      out.write("        {\n");
      out.write("            document.close();\n");
      out.write("            return;\n");
      out.write("        }\n");
      out.write("        \n");
      out.write("        var req = initRequest();\n");
      out.write("        \n");
      out.write("        req.open(\"POST\", url, false);\n");
      out.write("        req.send(null);//no parameters\n");
      out.write("\n");
      out.write("        if (req.readyState == 4)\n");
      out.write("        {\n");
      out.write("            if (req.status == 200)\n");
      out.write("            {\n");
      out.write("                if(req.responseText == \"\")\n");
      out.write("                {\n");
      out.write("                    return \"\";\n");
      out.write("                }\n");
      out.write("                \n");
      out.write("                mainMachinePage(_machineName);\n");
      out.write("            }\n");
      out.write("            else\n");
      out.write("            {\n");
      out.write("               // requestIcons();\n");
      out.write("            }\n");
      out.write("        }\n");
      out.write("\n");
      out.write("        document.close();   \n");
      out.write("        \n");
      out.write("    }\n");
      out.write("    \n");
      out.write("    function exportCurrentMachine()\n");
      out.write("    {\n");
      out.write("        var url = \"vBovJWS.jsp?data=exportCurrentMachine\"; \n");
      out.write("        \n");
      out.write("        var req = initRequest();\n");
      out.write("        \n");
      out.write("        req.open(\"POST\", url, false);\n");
      out.write("        req.send(null);//no parameters\n");
      out.write("\n");
      out.write("        if (req.readyState == 4)\n");
      out.write("        {\n");
      out.write("            if (req.status == 200)\n");
      out.write("            {\n");
      out.write("                if(req.responseText == \"\")\n");
      out.write("                {\n");
      out.write("                    return \"\";\n");
      out.write("                }\n");
      out.write("                \n");
      out.write("                alert(\"done\");\n");
      out.write("            }\n");
      out.write("            else\n");
      out.write("            {\n");
      out.write("               // requestIcons();\n");
      out.write("            }\n");
      out.write("        }\n");
      out.write("\n");
      out.write("        document.close();   \n");
      out.write("        \n");
      out.write("    }\n");
      out.write("    \n");
      out.write("    function startCurrentMachine(_machineName)\n");
      out.write("    {\n");
      out.write("        \n");
      out.write("     var url = \"vBovJWS.jsp?data=startCurrentMachine\"; \n");
      out.write("        \n");
      out.write("        var req = initRequest();\n");
      out.write("        \n");
      out.write("        req.open(\"POST\", url, false);\n");
      out.write("        req.send(null);//no parameters\n");
      out.write("\n");
      out.write("        if (req.readyState == 4)\n");
      out.write("        {\n");
      out.write("            if (req.status == 200)\n");
      out.write("            {\n");
      out.write("                if(req.responseText == \"\")\n");
      out.write("                {\n");
      out.write("                    return \"\";\n");
      out.write("                }\n");
      out.write("                \n");
      out.write("                mainMachinePage(_machineName);\n");
      out.write("            }\n");
      out.write("            else\n");
      out.write("            {\n");
      out.write("               // requestIcons();\n");
      out.write("            }\n");
      out.write("        }\n");
      out.write("\n");
      out.write("        document.close();   \n");
      out.write("        \n");
      out.write("    }\n");
      out.write("    \n");
      out.write("    function printMannagedMachineList()\n");
      out.write("    {\n");
      out.write("        //alert(\"printMannagedMachineList()\");\n");
      out.write("        var url = \"vBovJWS.jsp?data=getListOfMachines\";\n");
      out.write("        var req = initRequest();\n");
      out.write("        var responseText = \"\";\n");
      out.write("        \n");
      out.write("        req.open(\"POST\", url, false);\n");
      out.write("        req.send(null);//no parameters\n");
      out.write("        //alert(\"printMannagedMachineList() : Sent\");\n");
      out.write("        if (req.readyState == 4)\n");
      out.write("        {\n");
      out.write("            if (req.status == 200)\n");
      out.write("            {\n");
      out.write("                //alert(\"req.status == 200\"); \n");
      out.write("                \n");
      out.write("                if(req.responseText == \"\")\n");
      out.write("                {\n");
      out.write("                    //alert(\"req.responseText == \\\"\\\"\");\n");
      out.write("                    return \"\";\n");
      out.write("                }\n");
      out.write("                \n");
      out.write("                var responseHTML = \"\";\n");
      out.write("                responseText = '(' + req.responseText + ')';\n");
      out.write("                var mannagedMachineList= eval(responseText);\n");
      out.write("                var numberOfMannagedMachines = mannagedMachineList.length;\n");
      out.write("                responseHTML += \"<ul data-role=\\\"listview\\\" >\";\n");
      out.write("                responseHTML += \"<li data-role=\\\"list-divider\\\">Pages</li>\";  \n");
      out.write("                \n");
      out.write("                for(var mannagedMachineCount = 0; mannagedMachineCount < numberOfMannagedMachines; mannagedMachineCount++)\n");
      out.write("                {\n");
      out.write("                    responseHTML +=\"<li><a onclick='mainMachinePage(\\\"\"+ mannagedMachineList[mannagedMachineCount].iDeviceName+ \"\\\")' href=\\\"\\\" >\"  + mannagedMachineList[mannagedMachineCount].iDeviceName +\": <div style='float:right'>\"+mannagedMachineList[mannagedMachineCount].iDeviceState +\"</div></a></li>\";\n");
      out.write("                }\n");
      out.write("\n");
      out.write("                responseHTML += \"</ul>\";\n");
      out.write("                //document.getElementById(\"machineListDiv\").innerHTML = responseHTML;\n");
      out.write("                $(\"#machineListDiv\").html(responseHTML);\n");
      out.write("                alert(responseHTML);\n");
      out.write("                \n");
      out.write("                \n");
      out.write("                $(\"#machineListDiv\").trigger(\"create\");\n");
      out.write("                \n");
      out.write("                //$(\"#machineListDiv\").listview(); //this line would prevent errors but prevents the list from viewing corectly\n");
      out.write("                //$(\"#machineListDiv\").listview(\"refresh\");\n");
      out.write("                \n");
      out.write("            }\n");
      out.write("        }\n");
      out.write("    }\n");
      out.write("    \n");
      out.write("    function mainMachinePage(_machineName)\n");
      out.write("    {\n");
      out.write("       var url = \"vBovJWS.jsp?data=getMachineDetails&machineName=\" + _machineName; \n");
      out.write("        var req = initRequest();\n");
      out.write("        \n");
      out.write("        req.open(\"POST\", url, false);\n");
      out.write("        req.send(null);//no parameters\n");
      out.write("\n");
      out.write("        if (req.readyState == 4)\n");
      out.write("        {\n");
      out.write("            if (req.status == 200)\n");
      out.write("            {\n");
      out.write("                if(req.responseText == \"\")\n");
      out.write("                {\n");
      out.write("                    \n");
      out.write("                    return \"\";\n");
      out.write("                }\n");
      out.write("                \n");
      out.write("                var responseHTML = \"\";\n");
      out.write("                var responseProgressesHTML = \"\";\n");
      out.write("                var responseTex = '(' + req.responseText + ')';\n");
      out.write("                var mannagedMachine = eval(responseTex);\n");
      out.write("                \n");
      out.write("                responseHTML += \"<h1>\" + mannagedMachine.mannagedBoxHostName +\" : \"+ mannagedMachine.iDeviceName+ \"</h1>\";\n");
      out.write("\n");
      out.write("                responseHTML += \"<table data-role=\\\"table\\\"  id=\\\"machineDetailTableId\\\" class=\\\"ui-responsive table-stroke\\\" style=\\\"display:table;\\\" ><thead><tr>\";\n");
      out.write("\n");
      out.write("                    responseHTML += \"<th >Current State</th>\"\n");
      out.write("                    responseHTML += \"<th >Host URL</th>\"\n");
      out.write("                    responseHTML += \"<th >RAM</th>\"\n");
      out.write("\n");
      out.write("                responseHTML += \"</tr></thead><tbody><tr>\";\n");
      out.write("\n");
      out.write("                    responseHTML += \"<td>\" + mannagedMachine.iDeviceState + \"</td>\";\n");
      out.write("                    responseHTML += \"<td>\" + mannagedMachine.mannagedBoxHostURL + \"</td>\";\n");
      out.write("                    responseHTML += \"<td>\" + mannagedMachine.mannagedBoxRam + \"</td>\";\n");
      out.write("\n");
      out.write("                responseHTML += \"</tr></tbody></table>\";\n");
      out.write("\n");
      out.write("\n");
      out.write("                var numberOfProgresses = mannagedMachine.progresses.length;\n");
      out.write("                \n");
      out.write("                //for(var numberOfProgressesCount = 0; numberOfProgressesCount < numberOfProgresses; numberOfProgressesCount++)\n");
      out.write("                for(var numberOfProgressesCount = numberOfProgresses-1; 0 < numberOfProgressesCount; numberOfProgressesCount--)\n");
      out.write("                {\n");
      out.write("                   responseProgressesHTML +=  \"<h2>\" + mannagedMachine.progresses[numberOfProgressesCount].dateCreatedString + \" \" + mannagedMachine.progresses[numberOfProgressesCount].operationDescription + \"</h2>\";\n");
      out.write("                }\n");
      out.write("               \n");
      out.write("                document.getElementById(\"contentDiv\").innerHTML = responseHTML + getMachineOptions(mannagedMachine) + responseProgressesHTML + \"<p></p>\";\n");
      out.write("                $(\"#machineDetailTableId\").table();\n");
      out.write("                $(\"#machineOptionsGroupId\").controlgroup();\n");
      out.write("                //$(\"#machineOptionsGroupId\").trigger(\"create\");\n");
      out.write("                //\n");
      out.write("//printMannagedMachineList();\n");
      out.write("            }\n");
      out.write("            else\n");
      out.write("            {\n");
      out.write("               // requestIcons();\n");
      out.write("            }\n");
      out.write("        }\n");
      out.write("\n");
      out.write("        document.close();\n");
      out.write("                \n");
      out.write("    }\n");
      out.write("    \n");
      out.write("    function getMachineOptions(_mannagedMachine)\n");
      out.write("    {\n");
      out.write("        var responseHTML = \"\";\n");
      out.write("        responseHTML += \"<div data-role='controlgroup'  id=\\\"machineOptionsGroupId\\\">\";\n");
      out.write("        \n");
      out.write("        if(_mannagedMachine.iDeviceState  == \"Running\")\n");
      out.write("        {\n");
      out.write("          responseHTML += \"<a onclick='stopCurrentMachine(\\\"\"+ _mannagedMachine.iDeviceName+\"\\\")' data-role='button' >Stop</a>\";\n");
      out.write("          responseHTML += \"<a href='machineSchedule.jsp?machineName=\"+ _mannagedMachine.iDeviceName+\"' data-role='button' >Schedule</a>\";\n");
      out.write("        }\n");
      out.write("\n");
      out.write("        if(_mannagedMachine.iDeviceState == \"PoweredOff\" || _mannagedMachine.iDeviceState  == \"Aborted\")\n");
      out.write("        {\n");
      out.write("            responseHTML += \"<a onclick='startCurrentMachine(\\\"\"+ _mannagedMachine.iDeviceName+\"\\\")' data-role='button' >Start</a>\";\n");
      out.write("            responseHTML += \"<a onclick=\\\"getMachineSchedule('\"+ _mannagedMachine.iDeviceName+\"')\\\" data-role='button' >Schedule</a>\";\n");
      out.write("           \n");
      out.write("            responseHTML += \"<a onclick='exportCurrentMachine()' data-role='button' >Export</a>\";\n");
      out.write("        }\n");
      out.write("        \n");
      out.write("       responseHTML += \"</div>\";\n");
      out.write("        \n");
      out.write("        return responseHTML;\n");
      out.write("    }\n");
      out.write("    \n");
      out.write("    function getMachineSchedule(_machineName)\n");
      out.write("    {\n");
      out.write("      //the <a href > that this is called from was not loading the target page correctly, not all scripts were loaded, this is a workaround.\n");
      out.write("        \n");
      out.write("        Global_SelectedMachineName = _machineName; \n");
      out.write("        //$.mobile.($(\"#machineSchedulePageId\"));\n");
      out.write("         \n");
      out.write("        //window.location.href=  \"machineSchedule.jsp?machineName=\"+ _machineName;\n");
      out.write("}\n");
      out.write("    \n");
      out.write("    function refreshMachineList()\n");
      out.write("    {\n");
      out.write("      var url = \"vBovJWS.jsp?data=rebuildManagerList\"; \n");
      out.write("        \n");
      out.write("        var req = initRequest();\n");
      out.write("        \n");
      out.write("        req.open(\"POST\", url, false);\n");
      out.write("        req.send(null);//no parameters\n");
      out.write("\n");
      out.write("        if (req.readyState == 4)\n");
      out.write("        {\n");
      out.write("            if (req.status == 200)\n");
      out.write("            {\n");
      out.write("                if(req.responseText == \"\")\n");
      out.write("                {\n");
      out.write("                    return \"\";\n");
      out.write("                }\n");
      out.write("           }\n");
      out.write("            else\n");
      out.write("            {\n");
      out.write("               // requestIcons();\n");
      out.write("            }\n");
      out.write("        }\n");
      out.write("\n");
      out.write("        document.close();   \n");
      out.write("        \n");
      out.write("        printMannagedMachineList();\n");
      out.write("    }\n");
      out.write("   \n");
      out.write("\n");
      out.write("</script>\n");
      out.write("\n");
      out.write("\n");
      out.write("    </head>\n");
      out.write("    \n");
      out.write("    <body>\n");
      out.write("\n");
      out.write("    <!-- Start of first page -->\n");
      out.write("    <div data-role=\"page\" id=\"indexPageId\">\n");
      out.write("\n");
      out.write("\t<div data-role=\"header\" data-theme=\"f\">\n");
      out.write("            <h1>Navigation System</h1>\n");
      out.write("            <a href=\"../../\" data-icon=\"home\" data-iconpos=\"notext\" data-direction=\"reverse\">Home</a>\n");
      out.write("            <a href=\"../nav.html\" data-icon=\"search\" data-iconpos=\"notext\" data-rel=\"dialog\" data-transition=\"fade\">Search</a>\n");
      out.write("        </div><!-- /header -->\n");
      out.write("\n");
      out.write("\t<div role=\"main\" class=\"ui-content\">\n");
      out.write("            <div style=\"width:20%; float:left;\">\n");
      out.write("                <ul data-role=\"listview\" data-insert=\"true\" data-divider-theme=\"a\">\n");
      out.write("                    <li data-role=\"list-divider\">Pages</li>\n");
      out.write("                    <li><a href=\"admin.jsp\">Admin</a></li>\n");
      out.write("                    <li><a onclick=\"refreshMachineList()\" >Refresh Machine List</a></li>\n");
      out.write("                </ul>\n");
      out.write("                <p></p>\n");
      out.write("                <div id=\"machineListDiv\" >\n");
      out.write("                </div>\n");
      out.write("            </div>\n");
      out.write("            <div style=\"width:78%; float:left; margin-left:2%;\">\n");
      out.write("                <div id=\"contentDiv\"></div>\n");
      out.write("            </div>\n");
      out.write("         </div>         \n");
      out.write("\n");
      out.write("        <div data-role=\"footer\" class=\"footer-docs\" data-theme=\"c\">\n");
      out.write("            <p>&copy; 2012 jQuery Foundation and other contributors</p>\n");
      out.write("        </div>\n");
      out.write("    </div><!-- /page -->\n");
      out.write("\n");
      out.write("<!-- Start of second page -->\n");
      out.write("<div data-role=\"page\" id=\"machineSchedulePageId\">\n");
      out.write("\n");
      out.write("\t<div data-role=\"header\">\n");
      out.write("\t\t<h1>Bar</h1>\n");
      out.write("\t</div><!-- /header -->\n");
      out.write("\n");
      out.write("\t<div role=\"main\" class=\"ui-content\">\n");
      out.write("\t\t<p>I'm the second in the source order so I'm hidden when the page loads. I'm just shown if a link that references my id is beeing clicked.</p>\n");
      out.write("\t\t<p><a href=\"#indexPageId\">Back to foo</a></p>\n");
      out.write("\t</div><!-- /content -->\n");
      out.write("\n");
      out.write("\t<div data-role=\"footer\">\n");
      out.write("\t\t<h4>Page Footer</h4>\n");
      out.write("\t</div><!-- /footer -->\n");
      out.write("</div><!-- /page -->\n");
      out.write("</body>\n");
      out.write("</html>\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
