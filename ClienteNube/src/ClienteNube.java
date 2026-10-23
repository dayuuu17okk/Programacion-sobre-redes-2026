
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClienteNube {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Solicita la IP del servidor (permite probar en red local entre dos PCs físicas)
        System.out.print("Ingrese la dirección IP del servidor (ej: 192.168.1.50 o localhost): ");
        String ipServidor = scanner.nextLine().trim();
        int puerto = 6767;

        System.out.println("[CLIENTE] Conectando a " + ipServidor + ":" + puerto + "...");

        try (Socket socket = new Socket(ipServidor, puerto);
             DataInputStream entrada = new DataInputStream(socket.getInputStream());
             DataOutputStream salida = new DataOutputStream(socket.getOutputStream())) {

            System.out.println("[CLIENTE] " + entrada.readUTF());
            boolean activo = true;

            while (activo) {
                System.out.print("\nClienteNube> ");
                String comando = scanner.nextLine();
                if (comando.trim().isEmpty()) continue;

                salida.writeUTF(comando);

                String[] partes = comando.trim().split(" ", 2);
                String cmd = partes[0].toUpperCase();
                String arg = partes.length > 1 ? partes[1] : "";

                if (cmd.equals("UPLOAD") && !arg.isEmpty()) {
                    File f = new File(arg);
                    String respuestaServidor = entrada.readUTF();
                    if (respuestaServidor.equals("READY_TO_RECEIVE")) {
                        if (f.exists() && f.isFile()) {
                            salida.writeLong(f.length());
                            try (FileInputStream fis = new FileInputStream(f)) {
                                byte[] buffer = new byte[4096];
                                int leidos;
                                while ((leidos = fis.read(buffer)) != -1) {
                                    salida.write(buffer, 0, leidos);
                                }
                            }
                            System.out.println(entrada.readUTF());
                        } else {
                            System.out.println("[ERROR] El archivo local no existe.");
                            salida.writeLong(0);
                        }
                    } else {
                        System.out.println(respuestaServidor);
                    }
                } else if (cmd.equals("DOWNLOAD") && !arg.isEmpty()) {
                    String respuestaServidor = entrada.readUTF();
                    if (respuestaServidor.equals("FILE_EXISTS")) {
                        long tamano = entrada.readLong();
                        File archivoDestino = new File("descargado_" + arg);
                        try (FileOutputStream fos = new FileOutputStream(archivoDestino)) {
                            byte[] buffer = new byte[4096];
                            int leidos;
                            long totalLeido = 0;
                            while (totalLeido < tamano && (leidos = entrada.read(buffer, 0, (int) Math.min(buffer.length, tamano - totalLeido))) != -1) {
                                fos.write(buffer, 0, leidos);
                                totalLeido += leidos;
                            }
                        }
                        System.out.println("[CLIENTE] Archivo descargado con éxito como: descargado_" + arg);
                    } else {
                        System.out.println(respuestaServidor);
                    }
                } else {
                    String respuesta = entrada.readUTF();
                    System.out.println(respuesta);
                    if (cmd.equals("EXIT") || respuesta.startsWith("EXIT_OK")) {
                        activo = false;
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("[CLIENTE] Error de red o conexión perdida: " + e.getMessage());
        }
    }
}
