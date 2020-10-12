//Copyright Asaad Noman Abbasi

import javax.swing.*;
import java.awt.image.BufferStrategy;
import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent; 
import java.awt.event.ItemListener; 
import javax.swing.JColorChooser;
import javax.swing.JToolBar;  
import java.awt.BorderLayout;  

@SuppressWarnings("serial")
public class Window extends Canvas {

    private JFrame window              = null;
    private Grid grid                  = null;
    private static  Point  window_size = null;
    
    private static JToolBar toolbar;
    private JToggleButton erase_button,bucket_button,selection_button;
    private JButton about_button, colorpicker_button,save_button,export_button,load_button,exit_button,cleargrid_button;
    
    public static Point get_window_size() { 
        return window_size; 
    }

    //Set the visiblit = visible = true // 
    public static void toolbar_visiblity_status(boolean b) {
        toolbar.setVisible(b);
    }

    //This setsup buttons addd to the toolbar
    private void setup_toolbar_buttons() {
        erase_button = new JToggleButton("Eraser");
        bucket_button = new JToggleButton("Bucket");
        selection_button = new JToggleButton("Select Remove");
        colorpicker_button = new JButton("Color Picker");
        save_button = new JButton("Save File");
        load_button = new JButton("Load File");
        export_button = new JButton("Export PNG");
        exit_button = new JButton("Quit Pixelbit");
        cleargrid_button = new JButton("Clear Grid");
        about_button = new JButton("About");

        erase_button.setBackground(Color.white);
        bucket_button.setBackground(Color.white);
        selection_button.setBackground(Color.white);
        colorpicker_button.setBackground(Color.white);
        save_button.setBackground(Color.white);
        load_button.setBackground(Color.white);
        export_button.setBackground(Color.white);
        
        exit_button.setBackground(Color.white);
        exit_button.setForeground(Color.RED);

        cleargrid_button.setBackground(Color.white);
        about_button.setBackground(Color.white);

        toolbar.addSeparator(new Dimension(80,0));
        toolbar.add(erase_button);
        toolbar.add(bucket_button);
        toolbar.add(selection_button);
        toolbar.add(colorpicker_button);
        toolbar.add(save_button);
        toolbar.add(load_button);
        toolbar.add(export_button);
        toolbar.add(cleargrid_button);
        toolbar.add(about_button);
        toolbar.add(exit_button);
    }

    //This setsup our toolbar button actions
    private void setup_buttons_actions() {

        //This is the Color picket button
        colorpicker_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(null,"Pixelbit Color Picker", Color.red);
                grid.set_current_grid_color(newColor);
                colorpicker_button.setBackground(newColor);
            }
        });

        //This is the Save button
        save_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               if(Utils.write_to_file(grid.get_pixels()) < 0)
                    JOptionPane.showMessageDialog(null, "Error: could not save file");
            }
        });

        // This is the about button
        about_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,"Pixelbit created by Asaad Noman Abbasi // BCS 2-F // 1912297", "About | Pixelbit ", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // This is the load button
        load_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               if(grid.load_pixels_to_grid() < 0)
                JOptionPane.showMessageDialog(null,"Error: Could not load file");
            }
        });

        // This is the export button
        export_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(Utils.export_pb_data(window) < 0)
                   JOptionPane.showMessageDialog(null, "Error: exporting art");
            }
        });

         // This is the exit button
         exit_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(1);  
            }
        });

        // This is the Clear Grid button
        cleargrid_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                grid.clear_grid();
            }
        });
        
        //This is the erase button toggle
        erase_button.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                if(state == ItemEvent.SELECTED) {
                    if(!grid.get_erase_status()) {
                        grid.set_erase_status(true);
                        //Utils.LOG("Erase tool on");

                        //If the tool is on disable the others
                        bucket_button.setEnabled(false);
                        selection_button.setEnabled(false);
                    }
                } else {

                    if(grid.get_erase_status()) {
                        grid.set_erase_status(false);
                        //Utils.LOG("ERASE TOOL OFF");

                        //if the tool is off enable the others
                        bucket_button.setEnabled(true);
                        selection_button.setEnabled(true);
                    }
                }
            }
        });

        // This is the Bucket button toggle
        bucket_button.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    if (!grid.get_bucket_status()) {
                        grid.set_bucket_status(true);
                        //Utils.LOG("Bucket tool on");

                        // If the tool is on disable the others
                        erase_button.setEnabled(false);
                        selection_button.setEnabled(false);
                    }
                } else {
                    if (grid.get_bucket_status()) {
                        grid.set_bucket_status(false);
                        //Utils.LOG("Bucket TOOL OFF");

                        // if the tool is off enable the others
                        erase_button.setEnabled(true);
                        selection_button.setEnabled(true);
                    }
                }
            }
        });

         // This is the Selection button toggle
        selection_button.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    if (!grid.get_selection_status()) {
                        grid.set_selection_status(true);
                        //Utils.LOG("Selection tool on");

                        // If the tool is on disable the others
                        erase_button.setEnabled(false);
                        bucket_button.setEnabled(false);
                    }
                } else {
                    if (grid.get_selection_status()) {
                        grid.set_selection_status(false);
                        //Utils.LOG("Selection TOOL OFF");

                        // if the tool is off enable the others
                        erase_button.setEnabled(true);
                        bucket_button.setEnabled(true);
                    }
                }
            }
        });
    }

    //Create Our Beloved Main Window
    public Window() {

        /* Setting up our window */

        window_size = new Point(900,700);
        window = new JFrame();
        window.setSize(new Dimension(window_size.x,window_size.y));
        window.setResizable(false);
        window.setUndecorated(true);
        window.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        /* Setting up our toolbar */

        toolbar = new JToolBar();
        toolbar.setBackground(Color.white);
        toolbar.setRollover(false);
        toolbar.setFloatable(false);

        setup_toolbar_buttons();
        setup_buttons_actions();

        window.getContentPane().add(toolbar,BorderLayout.NORTH);

        /* Setting the window again */

        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getRootPane().setBorder(BorderFactory.createMatteBorder(12,0,0,0,Color.white));
        window.getContentPane().setBackground(Color.white);
        window.setVisible(true);
        window.add(this);

        /* Our Undecorated  Window Handler For Moving the window with borders*/

        WindowHandler window_handler = new WindowHandler(window);
        window.addMouseListener(window_handler);
        window.addMouseMotionListener(window_handler);

        /* Our Grid  */
        grid = new Grid(this);
    }

    //Render Our Window
    public void render_window() {

        //Get & Load Our Buffers
        BufferStrategy bs = getBufferStrategy();
        if(bs == null) {
            createBufferStrategy(3);
            return;
        }

        /* Drawing our Buffers with our grid */

        Graphics g = bs.getDrawGraphics();
        Graphics g2d = (Graphics2D)g;

        g2d.clearRect(0,0,getWidth(),getHeight());

        grid.render_grid((Graphics2D)g2d);

        g.dispose();
        g2d.dispose();
        bs.show();
    }
}