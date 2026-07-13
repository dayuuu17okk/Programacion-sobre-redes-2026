package com.examen;

public class Main {

    public static void main(String[] args) {

        LogManager.inicializar();

        Estandarizador estandarizador = new Estandarizador();
        estandarizador.estandarizar("juegos.dat");

        GestorPartidas gestor = new GestorPartidas();

        Menu menu = new Menu(gestor);

        menu.iniciar();

    }

}