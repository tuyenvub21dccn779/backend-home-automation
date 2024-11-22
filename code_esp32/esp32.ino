#include <ArduinoJson.h>
#include <SoftwareSerial.h>
#include <WiFi.h>
#include <HTTPClient.h>

// Wifi
// const char* ssid = "BachLan";
// const char* password = "24102005";

const char* ssid = "Samsunga12";
const char* password = "5432167891";

// String server = "http://192.168.1.238:8083/";
String server = "http://192.168.27.145:8083/";


// Timer variables
unsigned long lastTime = 0;
unsigned long timerDelay = 1000;  // Set timer to 6 seconds

// Pin definitions for SoftwareSerial
#define rxPin 16
#define txPin 17
SoftwareSerial mySerial(rxPin, txPin);

// Sensor data variables
String fan, bulb, lightsensor, flamesensor, temperaturesensor;

void setup() {
  // Define pin modes for TX and RX
  pinMode(rxPin, INPUT);
  pinMode(txPin, OUTPUT);
  Serial.begin(115200);
  mySerial.begin(19200);

  // Connect to Wi-Fi
  WiFi.begin(ssid, password);
  Serial.println("Connecting to WiFi...");
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.print("Connected to WiFi network with IP Address: ");
  Serial.println(WiFi.localIP());

  // Fetch initial sensor data
  fan = fetchSensorData("fan/1");
  bulb = fetchSensorData("bulb/2");
  lightsensor = fetchSensorData("lightsensor/2");
  flamesensor = fetchSensorData("flamesensor/1");
  temperaturesensor = fetchSensorData("temperaturesensor/3");

  // Print initial sensor values
  Serial.println("Initial Sensor Data:");
  Serial.println(fan);
  Serial.println(bulb);
  Serial.println(lightsensor);
  Serial.println(flamesensor);
  Serial.println(temperaturesensor);
}

void loop() {
  // Reconnect Wi-Fi if disconnected
  if (WiFi.status() != WL_CONNECTED) {
    reconnectWiFi();
  }

  // Send updated sensor data every `timerDelay` milliseconds
  if (millis() - lastTime >= timerDelay) {
    String fan = fetchSensorData("fan/1");
    String bulb = fetchSensorData("bulb/2");
    // Parse fan data
    StaticJsonDocument<100> fanDoc;
    DeserializationError fanError = deserializeJson(fanDoc, fan);
    if (fanError) {
      Serial.print(F("Fan JSON parsing failed: "));
      Serial.println(fanError.f_str());
      return;
    }

    // Parse bulb data
    StaticJsonDocument<100> bulbDoc;
    DeserializationError bulbError = deserializeJson(bulbDoc, bulb);
    if (bulbError) {
      Serial.print(F("Bulb JSON parsing failed: "));
      Serial.println(bulbError.f_str());
      return;
    }

    // Create new JSON structure for the output
    StaticJsonDocument<100> outputDoc;

    // Add fan data
    // JsonObject fan = outputDoc.createNestedObject("fan");
    outputDoc["sf"] = fanDoc["state"];
    outputDoc["mf"] = fanDoc["mode"];
    JsonArray thresholds = outputDoc.createNestedArray("thres");
    for (JsonObject speed : fanDoc["speeds"].as<JsonArray>()) {
      thresholds.add(speed["threshold"].as<int>());
    }

    // Add bulb data
    // JsonObject bulb = outputDoc.createNestedObject("bulb");
    outputDoc["sb"] = bulbDoc["state"];
    outputDoc["mb"] = bulbDoc["mode"];

    // Serialize and print the combined JSON structure
    String output;
    serializeJson(outputDoc, output);
    mySerial.println(output);
    Serial.println(output);


    lastTime = millis();  // Update last time for the next cycle
  }

  // Read and print data from Arduino (if available)
  if (mySerial.available() > 0) {
    String data = mySerial.readStringUntil('\n');
    StaticJsonDocument<100> doc;
    DeserializationError error = deserializeJson(doc, data);
    if (error) {
      Serial.print("deserializeJson() failed: ");
      Serial.println(error.c_str());
    } else {
      Serial.println(data);
      if (!doc.containsKey("type") || doc["type"].isNull()) {
        return;
      }
      String type = doc["type"];
      if (type == "sensordata") {
        if (!doc.containsKey("name") || doc["name"].isNull()
            || !doc.containsKey("value") || doc["value"].isNull()) {
          return;
        }
        String name = doc["name"];
        float value = doc["value"];
        StaticJsonDocument<100> outputDoc;
        outputDoc["value"] = value;
        if (name == "temperaturesensor") {
          DeserializationError error = deserializeJson(outputDoc["sensor"], temperaturesensor);
          if (error) {
            return;
          }
        } else if (name == "flamesensor") {
          DeserializationError error = deserializeJson(outputDoc["sensor"], flamesensor);
          if (error) {
            return;
          }
        } else if (name == "lightsensor") {
          DeserializationError error = deserializeJson(outputDoc["sensor"], lightsensor);
          if (error) {
            return;
          }
        }

        String output;
        serializeJson(outputDoc, output);
        httpPOSTRequest((server + "sensordata").c_str(), output);
      } else if (type == "device") {
        if (!doc.containsKey("name") || doc["name"].isNull()
            || !doc.containsKey("state") || doc["state"].isNull()
            || !doc.containsKey("mode") || doc["mode"].isNull()) {
          return;
        }
        String name = doc["name"];
        int state = doc["state"];
        int mode = doc["mode"];
        StaticJsonDocument<100> outputDoc;
        if (name == "fan") {
          Serial.println(fan);
          DeserializationError error = deserializeJson(outputDoc["fan"], fan);
          if (error) {
            return;
          }
          outputDoc["fan"]["state"] = state;
          outputDoc["fan"]["mode"] = mode;
        } else if (name == "bulb") {
          Serial.println(bulb);
          DeserializationError error = deserializeJson(outputDoc["bulb"], bulb);
          if (error) {
            return;
          }
          outputDoc["bulb"]["state"] = state;
          outputDoc["bulb"]["mode"] = mode;
        }
        
        String output;
        serializeJson(outputDoc, output);
        Serial.println(output);
        httpPOSTRequest((server + name + "controlhistory").c_str(), output);
      }
    }
  }
}

