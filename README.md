vBox-Backup-Manager v1
===================

<h2>The Short</h2>

<p>Manage scheduled backups through any number of vBox hosts. (JQuery Mobile + JSP + MySQL)
Local Change.</p>


<h2>The Long</h2>

<p>At its core is scheduling backups ( a Stop, Export, Start) . The Schedules can be recurring (e.g nightly, weekly...). Other schedule events such as Stop, Start, Re-Start can also be scheduled.</p>

<p>Each event’s success is logged and can be reviewed via the GUI.</p>

<p>You can manage several vBox hosts (vBox installs) and have all their machines managed in one place.</p>

<p>The interface is web based.</p>

<h2>Setup Overview</h2>
<ol>
<li>Setup requires a MySQL DB and the running of the included script to build the tables and stored procedures.</li>
<li>Setting a server JDBC resource (e.g. GlassFish)</li>
<li>Placing the WAR in your web directory</li>
</ol>

<h2>Future</h2>
<ol>
<li>A Supporting Website.</li>
<li>Better internal documentation</li>
<li>System Monitoring/ Management</li>
</ol>

