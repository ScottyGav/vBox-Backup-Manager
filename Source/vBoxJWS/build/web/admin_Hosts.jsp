<%-- 
    Document   : admin
    Created on : 27/11/2013, 7:56:56 AM
    Author     : Admin
--%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page language="java" import="vBoxJwsTools.*" %>


<%
    VBoxHostMannager vBoxHostMannager = (VBoxHostMannager)getServletContext().getAttribute("vBoxHostMannager");

    String hostAlternateName= request.getParameter("alternativeHostName");
    String hostURL = request.getParameter("hostURL");
    String hostUserName = request.getParameter("userName");
    String hostPassword = request.getParameter("password");

    if(hostAlternateName != "" && hostAlternateName != null)
    {
      vBoxHostMannager.insertIDeviceHost(hostURL, hostAlternateName, hostUserName, hostPassword);  
    }
%>

<%

String data = request.getParameter("data");

if(data != null)
{
    if(data.equals("deleteHost"))
    {
        vBoxHostMannager.deleteIDeviceHost(hostURL); 
    }
}
%>

<%!

public String  printHostList()
{
    String resultHTML = "";
    VBoxHostMannager.MannagedBoxHost mannagedBoxHost  = null;

    VBoxHostMannager vBoxHostMannager = (VBoxHostMannager)getServletContext().getAttribute("vBoxHostMannager");
    ArrayList mannagedBoxHostsList = vBoxHostMannager.getMannagedBoxHostsList();

    resultHTML += "<table data-role=\"table\" id=\"HostListTableId\" class=\"ui-responsive table-stroke\" style=\"display:table;\" >";
    resultHTML += "<thead><tr><th>Host Name</th>";
    resultHTML += "<th>URL</th>";
    resultHTML += "<th></th></tr></thead><tbody>";

    for(Object mannagedBoxHostObject : mannagedBoxHostsList)
    {
        mannagedBoxHost = (VBoxHostMannager.MannagedBoxHost)mannagedBoxHostObject;
        resultHTML += "<tr><td>" + mannagedBoxHost.getIDeviceHostName() +" </td><td>"+mannagedBoxHost.getHostURL()+ "</td><td><a class=\"ui-btn ui-mini\" style=\"float:left; width:90%;\" href=\"admin_Hosts.jsp?data=deleteHost&hostURL="+mannagedBoxHost.getHostURL()+"\"  > Delete </a></td></tr>" ;
    }

    return resultHTML += "</tbody></table>";
}

%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        
        <link rel="stylesheet" href="jquery/jquery.mobile-1.4.2.min.css" />
	<script src="jquery/jquery-1.9.1.min.js"></script>
	<script src="jquery/jquery.mobile-1.4.2.min.js"></script>
        
        <script src="centerfire.js"></script>
    </head>
    <body>
        
       <div data-role="page" class="type-interior" id="one">
            
            <div data-role="header" data-theme="f">
		<h2>Add vBox Web Server (Host)</h2>
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
                            <li data-role="list-divider">Options</li>
                            <li><a href="admin_Hosts.jsp">Hosts</a></li>
                            <li><a href="admin_BackupPath.jsp">Backup Paths</a></li>
                            <li><a href="vBoxJWS.jsp" rel="external">Machines</a></li>
                        </div>
                        
                        <div id="machineListDiv" >    
                        </div>
                </div>
                <div style="width:78%; float:left; margin-left:2%;">
                  
                    <div id="contentDiv">
                        <h2>Adding a host</h2>
                        
                        <p>Each vbox host must be running the vBox Web Server. The following code placed with in a .bat file will get this done.</p>
                        <pre><code>
                        cd /.
                        cd C:\Program Files\Oracle\VirtualBox\
                        VBoxWebSrv -H 'the.hosts.IP.Address'                        
                        pause
                        </code></pre>
                        
                        <p>Once the required details below are saved, vBoxJWS will connect to the host and create a list of all the machines to be managed from that and every host saved.</p>
                        
                        <form action="admin_Hosts.jsp" method="post">
                            <label for="alternativeHostName">Alternative Host Name:</label>
                            <input type="text" name="alternativeHostName" id="alternativeHostName" value="" placeholder="Something Descriptive" />

                            <label for="hostURL">Host URL:</label>
                            <input type="text" name="hostURL" id="hostURL" value="" placeholder="http://10.1.1.12:18083/" />

                            <label for="userName">User Name:</label>
                            <input type="text" name="userName" id="userName" value="" placeholder="not used" />

                            <label for="password">Password:</label>
                            <input type="text" name="password" id="password" value="" placeholder="not used" />

                            <button type="submit" >Add Machine</button>
                            
                        </form>
                        
                         <h2>vBox Web Servers</h2>
                        
                        <%
                            out.println(printHostList());
                        %>
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
            }
        }

        document.close();   
        
        printMannagedMachineList();
    }
</script>
        </div>
   </body>
</html>
