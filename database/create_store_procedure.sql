USE automationhome;
-- Get Bulbs by Room ID
DELIMITER $$
CREATE PROCEDURE getBulbsByRoomId(IN RoomId INT)
BEGIN
    SELECT d.id, d.name , d.description, d.state, d.mode, d.location
    FROM tblBulb b
    INNER JOIN tblDevice d ON b.tblDeviceid = d.id
    WHERE b.tblRoomid = RoomId;
END$$
DELIMITER ;

-- Get Fans by Room ID
DELIMITER $$
CREATE PROCEDURE getFansByRoomId(IN RoomId INT)
BEGIN
    SELECT d.id, d.name , d.description, d.state, d.mode, d.location
    FROM tblFan f
    INNER JOIN tblDevice d ON f.tblDeviceid = d.id
    WHERE f.tblRoomid = RoomId;
END$$
DELIMITER ;

-- Get Flame Sensors by Room ID
DELIMITER $$
CREATE PROCEDURE getFlameSensorsByRoomId(IN RoomId INT)
BEGIN
    SELECT s.id, s.name, s.description, s.location
    FROM tblFlameSensor fs
    INNER JOIN tblSensor s ON fs.tblSensorid = s.id
    WHERE fs.tblRoomid = RoomId;
END$$
DELIMITER ;

-- Get Light Sensors by Room ID
DELIMITER $$
CREATE PROCEDURE getLightSensorsByRoomId(IN RoomId INT)
BEGIN
    SELECT s.id, s.name, s.description, s.location
    FROM tblLightSensor ls
    INNER JOIN tblSensor s ON ls.tblSensorid = s.id
    WHERE ls.tblRoomid = RoomId;
END$$
DELIMITER ;

-- Get Temperature Sensors by Room ID
DELIMITER $$
CREATE PROCEDURE getTemperatureSensorsByRoomId(IN RoomId INT)
BEGIN
    SELECT s.id, s.name, s.description, s.location, ts.accuracy
    FROM tblTemperatureSensor ts
    INNER JOIN tblSensor s ON ts.tblSensorid = s.id
    WHERE ts.tblRoomid = RoomId;
END$$
DELIMITER ;

-- Get Speeds by Fan ID
DELIMITER $$
CREATE PROCEDURE getSpeedsByFanId(IN FanId INT)
BEGIN
    SELECT s.id, s.speed, s.threshold
    FROM tblSpeed s
    WHERE s.tblFanTblDeviceid = FanId;
END$$
DELIMITER ;
