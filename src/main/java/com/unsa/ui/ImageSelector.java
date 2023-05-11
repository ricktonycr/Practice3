package com.unsa.ui;

import com.unsa.logic.Utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;


public class ImageSelector extends JPanel implements MouseListener {
    public BufferedImage image;
    private boolean isInput;
    private int id;

    public ImageSelector(boolean input,int id){
        super();
        this.isInput = input;
        this.id = id;
        this.addMouseListener(this);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(255,255,255));
        g.fillRect(0,0,this.getWidth(),this.getHeight());
        g.drawImage(image, 0, 0, this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if( isInput ){
            final JFileChooser f = new JFileChooser();
            JFileChooser fc = new JFileChooser();
            int result = fc.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                try {
                    this.image = ImageIO.read(file);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            this.repaint();
            BufferedImage imageg = new BufferedImage(image.getWidth(), image.getHeight(),
                    BufferedImage.TYPE_BYTE_GRAY);
            Graphics g = imageg.getGraphics();
            g.drawImage(image, 0, 0, null);
            g.dispose();
            this.image = imageg;
            Utils.loadImage(imageg,this.id);
        }else{
            if(this.id == 2)
                this.image = Utils.transform();
            //if(this.id == 3)
                //this.image = Utils.aritmeticOperations();
            if(this.id == 6)
                this.image = Utils.convolucional();
            this.repaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
