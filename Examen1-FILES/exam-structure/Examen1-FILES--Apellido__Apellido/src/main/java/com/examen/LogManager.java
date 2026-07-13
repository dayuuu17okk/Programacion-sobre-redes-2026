package com.examen;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class LogManager {

    public static void inicializar() {

        try {

            File archivo = new File("crash.log");

            if (!archivo.exists()) {
                archivo.createNewFile();
            }

        } catch (Exception e) {

        }

    }

    public static void registrarError(String mensaje, Exception e) {

        try {

            FileWriter fw = new FileWriter("crash.log", true);

            PrintWriter pw = new PrintWriter(fw);

            pw.println("-------------------------------------");
            pw.println(LocalDateTime.now());
            pw.println("ERROR: " + mensaje);

            if (e != null) {
                e.printStackTrace(pw);
            }

            pw.close();
            fw.close();

        } catch (Exception ex) {

        }

    }

    public static void registrarInfo(String mensaje) {

        try {

            FileWriter fw = new FileWriter("crash.log", true);

            PrintWriter pw = new PrintWriter(fw);

            pw.println("-------------------------------------");
            pw.println(LocalDateTime.now());
            pw.println("INFO: " + mensaje);

            pw.close();
            fw.close();

        } catch (Exception e) {

        }

    }

}

