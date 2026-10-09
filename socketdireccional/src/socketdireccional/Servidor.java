package socketdireccional;

import java.io.*;
import java.net.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Servidor {

    private static final int PUERTO = 3101;
    private static final String ALUMNA = "Daiana Rivera";
    private static final String CLAVE = "DaianaKey3101ABC";

    private static final String RESET = "\u001B[0m";
    private static final String VERDE = "\u001B[32m";
    private static final String AZUL = "\u001B[34m";
    private static final String AMARILLO = "\u001B[33m";
    private static final String ROJO = "\u001B[31m";
    private static final String CYAN = "\u001B[36m";

    private static final String[] PRODUCTOS = {
        "Laptop - Stock: 10",
        "Mouse - Stock: 25",
        "Teclado - Stock: 15",
        "Monitor - Stock: 8"
    };

    public static void main(String[] args) {

        log("SISTEMA", AMARILLO,
                "Servidor de " + ALUMNA + " iniciado.");

        try (ServerSocket servidor = new ServerSocket(PUERTO)) {

            log("SERVIDOR", VERDE,
                    "Esperando conexiones en el puerto " + PUERTO);

            while (true) {
                try (Socket cliente = servidor.accept();
                     DataInputStream entrada = new DataInputStream(
                             new BufferedInputStream(cliente.getInputStream()));
                     DataOutputStream salida = new DataOutputStream(
                             new BufferedOutputStream(cliente.getOutputStream()))) {

                    log("CONEXION", VERDE,
                            "Cliente conectado: "
                            + cliente.getInetAddress().getHostAddress());

                    atenderCliente(entrada, salida);

                    log("CONEXION", AMARILLO,
                            "Cliente desconectado.");

                } catch (Exception e) {
                    log("ERROR", ROJO,
                            "Error con el cliente: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            log("ERROR", ROJO,
                    "No se pudo iniciar el servidor: " + e.getMessage());
        }
    }

    private static void atenderCliente(
            DataInputStream entrada,
            DataOutputStream salida) throws Exception {

        boolean autenticado = false;
        boolean activo = true;

        java.util.ArrayList<String> historial =
                new java.util.ArrayList<>();

        enviarTexto(salida,
                "Hola, tengo un problema con mi pedido. "
                + "Bienvenido al servicio de atencion al cliente. "
                + "Escribi ayuda para ver las opciones.");

        while (activo) {

            String mensaje = recibirTexto(entrada);
            String comando = mensaje.toLowerCase().trim();

            historial.add("Cliente: " + mensaje);

            log("RECIBIDO", AZUL, mensaje);

            String respuesta;

            if (comando.equals("autenticar")) {

                autenticado = true;

                respuesta = "Autenticacion completada para esta sesion.";

            } else if (comando.equals("ayuda")) {

                respuesta = "COMANDOS DISPONIBLES:\n"
                        + "autenticar - Iniciar sesion\n"
                        + "ayuda - Ver comandos\n"
                        + "ver stock de un producto - Consultar inventario\n"
                        + "subir comprobante - Enviar un archivo\n"
                        + "ver historial de chat - Consultar historial\n"
                        + "salir - Cerrar sesion";

            } else if (comando.startsWith("ver stock de ")) {

                String producto = comando.substring(
                        "ver stock de ".length()).trim();

                respuesta = buscarProducto(producto);

            } else if (comando.equals("ver stock de un producto")) {

                respuesta = "Productos disponibles:\n"
                        + String.join("\n", PRODUCTOS);

            } else if (comando.equals("subir comprobante")) {

                if (!autenticado) {
                    respuesta = "Acceso denegado. Primero escribi autenticar.";
                    enviarTexto(salida, respuesta);
                    historial.add("Servidor: " + respuesta);
                    continue;
                }

                enviarTexto(salida, "ENVIAR_ARCHIVO");

                long longitud = entrada.readLong();

                if (longitud <= 0 || longitud > 20_000_100) {
                    throw new IOException("Tamano de archivo invalido.");
                }

                byte[] archivoCifrado = new byte[(int) longitud];
                entrada.readFully(archivoCifrado);

                byte[] archivoOriginal = descifrarBytes(archivoCifrado);

                String nombre = "comprobante_" + System.currentTimeMillis()
                        + ".dat";

                try (FileOutputStream archivo =
                             new FileOutputStream(nombre)) {
                    archivo.write(archivoOriginal);
                }

                respuesta = "Comprobante recibido y guardado como " + nombre;

            } else if (comando.equals("ver historial de chat")) {

                respuesta = historial.isEmpty()
                        ? "El historial esta vacio."
                        : String.join("\n", historial);

            } else if (comando.equals("salir")) {

                respuesta = "Sesion cerrada. Hasta luego.";
                activo = false;

            } else {

                respuesta = "No reconozco ese comando. Escribi ayuda "
                        + "para ver las opciones.";
            }

            enviarTexto(salida, respuesta);
            historial.add("Servidor: " + respuesta);

            log("ENVIADO", VERDE, respuesta);
        }
    }

    private static String buscarProducto(String producto) {

        for (String item : PRODUCTOS) {
            String nombre = item.split(" - ")[0];

            if (nombre.equalsIgnoreCase(producto)) {
                return "Producto encontrado: " + item;
            }
        }

        return "Producto no encontrado. Escribi 'ayuda' para consultar opciones.";
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
