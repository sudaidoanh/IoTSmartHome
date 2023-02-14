import sys
import RPi.GPIO as GPIO
import Adafruit_DHT as dht
import pyrebase
import time

config = {
    "apiKey": "AIzaSyBqkX6_iG60ijhP8ugTv9KTsE1n3Yj5b7A",
    "authDomain": "graduationproject-391b2.firebaseapp.com",
    "databaseURL": "https://graduationproject-391b2-default-rtdb.asia-southeast1.firebasedatabase.app",
    "projectId": "graduationproject-391b2",
    "storageBucket": "graduationproject-391b2.appspot.com",
}

firebase = pyrebase.initialize_app(config)
db = firebase.database()

GPIO.setmode(GPIO.BCM)

livingroom_dht = 21
bedroom_dht = 2
kitchen_dht = 3


sensor_dht = dht.DHT11

while True:
    living_hum, living_temp = dht.read_retry(sensor_dht, livingroom_dht)
    bedroom_hum, bedroom_temp = dht.read_retry(sensor_dht, bedroom_dht)
    kitchen_hum, kitchen_temp = dht.read_retry(sensor_dht, kitchen_dht)
    #lingving_hum, tempAdafruit_DHT.read_retry(sensor_dht, livingroom_dht)
    t = time.localtime()
    current_time = time.strftime("%Y:%m:%d:%H:%M", t)
    # print(t)
    # sys.exit()

    if living_temp is not None and living_hum is not None and bedroom_hum is not None and bedroom_temp is not None:
        print(
            'Living Room: Temp={0:0.1f}*C  Humidity={1:0.1f}%'.format(living_temp, living_hum))
        print(
            'Bedroom: Temp={0:0.1f}*C  Humidity={1:0.1f}%'.format(bedroom_temp, bedroom_hum))
        print(
            'Kitchen: Temp={0:0.1f}*C  Humidity={1:0.1f}%'.format(kitchen_temp, kitchen_hum))

        if living_temp > 30:
            db.child("livingroom").child("fan").set(
                {"status": 'true', "laston": current_time})

        if bedroom_temp > 30:
            db.child("bedroom").child("fan").set(
                {"status": 'true', "laston": current_time})

        if kitchen_temp > 30:
            db.child("kitchen").child("fan").set(
                {"status": 'true', "laston": current_time})

        data = {
            "livingroom/" + current_time + "/": {
                "temperature": living_temp,
                "humidity": living_hum
            },
            "livingroom/current/": {
                "temperature": living_temp,
                "humidity": living_hum
            },
            "bedroom/" + current_time + "/": {
                "temperature": bedroom_temp,
                "humidity": bedroom_hum
            },
            "bedroom/current/": {
                "temperature": bedroom_temp,
                "humidity": bedroom_hum
            },

            "kitchen/" + current_time + "/": {
                "temperature": kitchen_temp,
                "humidity": kitchen_hum
            },
            "kitchen/current/": {
                "temperature": kitchen_temp,
                "humidity": kitchen_hum
            }
        }
        db.update(data)
    else:
        print('Failed to get reading. Try again!')

    time.sleep(60*60)
