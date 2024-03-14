import java.awt.*;
import java.net.*;
import java.io.*;

public class Pixler {
    private Socket clientSocket;
    private  PrintWriter out;
    private  BufferedReader in;

    private int[] size;

    public Pixler(String host, int port) {
        try {
            clientSocket = new Socket(host, port);
            System.out.println("Socket successfully created and connected to the server.");
            out = new PrintWriter(clientSocket.getOutputStream(), false);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            size = getSize();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * send one message to the server
     *  supported commands:
     *  <p>
     * - `HELP`: Prints a help text with the available commands. <br>
     * - `PX x y rrggbb`: PX x y rrggbb: Color the pixel (x,y) with the given hexadecimal color rrggbb, e.g. PX 10 10 ff0000 <br>
     * - `PX x y rrggbbaa`: Color the pixel (x,y) with the given hexadecimal color rrggbb (alpha channel is ignored for now), e.g. PX 10 10 ff0000ff <br>
     * - `PX x y gg`: Color the pixel (x,y) with the hexadecimal color gggggg. Basically this is the same as the other commands, but is a more efficient way of filling white, black or gray areas, e.g. PX 10 10 00 to paint black <br>
     * - `PX x y`: Get the color value of the pixel (x,y), e.g. PX 10 10 <br>
     * - `SIZE`: Get the size of the drawing surface, e.g. SIZE 1920 1080 <br>
     * - `OFFSET x y`: Apply offset (x,y) to all further pixel draws on this connection. This can e.g. be used to pre-calculate an image/animation and simply use the OFFSET command to move it around the screen without the need to re-calculate it, e.g. OFFSET 100 100 <br>
     *  </p>
     *  @param message the message to send
     */
    public void sendMessage (String message) {
        out.println(message);
    }

    /**
     * send a message to the server and wait for the response
     * @param message the message to send
     * @return the answer of the server
     */
    public String sendMessageAndReceive (String message) {
        try {
            out.flush();
            out.println(message);
            out.flush();

            return in.readLine();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return  null;
    }


    /**
     * close socket connection
     */
    public void terminateConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * set one pixel at position x y on the canvas to the color @color
     * @param x the x-coordinate on the canvas
     * @param y the y-coordinate on the canvas
     * @param color the color value for the pixel
     */
    public void setPixel(int x, int y, Color color) {
        // System.out.printf("PX %d %d %02x%02x%02x\\n", x, y, color.getRed(), color.getGreen(), color.getBlue());
        sendMessage(String.format("PX %d %d %02x%02x%02x\\n", x, y, color.getRed(), color.getGreen(), color.getBlue()));
    }

    /**
     * get the color value of the pixel at x, y
     * @param x the x-coordinate on the canvas
     * @param y the y-coordinate on the canvas
     * @return the color of the pixel
     */
    public Color getPixel(int x, int y) {
        if(x < size[0] & y < size[1]) {
            String color = sendMessageAndReceive(String.format("PX %d %d", x, y)).split(" ")[3];
            System.out.println(color);
            int red = Integer.valueOf(color.substring(0, 2), 16);
            int green = Integer.valueOf(color.substring(2, 4), 16);
            int blue = Integer.valueOf(color.substring(4, 6), 16);
            return new Color(red, green, blue);
        } else {
            System.err.printf("Pixel %d %d ist außerhalb des Bildbereichs (%d x %d) \n", x, y, size[0], size[1]);
        }
        return  null;
    }

    /**
     * get the size of the canvas
     * @return array with the size of the canvas [x, y]
     */
    public int[] getSize() {
        int[] result = new int[2];
        String[] size = sendMessageAndReceive("SIZE").split(" ");
        result[0] = Integer.parseInt(size[1]);
        result[1] = Integer.parseInt(size[2]);
        return result;
    }
}
