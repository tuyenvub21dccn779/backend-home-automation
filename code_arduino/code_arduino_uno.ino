#include <Wire.h>
#include <LiquidCrystal_I2C.h>
#include "DHT.h"
#include <ArduinoJson.h>
#include <SoftwareSerial.h>

// Sensor and WiFi setup
#define DHTPIN 10
#define LIGHTSENSOR 11
#define FLAMESENSOR 9
#define INRELAY1 8
#define DHTTYPE DHT11
#define rxPin 12
#define txPin 13
#define BUFFER_SIZE 256

unsigned long lastTime = 0;
unsigned long timerDelay = 60000;
DHT dht(DHTPIN, DHTTYPE);
SoftwareSerial mySerial(rxPin, txPin);

// Motor pins
int in1 = 6, in2 = 7, ena = 5;

// Initialize LCD
LiquidCrystal_I2C lcd(0x27, 16, 2);

// Variables
float thresholdsFan[3];  // Adjust size as needed for fan speed thresholds
int stateBulb = -1, stateFan = -1, modeBulb = -1, modeFan = -1;
int lightSensorLast = -1, flameSensorLast = -1;
int stateBulbLast = -1, stateFanLast = -1;
float temperatureLast = NAN;

// char serialBuffer[BUFFER_SIZE];
// int bufferIndex = 0;

void setup() {
  Serial.begin(115200);
  lcd.init();
  lcd.backlight();
  mySerial.begin(19200);
  dht.begin();

  // Initialize pins
  pinMode(in1, OUTPUT);
  pinMode(in2, OUTPUT);
  pinMode(ena, OUTPUT);
  pinMode(INRELAY1, OUTPUT);
  pinMode(LIGHTSENSOR, INPUT);
  pinMode(FLAMESENSOR, INPUT);

  lcd.clear();
  lcd.print("Initializing...");


  StaticJsonDocument<100> doc;
  String data;
  do {
    if (mySerial.available()) {
      data = mySerial.readStringUntil('\n');

      DeserializationError error = deserializeJson(doc, data);
      if (error) {
        Serial.print("Failed to parse ");
        Serial.print(": ");
        Serial.println(error.c_str());
      } else {
        Serial.println("data received:");
        Serial.println(data);

        if (doc.containsKey("sb") && !doc["sb"].isNull()
            && doc.containsKey("mb") && !doc["mb"].isNull()) {
          stateBulb = doc["sb"];
          modeBulb = doc["mb"];

        }
        if (doc.containsKey("sf") && !doc["sf"].isNull()
            && doc.containsKey("mf") && !doc["mf"].isNull()
            && doc.containsKey("thres") && !doc["thres"].isNull()) {
          JsonArray thresholds = doc["thres"];
          stateFan = doc["sf"];
          modeFan = doc["mf"];
          
          for (int i = 0; i < 3; i++) {
            thresholdsFan[i] = thresholds[i].as<int>();
          }
        }
      }
    }
  } while (doc.isNull());
  lcd.clear();
}

// Function to clear the buffer
// void clearBuffer() {
//   memset(serialBuffer, 0, BUFFER_SIZE);
//   bufferIndex = 0;
// }

void loop() {
  if (modeBulb == 1 && stateBulb != stateBulbLast) {
    sendBulbData();
  }

  if (modeFan == 1 && stateFan != stateFanLast) {
    sendFanData();
  }
  StaticJsonDocument<100> doc;
  if (mySerial.available()) {
    String data = mySerial.readStringUntil('\n');
    Serial.println(data);
    DeserializationError error = deserializeJson(doc, data);
    if (error) {
      Serial.print("Failed to parse ");
      Serial.print(": ");
      Serial.println(error.c_str());
    } else {
      Serial.println("data received:");
      Serial.println(data);
      if (doc.containsKey("sb") && !doc["sb"].isNull()
          && doc.containsKey("mb") && !doc["mb"].isNull()) {
        stateBulb = doc["sb"];
        modeBulb = doc["mb"];
      }
      if (doc.containsKey("sf") && !doc["sf"].isNull()
          && doc.containsKey("mf") && !doc["mf"].isNull()
          && doc.containsKey("thres") && !doc["thres"].isNull()) {
        JsonArray thresholds = doc["thres"];
        stateFan = doc["sf"];
        modeFan = doc["mf"];
        stateFanLast = stateFan;
        for (int i = 0; i < 3; i++) {
          thresholdsFan[i] = thresholds[i].as<int>();
        }
        // if (modeFan == 1 && stateFan != stateFanLast) {
        //   sendFanData();
        // }
      }
    }
  }



  updateSensorStates();
}





