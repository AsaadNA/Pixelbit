//Copyright Asaad Noman Abbasi

import java.awt.geom.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

//Loaded Pixel
class LPixel {
    
    public int x , y;
    public Color color;

    public LPixel(int x,  int y , Color color) {
        this.x = x; this.y = y;
        this.color = color;
    }
}

//Normal Pixel
public class Pixel {

    private int               x = 0, y = 0;
    private static int size     = 11;
    private Rectangle2D pixel   = null;

    private boolean filled = false;
    private Color   filled_color = Color.BLACK;

    private static boolean show_lines = true;
    public static void set_show_lines(boolean b) { show_lines = b; }

    public static int get_pixel_size() { return size; }

    /* GETTERS & SETTERS */
    
    public int get_pixel_x() { return x; }
    public int get_pixel_y() { return y; }
    public Color get_pixel_color() { return this.filled_color; }
    public boolean get_filled_state() { return this.filled; }

    public void set_pixel_color(Color color)  { this.filled_color = color; }
    public void set_pixel_filled(boolean b)   { this.filled = b; }

    //Create Our Pixel
    public Pixel(int x , int y) {
        this.x = x; this.y = y;
        pixel = new Rectangle2D.Double(x,y,size,size);
    }

    //This checks whether the mouse is inside the bound of the pixels
    public boolean mouse_contains(MouseEvent e) {
        return (pixel.contains(e.getPoint()));
    }

    //Does Things like filling the pixel , after checking if the mouse is in the bounds of the pixel
    public void on_mouse_click(MouseEvent e , Color current_grid_color , boolean eraser_tool_status) {
        if(mouse_contains(e)) {
            //if the eraser tool is off , it fills the pixel otherwise it does not
            if(!eraser_tool_status) {
                filled_color = current_grid_color;
                filled = true;
            } else {
                filled = false;
            }
        }
    }

    //Render our Pixel
    public void render_pixel(Graphics2D g2d) {
        g2d.setColor(Color.GRAY); //This is the color for the drawRect();
        if(filled) {
            g2d.setColor(filled_color);
            g2d.fillRect(x,y,size,size);
        } else if(show_lines){
            g2d.drawRect(x,y,size,size);    
        }
    }
}