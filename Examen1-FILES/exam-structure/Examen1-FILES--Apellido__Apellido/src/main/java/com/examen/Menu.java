

package com.examen;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Menu {

    private GestorPartidas gestor;
    private BufferedReader reader;

    public Menu(GestorPartidas gestor) {

        this.gestor = gestor;
        this.reader = new BufferedReader(new InputStreamReader(System.in));

        gestor.cargar("juegos.csv");

    }

    public void iniciar() {

        boolean salir = false;

        while (!salir) {

            try {

                System.out.println();
                System.out.println("\u001B[36m========================================");
                System.out.println("     HISTORIAL LEAGUE OF LEGENDS");
                System.out.println("========================================\u001B[0m");
                System.out.println("\u001B[32m1\u001B[0m - Mostrar historial");
                System.out.println("\u001B[32m2\u001B[0m - Eliminar partida");
                System.out.println("\u001B[32m3\u001B[0m - Mes con más victorias");
                System.out.println("\u001B[32m4\u001B[0m - Cantidad de partidas");
                System.out.println("\u001B[31m5\u001B[0m - Guardar y salir");
                System.out.print("Seleccione una opción: ");

                String opcion = reader.readLine();

                switch (opcion) {

                    case "1":

                        gestor.mostrarTodos();

                        break;

                    case "2":

                        System.out.print("Ingrese el índice de la partida: ");

                        int indice = Integer.parseInt(reader.readLine());

                        gestor.eliminar(indice);

                        break;

                    case "3":

                        System.out.println("Mes con más victorias: "
                                + gestor.mesMasVictorias());

                        break;

                    case "4":

                        System.out.println("Cantidad de partidas: "
                                + gestor.cantidadPartidas());

                        break;

                    case "5":

                        gestor.guardar("juegos.csv");

                        System.out.println("Datos guardados correctamente.");

                        salir = true;

                        break;

                    default:

                        System.out.println("Opción inválida.");

                        break;

                }

            } catch (Exception e) {

                LogManager.registrarError("Error en el menú", e);

            }

        }

    }

}
