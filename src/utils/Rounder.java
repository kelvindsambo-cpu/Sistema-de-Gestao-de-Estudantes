package utils;

public class Rounder {
    public static double round(double valor, int casas){
        int n = (int) Math.pow(10,casas);
        return (double) Math.round(valor*n)/n;
    }

}
