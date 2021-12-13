
//########################## RTC DS1302 ########################################

// CONNECTIONS:
// DS1302 CLK/SCLK --> D15
// DS1302 DAT/IO --> D2
// DS1302 RST/CE --> D4
// DS1302 VCC --> 3.3v - 5v
// DS1302 GND --> GND

#include <ThreeWire.h>
#include <RtcDS1302.h>

ThreeWire myWire(4, 15, 2); // IO, SCLK, CE
RtcDS1302<ThreeWire> Rtc(myWire);

String buffertimestamp = "";
//########################## RTC DS1302 ########################################

//########################## LCD ########################################
#include <Wire.h>
#include <LiquidCrystal_I2C.h>

// Set the LCD address to 0x27 for a 16 chars and 2 line display
LiquidCrystal_I2C lcd(0x27, 16, 2);

//########################## LCD ########################################

//########################## RFID ########################################
#include <SPI.h>//https://www.arduino.cc/en/reference/SPI
#include <MFRC522.h>//https://github.com/miguelbalboa/rfid

//Constants
#define SS_PIN 5
#define RST_PIN 0

//Parameters
const int ipaddress[4] = {103, 97, 67, 25};

//Variables
byte nuidPICC[4] = {0, 0, 0, 0};
MFRC522::MIFARE_Key key;
MFRC522 rfid = MFRC522(SS_PIN, RST_PIN);
String UID = "";
//########################## RFID ########################################

//########################## FIREBASE ########################################
#if defined(ESP32)
#include <WiFi.h>
#include <FirebaseESP32.h>
#elif defined(ESP8266)
#include <ESP8266WiFi.h>
#include <FirebaseESP8266.h>
#endif

//Provide the token generation process info.
#include <addons/TokenHelper.h>

//Provide the RTDB payload printing info and other helper functions.
#include <addons/RTDBHelper.h>

/* 1. Define the WiFi credentials */
#define WIFI_SSID "Coki"
#define WIFI_PASSWORD "cindelcokitompel"

//For the following credentials, see examples/Authentications/SignInAsUser/EmailPassword/EmailPassword.ino

/* 2. Define the API Key */
#define API_KEY "AIzaSyB6BAGvkaM5rTPW2aY0dw3E_19nYUTKLhA"

/* 3. Define the RTDB URL */
#define DATABASE_URL "https://tugas-akhir-iot-b307d-default-rtdb.asia-southeast1.firebasedatabase.app/" //<databaseName>.firebaseio.com or <databaseName>.<region>.firebasedatabase.app

/* 4. Define the user Email and password that alreadey registerd or added in your project */
#define USER_EMAIL "vitorizki37@gmail.com"
#define USER_PASSWORD "lajelboy37"

//Define Firebase Data object
FirebaseData fbdo_driverName;
FirebaseData fbdo_timeStamp;
FirebaseData fbdo_isAuthorized;
FirebaseData fbdo_registeredUID;
FirebaseData fbdo_transportName;


FirebaseAuth auth;
FirebaseConfig config;

unsigned long sendDataPrevMillis = 0;

unsigned long count = 0;

//Buzzer Setup
#define buzzPin 2



