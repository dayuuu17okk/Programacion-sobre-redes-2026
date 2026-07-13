package com.examen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class GestorPartidas {

    private ArrayList<Partida> partidas;

    public GestorPartidas() {
        this.partidas = new ArrayList<>();
    }

    public void cargar(String rutaCsv) {

        try {

            BufferedReader br = new BufferedReader(new FileReader(rutaCsv));

            String linea = br.readLine();

            while ((linea = br.readLine()) != null) {

                String[] datos = linea.split(";");

                Partida partida = new Partida(
                        datos[0],
                        Integer.parseInt(datos[1]),
                        Integer.parseInt(datos[2]),
                        Integer.parseInt(datos[3]),
                        Integer.parseInt(datos[4]),
                        Integer.parseInt(datos[5]),
                        Integer.parseInt(datos[6])
                );

                partidas.add(partida);

            }

            br.close();

        } catch (Exception e) {

            LogManager.registrarError("Error al cargar partidas", e);

        }

    }

    public void mostrarTodos() {

        if (partidas.isEmpty()) {

            System.out.println("No hay partidas cargadas.");
            return;

        }

        System.out.printf("%-8s %-5s %-5s %-5s %-7s %-7s %-7s%n",
                "Fecha",
                "Win",
                "Torre",
                "FB",
                "Kills",
                "Deaths",
                "Assist");

        for (Partida p : partidas) {

            System.out.printf("%-8s %-5d %-5d %-5d %-7d %-7d %-7d%n",
                    p.getFecha(),
                    p.getHasWon(),
                    p.getIsFirstTower(),
                    p.getIsFirstBlood(),
                    p.getKills(),
                    p.getDeaths(),
                    p.getAssists());

        }

    }

    public String mesMasVictorias() {

        if (partidas.isEmpty()) {
            return "SIN DATOS";
        }

        int[] meses = new int[13];

        for (Partida p : partidas) {

            if (p.getHasWon() == 1) {

                String[] fecha = p.getFecha().split("/");

                int mes = Integer.parseInt(fecha[1]);

                meses[mes]++;

            }

        }

        int mayor = 0;
        int mes = 0;

        for (int i = 1; i < meses.length; i++) {

            if (meses[i] > mayor) {

                mayor = meses[i];
                mes = i;

            }

        }

        return String.format("%02d", mes);

    }

    public void guardar(String rutaCsv) {

        try {

            BufferedWriter bw = new BufferedWriter(new FileWriter(rutaCsv));

            bw.write("date;hasWon;isFirstTower;isFirstBlood;kills;deaths;assists");
            bw.newLine();

            for (Partida p : partidas) {

                bw.write(
                        p.getFecha() + ";" +
                        p.getHasWon() + ";" +
                        p.getIsFirstTower() + ";" +
                        p.getIsFirstBlood() + ";" +
                        p.getKills() + ";" +
                        p.getDeaths() + ";" +
                        p.getAssists()
                );

                bw.newLine();

            }

            bw.close();

        } catch (Exception e) {

            LogManager.registrarError("Error al guardar partidas", e);

        }

    }

    public void eliminar(int indice) {

        if (indice >= 0 && indice < partidas.size()) {

            partidas.remove(indice);

            System.out.println("Partida eliminada.");

        } else {

            System.out.println("Indice invalido.");

        }

    }

    public int cantidadPartidas() {
        return partidas.size();
    }

    public List<Partida> getPartidas() {
        return partidas;
    }

}
