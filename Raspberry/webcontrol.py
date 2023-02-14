import RPi.GPIO as GPIO
import pyrebase
import pyfirmata
import time
board = pyfirmata.Arduino("/dev/ttyACM0")

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
GPIO.setwarnings(False)

led_livingroom = 20
led_bedroom = 16
led_kitchen = 12
led_toilet = 7
GPIO.setup(led_livingroom, GPIO.OUT)
GPIO.setup(led_bedroom, GPIO.OUT)
GPIO.setup(led_kitchen, GPIO.OUT)
GPIO.setup(led_toilet, GPIO.OUT)

fan_livingroom = 4
fan_bedroom = 5
fan_kitchen = 6


def tobool(a):
    if a == "true":
        return 1
    else:
        return 0


def handleLightLivingRoom(a):
    GPIO.output(led_livingroom, tobool(a["data"]))


def handleFanLivingRoom(a):
    board.digital[fan_livingroom].write(tobool(a["data"]))


def handleLightBedroom(a):
    GPIO.output(led_bedroom, tobool(a["data"]))


def handleFanBedroom(a):
    board.digital[fan_bedroom].write(tobool(a["data"]))


def handleLightKitchen(a):
    GPIO.output(led_kitchen, tobool(a["data"]))


def handleFanKitchen(a):
    board.digital[fan_kitchen].write(tobool(a["data"]))


def handleLightToilet(a):
    GPIO.output(led_toilet, tobool(a["data"]))


lightLivingRoomStream = db.child(
    "livingroom/light/status").stream(handleLightLivingRoom)
fanLivingRoomStream = db.child(
    "livingroom/fan/status").stream(handleFanLivingRoom)

lighBedroomtream = db.child("bedroom/light/status").stream(handleLightBedroom)
fanBedroomStream = db.child("bedroom/fan/status").stream(handleFanBedroom)

lightKitchenStream = db.child(
    "kitchen/light/status").stream(handleLightKitchen)
fanKitchenStream = db.child("kitchen/fan/status").stream(handleFanKitchen)

lightToiletStream = db.child("toilet/light/status").stream(handleLightToilet)
