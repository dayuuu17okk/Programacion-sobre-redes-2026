package tarea.com;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        int opcion;

        do {
            Utils.getOut().println("\n===== MENU =====");
            Utils.getOut().println("1 - Ejercicio 1 (Lista nombres)");
            Utils.getOut().println("2 - Ejercicio 2 (Buscar)");
            Utils.getOut().println("3 - Modificar lista");
            Utils.getOut().println("4 - Recorrer lista");
            Utils.getOut().println("5 - Procesar texto");
            Utils.getOut().println("6 - Normalización");
            Utils.getOut().println("7 - Diccionario");
            Utils.getOut().println("8 - Traductor");
            Utils.getOut().println("9 - Traductor de frases");
            Utils.getOut().println("10 - Contador de palabras");
            Utils.getOut().println("11 - Eliminar duplicados");
            Utils.getOut().println("12 - Sistema de alumnos");
            Utils.getOut().println("0 - Salir");

            Utils.getOut().print("Opcion: ");

            opcion = Integer.parseInt(Utils.getLector().readLine());

            switch (opcion) {
                case 1: Guia1.Ejercicio1(); break;
                case 2: Guia1.Ejercicio2(); break;
                case 3: Guia1.Ejercicio3(); break;
                case 4: Guia1.Ejercicio4(); break;
                case 5: Guia1.Ejercicio5(); break;
                case 6: Guia1.Ejercicio6(); break;
                case 7: Guia1.Ejercicio7(); break;
                case 8: Guia1.Ejercicio8(); break;
                case 9: Guia1.Ejercicio9(); break;
                case 10: Guia1.Ejercicio10(); break;
                case 11: Guia1.Ejercicio11(); break;
                case 12: Guia1.Ejercicio12(); break;
                case 0: Utils.getOut().println("Saliendo..."); break;
                default: Utils.getOut().println("Opcion invalida");
            }

        } while (opcion != 0);
    }
}
