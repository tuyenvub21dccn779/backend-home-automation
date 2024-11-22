-- Step 0: Drop the database if it already exists
DROP DATABASE IF EXISTS automationhome;

-- Step 1: Create the database
CREATE DATABASE automationhome;
USE automationhome;

-- Step 2: Create Tables

CREATE TABLE tblHomeOwner (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50),
    password VARCHAR(50),
    name VARCHAR(250),
    tblHomeid INT
);

CREATE TABLE tblHome (
    id INT AUTO_INCREMENT PRIMARY KEY,
    address VARCHAR(250),
    description VARCHAR(500)
);

CREATE TABLE tblFloor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(250),
    description VARCHAR(500),
    tblHomeid INT
);

CREATE TABLE tblRoom (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(250),
    description VARCHAR(500),
    tblFloorid INT
);

CREATE TABLE tblDevice (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(250),
    description VARCHAR(500),
    location VARCHAR(250),
    state INT,
    mode INT
);

CREATE TABLE tblBulb (
    tblDeviceid INT PRIMARY KEY,
    tblRoomid INT
);

CREATE TABLE tblFan (
    tblDeviceid INT PRIMARY KEY,
    tblRoomid INT
);

CREATE TABLE tblDeviceControlHistory (
    id INT AUTO_INCREMENT PRIMARY KEY,
    starttime DATETIME,
    endtime DATETIME,
    mode INT,
    state INT
);

CREATE TABLE tblBulbControlHistory (
    tblDeviceControlHistoryid INT PRIMARY KEY,
    tblBulbtblDeviceid INT,
    tblHomeOwnerid INT
);

CREATE TABLE tblFanControlHistory (
    tblDeviceControlHistoryid INT PRIMARY KEY,
    threshold FLOAT,
    tblHomeOwnerid INT,
    tblFantblDeviceid INT
);

CREATE TABLE tblSpeedModifyHistory (
	id INT AUTO_INCREMENT PRIMARY KEY,
    speed INT,
    threshold FLOAT,
    tblHomeOwnerid INT,
    time DATETIME,
    tblSpeedid INT
);

CREATE TABLE tblSensor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(250),
    description VARCHAR(500),
    location VARCHAR(250)
);

CREATE TABLE tblLightSensor (
    tblSensorid INT PRIMARY KEY,
    tblRoomid INT
);

CREATE TABLE tblTemperatureSensor (
    tblSensorid INT PRIMARY KEY,
    accuracy FLOAT,
    tblRoomid INT
);

CREATE TABLE tblFlameSensor (
    tblSensorid INT PRIMARY KEY,
    tblRoomid INT
);

CREATE TABLE tblSpeed (
    id INT AUTO_INCREMENT PRIMARY KEY,
    speed INT,
    threshold FLOAT,
    tblFanTblDeviceid INT
);

CREATE TABLE tblSensorData (
    id INT AUTO_INCREMENT PRIMARY KEY,
    time DATETIME,
    value FLOAT,
    tblSensorid INT
);

-- Step 3: Add Foreign Keys

-- tblHomeOwner
ALTER TABLE tblHomeOwner
ADD CONSTRAINT fk_HomeOwner_Home FOREIGN KEY (tblHomeid) REFERENCES tblHome(id);

-- tblFloor
ALTER TABLE tblFloor
ADD CONSTRAINT fk_Floor_Home FOREIGN KEY (tblHomeid) REFERENCES tblHome(id);

-- tblRoom
ALTER TABLE tblRoom
ADD CONSTRAINT fk_Room_Floor FOREIGN KEY (tblFloorid) REFERENCES tblFloor(id);

-- tblBulb
ALTER TABLE tblBulb
ADD CONSTRAINT fk_Bulb_Device FOREIGN KEY (tblDeviceid) REFERENCES tblDevice(id),
ADD CONSTRAINT fk_Bulb_Room FOREIGN KEY (tblRoomid) REFERENCES tblRoom(id);

-- tblFan
ALTER TABLE tblFan
ADD CONSTRAINT fk_Fan_Device FOREIGN KEY (tblDeviceid) REFERENCES tblDevice(id),
ADD CONSTRAINT fk_Fan_Room FOREIGN KEY (tblRoomid) REFERENCES tblRoom(id);

-- tblDeviceControlHistory does not have dependencies, no foreign keys added here

-- tblBulbControlHistory
ALTER TABLE tblBulbControlHistory
ADD CONSTRAINT fk_BulbControlHistory_Bulb FOREIGN KEY (tblBulbtblDeviceid) REFERENCES tblBulb(tblDeviceid),
ADD CONSTRAINT fk_BulbControlHistory_HomeOwner FOREIGN KEY (tblHomeOwnerid) REFERENCES tblHomeOwner(id),
ADD CONSTRAINT fk_BulbControlHistory_DeviceControlHistory 
    FOREIGN KEY (tblDeviceControlHistoryid) REFERENCES tblDeviceControlHistory(id);

-- tblFanControlHistory
ALTER TABLE tblFanControlHistory
ADD CONSTRAINT fk_FanControlHistory_Fan FOREIGN KEY (tblFantblDeviceid) REFERENCES tblFan(tblDeviceid),
ADD CONSTRAINT fk_FanControlHistory_HomeOwner FOREIGN KEY (tblHomeOwnerid) REFERENCES tblHomeOwner(id),
ADD CONSTRAINT fk_FanControlHistory_DeviceControlHistory 
    FOREIGN KEY (tblDeviceControlHistoryid) REFERENCES tblDeviceControlHistory(id);

-- tblThresholdSpeedModifyHistory
ALTER TABLE tblSpeedModifyHistory
ADD CONSTRAINT fk_SpeedModifyHistory_Fan FOREIGN KEY (tblSpeedid) REFERENCES tblSpeed(id),
ADD CONSTRAINT fk_SpeedModifyHistory_HomeOwner FOREIGN KEY (tblHomeOwnerid) REFERENCES tblHomeOwner(id);


-- tblLightSensor
ALTER TABLE tblLightSensor
ADD CONSTRAINT fk_LightSensor_Sensor FOREIGN KEY (tblSensorid) REFERENCES tblSensor(id),
ADD CONSTRAINT fk_LightSensor_Room FOREIGN KEY (tblRoomid) REFERENCES tblRoom(id);

-- tblTemperatureSensor
ALTER TABLE tblTemperatureSensor
ADD CONSTRAINT fk_TemperatureSensor_Sensor FOREIGN KEY (tblSensorid) REFERENCES tblSensor(id),
ADD CONSTRAINT fk_TemperatureSensor_Room FOREIGN KEY (tblRoomid) REFERENCES tblRoom(id);

-- tblFlameSensor
ALTER TABLE tblFlameSensor
ADD CONSTRAINT fk_FlameSensor_Sensor FOREIGN KEY (tblSensorid) REFERENCES tblSensor(id),
ADD CONSTRAINT fk_FlameSensor_Room FOREIGN KEY (tblRoomid) REFERENCES tblRoom(id);

-- tblSpeed
ALTER TABLE tblSpeed
ADD CONSTRAINT fk_Speed_Fan FOREIGN KEY (tblFanTblDeviceid) REFERENCES tblFan(tblDeviceid);

-- tblSensorData
ALTER TABLE tblSensorData
ADD CONSTRAINT fk_SensorData_Sensor FOREIGN KEY (tblSensorid) REFERENCES tblSensor(id);
