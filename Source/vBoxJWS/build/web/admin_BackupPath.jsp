<%-- 
    Document   : admin_BackupPath
    Created on : 05/01/2014, 11:55:30 AM
    Author     : Admin
--%>

<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>


<%@ page language="java" import="vBoxJwsTools.*" %>
<!DOCTYPE html>
<%
    VBoxHostMannager vBoxHostMannager = (VBoxHostMannager)getServletContext().getAttribute("vBoxHostMannager");

    //Ajax postbacks
    String data = request.getParameter("data");
    if(data != null)
    {
        if(data.equals("deletePath"))
        {
            String pathId = request.getParameter("pathId");
            vBoxHostMannager.deleteBackupPath(pathId);
        }
    }

    //form submittals
    String backupPath = request.getParameter("backupPath");
    if(backupPath != "" && backupPath != null)
    {
      vBoxHostMannager.addBackupPath( backupPath);  
    }
%>

<%!

public String printBackupPaths()
{
    VBoxHostMannager vBoxHostMannager = (VBoxHostMannager)getServletContext().getAttribute("vBoxHostMannager");
    String htmlResponce = "";
    ArrayList BackupPathList = vBoxHostMannager.getBackupPaths();

    int listLength = BackupPathList.size();

    htmlResponce += "<table data-role=\"table\" id=\"HostListTableId\" class=\"ui-responsive table-stroke\" style=\"display:table;\" >";
    htmlResponce += "<thead><tr><th>Backup Path</th>";
    htmlResponce += "<th></th></thead><tbody>";

    for(int count = 0; count < listLength; count++ )
    {
        String[] eventPair = (String[]) BackupPathList.get(count);
        String id = eventPair[0];
        String path = eventPair[1];
        htmlResponce += "<tr><td>" + path +" </td><td><a class=\"ui-btn ui-mini\" style=\"float:left; width:90%;\" href=\"admin_BackupPath.jsp?data=deletePath&pathId="+id+"\" > Delete </a></td></tr>" ;
    }

    return htmlResponce+= "</tbody></table>";
}

%>

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
		<h1>Navigation System</h1>
		<a href="../../" data-icon="home" data-iconpos="notext" data-direction="reverse">Home</a>
		<a href="../nav.html" data-icon="search" data-iconpos="notext" data-rel="dialog" data-transition="fade">Search</a>
	</div><!-- /header -->
            
             <div role="main" class="ui-content">
                <div style="width:20%; float:left;">
                    <div data-role="listview" data-theme="c" data-dividertheme="d">
                        <li data-role="list-divider">Pages</li>
                        <li><a href="vBoxJWS.jsp" rel="external" >Machines</a></li>
                    </div>
                    <p></p>
                    <div data-role="listview" data-theme="c" data-dividertheme="d">
                        <li data-role="list-divider">Admin Options</li>
                        <li><a href="admin_Hosts.jsp">Hosts</a></li>
                        <li><a href="admin_BackupPath.jsp">Backup Paths</a></li>
                        <li><a href="vBoxJWS.jsp" rel="external">Machines</a></li>
                    </div>

                    <div id="machineListDiv" >
                    </div>
                </div>
		<!--/content-primary -->

                <div style="width:78%; float:left; margin-left:2%;">
                    <div id="contentDiv">
                        <h2>Add Backup Path</h2>
                        <p>Allows for paths to be added, for consistancy during backups</p>
                        
                         <form action="admin_BackupPath.jsp" method="post">
                            <label for="backupPath">Backup Path:</label>
                            <input type="text" name="backupPath" id="backupPath" value="" placeholder="//Some/shared/location" />
                            
                            <button type="submit" >Add Path</button>
                         </form>
                        
                        <h2>Saved Backup Paths</h2>
                        
                        <%
                            out.println(printBackupPaths());
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
