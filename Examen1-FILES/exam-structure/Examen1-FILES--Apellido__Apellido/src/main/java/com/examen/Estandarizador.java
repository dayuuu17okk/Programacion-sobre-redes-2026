
package com.examen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Se encarga de leer el archivo original juegos.dat y reestructurarlo
 * a un formato estandarizado CSV con separador " ; ".
 *
 * El archivo original usa '+' como separador.
 * Ejemplo:
 *
 * 20/04+1+0+1+4+7+5
 *
 * pasa a:
 *
 * 20/04;1;0;1;4;7;5
 */
public class Estandarizador {

    /**
     * Lee el archivo original (juegos.dat), reemplaza '+'
     * por ';', guarda el resultado en juegos.csv
     * y elimina el archivo original.
     *
     * @param rutaOriginal Ruta del archivo juegos.dat
     */
    public void estandarizar(String rutaOriginal) {

        try {

            File archivo = new File(rutaOriginal);

            if (!archivo.exists()) {
                return;
            }

            BufferedReader br = new BufferedReader(new FileReader(archivo));

            BufferedWriter bw = new BufferedWriter(new FileWriter("juegos.csv"));

            String linea;

            while ((linea = br.readLine()) != null) {

                linea = linea.replace("+", ";");

                bw.write(linea);
                bw.newLine();

            }

            br.close();
            bw.close();

            archivo.delete();

        } catch (Exception ex) {

            LogManager.registrarError("Error al estandarizar archivo", ex);

        }

    }

    /**
     * Genera una clave AES de 256 bits.
     *
     * @return SecretKey generada.
     */
    public static SecretKey generarClaveAES() {

        try {

            KeyGenerator generador = KeyGenerator.getInstance("AES");
            generador.init(256);

            return generador.generateKey();

        } catch (Exception ex) {

            Logger.getLogger(Estandarizador.class.getName()).log(Level.WARNING, null, ex);

        }

        return null;

    }

    /**
     * Guarda una clave AES en un archivo.
     *
     * @param clave Clave AES
     * @param rutaArchivo Ruta del archivo
     */
    public static void guardarClave(SecretKey clave, String rutaArchivo) {

        try {

            String claveEnTexto = Base64.getEncoder().encodeToString(clave.getEncoded());

            BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo));

            bw.write(claveEnTexto);

            bw.close();

        } catch (Exception ex) {

            Logger.getLogger(Estandarizador.class.getName()).log(Level.WARNING,
                    "Error al guardar la clave en el archivo", ex);

        }

    }

    /**
     * Recupera una clave AES desde un archivo.
     *
     * @param rutaArchivo Ruta del archivo
     * @return SecretKey recuperada
     */
    public static SecretKey recuperarClave(String rutaArchivo) {

        try {

            BufferedReader br = new BufferedReader(new FileReader(rutaArchivo));

            String textoLeido = br.readLine();

            br.close();

            byte[] bytesClave = Base64.getDecoder().decode(textoLeido);

            return new SecretKeySpec(bytesClave, 0, bytesClave.length, "AES");

        } catch (Exception ex) {

            Logger.getLogger(Estandarizador.class.getName()).log(Level.WARNING,
                    "Error al recuperar la clave del archivo", ex);

        }

        return null;

    }

    /**
     * Encripta un texto usando AES.
     *
     * @param datos Texto plano
     * @param clave Clave AES
     * @return Texto encriptado en Base64
     */
    public static String encriptar(String datos, SecretKey clave) {

        try {

            Cipher cipher = Cipher.getInstance("AES");

            cipher.init(Cipher.ENCRYPT_MODE, clave);

            byte[] datosEncriptados = cipher.doFinal(datos.getBytes());

            return Base64.getEncoder().encodeToString(datosEncriptados);

        } catch (Exception ex) {

            LogManager.registrarError("Error al encriptar datos", ex);

        }

        return "";

    }

    /**
     * Desencripta un texto usando AES.
     *
     * @param datosEncriptados Texto encriptado
     * @param clave Clave AES
     * @return Texto desencriptado
     */
    public static String desencriptar(String datosEncriptados, SecretKey clave) {

        try {

            Cipher cipher = Cipher.getInstance("AES");

            cipher.init(Cipher.DECRYPT_MODE, clave);

            byte[] datos = Base64.getDecoder().decode(datosEncriptados);

            byte[] desencriptado = cipher.doFinal(datos);

            return new String(desencriptado);

        } catch (Exception ex) {

            LogManager.registrarError("Error al desencriptar datos", ex);

        }

        return "";

    }

}




