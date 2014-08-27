<%-- 
    Document   : admin
    Created on : 27/11/2013, 7:56:56 AM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page language="java" import="vBoxJwsTools.*" %>
<!DOCTYPE html>
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
        if(data.equals("rebuildManagerList"))
        {
            //rebuild the JWS server.
            vBoxHostManager.refreshManagedMachines();
			//rebuild the JWS server.
        }
    }
%>   
    
<%!
    public void requeryJWS()
    {
        VBoxHostManager vBoxHostManager = (VBoxHostManager)getServletContext().getAttribute("vBoxHostManager");
        vBoxHostManager.refreshManagedMachines();
    }
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        
        <link rel="stylesheet" href="css/themes/vBoxJWS.min.css" />
        <link rel="stylesheet" href="css/themes/jquery.mobile.icons.min.css" />
        
        <link rel="stylesheet" href="http://code.jquery.com/mobile/1.4.3/jquery.mobile.structure-1.4.3.min.css" />
        <script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
        <script src="http://code.jquery.com/mobile/1.4.3/jquery.mobile-1.4.3.min.js"></script>
        
    </head>
    <body>
        
       <div data-role="page" class="type-interior" id="AdminPageId">
            
            <div data-role="header" data-theme="a">
                <h1>vBoxManager</h1>
                <a href="vBoxJWS.jsp" rel="external" data-icon="home" data-iconpos="notext" data-direction="reverse">Home</a>
            </div><!-- /header -->
            
            <div role="main" class="ui-content">

                <div style="width:20%; float:left;">
                    
                    <div data-role="listview" data-theme="c" data-dividertheme="d">
                        <!--li data-role="list-divider">Pages</li-->
                        <li><a href="vBoxJWS.jsp" rel="external">Machines</a></li>
                    </div>
                        
                    <p></p>
                    <div data-role="listview" data-theme="c" data-dividertheme="d">
                        <li data-role="list-divider" data-theme="a">Admin Options</li>
                        <li><a href="admin_Hosts.jsp">Hosts</a></li>
                        <li><a href="admin_BackupPath.jsp">Backup Paths</a></li>
                        <li><a onclick="refreshMachineList()" >Refresh Machine List</a></li>
                    </div>

                    <div id="machineListDiv" >
                    </div>
                </div> 

                <div style="width:78%; float:left; margin-left:2%;">
                    <div id="contentDiv">
                        
                    </div>
                </div>
            
            </div>
            
            <div data-role="footer" class="footer-docs" data-theme="a">
               <p></p>
            </div>
            
            
            
            <script type="text/javascript">
                function refreshMachineList()
                {
                    var ajax = $.ajax(
                    {
                        url:"vBoxJWS.jsp?data=rebuildManagerList",
                        dataType: "json",
                        type: "POST",
                        async: false,//wait for the response
                        data:
                        {

                        }
                    });    

                    ajax.done(function()
                    {
                        printManagedMachineList();
                    }); 
                 }
            </script>
        </div>
     </body>
</html>
