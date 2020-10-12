import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//This is used for moving our borderless window
public class WindowHandler extends MouseAdapter {

    private JFrame window = null;
    private Point  window_position = null;

    public WindowHandler(JFrame window) {
        this.window = window;
    }

    public void mousePressed(MouseEvent e) {
        window_position = e.getPoint();
    }

    public void mouseReleased(MouseEvent e) {
        window_position = null;
    }

    //This takes the current location and finds delta and sets it to the location of mouse
    public void mouseDragged(MouseEvent e) {
        Point current_window_position = e.getLocationOnScreen();
        window.setLocation(current_window_position.x - window_position.x , current_window_position.y - window_position.y);
    }
}