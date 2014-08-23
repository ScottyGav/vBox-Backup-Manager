<%-- 
    Document   : admin
    Created on : 27/11/2013, 7:56:56 AM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page language="java" import="vBoxJwsTools.*" %>
<!DOCTYPE html>
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
        if(data.equals("rebuildManagerList"))
        {
            //rebuild the JWS server
            vBoxHostMannager.refreshMannagedMachines();
        }
    }
%>   
    
<%!
    public void requeryJWS()
    {
        VBoxHostMannager vBoxHostMannager = (VBoxHostMannager)getServletContext().getAttribute("vBoxHostMannager");
        vBoxHostMannager.refreshMannagedMachines();
    }
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>jQuery Mobile Demos</title>
        
        <link rel="stylesheet" href="jquery/jquery.mobile-1.4.2.min.css" />
        <script src="jquery/jquery-1.9.1.min.js"></script>
        <script src="jquery/jquery.mobile-1.4.2.min.js"></script>
        <script src="centerfire.js"></script>
        
    </head>
    <body>
        
       <div data-role="page" class="type-interior" id="one">
            
            <div data-role="header" data-theme="f">
                <h1>Navigation System</h1>
                <a href="../../" data-icon="home" data-iconpos="notext" data-direction="reverse">Home</a>
                <a href="../nav.html" data-icon="search" data-iconpos="notext" data-rel="dialog" data-transition="fade">Search</a>
            </div><!-- /header -->
            
            <div role="main" class="ui-content">

                <div style="width:20%; float:left;">
                    
                    <div data-role="listview" data-theme="c" data-dividertheme="d">
                        <li data-role="list-divider">Pages</li>
                        <li><a href="vBoxJWS.jsp" rel="external">Machines</a></li>
                    </div>
                        
                    <p></p>
                    <div data-role="listview" data-theme="c" data-dividertheme="d">
                        <li data-role="list-divider">Admin Options</li>
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
            
            <div data-role="footer" class="footer-docs" data-theme="c">
                <p>&copy; 2012 jQuery Foundation and other contributors</p>
            </div>
            
            <script type="text/javascript">
                function refreshMachineList()
                {
                  var url = "vBoxJWS.jsp?data=rebuildManagerList"; 

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
                       }
                        else
                        {
                           // requestIcons();
                        }
                    }

                    document.close();   

                    printMannagedMachineList();
                }
            </script>
        </div>
     </body>
</html>
