package com.unsa.logic;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Objects;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.lang.Math.*;

public class Utils {
    private static BufferedImage image1;
    private static BufferedImage image2;
    private static BufferedImage image3;
    private static BufferedImage image4;
    private static BufferedImage image5;
    private static JComboBox comboOperaciones;
    private static JTextField a;
    private static JTextPane pane;
    public static Array2D input;
    public static Array2D intermediate;
    public static Array2D output;

    public static void loadImage(BufferedImage image, int id){
        switch (id){
            case 1:
                image1 = image;
            case 2:
                image2 = image;
            case 4:
                image4 = image;
        }
    }

    public static void setComboOperaciones(JComboBox combo){
        comboOperaciones = combo;
    }

    public static void setFactor(JTextField txt){
        a = txt;
    }

    static ComplexNumber [] recursiveFFT (ComplexNumber [] x){
        ComplexNumber z1,z2,z3,z4,tmp,cTwo;
        int n = x.length;
        int m = n/2;
        ComplexNumber [] result = new ComplexNumber [n];
        ComplexNumber [] even = new ComplexNumber [m];
        ComplexNumber [] odd = new ComplexNumber [m];
        ComplexNumber [] sum = new ComplexNumber [m];
        ComplexNumber [] diff = new ComplexNumber [m];
        cTwo = new ComplexNumber(2,0);
        if(n==1){
            result[0] = x[0];
        }else{
            z1 = new ComplexNumber(0.0, -2*(Math.PI)/n);
            tmp = ComplexNumber.cExp(z1);
            z1 = new ComplexNumber(1.0, 0.0);
            for(int i=0;i<m;++i){
                z3 = ComplexNumber.cSum(x[i],x[i+m]);
                sum[i] = ComplexNumber.cDiv(z3,cTwo);

                z3 = ComplexNumber.cDif(x[i],x[i+m]);
                z4 = ComplexNumber.cMult(z3,z1);
                diff[i] = ComplexNumber.cDiv(z4,cTwo);

                z2 = ComplexNumber.cMult(z1,tmp);
                z1 = new ComplexNumber(z2);
            }
            even = recursiveFFT(sum);
            odd = recursiveFFT(diff);

            for(int i=0;i<m;++i){
                result[i*2] = new ComplexNumber(even[i]);
                result[i*2 + 1] = new ComplexNumber(odd[i]);
            }
        }
        return result;
    }
    public static BufferedImage transform(){
        if(image1 != null) {
            byte[] pixelsGRAY = ((DataBufferByte)image1.getRaster().getDataBuffer()).getData();
            int[] array = new int[pixelsGRAY.length];

            for (int i = 0; i < pixelsGRAY.length; i++)
                array[i] = pixelsGRAY[i];

            input = new Array2D(array,image1.getWidth(),image1.getHeight());
            intermediate = new Array2D(array,image1.getWidth(),image1.getHeight());
            output = new Array2D(array,image1.getWidth(),image1.getHeight());

            for(int i=0;i<input.size;++i){
                intermediate.putColumn(i, recursiveFFT(input.getColumn(i)));
            }
            for(int i=0;i<intermediate.size;++i){
                output.putRow(i, recursiveFFT(intermediate.getRow(i)));
            }
            for(int j=0;j<output.values.length;++j){
                for(int i=0;i<output.values[0].length;++i){
                    intermediate.values[i][j] = output.values[i][j];
                    input.values[i][j] = output.values[i][j];
                }
            }

            BufferedImage r = new BufferedImage(image1.getWidth(),image1.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
            WritableRaster q = r.getRaster();
            double[][] mag = new double[image1.getWidth()][image1.getHeight()];
            double min = Double.MAX_VALUE;
            double max = -Double.MAX_VALUE;
            for (int x = 0; x < image1.getWidth(); x++){
                for (int y = 0; y < image1.getHeight(); y++){
                    mag[x][y] =  Math.log(output.values[x][y].magnitude()+1);
                    if(mag[x][y] < min) min = mag[x][y];
                    if(mag[x][y] > max) max = mag[x][y];
                }
            }
            for (int x = 0; x < image1.getWidth(); x++){
                for (int y = 0; y < image1.getHeight(); y++){
                    int xx = (x > image1.getWidth()/2)?x-image1.getWidth()/2:image1.getWidth()/2-x;
                    int yy = (y > image1.getHeight()/2)?y-image1.getHeight()/2:image1.getHeight()/2-y;
                    q.setPixel(x,y,new int[]{255-(int) ( (mag[xx][yy] - min)/(max - min) * 255)});
                }
            }

            return r;
        }else{
            JOptionPane.showMessageDialog(null, "Debe ingresar la imagen entrada.");
            return null;
        }
    }

    public static void setPane(JTextPane p){
        pane = p;
    }

    public static BufferedImage convolucional(){
        if(image4 != null && !pane.getText().isBlank()){
            image5 = new BufferedImage(image4.getWidth(), image4.getHeight(),TYPE_INT_RGB);
            String kernelText = pane.getText();
            String lineas[] = kernelText.split("\n");
            String temp[] = lineas[0].split(";");
            Double values[][] = new Double[lineas.length][temp.length];
            int centerX = lineas.length/2;
            int centerY = temp.length/2;

            for (int i = 0; i < lineas.length; i++){
                temp = lineas[i].split(";");
                for (int j = 0; j < temp.length; j++){
                    values[i][j] = Double.valueOf(temp[j]);
                }
            }
            Color colorAux1;
            for ( int i = 0; i < image4.getWidth(); i++){
                for (int j = 0; j < image4.getHeight(); j++){
                    Double r = 0.0;
                    Double g = 0.0;
                    Double b = 0.0;
                    for (int m = 0; m < values.length; m++){
                        for (int n = 0; n < values[m].length; n++){
                            double kernelFactor = values[m][n];
                            int x = i + (m - centerX);
                            int y = j + (n - centerY);
                            if (x > 0 && x < image4.getWidth() && y > 0 && y < image4.getHeight())
                                colorAux1 = new Color(image4.getRGB(x, y));
                            else
                                colorAux1 = new Color(0,0,0);
                            r += colorAux1.getRed()*kernelFactor;
                            g += colorAux1.getGreen()*kernelFactor;
                            b += colorAux1.getBlue()*kernelFactor;
                        }
                    }

                    int ri = (int) Math.min( r, 255);
                    int gi = (int) Math.min( g,255);
                    int bi = (int) Math.min( b,255);
                    image5.setRGB(i,j,(ri << 16) | (gi << 8) | bi);
                }
            }
            return image5;
        }else{
            JOptionPane.showMessageDialog(null, "Debe ingresar una imagen y un kernel correcto.");
            return null;
        }
    }
}
