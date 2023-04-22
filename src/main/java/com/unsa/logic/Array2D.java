package com.unsa.logic;

public class Array2D {
    public int width;
    public int height;
    public int size;
    public ComplexNumber [][] values;
    public Array2D(){}
    public Array2D(Array2D a){

        width = a.width;
        height = a.height;
        //System.out.println("NEW 2D 1 w: "+width+" height: "+height);
        size = a.size;
        values = new ComplexNumber[size][size];
        for(int j=0;j<size;++j){
            for(int i=0;i<size;++i){
                ComplexNumber c = new ComplexNumber(a.values[i][j]);
                values[i][j] = c;
            }
        }
    }
    public Array2D(int w, int h){
        width = w;
        height = h;
        //System.out.println("NEW 2D 2 w: "+width+" height: "+height);
        int n=0;
        while(Math.pow(2,n)<Math.max(w,h)){
            ++n;
        }
        size = (int) Math.pow(2,n);
        values = new ComplexNumber [size][size];
        for(int j=0;j<size;++j){
            for(int i=0;i<size;++i){
                values[i][j] = new ComplexNumber(0,0);
            }
        }
    }

    public Array2D(int s){
        width = s;
        height = s;
//System.out.println("NEW 2D 3 w: "+width+" height: "+height);
        int n=0;
        while(Math.pow(2,n)<s){
            ++n;
        }
        size = (int) Math.pow(2,n);
        values = new ComplexNumber [size][size];

        for(int j=0;j<size;++j){
            for(int i=0;i<size;++i){
                values[i][j] = new ComplexNumber(0,0);
            }
        }
    }

    public Array2D(int [] p, int w, int h){
        width = w;
        height = h;
        //System.out.println("NEW 2D 4 w: "+width+" height: "+height);
        int n=0;

        while(Math.pow(2,n)<Math.max(w,h)){
            ++n;
        }
        //System.out.println("n is "+n+" w is "+w+" h is "+h);

        size = (int) Math.pow(2,n);
        values = new ComplexNumber [size][size];
        for(int j=0;j<size;++j){
            for(int i=0;i<size;++i){
                values[i][j] = new ComplexNumber(0,0);
            }
        }
        /*System.err.println("Just about to add image to array");*/
        /* DONALD NAIRN 2003 */
        for(int j=0;j<h;++j){
            for(int i=0;i<w;++i){

                values[i][j] = new ComplexNumber(p[i+(j*w)], 0.0);
            }
        }
    }

    public Array2D(int [][] v, int w, int h){
        width = w;
        height = h;
//System.out.println("NEW 2D 5 w: "+width+" height: "+height);
        int n=0;
        while(Math.pow(2,n)<Math.max(w,h)){
            ++n;
        }
        size = (int) Math.pow(2,n);
        values = new ComplexNumber [size][size];

        for(int j=0;j<size;++j){
            for(int i=0;i<size;++i){
                values[i][j] = new ComplexNumber(0,0);
            }
        }
        for(int j=0;j<h;++j){
            for(int i=0;i<w;++i){
                values[i][j] = new ComplexNumber(v[i][j], 0.0);
            }
        }
    }

    public Array2D(ComplexNumber [][] v, int w, int h){
        width = w;
        height = h;
        //System.out.println("NEW 2D 6 w: "+width+" height: "+height);
        int n=0;
        while(Math.pow(2,n)<Math.max(w,h)){
            ++n;
        }
        size = (int) Math.pow(2,n);
        values = new ComplexNumber [size][size];

        for(int j=0;j<size;++j){
            for(int i=0;i<size;++i){
                values[i][j] = new ComplexNumber(0,0);
            }
        }
        for(int j=0;j<h;++j){
            for(int i=0;i<w;++i){
                values[i][j] = new ComplexNumber(v[i][j]);
            }
        }
    }

    public ComplexNumber [] getColumn(int n){
        ComplexNumber [] c = new ComplexNumber [size];
        for(int i=0;i<size;++i){
            c[i] = new ComplexNumber(values[n][i]);
        }
        return c;
    }
    public void putColumn(int n, ComplexNumber [] c){
        for(int i=0;i<size;++i){
            values[n][i] = new ComplexNumber(c[i]);
        }
    }

