
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorNube {
    private static final int PUERTO = 6767;
    public static final String CARPETA_SERVIDOR = "./repositorio_nube";

    public static void main(String[] args) {
        // Crear carpeta local de la nube si no existe
        File carpeta = new File(CARPETA_SERVIDOR);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        System.out.println("[SERVIDOR] Iniciando Servidor de Nube en el puerto " + PUERTO + "...");

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println("[SERVIDOR] Escuchando conexiones concurrentes de clientes...");

            while (true) {
                Socket socketCliente = serverSocket.accept();
                System.out.println("[SERVIDOR] ¡Nuevo cliente conectado desde: " + socketCliente.getInetAddress() + "!");

                // Derivamos la conexión a un hilo independiente (concurrencia)
                ManejadorCliente manejador = new ManejadorCliente(socketCliente);
                new Thread(manejador).start();
            }
        } catch (IOException e) {
            System.err.println("[SERVIDOR] Error en el servidor: " + e.getMessage());
        }
    }
}


