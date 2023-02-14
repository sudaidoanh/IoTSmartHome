import pyfirmata
import time
import pyrebase
import RPi.GPIO as GPIO
import telegram
GPIO.setmode(GPIO.BCM)
buzzer = 27
GPIO.setup(buzzer, GPIO.OUT)
GPIO.setup(buzzer, GPIO.LOW)

board = pyfirmata.Arduino("/dev/ttyACM0")
it = pyfirmata.util.Iterator(board)
it.start()
config = {
    "apiKey": "AIzaSyBqkX6_iG60ijhP8ugTv9KTsE1n3Yj5b7A",
    "authDomain": "graduationproject-391b2.firebaseapp.com",
    "databaseURL": "https://graduationproject-391b2-default-rtdb.asia-southeast1.firebasedatabase.app",
    "projectId": "graduationproject-391b2",
    "storageBucket": "graduationproject-391b2.appspot.com",
}

firebase = pyrebase.initialize_app(config)
db = firebase.database()

gas = board.analog[0]
gas.enable_reporting()
gas_value = gas.read()
time.sleep(2)
print('gas sensor is running')

my_token = "5819273152:AAGARCW67briVR9xdmJAqbQIIyk5HACYsfA"
bot = telegram.Bot(token=my_token)

while True:
    gas_value = float(gas.read())
    print(gas_value)
    if gas_value > 0:
        GPIO.output(buzzer, 1)
        time.sleep(3)
        GPIO.output(buzzer, 0)
        t = time.localtime()
        current_time = time.strftime("%Y:%m:%d:%H:%M", t)
        data = {
            'gasValue': gas_value,
            'warning': 'gas vuot nguong'}
        db.child('kitchen').child('gas').child(current_time).set(data)
        bot.sendMessage(chat_id="5749681599", text="Gas vượt ngưỡng")
        time.sleep(10)
    else:
        GPIO.output(buzzer, 0)
    time.sleep(2)
