/*
MySQL Data Transfer
Source Host: localhost
Source Database: vboxjws
Target Host: localhost
Target Database: vboxjws
Date: 10/07/2014 2:07:03 PM
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for tblbackuppaths
-- ----------------------------
CREATE TABLE `tblbackuppaths` (
  `pkBackupPathId` int(11) NOT NULL AUTO_INCREMENT,
  `backupPath` text,
  PRIMARY KEY (`pkBackupPathId`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for tblmachineschedules
-- ----------------------------
CREATE TABLE `tblmachineschedules` (
  `pkMachineScheduleId` int(11) NOT NULL AUTO_INCREMENT,
  `fkManagedMachineId` int(11) NOT NULL,
  `fkScheduleEventTypeId` int(11) NOT NULL,
  `startDateTime` datetime NOT NULL,
  `fkScheduleEventFrequencyId` int(11) NOT NULL,
  `scheduleConfigText` varchar(100) NOT NULL DEFAULT '' COMMENT 'for capturing additional information, eg a file path,',
  `fkStatusId` int(11) DEFAULT NULL,
  PRIMARY KEY (`pkMachineScheduleId`)
) ENGINE=MyISAM AUTO_INCREMENT=63 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for tblmanagedboxhost
-- ----------------------------
CREATE TABLE `tblmanagedboxhost` (
  `pkManagedBoxHostId` int(11) NOT NULL AUTO_INCREMENT,
  `hostURL` varchar(255) NOT NULL,
  `hostAlternateName` varchar(255) NOT NULL,
  `hostUserName` varchar(255) NOT NULL,
  `hostPassword` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`pkManagedBoxHostId`)
) ENGINE=MyISAM AUTO_INCREMENT=25 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for tblmanagedmachine
-- ----------------------------
CREATE TABLE `tblmanagedmachine` (
  `pkManagedMachineId` int(11) NOT NULL AUTO_INCREMENT,
  `fkManagedBoxHostId` int(11) DEFAULT NULL,
  `machineName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`pkManagedMachineId`)
) ENGINE=MyISAM AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for tblscheduleeventfrequency
-- ----------------------------
CREATE TABLE `tblscheduleeventfrequency` (
  `pkfkScheduleEventFrequencyId` int(11) NOT NULL AUTO_INCREMENT,
  `scheduleEventFrequency` varchar(100) NOT NULL,
  PRIMARY KEY (`pkfkScheduleEventFrequencyId`)
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for tblscheduleeventtypes
-- ----------------------------
CREATE TABLE `tblscheduleeventtypes` (
  `pkScheduleEventTypeId` int(11) NOT NULL AUTO_INCREMENT,
  `scheduduleEventType` varchar(100) NOT NULL,
  PRIMARY KEY (`pkScheduleEventTypeId`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for tblschedulereciepts
-- ----------------------------
CREATE TABLE `tblschedulereciepts` (
  `pkScheduleRecieptId` int(11) NOT NULL AUTO_INCREMENT,
  `fkMachineScheduleId` int(11) DEFAULT NULL,
  `fkScheduleRecieptStatusId` int(11) DEFAULT NULL,
  `completedDateTime` datetime DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`pkScheduleRecieptId`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for tblschedulerecieptstatus
-- ----------------------------
CREATE TABLE `tblschedulerecieptstatus` (
  `pkScheduleRecieptStatusId` int(11) NOT NULL AUTO_INCREMENT,
  `scheduleRecieptStatus` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`pkScheduleRecieptStatusId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for tblschedulestatuses
-- ----------------------------
CREATE TABLE `tblschedulestatuses` (
  `pkScheduleStatusId` int(11) NOT NULL AUTO_INCREMENT,
  `ScheduleStatus` text,
  PRIMARY KEY (`pkScheduleStatusId`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Procedure structure for spDeleteBackupPathId
-- ----------------------------
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `spDeleteBackupPathId`(backupPathId int)
delete FROM
tblbackuppaths
WHERE
tblbackuppaths.pkBackupPathId =  backupPathId;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for spDeleteMachineSchedule
-- ----------------------------
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `spDeleteMachineSchedule`(_machineScheduleId int)
delete from tblmachineschedules
where tblmachineschedules.pkMachineScheduleId = _machineScheduleId;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for spDeleteManagedBoxHost
-- ----------------------------
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `spDeleteManagedBoxHost`(hostURL varchar(255))
delete FROM
tblmanagedboxhost
WHERE
tblmanagedboxhost.hostURL =  hostURL;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for spDeleteScheduleReciepts
-- ----------------------------
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `spDeleteScheduleReciepts`(_machineScheduleId int)
delete FROM
tblschedulereciepts
WHERE
tblschedulereciepts.fkMachineScheduleId =  _machineScheduleId;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for spInsertBackupPath
-- ----------------------------
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `spInsertBackupPath`(_backupPath text)
insert into tblbackuppaths
(backupPath)
values
(_backupPath);;
DELIMITER ;

-- ----------------------------
-- Procedure structure for spInsertMachineSchedule
-- ----------------------------
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `spInsertMachineSchedule`(_fkManagedMachineId int,_fkScheduleEventTypeId int,_startDateTime datetime,_fkScheduleEventFrequencyId int,_scheduleConfigText varchar(100))
insert into tblmachineschedules
(fkManagedMachineId,
fkScheduleEventTypeId,
startDateTime,
fkScheduleEventFrequencyId,
scheduleConfigText
)
values
(_fkManagedMachineId,
_fkScheduleEventTypeId,
_startDateTime,
_fkScheduleEventFrequencyId,
_scheduleConfigText);;
DELIMITER ;

-- ----------------------------
-- Procedure structure for spInsertManagedBoxHost
-- ----------------------------
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `spInsertManagedBoxHost`(_hostURL text, _hostAlternateName text, _hostUserName text, _hostPassword text)
insert into tblmanagedboxhost
(hostURL,hostAlternateName,hostUserName,hostPassword) 
values 
(_hostURL,_hostAlternateName,_hostUserName,_hostPassword);;
DELIMITER ;

-- ----------------------------
-- Procedure structure for spInsertManagedMachine
-- ----------------------------
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `spInsertManagedMachine`(_managedBoxHostId int,  _machineName varchar(255))
insert into tblmanagedmachine (fkManagedBoxHostId,  machineName ) values (_managedBoxHostId,  _machineName);;
DELIMITER ;

-- ----------------------------
-- Procedure structure for spInsertScheduleReciept
-- ----------------------------
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `spInsertScheduleReciept`(_fkMachineScheduleId int,  _fkScheduleRecieptStatusId int, _description varchar(200))
insert into
tblschedulereciepts

(fkMachineScheduleId,fkScheduleRecieptStatusId,completedDateTime,description)

values

(_fkMachineScheduleId, _fkScheduleRecieptStatusId, NOW(), _description);;
DELIMITER ;

-- ----------------------------
-- Procedure structure for spSelectBackupPaths
-- ----------------------------
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `spSelectBackupPaths`()
SELECT
tblbackuppaths.pkBackupPathId,
tblbackuppaths.backupPath
FROM
tblbackuppaths;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for spSelectMachineSchedules
-- ----------------------------
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `spSelectMachineSchedules`(_machineId int)
SELECT
tblmachineschedules.pkMachineScheduleId,
tblmachineschedules.fkManagedMachineId,
tblmachineschedules.fkScheduleEventTypeId,
tblmachineschedules.startDateTime,
tblmachineschedules.fkScheduleEventFrequencyId,
tblmachineschedules.scheduleConfigText

FROM
tblmachineschedules
Inner Join tblscheduleeventtypes ON tblscheduleeventtypes.pkScheduleEventTypeId = tblmachineschedules.fkScheduleEventTypeId
Inner Join tblscheduleeventfrequency ON tblscheduleeventfrequency.pkfkScheduleEventFrequencyId = tblmachineschedules.fkScheduleEventFrequencyId


WHERE
tblmachineschedules.fkManagedMachineId =  _machineId;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for spSelectManagedBoxHosts
-- ----------------------------
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `spSelectManagedBoxHosts`()
SELECT
tblmanagedboxhost.pkManagedBoxHostId,
tblmanagedboxhost.hostURL,
tblmanagedboxhost.hostAlternateName,
tblmanagedboxhost.hostUserName,
tblmanagedboxhost.hostPassword
FROM
tblmanagedboxhost;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for spSelectManagedMachines
-- ----------------------------
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `spSelectManagedMachines`()
SELECT
tblmanagedmachine.pkManagedMachineId,
tblmanagedmachine.machineName,
tblmanagedboxhost.hostURL,
tblmanagedboxhost.hostAlternateName
FROM
tblmanagedmachine
Inner Join tblmanagedboxhost ON tblmanagedboxhost.pkManagedBoxHostId = tblmanagedmachine.fkManagedBoxHostId;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for spSelectManagedMachinesForHostId
-- ----------------------------
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `spSelectManagedMachinesForHostId`(hostId int)
SELECT
tblmanagedmachine.pkManagedMachineId,
tblmanagedmachine.machineName

FROM
tblmanagedmachine
WHERE
tblmanagedmachine.fkManagedBoxHostId =  hostId;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for spSelectScheduleEventTypeForId
-- ----------------------------
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `spSelectScheduleEventTypeForId`(typeId int)
SELECT
tblscheduleeventtypes.pkScheduleEventTypeId,
tblscheduleeventtypes.scheduduleEventType
FROM
tblscheduleeventtypes
where pkScheduleEventTypeId = typeId;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for spSelectScheduleEventTypes
-- ----------------------------
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `spSelectScheduleEventTypes`()
SELECT
tblscheduleeventtypes.pkScheduleEventTypeId,
tblscheduleeventtypes.scheduduleEventType
FROM
tblscheduleeventtypes;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for spSelectScheduleFrequencies
-- ----------------------------
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `spSelectScheduleFrequencies`()
SELECT
tblscheduleeventfrequency.pkfkScheduleEventFrequencyId,
tblscheduleeventfrequency.scheduleEventFrequency
FROM
tblscheduleeventfrequency;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for spSelectScheduleFrequencyForId
-- ----------------------------
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `spSelectScheduleFrequencyForId`(frequencyId int)
SELECT
tblscheduleeventfrequency.pkfkScheduleEventFrequencyId,
tblscheduleeventfrequency.scheduleEventFrequency
FROM
tblscheduleeventfrequency
WHERE
tblscheduleeventfrequency.pkfkScheduleEventFrequencyId =  frequencyId;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for spSelectScheduleRecieptsForScheduleId
-- ----------------------------
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `spSelectScheduleRecieptsForScheduleId`(_machineScheduleId int)
SELECT
tblschedulereciepts.pkScheduleRecieptId,
tblschedulereciepts.fkMachineScheduleId,
tblschedulereciepts.fkScheduleRecieptStatusId,
tblschedulereciepts.completedDateTime,
tblschedulereciepts.description,
tblschedulerecieptstatus.scheduleRecieptStatus
FROM
tblschedulereciepts
Inner Join tblschedulerecieptstatus ON tblschedulerecieptstatus.pkScheduleRecieptStatusId = tblschedulereciepts.fkScheduleRecieptStatusId
WHERE
tblschedulereciepts.fkMachineScheduleId =  _machineScheduleId;;
DELIMITER ;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `tblbackuppaths` VALUES ('6', '\\\\10.1.1.10\\public');
INSERT INTO `tblmachineschedules` VALUES ('44', '7', '2', '2014-04-28 18:10:00', '4', 'null', null);
INSERT INTO `tblmachineschedules` VALUES ('42', '7', '2', '2014-04-12 16:29:00', '9', 'null', null);
INSERT INTO `tblmachineschedules` VALUES ('43', '9', '2', '2014-04-15 07:38:00', '9', 'null', null);
INSERT INTO `tblmachineschedules` VALUES ('45', '10', '1', '2014-07-01 22:38:00', '1', 'null', null);
INSERT INTO `tblmachineschedules` VALUES ('46', '11', '1', '2014-07-01 22:42:00', '1', 'null', null);
INSERT INTO `tblmachineschedules` VALUES ('47', '11', '2', '2014-07-01 22:42:00', '1', 'null', null);
INSERT INTO `tblmachineschedules` VALUES ('48', '12', '1', '2014-07-01 23:04:00', '1', 'null', null);
INSERT INTO `tblmachineschedules` VALUES ('57', '16', '1', '2014-07-09 08:05:00', '1', 'null', null);
INSERT INTO `tblmachineschedules` VALUES ('56', '16', '1', '2014-07-09 08:03:00', '1', 'null', null);
INSERT INTO `tblmachineschedules` VALUES ('58', '16', '1', '2014-07-09 08:06:00', '1', 'null', null);
INSERT INTO `tblmanagedboxhost` VALUES ('1', 'http://10.1.1.12:18083/', 'Virtual Dev - XP', ' ', ' ');
INSERT INTO `tblmanagedboxhost` VALUES ('21', 'http://10.1.1.11:18083', 'Win 8', 'x', 'x');
INSERT INTO `tblmanagedmachine` VALUES ('7', '21', 'x Clone');
INSERT INTO `tblmanagedmachine` VALUES ('9', '21', 'Games');
INSERT INTO `tblmanagedmachine` VALUES ('10', '0', 'Games');
INSERT INTO `tblmanagedmachine` VALUES ('11', '0', 'Games');
INSERT INTO `tblmanagedmachine` VALUES ('12', '0', 'Games');
INSERT INTO `tblmanagedmachine` VALUES ('13', '0', 'x Clone');
INSERT INTO `tblmanagedmachine` VALUES ('14', '0', 'x Clone');
INSERT INTO `tblmanagedmachine` VALUES ('15', '0', 'x Clone');
INSERT INTO `tblmanagedmachine` VALUES ('16', '0', 'x Clone');
INSERT INTO `tblmanagedmachine` VALUES ('17', '0', 'x Clone');
INSERT INTO `tblmanagedmachine` VALUES ('18', '0', 'x Clone');
INSERT INTO `tblmanagedmachine` VALUES ('19', '0', 'x Clone');
INSERT INTO `tblmanagedmachine` VALUES ('20', '0', 'x Clone');
INSERT INTO `tblscheduleeventfrequency` VALUES ('1', 'Once Off');
INSERT INTO `tblscheduleeventfrequency` VALUES ('2', 'Every 12 Hours');
INSERT INTO `tblscheduleeventfrequency` VALUES ('3', 'Every 24 Hours');
INSERT INTO `tblscheduleeventfrequency` VALUES ('4', 'Every 7 Days');
INSERT INTO `tblscheduleeventfrequency` VALUES ('5', 'Every 14 Days');
INSERT INTO `tblscheduleeventfrequency` VALUES ('6', 'Every 28 Days');
INSERT INTO `tblscheduleeventfrequency` VALUES ('7', 'Every 10 Minutes (For your Testing)');
INSERT INTO `tblscheduleeventfrequency` VALUES ('8', 'Every 5 Minutes (For your Testing)');
INSERT INTO `tblscheduleeventfrequency` VALUES ('9', 'Every 1 Minute (For your testing)');
INSERT INTO `tblscheduleeventtypes` VALUES ('1', 'Backup (Export Allpiance)');
INSERT INTO `tblscheduleeventtypes` VALUES ('2', 'Restart');
INSERT INTO `tblscheduleeventtypes` VALUES ('3', 'Shut Down');
INSERT INTO `tblscheduleeventtypes` VALUES ('4', 'Start Up');
INSERT INTO `tblschedulereciepts` VALUES ('1', '1', '1', null, null);
INSERT INTO `tblschedulereciepts` VALUES ('2', '42', '2', '2014-04-28 17:57:34', 'Scheduled Task: \'Restart\' succeeded in starting up.');
INSERT INTO `tblschedulereciepts` VALUES ('3', '42', '2', '2014-04-28 17:57:34', 'Scheduled Task: \'Restart\' succeeded in shutting down.');
INSERT INTO `tblschedulereciepts` VALUES ('4', '42', '2', '2014-04-28 17:58:26', 'Scheduled Task: \'Restart\' succeeded in shutting down.');
INSERT INTO `tblschedulereciepts` VALUES ('5', '42', '2', '2014-04-28 17:58:27', 'Scheduled Task: \'Restart\' succeeded in starting up.');
INSERT INTO `tblschedulereciepts` VALUES ('6', '42', '2', '2014-04-28 17:59:23', 'Scheduled Task: \'Restart\' succeeded in shutting down.');
INSERT INTO `tblschedulereciepts` VALUES ('7', '42', '2', '2014-04-28 17:59:26', 'Scheduled Task: \'Restart\' succeeded in starting up.');
INSERT INTO `tblschedulereciepts` VALUES ('8', '42', '2', '2014-04-28 18:00:23', 'Scheduled Task: \'Restart\' succeeded in shutting down.');
INSERT INTO `tblschedulereciepts` VALUES ('9', '42', '2', '2014-04-28 18:00:26', 'Scheduled Task: \'Restart\' succeeded in starting up.');
INSERT INTO `tblschedulereciepts` VALUES ('10', '42', '2', '2014-04-28 18:01:23', 'Scheduled Task: \'Restart\' succeeded in shutting down.');
INSERT INTO `tblschedulereciepts` VALUES ('11', '42', '2', '2014-04-28 18:01:26', 'Scheduled Task: \'Restart\' succeeded in starting up.');
INSERT INTO `tblschedulereciepts` VALUES ('12', '42', '2', '2014-04-28 18:02:23', 'Scheduled Task: \'Restart\' succeeded in shutting down.');
INSERT INTO `tblschedulereciepts` VALUES ('13', '42', '2', '2014-04-28 18:02:26', 'Scheduled Task: \'Restart\' succeeded in starting up.');
INSERT INTO `tblschedulereciepts` VALUES ('14', '42', '2', '2014-04-28 18:03:23', 'Scheduled Task: \'Restart\' succeeded in shutting down.');
INSERT INTO `tblschedulereciepts` VALUES ('15', '42', '2', '2014-04-28 18:03:26', 'Scheduled Task: \'Restart\' succeeded in starting up.');
INSERT INTO `tblschedulereciepts` VALUES ('16', '42', '2', '2014-04-28 18:04:23', 'Scheduled Task: \'Restart\' succeeded in shutting down.');
INSERT INTO `tblschedulereciepts` VALUES ('17', '42', '2', '2014-04-28 18:04:26', 'Scheduled Task: \'Restart\' succeeded in starting up.');
INSERT INTO `tblschedulereciepts` VALUES ('18', '42', '2', '2014-04-28 18:05:23', 'Scheduled Task: \'Restart\' succeeded in shutting down.');
INSERT INTO `tblschedulereciepts` VALUES ('19', '42', '2', '2014-04-28 18:05:26', 'Scheduled Task: \'Restart\' succeeded in starting up.');
INSERT INTO `tblschedulereciepts` VALUES ('20', '42', '2', '2014-04-28 18:06:23', 'Scheduled Task: \'Restart\' succeeded in shutting down.');
INSERT INTO `tblschedulereciepts` VALUES ('21', '42', '2', '2014-04-28 18:06:26', 'Scheduled Task: \'Restart\' succeeded in starting up.');
INSERT INTO `tblschedulereciepts` VALUES ('22', '42', '2', '2014-04-28 18:07:23', 'Scheduled Task: \'Restart\' succeeded in shutting down.');
INSERT INTO `tblschedulereciepts` VALUES ('23', '42', '2', '2014-04-28 18:07:26', 'Scheduled Task: \'Restart\' succeeded in starting up.');
INSERT INTO `tblschedulereciepts` VALUES ('24', '42', '2', '2014-04-28 18:08:23', 'Scheduled Task: \'Restart\' succeeded in shutting down.');
INSERT INTO `tblschedulereciepts` VALUES ('25', '42', '2', '2014-04-28 18:08:26', 'Scheduled Task: \'Restart\' succeeded in starting up.');
INSERT INTO `tblschedulereciepts` VALUES ('26', '42', '2', '2014-04-28 18:09:23', 'Scheduled Task: \'Restart\' succeeded in shutting down.');
INSERT INTO `tblschedulereciepts` VALUES ('27', '42', '2', '2014-04-28 18:09:26', 'Scheduled Task: \'Restart\' succeeded in starting up.');
INSERT INTO `tblschedulereciepts` VALUES ('28', '42', '2', '2014-04-28 18:10:23', 'Scheduled Task: \'Restart\' succeeded in shutting down.');
INSERT INTO `tblschedulereciepts` VALUES ('29', '42', '2', '2014-04-28 18:10:26', 'Scheduled Task: \'Restart\' succeeded in starting up.');
INSERT INTO `tblschedulereciepts` VALUES ('30', '0', '2', '2014-04-28 18:11:15', 'Scheduled Task: \'Restart\' succeeded in shutting down.');
INSERT INTO `tblschedulereciepts` VALUES ('31', '0', '2', '2014-04-28 18:11:18', 'Scheduled Task: \'Restart\' succeeded in starting up.');
INSERT INTO `tblschedulereciepts` VALUES ('32', '42', '2', '2014-04-28 18:11:23', 'Scheduled Task: \'Restart\' succeeded in shutting down.');
INSERT INTO `tblschedulereciepts` VALUES ('33', '42', '1', '2014-04-28 18:12:23', 'Scheduled Task: \'Restart\' failed to stop the machine. It was possibly already shut down.');
INSERT INTO `tblschedulereciepts` VALUES ('34', '42', '2', '2014-04-28 18:12:26', 'Scheduled Task: \'Restart\' succeeded in starting up.');
INSERT INTO `tblschedulereciepts` VALUES ('35', '42', '2', '2014-04-28 18:13:23', 'Scheduled Task: \'Restart\' succeeded in shutting down.');
INSERT INTO `tblschedulereciepts` VALUES ('36', '42', '2', '2014-04-28 18:13:26', 'Scheduled Task: \'Restart\' succeeded in starting up.');
INSERT INTO `tblschedulereciepts` VALUES ('37', '42', '2', '2014-04-28 18:14:23', 'Scheduled Task: \'Restart\' succeeded in shutting down.');
INSERT INTO `tblschedulereciepts` VALUES ('38', '42', '2', '2014-04-28 18:14:26', 'Scheduled Task: \'Restart\' succeeded in starting up.');
INSERT INTO `tblschedulereciepts` VALUES ('39', '1', '1', '0000-00-00 00:00:00', 'test');
INSERT INTO `tblschedulereciepts` VALUES ('40', '0', '1', '2014-07-01 22:38:50', 'Scheduled Task: \'Backup (Export Allpiance)\' failed to stop the machine. It was possibly already shut down.');
INSERT INTO `tblschedulereciepts` VALUES ('41', '52', '1', '2014-07-08 08:11:03', 'Scheduled Task: \'Backup (Export Allpiance)\' failed to stop the machine. It was possibly already shut down.');
INSERT INTO `tblschedulereciepts` VALUES ('42', '53', '1', '2014-07-08 08:15:41', 'Scheduled Task: \'Backup (Export Allpiance)\' failed to stop the machine. It was possibly already shut down.');
INSERT INTO `tblschedulereciepts` VALUES ('43', '56', '1', '2014-07-09 08:04:07', 'Scheduled Task: \'Backup (Export Allpiance)\' failed to stop the machine. It was possibly already shut down.');
INSERT INTO `tblschedulereciepts` VALUES ('44', '57', '1', '2014-07-09 08:06:01', 'Scheduled Task: \'Backup (Export Allpiance)\' failed to stop the machine. It was possibly already shut down.');
INSERT INTO `tblschedulereciepts` VALUES ('45', '0', '1', '2014-07-09 08:09:45', 'Scheduled Task: \'Backup (Export Allpiance)\' failed to stop the machine. It was possibly already shut down.');
INSERT INTO `tblschedulerecieptstatus` VALUES ('1', 'Failed');
INSERT INTO `tblschedulerecieptstatus` VALUES ('2', 'Completed');
INSERT INTO `tblschedulestatuses` VALUES ('1', 'Running');
INSERT INTO `tblschedulestatuses` VALUES ('2', 'Paused');
INSERT INTO `tblschedulestatuses` VALUES ('3', 'Stopped');
INSERT INTO `tblschedulestatuses` VALUES ('4', 'No Status Set');