// Fetch sensor data from server (GET request)
String fetchSensorData(const String& sensorPath) {
  String response = httpGETRequest((server + sensorPath).c_str());
  return response;
}

// WiFi reconnection logic
void reconnectWiFi() {
  Serial.println("WiFi disconnected, reconnecting...");
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.print("Reconnected to WiFi with IP Address: ");
  Serial.println(WiFi.localIP());
}

// Get data from the server using HTTP GET request
String httpGETRequest(const char* serverName) {
  WiFiClient client;
  HTTPClient http;

  // Connect to server
  http.begin(client, serverName);

  // Send HTTP GET request
  int httpResponseCode = http.GET();

  String payload = "{}";  // Default empty JSON payload
  Serial.println("Get data from server...");
  if (httpResponseCode > 0) {
    Serial.print("HTTP Response code: ");
    Serial.println(httpResponseCode);
    payload = http.getString();  // Store the response
  } else {
    Serial.print("Error code: ");
    Serial.println(httpResponseCode);
  }

  http.end();  // Close HTTP connection
  return payload;
}

// POST data to the server
int httpPOSTRequest(const char* serverName, String data) {
  WiFiClient client;
  HTTPClient http;

  // Connect to server
  http.begin(client, serverName);

  // Set content type to JSON
  http.addHeader("Content-Type", "application/json");

  // Send HTTP POST request
  int httpResponseCode = http.POST(data);

  Serial.println("Post data to server...");
  Serial.print("HTTP Response code: ");
  Serial.println(httpResponseCode);

  http.end();  // Close HTTP connection
  return httpResponseCode;
}

// PUT data to the server
int httpPUTRequest(const char* serverName, String data) {
  WiFiClient client;
  HTTPClient http;

  // Connect to server
  http.begin(client, serverName);

  // Set content type to JSON
  http.addHeader("Content-Type", "application/json");

  // Send HTTP PUT request
  int httpResponseCode = http.PUT(data);

  Serial.println("Put data to server...");
  Serial.print("HTTP Response code: ");
  Serial.println(httpResponseCode);

  http.end();  // Close HTTP connection
  return httpResponseCode;
}
