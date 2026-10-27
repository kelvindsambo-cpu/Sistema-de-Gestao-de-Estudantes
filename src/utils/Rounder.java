package utils;

import java.util.Locale;

public class Rounder {
    public static double round(double valor, int casas){
        int n = (int) Math.pow(10,casas);
        return (double) Math.round(valor*n)/n;
    }

    public static String formatar(double valor, int casas) {
        return String.format(Locale.ROOT, "%." + casas + "f", valor);
    }

}
