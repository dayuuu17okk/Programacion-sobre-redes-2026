

import java.io.*;
import java.net.Socket;

public class ManejadorCliente implements Runnable {
    private Socket socket;

    public ManejadorCliente(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (DataInputStream entrada = new DataInputStream(socket.getInputStream());
             DataOutputStream salida = new DataOutputStream(socket.getOutputStream())) {

            salida.writeUTF("CONEXION_ESTABLECIDA: Por favor, inicie sesión con LOGIN <usuario> <clave>");

            boolean autenticado = false;
            boolean sesionActiva = true;

            while (sesionActiva) {
                String peticion = entrada.readUTF();
                System.out.println("[HILO " + Thread.currentThread().getName() + "] Comando recibido: " + peticion);

                String[] partes = peticion.trim().split(" ", 2);
                String comando = partes[0].toUpperCase();
                String argumentos = partes.length > 1 ? partes[1] : "";

                if (!autenticado) {
                    if (comando.equals("LOGIN")) {
                        String[] creds = argumentos.split(" ");
                        // Credenciales predeterminadas para testeo: admin / 1234
                        if (creds.length == 2 && creds[0].equals("admin") && creds[1].equals("1234")) {
                            autenticado = true;
                            salida.writeUTF("LOGIN_OK: Autenticación exitosa. Bienvenido.");
                            System.out.println("[HILO " + Thread.currentThread().getName() + "] Cliente autenticado con éxito.");
                        } else {
                            salida.writeUTF("LOGIN_ERROR: Usuario o contraseña incorrectos.");
                        }
                    } else if (comando.equals("EXIT")) {
                        salida.writeUTF("EXIT_OK: Cerrando conexión.");
                        sesionActiva = false;
                    } else {
                        salida.writeUTF("ERROR: Debe autenticarse primero usando LOGIN <usuario> <clave>");
                    }
                } else {
                    switch (comando) {
                        case "LOGIN":
                            salida.writeUTF("INFO: Ya se encuentra autenticado en el sistema.");
                            break;

                        case "LIST":
                            File dir = new File(ServidorNube.CARPETA_SERVIDOR);
                            File[] archivos = dir.listFiles();
                            if (archivos == null || archivos.length == 0) {
                                salida.writeUTF("LIST_VACIO: No hay archivos almacenados en la nube.");
                            } else {
                                StringBuilder sb = new StringBuilder("LIST_OK:\n--- ARCHIVOS EN NUBE ---\n");
                                for (File f : archivos) {
                                    if (f.isFile()) {
                                        sb.append(" - ").append(f.getName()).append(" (").append(f.length()).append(" bytes)\n");
                                    }
                                }
                                salida.writeUTF(sb.toString());
                            }
                            break;

                        case "UPLOAD":
                            if (argumentos.isEmpty()) {
                                salida.writeUTF("ERROR: Debe especificar el nombre del archivo.");
                                break;
                            }
                            salida.writeUTF("READY_TO_RECEIVE");
                            long tamano = entrada.readLong();
                            File archivoDestino = new File(ServidorNube.CARPETA_SERVIDOR, argumentos);
                            try (FileOutputStream fos = new FileOutputStream(archivoDestino)) {
                                byte[] buffer = new byte[4096];
                                int leidos;
                                long totalLeido = 0;
                                while (totalLeido < tamano && (leidos = entrada.read(buffer, 0, (int) Math.min(buffer.length, tamano - totalLeido))) != -1) {
                                    fos.write(buffer, 0, leidos);
                                    totalLeido += leidos;
                                }
                            }
                            salida.writeUTF("UPLOAD_OK: Archivo subido y guardado correctamente en la nube.");
                            System.out.println("[HILO " + Thread.currentThread().getName() + "] Archivo recibido y guardado: " + argumentos);
                            break;

                        case "DOWNLOAD":
                            if (argumentos.isEmpty()) {
                                salida.writeUTF("ERROR: Debe especificar el archivo a descargar.");
                                break;
                            }
                            File archivoOrigen = new File(ServidorNube.CARPETA_SERVIDOR, argumentos);
                            if (!archivoOrigen.exists() || !archivoOrigen.isFile()) {
                                salida.writeUTF("ERROR: El archivo no existe en el servidor.");
                            } else {
                                salida.writeUTF("FILE_EXISTS");
                                salida.writeLong(archivoOrigen.length());
                                try (FileInputStream fis = new FileInputStream(archivoOrigen)) {
                                    byte[] buffer = new byte[4096];
                                    int leidos;
                                    while ((leidos = fis.read(buffer)) != -1) {
                                        salida.write(buffer, 0, leidos);
                                    }
                                }
                                System.out.println("[HILO " + Thread.currentThread().getName() + "] Archivo enviado al cliente: " + argumentos);
                            }
                            break;

                        case "EXIT":
                            salida.writeUTF("EXIT_OK: Sesión finalizada correctamente.");
                            sesionActiva = false;
                            break;

                        default:
                            salida.writeUTF("ERROR: Comando desconocido.");
                            break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("[SERVIDOR] Conexión cerrada con el cliente: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {}
            System.out.println("[SERVIDOR] Hilo finalizado.");
        }
    }
}