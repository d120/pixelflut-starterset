import java.awt.*;

public class Main {

    static Pixler pixler;

    /* ********** SETUP ********** */
    static String HOST =  "localhost"; // insert the IP or hostname of the pixelflut server
    static int PORT = 1234; // Port should stay the same
    /* ******* END SETUP **********+ */


    /**
     * create instance of pixler
     */
    public static void setupPixler() {
        pixler = new Pixler(HOST, PORT);
    }

    /**
     * draw a rectangle on the canvas
     * @param x x-coordinate of the start position
     * @param y y-coordinate of the start position
     * @param w the width of the rectangle
     * @param h the height of the rectangle
     * @param color the color of the rectangle
     */
    public static void printRect(int x, int y,  int w, int h, Color color) {
        for (int i = x; i < x+w; i++) {
            for (int j = y; j < y+h; j++) {
                pixler.setPixel(i, j, color);
            }
        }
    }

    /**
     * fill the canvas with one color
     * @param size the size of the canvas
     */
    public static void fillCanvas(int[] size) {
        Color color = new Color(255, 255, 255);
        for (int i = 0; i < size[0] ; i++) {
            for (int j = 0; j < size[1]; j++) {
                pixler.setPixel(i, j, color);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to Pixelflut");
        setupPixler();

        int[] size = pixler.getSize();

        // get color of pixel x=510 y=320
        System.out.println(pixler.getPixel(510, 320));

        // fill canvas with one color
        fillCanvas(size);

        // draw a rectangle
        printRect(200, 320, 100, 100, new Color(123, 241, 111));

        // terminate socket connection
        pixler.terminateConnection();
    }
}