void setup()
{
  Serial.begin(115200);

  //buzzer
  pinMode(buzzPin, OUTPUT);

  //LCD
  lcd.begin();
  lcd.backlight();

  //RTC
  Rtc.Begin();

  RtcDateTime compiled = RtcDateTime(__DATE__, __TIME__);
  printDateTime(compiled);
  Serial.println();

  if (!Rtc.IsDateTimeValid())
  {
    // Common Causes:
    //    1) first time you ran and the device wasn't running yet
    //    2) the battery on the device is low or even missing

    Serial.println("RTC lost confidence in the DateTime!");
    Rtc.SetDateTime(compiled);
  }

  if (Rtc.GetIsWriteProtected())
  {
    Serial.println("RTC was write protected, enabling writing now");
    Rtc.SetIsWriteProtected(false);
  }

  if (!Rtc.GetIsRunning())
  {
    Serial.println("RTC was not actively running, starting now");
    Rtc.SetIsRunning(true);
  }

  RtcDateTime now = Rtc.GetDateTime();
  if (now < compiled)
  {
    Serial.println("RTC is older than compile time!  (Updating DateTime)");
    Rtc.SetDateTime(compiled);
  }
  else if (now > compiled)
  {
    Serial.println("RTC is newer than compile time. (this is expected)");
  }
  else if (now == compiled)
  {
    Serial.println("RTC is the same as compile time! (not expected but all is fine)");
  }

  //FIREBASE
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED)
  {
    Serial.print(".");
    delay(300);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();

  Serial.printf("Firebase Client v%s\n\n", FIREBASE_CLIENT_VERSION);

  /* Assign the api key (required) */
  config.api_key = API_KEY;

  /* Assign the user sign in credentials */
  auth.user.email = USER_EMAIL;
  auth.user.password = USER_PASSWORD;

  /* Assign the RTDB URL (required) */
  config.database_url = DATABASE_URL;

  /* Assign the callback function for the long running token generation task */
  config.token_status_callback = tokenStatusCallback; //see addons/TokenHelper.h

  config.signer.tokens.legacy_token = "Vx6pn5Z6yywVOxbkiv59tVOfVIUt152JexmE09wW";

  Firebase.begin(&config, &auth);

  Firebase.reconnectWiFi(true);

  Firebase.setDoubleDigits(5);

  //################################################################################
  Serial.println(F("Initialize System"));
  //init rfid D8,D5,D6,D7
  SPI.begin();
  rfid.PCD_Init();

  Serial.print(F("Reader :"));
  rfid.PCD_DumpVersionToSerial();
  //################################################################################
}

void loop()
{
  boolean isValid = false;
  String drivername = "";
  String transport = "";

  readRFID();
  //Flash string (PROGMEM and  (FPSTR), String, C/C++ string, const char, char array, string literal are supported
  //in all Firebase and FirebaseJson functions, unless F() macro is not supported.
  if (Firebase.ready() && (millis() - sendDataPrevMillis > 2000 || sendDataPrevMillis == 0))
  {
    sendDataPrevMillis = millis();

    Firebase.setString(fbdo_timeStamp, "/Gate1/" + UID + "/TimeStamp", buffertimestamp) ? "ok" : fbdo_timeStamp.errorReason().c_str();

    //    Firebase.setInt(fbdo, "/Node2/WaterSensorValue", WaterSensorValue) ? "ok" : fbdo.errorReason().c_str();

    //get registeredUID
    Firebase.getString(fbdo_registeredUID, "/Access/" + UID ) ? fbdo_registeredUID.to<const char *>() : fbdo_registeredUID.errorReason().c_str();

    //get isAuthorized
    Firebase.getString(fbdo_isAuthorized, "/Access/" + UID + "/isAuthorized") ? fbdo_isAuthorized.to<const char *>() : fbdo_isAuthorized.errorReason().c_str();

    //get driverName
    Firebase.getString(fbdo_driverName, "/Access/" + UID + "/Driver") ? fbdo_driverName.to<const char *>() : fbdo_driverName.errorReason().c_str();
    drivername = (String) fbdo_driverName.to<const char *>();

    //get transport
    Firebase.getString(fbdo_transportName, "/Access/" + UID + "/Transport") ? fbdo_transportName.to<const char *>() : fbdo_transportName.errorReason().c_str();
    transport = (String) fbdo_transportName.to<const char *>();

    if (( (String) fbdo_registeredUID.to<const char *>() ) != "null" ) {
      if (( (String) fbdo_isAuthorized.to<const char *>() ) == "true" ) {
        isValid = true;
        //tampilkan lcd akses diterima
        updateLCD(drivername, transport);
      }
      else {
        isValid = false;
        //tampilkan lcd akses ditolak
        updateLCD2();
      }
    }
    else if (( (String) fbdo_registeredUID.to<const char *>() ) == "null" ) {
      isValid = false;
      //tampilkan lcd kartu tidak terdaftar
      updateLCD2();
    }
  }
}

