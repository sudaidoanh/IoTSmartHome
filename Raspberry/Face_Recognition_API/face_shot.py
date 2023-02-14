import cv2
import os

name = 'Doanh'  # replace with your name

cam = cv2.VideoCapture(0)

cv2.namedWindow("press space to take a photo", cv2.WINDOW_NORMAL)
cv2.resizeWindow("press space to take a photo", 500, 300)

img_counter = 0
face_detector = cv2.CascadeClassifier('haarcascade_frontalface_default.xml')
while True:
    ret, frame = cam.read()
    frame = cv2.flip(frame, 1)
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    faces = face_detector.detectMultiScale(gray, 1.3, 5)
    if not ret:
        print("failed to grab frame")

        break
    cv2.imshow("press space to take a photo", gray)

    k = cv2.waitKey(1)
    if k % 256 == 27:
        # ESC pressed
        print("Escape hit, closing…")
        break
    elif k % 256 == 32:
        # SPACE pressed
        img_name = "dataset/" + name + "/image_{}.jpg".format(img_counter)
        cv2.imwrite(img_name, gray)
        print("{} written!".format(img_name))
        img_counter += 1
    if k == ord("q"):
        break

cam.release()

cv2.destroyAllWindows()
