import random
import time
import Pixler


############################ Setup ##########################################

HOST = 'xx.xx.xx.xx'  # define Pixelflut server address
PORT = 1234  # port should stay the same
pixler = Pixler.Pixler(HOST, PORT)  # create a instance of pixler

############################ END SETUP ########################################


def rect(x, y, w, h, r, g, b):
    for i in range(x, x + w):
        for j in range(y, y + h):
            pixler.set_pixel(i, j, r, g, b)


def worm(size):
    """
    prints a modern art object on the canvas
    :param size: tuple (x, y) with the canvas size
    :return:
    """
    r = random.randint(0, 255)
    g = random.randint(0, 255)
    b = random.randint(0, 255)
    # start position on the canvas
    x = 720
    y = 510
    canvasX = int(size[0])
    canvasY = int(size[1])
    counter = 0
    while True:
        x += random.randint(0, 2) - 1
        y += random.randint(0, 2) - 1
        if (counter > 13337):
            # print("new color")
            r = random.randint(0, 255)
            g = random.randint(0, 255)
            b = random.randint(0, 255)
            counter = 0
        counter += 1
        # print(x % canvasX)
        pixler.set_pixel(x % canvasX, y % canvasY, r, g, b, 25)
        time.sleep(0.05)


def fill_canvas(size):
    x, y = size
    r = 255
    g = 255
    b = 255
    for i in range(x):
        for j in range(y):
            pixler.set_pixel(i, j, r, g, b)


def main():
    size = pixler.get_size() # request the size of the canvas
    print("Canvas size: x=" + str(size[0]) + " y=" + str(size[1]))
    # clear canvas first
    fill_canvas(size)

    # worm(size)
    rect(100, 200, 100, 100, 111, 0, 100)


if __name__ == "__main__":
    main()