void startBuzzer() {
  digitalWrite(buzzPin, HIGH);
  delay(3000);
  digitalWrite(buzzPin, LOW);
}

void readRFID(void ) { /* function readRFID */
  ////Read RFID card

  for (byte i = 0; i < 6; i++) {
    key.keyByte[i] = 0xFF;
  }
  // Look for new 1 cards
  if ( ! rfid.PICC_IsNewCardPresent())
    return;

  // Verify if the NUID has been readed
  if (  !rfid.PICC_ReadCardSerial())
    return;

  // Store NUID into nuidPICC array
  for (byte i = 0; i < 4; i++) {
    nuidPICC[i] = rfid.uid.uidByte[i];
  }

  Serial.print(F("RFID In dec: "));
  //  printDec(rfid.uid.uidByte, rfid.uid.size);
  UID = getDec(rfid.uid.uidByte, rfid.uid.size);
  Serial.println(UID);
  //  Serial.println();

  //getTimeStamp
  RtcDateTime now = Rtc.GetDateTime();
  getDateTime(now);

  //get

  // Halt PICC
  rfid.PICC_HaltA();

  // Stop encryption on PCD
  rfid.PCD_StopCrypto1();


}

/**
   Helper routine to dump a byte array as dec values to Serial.
*/
void printDec(byte *buffer, byte bufferSize) {
  for (byte i = 0; i < bufferSize; i++) {
    Serial.print(buffer[i] < 0x10 ? " 0" : " ");
    Serial.print(buffer[i], DEC);
  }
}

/**
   Helper routine to dump a byte array as dec values to Serial.
*/
String getDec(byte *buffer, byte bufferSize) {
  String UID = "";
  for (byte i = 0; i < bufferSize; i++) {
    UID += (String) buffer[i];
  }
  return UID;
}

#define countof(a) (sizeof(a) / sizeof(a[0]))

void getDateTime(const RtcDateTime& dt)
{
  char datestring[20];

  snprintf_P(datestring,
             countof(datestring),
             PSTR("%02u/%02u/%04u %02u:%02u:%02u"),
             dt.Month(),
             dt.Day(),
             dt.Year(),
             dt.Hour(),
             dt.Minute(),
             dt.Second() );
  buffertimestamp = datestring;
}

void printDateTime(const RtcDateTime& dt)
{
  char datestring[20];

  snprintf_P(datestring,
             countof(datestring),
             PSTR("%02u/%02u/%04u %02u:%02u:%02u"),
             dt.Month(),
             dt.Day(),
             dt.Year(),
             dt.Hour(),
             dt.Minute(),
             dt.Second() );
  Serial.println(datestring);
}

void updateLCD(String drivername, String transport) {
  lcd.clear();
  delay(1000);
  lcd.setCursor(0, 0);
  lcd.print(drivername);
  delay(2000);
  lcd.setCursor(0, 1);
  lcd.print(transport);
  delay(3000);

  lcd.clear();
  delay(1000);
  lcd.setCursor(0, 0);
  lcd.print(buffertimestamp.substring(0, 10));
  lcd.setCursor(0, 1);
  lcd.print(buffertimestamp.substring(10, 16) + " WIB");
  delay(1000);

  lcd.clear();
  delay(1000);
  lcd.setCursor(0, 0);
  lcd.print("CHECKPOINT ANDA");
  lcd.setCursor(0, 1);
  lcd.print("TELAH BERHASIL");
  delay(1000);
}
void updateLCD2() {
  lcd.clear();
  delay(2000);
  lcd.setCursor(0, 0);
  lcd.print("KARTU ANDA");
  lcd.setCursor(0, 1);
  lcd.print("TIDAK TERDAFTAR");
  delay(5000);
}
