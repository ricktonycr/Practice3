package com.unsa.logic;

public class ComplexNumber {
    public double real;
    public double img;
    public ComplexNumber(){}
    public ComplexNumber(double r, double i){
        real = r;
        img = i;
    }

    public ComplexNumber(ComplexNumber c){
        real = c.real;
        img = c.img;
    }

    public double magnitude(){
        return Math.sqrt(this.cNorm());
    }

    public double phaseAngle(){
        if(real==0 && img == 0) return 0;
        else return Math.atan(img /real);
    }

    double cNorm (){
        return real*real + img * img;
    }

    public static ComplexNumber cExp (ComplexNumber z){
        ComplexNumber x,y;
        x = new ComplexNumber(Math.exp(z.real),0.0);
        y = new ComplexNumber(Math.cos(z.img),Math.sin(z.img));
        return cMult (x,y);
    }

    public static ComplexNumber cMult (ComplexNumber z1, ComplexNumber z2){
        ComplexNumber z3 = new ComplexNumber();
        z3.real = (z1.real)*(z2.real) - (z1.img)*(z2.img);
        z3.img = (z1.real)*(z2.img) + (z1.img)*(z2.real);
        return z3;
    }

    public static ComplexNumber cSum (ComplexNumber z1, ComplexNumber z2){
        ComplexNumber z3 = new ComplexNumber();
        z3.real = z1.real + z2.real;
        z3.img = z1.img + z2.img;
        return z3;
    }

    public static ComplexNumber cDiv (ComplexNumber z1, ComplexNumber z2){
        ComplexNumber z3 = new ComplexNumber();
        double n = z2.cNorm();

        z3.real = ((z1.real*z2.real) + (z1.img *z2.img))/n;
        z3.img = ((z2.real*z1.img) - (z1.real*z2.img))/n;
        return z3;
    }

    public static ComplexNumber cDif (ComplexNumber z1, ComplexNumber z2){
        ComplexNumber z3 = new ComplexNumber();

        z3.real = z1.real - z2.real;
        z3.img = z1.img - z2.img;
        return z3;
    }
}
