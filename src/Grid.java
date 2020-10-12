//Copyright Asaad Noman Abbasi

import java.awt.*;
import java.util.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Color;

//This is the hnadler for the grid
class GridHandler implements KeyListener , MouseListener , MouseMotionListener {
public void mouseMoved(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void keyReleased(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}

//This is the main grid class
public class Grid extends GridHandler {

    private int                 rows = 0 , cols = 0;
    private ArrayList<Pixel>    pixels = null;

    private boolean shift_key_status = false;
    private boolean erase_key_status = false;
    private boolean bucket_key_status = false;
    private boolean selection_key_status = false;

    private Color current_grid_color;

    //Our getters and setters
    public boolean get_shift_status() { return shift_key_status; }
    public boolean get_erase_status() { return erase_key_status; }
    public boolean get_bucket_status() { return bucket_key_status; }
    public boolean get_selection_status() { return selection_key_status; }
    public ArrayList<Pixel> get_pixels() { return this.pixels; }
    
    public void set_current_grid_color(Color color) { this.current_grid_color = color; }
    public void set_erase_status(boolean b) { this.erase_key_status = b; }
    public void set_bucket_status(boolean b) { this.bucket_key_status = b; }
    public void set_selection_status(boolean b) { this.selection_key_status = b; }

    //Creates Our Grid
    public Grid(Canvas window_canvas) {

        current_grid_color = Color.red;

        window_canvas.addMouseListener(this);
        window_canvas.addMouseMotionListener(this);
        window_canvas.addKeyListener(this);
        
        rows = Window.get_window_size().y / Pixel.get_pixel_size();
        cols = Window.get_window_size().x / Pixel.get_pixel_size();

        allocate_grid_memory();
    }

    public void clear_grid() {
        pixels.clear();
        allocate_grid_memory();
    }

    //This allocates Arraylist for our grid
    public void allocate_grid_memory() {
        pixels = new ArrayList<Pixel>();
        int xPos = 0; int yPos = 0;
        for(int i = 0; i <= rows; i++) {
            for(int j = 0; j <= cols; j++) {
                Pixel new_pixel = new Pixel(xPos,yPos);
                pixels.add(new_pixel);
                xPos += Pixel.get_pixel_size();
            } xPos = 0; yPos += Pixel.get_pixel_size();
        }
    }

    //This gets the pixel on the given x,y coords on the grid
    private Pixel get_pixel_on_grid(int x , int y) {
       for(Pixel p : pixels) {
           if(p.get_pixel_x() == x && p.get_pixel_y() == y) {
                p.set_pixel_filled(true);
                return p;
           }
       } return null;
    }

    //This function implements the flood fill algorithm used in PAINT BUCKET TOOLS
    private void flood_fill(int x , int y , Color target_color , Color fill_color) {
        try {

            if(get_pixel_on_grid(x, y).get_pixel_color() == fill_color) return;
            if(get_pixel_on_grid(x, y).get_pixel_color() != target_color) return;
         
            get_pixel_on_grid(x, y).set_pixel_color(fill_color);

            //4 NODES WITH CHECKING NSEW [North,South,East,West]
            flood_fill(x + Pixel.get_pixel_size() , y  ,target_color , fill_color);
            flood_fill(x - Pixel.get_pixel_size() , y  ,target_color , fill_color);
            flood_fill(x , y + Pixel.get_pixel_size() ,target_color , fill_color);
            flood_fill(x , y - Pixel.get_pixel_size() ,target_color , fill_color);

            return; 

        } catch (NullPointerException e) { }
    }

    //This loades the pixels to the grid
    public int load_pixels_to_grid() {
        
        ArrayList<LPixel> loaded_pixels = Utils.load_file();
      
        //If array_list > 0 .. then file found and load else file could not be found
        if(loaded_pixels.size() != 0) {
            // Clear the grid then reallocate in mem
            pixels.clear();
            allocate_grid_memory();

            // Then for each loaded pixel check the whole grid and find it
            for (int i = 0; i <= loaded_pixels.size() - 1; i++) {
                for (int j = 0; j <= pixels.size() - 1; j++) {
                    if (loaded_pixels.get(i).x == pixels.get(j).get_pixel_x()
                            && loaded_pixels.get(i).y == pixels.get(j).get_pixel_y()) {
                        pixels.get(j).set_pixel_filled(true);
                        pixels.get(j).set_pixel_color(loaded_pixels.get(i).color);
                    }
                }
            } return 0;
        } return -1;
    }

    //Overrding the GRID_HANDLER keyPressed Function
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {           
            case KeyEvent.VK_SHIFT: {
                if(!shift_key_status) shift_key_status = true;
            } break;
        }
    }

    //Overrding the GRID_HANDLER keyReleased Function
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
            if(shift_key_status) shift_key_status = false;
        }
    }

    //Overriding the GRID_HANDLER mouseClicked Function
    public void mouseClicked(MouseEvent e) {
       //if the selection tool is off then use the click to fill the pixel
       if(!selection_key_status) {
       for(int i = 0; i <= pixels.size() - 1; i++) {
          if(bucket_key_status) {
              if(pixels.get(i).mouse_contains(e)) flood_fill(pixels.get(i).get_pixel_x(),pixels.get(i).get_pixel_y(),pixels.get(i).get_pixel_color(),current_grid_color);
          } else pixels.get(i).on_mouse_click(e,current_grid_color,erase_key_status);
       }}
    }

    //These are the stuff for our selection tool
    private int st_left = 0 , st_right = 0 , st_top = 0, st_bottom = 0;
    private Point st_start_point = new Point(), st_end_point = new Point();

    //This helps resize our shape according to the mouseDragged e.getPoint();
    private void resize_shape(int x , int y) {
        st_end_point.x = x;
        st_end_point.y = y;
        update_bounds(st_start_point,st_end_point);
    }
    
    //These update the bounds for setting our left,top,bottom,top params depending on the shape made by our mousedrag
    private void update_bounds(Point pt1 , Point pt2) {
        st_left = (pt1.x < pt2.x) ? pt1.x : pt2.x;
        st_right = (pt1.x > pt2.x) ? pt1.x : pt2.x;
        st_top = (pt1.y < pt2.y) ? pt1.y : pt2.y;
        st_bottom = (pt1.y > pt2.y) ? pt1.y : pt2.y;
    }

    //Overridng the GRID_HANDLER mousePressed Function
    public void mousePressed(MouseEvent e) {
        if(selection_key_status) {
            st_start_point = e.getPoint();
        }
    }

    //Overriding the GRID_HADNLER mouseDragged Function
    public void mouseDragged(MouseEvent e) {

        if(selection_key_status) {
            resize_shape(e.getX(), e.getY());
        }

        if(shift_key_status && !selection_key_status) {
            for(int i = 0; i <= pixels.size() - 1; i++) pixels.get(i).on_mouse_click(e,current_grid_color,erase_key_status);
        }
    }

    //Overriding the GRID_HADNLER mouseDragged Function
    public void mouseReleased(MouseEvent e) {
        //Check whether the pixels in our grid are within the range of our beloved seleected recatngle
        //if they are then just set their filled pixel status to false;
        if(selection_key_status) {            
            for(Pixel p : pixels) {
                if(p.get_filled_state() && (p.get_pixel_x() >= st_start_point.x && p.get_pixel_x() < st_end_point.x) && (p.get_pixel_y() >= st_start_point.y && p.get_pixel_y() < st_end_point.y)) {
                    p.set_pixel_filled(false);
                }
            } st_left = 0; st_right = 0; st_top = 0; st_bottom = 0;
        }
    }

    //Render Our Grid
    public void render_grid(Graphics2D g2d) {
        for(int i = 0; i <= pixels.size() - 1; i++) pixels.get(i).render_pixel(g2d);
        if(selection_key_status) {
            float thickness = 7;
            Stroke oldStroke = g2d.getStroke();
            g2d.setStroke(new BasicStroke(thickness));
            g2d.setColor(Color.black);
            g2d.drawRect(st_left,st_top,Math.abs(st_left - st_right),Math.abs(st_top - st_bottom));
            g2d.setStroke(oldStroke);
        }
    }
}