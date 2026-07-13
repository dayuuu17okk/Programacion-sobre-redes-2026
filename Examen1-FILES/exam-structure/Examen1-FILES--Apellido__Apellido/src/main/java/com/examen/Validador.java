package com.examen;

public class Validador {

    public static boolean esVacio(String texto) {

        if (texto == null) {
            return true;
        }

        return texto.trim().isEmpty();

    }

    public static boolean esEntero(String texto) {

        try {

            Integer.parseInt(texto);

            return true;

        } catch (Exception e) {

            return false;

        }

    }

    public static boolean esDecimal(String texto) {

        try {

            Float.parseFloat(texto);

            return true;

        } catch (Exception e) {

            return false;

        }

    }

    public static int convertirEntero(String texto) {

        try {

            return Integer.parseInt(texto);

        } catch (Exception e) {

            return 0;

        }

    }

    public static float convertirDecimal(String texto) {

        try {

            return Float.parseFloat(texto);

        } catch (Exception e) {

            return 0;

        }

    }

}








