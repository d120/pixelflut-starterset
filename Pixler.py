import socket


class Pixler:

    def __init__(self, host, port):
        """
        initialize the server connection
        :param host: the IP address of the pixelflut Server
        :param port: the port of the pixelflut server
        """
        self.HOST = host
        self.PORT = port
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.sock.connect((host, port))

    def send(self, message):
        """
        send the given message to the server.\n \n
        supported commands:
            - `HELP`: Prints a help text with the available commands. \n
            - `PX x y rrggbb`: PX x y rrggbb: Color the pixel (x,y) with the given hexadecimal color rrggbb, e.g. PX 10 10 ff0000 \n
            - `PX x y rrggbbaa`: Color the pixel (x,y) with the given hexadecimal color rrggbb (alpha channel is ignored for now), e.g. PX 10 10 ff0000ff \n
            - `PX x y gg`: Color the pixel (x,y) with the hexadecimal color gggggg. Basically this is the same as the other commands, but is a more efficient way of filling white, black or gray areas, e.g. PX 10 10 00 to paint black \n
            - `PX x y`: Get the color value of the pixel (x,y), e.g. PX 10 10 \n
            - `SIZE`: Get the size of the drawing surface, e.g. SIZE 1920 1080 \n
            - `OFFSET x y`: Apply offset (x,y) to all further pixel draws on this connection. This can e.g. be used to pre-calculate an image/animation and simply use the OFFSET command to move it around the screen without the need to re-calculate it, e.g. OFFSET 100 100 \n

        :param message: the message to send to the sercer e.g. 'PX x y rrggbb'
        :return: nothing
        """
        self.sock.sendto(message.encode(), (self.HOST, self.PORT))

    def send_and_receive(self, message):
        """
        same as the send-methode but returns the result
        :param message: the message to send
        :return: the response from the server
        """
        self.sock.sendall(message.encode())
        response = self.sock.recv(1024).decode()
        # print(response)
        return response

    def get_size(self):
        """
        request the size from the server and return it as x, y tuple
        :return: the canvas size as (x, y)
        """
        size = self.send_and_receive("SIZE").split()
        x = int(size[1])
        y = int(size[2])
        return x, y

    def set_pixel(self, x, y, r, g, b, a=255):
        """
        set one pixel on the canvas
        :param x: the x coordinate on the canvas
        :param y: the y coordinate on the canvas
        :param r: the red-color value
        :param g: the green-color value
        :param b: the blue-color value
        :param a: the alpha value
        :return: nothing
        """
        if a == 255:
            self.send('PX %d %d %02x%02x%02x\n' % (x, y, r, g, b))
        else:
            self.send('PX %d %d %02x%02x%02x%02x\n' % (x, y, r, g, b, a))

    def get_pixel(self, x, y):
        """
        get the color value for the given pixel
        @:param x the x-coordinate
        @:param y the y-coordinate
        @:returns the color value in hex e.g "c837ff"
        """
        pixel = self.send_and_receive("PX %d %d\n" % (x, y)).split()
        color = pixel[3]
        return color
