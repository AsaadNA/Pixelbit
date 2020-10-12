//Copyright Asaad Noman Abbasi

import java.io.File;
import java.io.PrintWriter;
import javax.swing.JOptionPane;
import java.util.*;
import java.io.IOException;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import javax.swing.JFrame;
import javax.imageio.ImageIO;

public class Utils {

    //This just LOGS To the console
    public static void LOG(String log) {
        System.out.println("(LOG): " + log);
    }

    //This checks if path match the file extension
    public static boolean check_file_extension(String s , String ext) {
        if(s.length() - 1 < 0) return false;
        s = s.substring(s.length() - ext.length(), s.length());
        if(s.contains(ext))
            return true;
        return false;
    }

    //Write To File file.pb => x,y,r,g,b
    public static int write_to_file(ArrayList<Pixel> pixels) {
        String path = "saved_files\\";
        path += JOptionPane.showInputDialog("Enter file path to save it with (.pb extension)");
        if(!check_file_extension(path, "pb")) {
            return -1;
        }
        
        // Directory Checking
        File dir = new File("saved_files");
        if (!dir.exists()) {
            if (dir.mkdir()) {
                LOG("new dir created");
            } else {
                LOG("dir exists");
            }
        }

        PrintWriter writer = null;
        try {
            writer = new PrintWriter(path, "UTF-8");
        } catch (IOException e) {
            LOG("Error Writing to File");
        }

        for (int i = 0; i <= pixels.size() - 1; i++) {
            if (pixels.get(i).get_filled_state()) {
                String format = pixels.get(i).get_pixel_x() + "," + pixels.get(i).get_pixel_y() + ","
                        + pixels.get(i).get_pixel_color().getRed() + "," + pixels.get(i).get_pixel_color().getGreen()
                        + "," + pixels.get(i).get_pixel_color().getBlue();
                writer.println(format);
            }
        }

        writer.close();
        JOptionPane.showMessageDialog(null, "File saved: " + path);
        return 0;
    }

    //This will load file from .pb theen it will return the loaded pixels in an arraylst of LPixels
    public static ArrayList<LPixel> load_file() {
        ArrayList<LPixel> pixels = new ArrayList<LPixel>();
        String path = "saved_files\\";
        path += JOptionPane.showInputDialog("Enter File to load (with .pb extension)");
        Scanner file_scanner = null;
        File file = new File(path);
        try {
            file_scanner = new Scanner(file);
            while(file_scanner.hasNextLine()) {
                String line = file_scanner.nextLine();
                String parts[] = line.split(",");
                Color lpc = new Color(Integer.parseInt(parts[2]),Integer.parseInt(parts[3]),Integer.parseInt(parts[4]));
                LPixel pixel = new LPixel(Integer.parseInt(parts[0]),Integer.parseInt(parts[1]),lpc);
                pixels.add(pixel);

            }
        } catch(IOException e) { }

        return pixels;
    }

    //This uses the robot to check where the window is and grab screen capture and do things with it
    public static int export_pb_data(JFrame window) {
        
        //HIDE STUFF HERE
        Pixel.set_show_lines(false);
        Window.toolbar_visiblity_status(false);
        //window.getRootPane().setBorder(BorderFactory.createMatteBorder(4,0,0,0,Color.white));

        try {
            String path = "exported_art\\";
            path += JOptionPane.showInputDialog("Enter File Path To Export PB Art (.png Extension)");
            
            if(!check_file_extension(path, "png")) { 
                Pixel.set_show_lines(true);
                Window.toolbar_visiblity_status(true);
                return -1; 
            }

            //Directory Checking 
            File dir = new File("exported_art");
            if(!dir.exists()) {
                if(dir.mkdir()) {
                    LOG("new dir created");
                } else {
                    LOG("dir exists");
                }
            }
            
            Point location_on_screen = window.getLocationOnScreen();
            Robot robot = new Robot();
            BufferedImage captured_source_image = robot.createScreenCapture(new Rectangle((int)location_on_screen.getX(),(int)location_on_screen.getY(),900,700));
            final int target_color = captured_source_image.getRGB(0, 0);
            final Image source_transparent = image_to_transparent(captured_source_image, new Color(target_color));
            final BufferedImage final_exported_image = image_to_bufferedimage(source_transparent);
            final File output_image = new File(path);
            if(ImageIO.write(final_exported_image,"PNG",output_image)) {
                
                //MAKE STUFF VISIBLE HERE
                Pixel.set_show_lines(true);
                Window.toolbar_visiblity_status(true);
                //window.getRootPane().setBorder(BorderFactory.createMatteBorder(4,0,0,0,Color.black));
                
                JOptionPane.showMessageDialog(window,"Successfully Export PB Art To: " + path);
            }

        } catch(IOException | AWTException e) { LOG("Error Exporting Image"); }
          return 0;
    }

    //This function helps finds the Alpha bits in our source BufferedImage and make the bits to transparent
    public static Image image_to_transparent(BufferedImage image , Color color) {
        ImageFilter filter = new RGBImageFilter() {
            int target_bit = color.getRGB() | 0xFFFFFFFF;
            public int filterRGB(int x , int y  , int rgb) {
                if((rgb | 0xFF000000) == target_bit) return 0x00FFFFFF & rgb;
                else return rgb;
            }
        };

        ImageProducer ip = new FilteredImageSource(image.getSource(),filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    //This converts a IMAGE to a buffered_image
    public static BufferedImage image_to_bufferedimage(Image image) {
        BufferedImage result = new BufferedImage(image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = result.createGraphics();
        g2d.drawImage(image, 0, 0, null); //Copies the IMAGE to the BUFFERED IMAGE
        g2d.dispose();
        return result;
    }
}