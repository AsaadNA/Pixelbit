//Copyright Asaad Noman Abbasi

import javax.swing.*;
import java.awt.*;


//This is our WAIT SCREEN
class WaitScreen {

    private final JWindow window;
    private long startTime;
    private int minimumMilliseconds;

    public WaitScreen() {
        window = new JWindow();
        var image = new ImageIcon("res/splash.png");
        window.getContentPane().add(new JLabel("", image, SwingConstants.CENTER));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        window.setBounds((int) ((screenSize.getWidth() - image.getIconWidth()) / 2),
                (int) ((screenSize.getHeight() - image.getIconHeight()) / 2),
                image.getIconWidth(), image.getIconHeight());
    }

    //Show the time for n amount of times
    public void show(int minimumMilliseconds) {
        this.minimumMilliseconds = minimumMilliseconds;
        window.setVisible(true);
        startTime = System.currentTimeMillis();
    }

    //Hide the window after the elapsed time
    public void hide() {
        long elapsedTime = System.currentTimeMillis() - startTime; //the deltaa time
        try {
            Thread.sleep(Math.max(minimumMilliseconds - elapsedTime, 0));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } window.setVisible(false);
    }
}

//Main Pixelbit
public class Pixelbit {

    private Window window = null;

    public Pixelbit() {

        WaitScreen waitscreen = new WaitScreen();
        waitscreen.show(3000);
        waitscreen.hide();

        window = new Window();

        long lastTime = System.nanoTime();
        double fps = 60.0;
        double ns = 1000000000 / fps;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;

        while(true) {

            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1) {
                delta--;
            } 
            
            window.render_window();
            frames++;

            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frames = 0;
            }
        }
    }

    public static void main(String args[]) {
        Pixelbit p = new Pixelbit();
    }
}