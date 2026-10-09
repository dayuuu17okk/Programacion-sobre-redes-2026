package socketdireccional;

import java.io.*;
import java.net.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Cliente {

    private static final String IP_SERVIDOR = "127.0.0.1";
    private static final int PUERTO = 3101;
    private static final String ALUMNA = "Daiana Rivera";
    private static final String CLAVE = "DaianaKey3101ABC";

    private static final String RESET = "\u001B[0m";
    private static final String VERDE = "\u001B[32m";
    private static final String AZUL = "\u001B[34m";
    private static final String AMARILLO = "\u001B[33m";
    private static final String ROJO = "\u001B[31m";
    private static final String CYAN = "\u001B[36m";

    public static void main(String[] args) {

        String ip = args.length > 0 ? args[0] : IP_SERVIDOR;

        log("SISTEMA", AMARILLO,
                "Iniciando cliente de " + ALUMNA);

        try (Socket socket = new Socket(ip, PUERTO);
             DataInputStream entrada = new DataInputStream(
                     new BufferedInputStream(socket.getInputStream()));
             DataOutputStream salida = new DataOutputStream(
                     new BufferedOutputStream(socket.getOutputStream()));
             Scanner scanner = new Scanner(System.in)) {

            log("CONEXION", VERDE,
                    "Conexion establecida con el servidor.");

            String saludo = recibirTexto(entrada);
            log("SERVIDOR", AZUL, saludo);

            boolean activo = true;

            while (activo) {

                System.out.print("\n" + CYAN + "[" + ALUMNA
                        + " - Cliente] > " + RESET);

                String mensaje = scanner.nextLine().trim();

                if (mensaje.isEmpty()) {
                    continue;
                }

                enviarTexto(salida, mensaje);

                log("ENVIADO", VERDE,
                        "Solicitud cifrada enviada: " + mensaje);

                if (mensaje.equalsIgnoreCase("subir comprobante")) {

                    String respuestaInicial = recibirTexto(entrada);

                    if (!respuestaInicial.equals("ENVIAR_ARCHIVO")) {
                        log("SERVIDOR", AZUL, respuestaInicial);
                        continue;
                    }

                    System.out.print(
                            "Ingresa la ruta del archivo que queres enviar: ");

                    String ruta = scanner.nextLine().trim();
                    File archivo = new File(ruta);

                    if (!archivo.exists() || !archivo.isFile()
                            || archivo.length() <= 0
                            || archivo.length() > 20_000_000) {

                        log("ERROR", ROJO,
                                "Archivo invalido, vacio o superior a 20 MB.");

                        log("SISTEMA", AMARILLO,
                                "Cerrando conexion para evitar errores.");

                        break;
                    }

                    byte[] contenido = new byte[(int) archivo.length()];

                    try (FileInputStream fis = new FileInputStream(archivo)) {

                        int totalLeido = 0;
                        int cantidad;

                        while (totalLeido < contenido.length
                                && (cantidad = fis.read(
                                        contenido, totalLeido,
                                        contenido.length - totalLeido)) != -1) {

                            totalLeido += cantidad;
                        }

                        if (totalLeido != contenido.length) {
                            throw new IOException(
                                    "No se pudo leer el archivo completo.");
                        }
                    }

                    byte[] cifrado = encriptarBytes(contenido);

                    salida.writeLong(cifrado.length);
                    salida.write(cifrado);
                    salida.flush();

                    log("ARCHIVO", VERDE,
                            "Comprobante cifrado y enviado.");

                    String respuestaArchivo = recibirTexto(entrada);

                    log("SERVIDOR", AZUL, respuestaArchivo);
                    continue;
                }

                String respuesta = recibirTexto(entrada);

                log("RECIBIDO", AZUL,
                        "Respuesta del servidor:\n" + respuesta);

                if (mensaje.equalsIgnoreCase("salir")) {
                    activo = false;

                    log("SISTEMA", AMARILLO,
                            "Cerrando sesion.");
                }
            }

        } catch (Exception e) {

            log("ERROR", ROJO,
                    "Error de conexion: " + e.getMessage());
        }
    }

    private static void enviarTexto(
            DataOutputStream salida, String texto) throws Exception {

        salida.writeUTF(encriptar(texto));
        salida.flush();
    }

    private static String recibirTexto(
            DataInputStream entrada) throws Exception {

        return desencriptar(entrada.readUTF());
    }

    private static String encriptar(String texto) throws Exception {

        byte[] datos = texto.getBytes(
                java.nio.charset.StandardCharsets.UTF_8);

        return java.util.Base64.getEncoder().encodeToString(
                encriptarBytes(datos));
    }

    private static String desencriptar(String texto) throws Exception {

        byte[] datos = java.util.Base64.getDecoder().decode(texto);

        return new String(descifrarBytes(datos),
                java.nio.charset.StandardCharsets.UTF_8);
    }

    private static byte[] encriptarBytes(byte[] datos) throws Exception {

        byte[] nonce = new byte[12];
        new java.security.SecureRandom().nextBytes(nonce);

        javax.crypto.Cipher cifrador =
                javax.crypto.Cipher.getInstance("AES/GCM/NoPadding");

        byte[] clave = CLAVE.getBytes(
                java.nio.charset.StandardCharsets.UTF_8);

        cifrador.init(
                javax.crypto.Cipher.ENCRYPT_MODE,
                new javax.crypto.spec.SecretKeySpec(clave, "AES"),
                new javax.crypto.spec.GCMParameterSpec(128, nonce));

        byte[] cifrado = cifrador.doFinal(datos);
        byte[] resultado = new byte[nonce.length + cifrado.length];

        System.arraycopy(nonce, 0, resultado, 0, nonce.length);
        System.arraycopy(cifrado, 0, resultado,
                nonce.length, cifrado.length);

        return resultado;
    }

    private static byte[] descifrarBytes(byte[] datos) throws Exception {

        if (datos.length < 13) {
            throw new IOException("Datos cifrados invalidos.");
        }

        byte[] nonce = new byte[12];
        System.arraycopy(datos, 0, nonce, 0, nonce.length);

        byte[] cifrado = new byte[datos.length - nonce.length];

        System.arraycopy(datos, nonce.length, cifrado, 0,
                cifrado.length);

        javax.crypto.Cipher descifrador =
                javax.crypto.Cipher.getInstance("AES/GCM/NoPadding");

        byte[] clave = CLAVE.getBytes(
                java.nio.charset.StandardCharsets.UTF_8);

        descifrador.init(
                javax.crypto.Cipher.DECRYPT_MODE,
                new javax.crypto.spec.SecretKeySpec(clave, "AES"),
                new javax.crypto.spec.GCMParameterSpec(128, nonce));

        return descifrador.doFinal(cifrado);
    }

    private static void log(
            String tipo, String color, String mensaje) {

        String hora = LocalTime.now().format(
                DateTimeFormatter.ofPattern("HH:mm:ss"));

        System.out.printf("[%s] %s%-12s%s | %s%n",
                hora, color, tipo, RESET, mensaje);
    }
}