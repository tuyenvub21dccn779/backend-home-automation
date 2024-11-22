USE automationhome;
-- Insert data into tblHome
INSERT INTO tblHome (id, address, description) 
VALUES (1, '123 Smart Street', 'A smart home equipped with IoT devices.');

-- Insert data into tblHomeOwner
INSERT INTO tblHomeOwner (id, username, password, name, tblHomeid) 
VALUES (1, 'homeowner1', 'password123', 'John Doe', 1);

-- Insert data into tblFloor
INSERT INTO tblFloor (id, name, description, tblHomeid) 
VALUES (1, 'First Floor', 'Main floor of the house', 1);

-- Insert data into tblRoom
INSERT INTO tblRoom (id, name, description, tblFloorid) 
VALUES (1, 'Living Room', 'Spacious living area', 1);

-- Insert data into tblDevice (for Fan and Bulb)
INSERT INTO tblDevice (id, name, description, location, state, mode) 
VALUES 
(1, 'Smart Fan', 'Ceiling fan with adjustable speed and temperature control', 'Living Room', 1, 1),
(2, 'Smart Bulb', 'Dimmable LED bulb', 'Living Room', 1, 1);

-- Insert data into tblFan
INSERT INTO tblFan (tblDeviceid, tblRoomid) 
VALUES (1, 1);

-- Insert sample speed data for a fan
INSERT INTO tblSpeed (id, speed, threshold, tblFanTblDeviceid) 
VALUES 
(1, 1, 20.0, 1), -- Speed level 1 with threshold 20.0 for fan with device ID 1
(2, 2, 25.0, 1), -- Speed level 2 with threshold 25.0 for fan with device ID 1
(3, 3, 30.0, 1); -- Speed level 3 with threshold 30.0 for fan with device ID 1


-- Insert data into tblBulb
INSERT INTO tblBulb (tblDeviceid, tblRoomid) 
VALUES (2, 1);

-- Insert data into tblSensor (for Flame Sensor, Light Sensor, and Temperature Sensor)
INSERT INTO tblSensor (id, name, description, location) 
VALUES 
(1, 'Flame Sensor', 'Detects fire or flames', 'Living Room'),
(2, 'Light Sensor', 'Measures ambient light levels', 'Living Room'),
(3, 'Temperature Sensor', 'Monitors room temperature', 'Living Room');

-- Insert data into tblFlameSensor
INSERT INTO tblFlameSensor (tblSensorid, tblRoomid) 
VALUES (1, 1);

-- Insert data into tblLightSensor
INSERT INTO tblLightSensor (tblSensorid, tblRoomid) 
VALUES (2, 1);

-- Insert data into tblTemperatureSensor
INSERT INTO tblTemperatureSensor (tblSensorid, accuracy, tblRoomid) 
VALUES (3, 0.5, 1);
