package com.unsa.logic;
import org.apache.tools.ant.types.CommandlineJava;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Objects;

import static java.awt.image.BufferedImage.TYPE_BYTE_GRAY;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.lang.Math.*;

public class Utils {
    private static BufferedImage image1;
    private static BufferedImage image2;
    private static BufferedImage image3;
    private static BufferedImage image4;
    private static BufferedImage image5;
    private static BufferedImage imageTemplate;
    private static BufferedImage imageMatching;
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
            case 5:
                imageTemplate = image;
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

    public static double distance(int a, int b){
        /*double ra = a.getRed();
        double rb = b.getRed();
        double ga = a.getGreen();
        double gb = b.getGreen();
        double ba = a.getBlue();
        double bb = b.getBlue();

        double sum = Math.pow(ra-rb,2)+Math.pow(ga-gb,2)+Math.pow(ba-bb,2);
        return Math.sqrt(sum);*/
        //System.out.println(a+","+b);
        return Math.sqrt(Math.pow(a-b,2));
    }

    public static BufferedImage convolucional(){
        System.out.println("start convolucional");
        System.out.println("image4 :"+image4.getWidth()+" "+image4.getHeight());
        System.out.println("imageTemplate :"+imageTemplate.getWidth()+" "+imageTemplate.getHeight());

        int aux;
        if(image4 != null && imageTemplate!=null){
            imageMatching = new BufferedImage(image4.getWidth(), image4.getHeight(),TYPE_BYTE_GRAY);
            WritableRaster rasterImage = image4.getRaster();
            WritableRaster rasterMatching = imageMatching.getRaster();
            WritableRaster rasterTemplate = imageTemplate.getRaster();

            int colorAux1,colorAux2;
            int centerX=imageTemplate.getWidth()/2;
            int centerY=imageTemplate.getHeight()/2;
            int minimo = Integer.MAX_VALUE;
            int posx=0,posy=0;
            double metricaDistancia=0;
            for ( int i = 0; i < image4.getWidth(); i++){
                for (int j = 0; j < image4.getHeight(); j++){
                    metricaDistancia=0.0;
                    for (int m = 0; m < imageTemplate.getWidth(); m++){
                        for (int n = 0; n < imageTemplate.getHeight(); n++) {
                            int x = i + (m - centerX);
                            int y = j + (n - centerY);
                            if (x > 0 && x < image4.getWidth() && y > 0 && y < image4.getHeight()){
                                colorAux1 = rasterImage.getSample(x,y,0);
                                //colorAux1 = new Color(image4.getRGB(i, j));
                                //colorAux2 = rasterTemplate.getSample(m,n,0);
                                //metricaDistancia+= distance(colorAux1,colorAux2);
                            }else {
                                colorAux1 = 0;
                            }
                            colorAux2 = rasterTemplate.getSample(m,n,0);
                            metricaDistancia+= distance(colorAux1,colorAux2);
                        }
                    }
                    //System.out.println("tot"+metricaDistancia);
                    if(metricaDistancia<minimo) {
                        minimo=(int)metricaDistancia;
                        posx=i;
                        posy=j;
                    }
                }
            }
            int p=0;
            for (int i = posx; i < imageTemplate.getWidth() ; i++) {
                for (int j = posy; j < imageTemplate.getHeight(); j++) {
                    p=rasterImage.getSample(i,j,0);
                    rasterMatching.setPixel(i,j,new int[]{p});
                }
            }

            //System.out.println("aux"+aux);
            //imageMatching.setRGB(i,j,aux);
            //rasterMatching.setPixel(i,j,new int[]{aux});


            //Graphics g = imageMatching.getGraphics();
            //g.drawImage(imageMatching, 0, 0, null);
            //g.dispose();
            //return result;
            System.out.println("finish convolucional");
            return imageMatching;
        }else{
            JOptionPane.showMessageDialog(null, "Debe ingresar una imagen y un template correcto.");
            return null;
        }
    }
}