    public void putRow(int n, ComplexNumber [] c){
        for(int i=0;i<size;++i){
            values[i][n] = new ComplexNumber(c[i]);
        }
    }

    public ComplexNumber [] getRow(int n){
        ComplexNumber [] r = new ComplexNumber [size];
        for(int i=0;i<size;++i){
            r[i] = new ComplexNumber(values[i][n]);
        }
        return r;
    }

    public double [][] DCToCentre(double [][] input){
        double [][] output = new double [width][height];
        int x = width/2;
        int y = height/2;
        int i2,j2;
        for(int j=0;j<height;++j){
            for(int i=0;i<width;++i){
                i2=i+x;
                j2=j+y;
                if(i2>=width)i2=i2%width;
                if(j2>=height)j2=j2%height;
                output[i][j] = input[i2][j2];
            }
        }
        return output;
    }

    public double [][] DCToTopLeft(double [][] input){
        double [][] output = new double [width][height];
        int i2,j2;
        int x = width/2;
        int y = height/2;
        for(int j=0;j<height;++j){
            for(int i=0;i<width;++i){
                i2=i+x;
                j2=j+x;
                if(i2>=width)i2=i2%width;
                if(j2>=height)j2=j2%height;
                output[i][j] = input[i2][j2];
            }
        }
        return output;
    }

    public ComplexNumber [][] DCToTopLeft(ComplexNumber [][] input){
        ComplexNumber [][] output = new ComplexNumber [width][height];
        int i2,j2;
        int x = width/2;
        int y = height/2;
        for(int j=0;j<height;++j){
            for(int i=0;i<width;++i){
                i2=i+x;
                j2=j+x;
                if(i2>=width)i2=i2%width;
                if(j2>=height)j2=j2%height;
                output[i][j] = input[i2][j2];
            }
        }
        return output;
    }

    public double [] DCToCentre(double [] input){
        double [][] input2 = new double [width][height];
        double [][] output2 = new double [width][height];
        double [] output = new double [width*height];
        for(int j=0;j<height;++j){
            for(int i=0;i<width;++i){
                input2[i][j] = input[j*width +i];
            }
        }
        int x = width/2;
        int y = height/2;
        int i2,j2;
        for(int j=0;j<height;++j){
            for(int i=0;i<width;++i){
                i2=i+x;
                j2=j+y;
                //if (input2[i][j] == 0){System.out.println("ZEROa at ("+i+","+j+") moved to ("+i2+","+j2+")");}
                if(i2>=width)i2=i2%width;
                if(j2>=height)j2=j2%height;
                output2[i][j] = input2[i2][j2];
            }
        }
        for(int j=0;j<height;++j){
            for(int i=0;i<width;++i){
                output[j*width +i] = output2[i][j];
            }
        }
        return output;
    }

    public double [] getReal(){
        double [] output = new double [width*height];
        for(int j=0;j<height;++j){
            for(int i=0;i<width;++i){
                output[(j*width)+i] = values[i][j].real;
                //if (values[i][j].real == 0){System.out.println("found ZERO at ("+i+","+j+")");}
            }
        }
        return output;
    }

    public double [] getImaginary(){
        double [] output = new double [width*height];
        for(int j=0;j<height;++j){
            for(int i=0;i<width;++i){
                output[(j*width)+i] = values[i][j].img;
            }
        }
        return output;
    }

    public double [] getMagnitude(){
        double [] output = new double [width*height];
        for(int j=0;j<height;++j){
            for(int i=0;i<width;++i){
                output[(j*width)+i] = values[i][j].magnitude();
            }
        }
        return output;
    }

    public double [] getPhase(){
        double [] output = new double [width*height];
        for(int j=0;j<height;++j){
            for(int i=0;i<width;++i){
                output[(j*width)+i] = values[i][j].phaseAngle();
            }
        }
        return output;
    }
}
