vBox-Backup-Manager v1
===================

<h2>The Short</h2>

<p>Manage scheduled backups through any number of vBox hosts. (JQuery Mobile + JSP + MySQL)
Local Change.</p>


<h2>The Long</h2>

<ul>
<li>JDK 1.8</li>
<li>Virtual Box 4.2.0</li>
<li>jQuery Mobile 1.4.2</li>
<li>MySQL 5.1.10</li>
</ul>

<p>At its core is scheduling backups ( a Stop, Export, Start) . The Schedules can be recurring (e.g nightly, weekly...). Other schedule events such as Stop, Start, Re-Start can also be scheduled.</p>

<p>Each event’s success is logged and can be reviewed via the GUI.</p>

<p>You can manage several vBox hosts (vBox installs) and have all their machines managed in one place.</p>

<p>The interface is web based.</p>

<h2>Setup Overview</h2>
<ol>
<li>Setup requires a MySQL DB and the running of the included script to build the database.</li>
<li>Setting up a server JDBC resource (e.g. GlassFish). Screen shots of a GlassFish resource implementation are included.</li>
<li>Building the project.</li>
</ol>

<h2>Future</h2>
<ol>
<li>A Supporting Website.</li>
<li>Better internal documentation</li>
<li>System Monitoring/ Management</li>
</ol>