// Function to update sensor states
void updateSensorStates() {
  float temperature = dht.readTemperature();
  int lightSensor = digitalRead(LIGHTSENSOR);
  int flameSensor = digitalRead(FLAMESENSOR);

  lcd.setCursor(0, 0);
  lcd.print("Temp:");
  if (!isnan(temperature)) {
    lcd.print(temperature);
    sendPeriodicData(temperature);
    lcd.print(" C");
  }



  // Light sensor status change
  if (lightSensor != lightSensorLast) {
    sendSensorData("lightsensor", lightSensor);
    lightSensorLast = lightSensor;
  }

  // Flame sensor status change
  if (flameSensor != flameSensorLast) {
    sendSensorData("flamesensor", flameSensor);
    flameSensorLast = flameSensor;
  }

  // Temperature periodic update

  if (flameSensor == 1) {
    digitalWrite(in1, LOW);
    digitalWrite(in2, HIGH);
  } else {
    digitalWrite(in1, LOW);
    digitalWrite(in2, LOW);
    stateFan = 0;
    stateBulb = 0;
  }
  controlFan(temperature);
  controlBulb();
}

void controlFan(float temperature) {
  if (modeFan == 1 && flameSensorLast == 1) {  // Auto mode
    int speedLevel = 0;
    for (int i = 0; i < 3; i++) {
      if (temperature >= thresholdsFan[i]) {
        speedLevel = i + 1;
      }
    }
    stateFan = speedLevel;
  }

  if (stateFan == 0) {
    analogWrite(ena, 0);
    lcd.setCursor(0, 1);
    lcd.print("Fan Speed: 0");
  } else {
    int pwmValue = map(stateFan, 1, 3, 85, 255);
    analogWrite(ena, pwmValue);
    lcd.setCursor(0, 1);
    lcd.print("Fan Speed: ");
    lcd.print(stateFan);
  }
}

void controlBulb() {
  if (modeBulb == 1 && flameSensorLast == 1) {
    stateBulb = lightSensorLast;
  }
  digitalWrite(INRELAY1, stateBulb);
}

void sendFanData() {
  StaticJsonDocument<100> doc;
  doc["type"] = "device";
  doc["name"] = "fan";
  doc["state"] = stateFan;
  doc["mode"] = modeFan;
  doc["threshold"] = thresholdsFan[stateFan - 1];
  String output;
  serializeJson(doc, output);
  mySerial.println(output);
  Serial.println(output);
  stateFanLast = stateFan;
}

void sendBulbData() {
  StaticJsonDocument<100> doc;
  doc["type"] = "device";
  doc["name"] = "bulb";
  doc["state"] = stateBulb;
  doc["mode"] = modeBulb;
  String output;
  serializeJson(doc, output);
  mySerial.println(output);
  Serial.println(output);
  stateBulbLast = stateBulb;
}

void sendSensorData(const char* sensorName, int sensorValue) {
  StaticJsonDocument<100> doc;
  doc["type"] = "sensordata";
  doc["name"] = sensorName;
  doc["value"] = sensorValue;
  String output;
  serializeJson(doc, output);
  mySerial.println(output);
  Serial.println(output);
}

void sendPeriodicData(float temperature) {
  if (millis() - lastTime >= timerDelay) {
    StaticJsonDocument<100> doc;
    doc["type"] = "sensordata";
    doc["name"] = "temperaturesensor";
    doc["value"] = temperature;
    String output;
    serializeJson(doc, output);
    mySerial.println(output);
    Serial.println(output);
    lastTime = millis();
  }
}
