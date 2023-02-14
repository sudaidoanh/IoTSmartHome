import RPi.GPIO as GPIO
import time
GPIO.setwarnings(False)
GPIO.setmode(GPIO.BCM)

pir = 22
led = 7

GPIO.setup(pir, GPIO.IN)
GPIO.setup(led, GPIO.OUT)
GPIO.output(led, GPIO.LOW)

while True:
    i = GPIO.input(pir)
    if i == 0:
        print("No intruders", i)
        time.sleep(1)
        GPIO.output(led, 0)

    elif i == 1:
        print("Intruders detected", i)
        GPIO.output(led, 1)
        time.sleep(3)
