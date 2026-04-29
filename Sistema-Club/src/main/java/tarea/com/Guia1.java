package tarea.com;

import java.io.IOException;
import java.util.ArrayList;

public class Guia1 {

    static ArrayList<String> nombres = new ArrayList<>();
    static java.util.HashMap<String, String> diccionario = new java.util.HashMap<>();
    static java.util.ArrayList<String> alumnos = new java.util.ArrayList<>();
    static java.util.HashMap<String, Integer> notas = new java.util.HashMap<>();



   
  
    public static void Ejercicio1() throws IOException {

        Utils.getOut().println("Ingrese 5 nombres:");

        for (int i = 0; i < 5; i++) {
            String nombre = Utils.getLector().readLine();
            nombres.add(nombre);
        }

        // Lista completa
        Utils.getOut().println("\nLista completa:");
        Utils.getOut().println(nombres);

        
        Utils.getOut().println("Cantidad de elementos: " + nombres.size());

       
        Utils.getOut().println("Primero: " + nombres.get(0));
        Utils.getOut().println("Último: " + nombres.get(nombres.size() - 1));

        
        Utils.getOut().println("\nNombres en MAYÚSCULAS:");
        for (String n : nombres) {
            Utils.getOut().println(n.toUpperCase());
        }
    }

    
    public static void Ejercicio2() throws IOException {

        Utils.getOut().println("\nIngrese un nombre a buscar:");
        String buscar = Utils.getLector().readLine();

        if (nombres.contains(buscar)) {
            int pos = nombres.indexOf(buscar);
            Utils.getOut().println("Existe en la posición: " + pos);
        } else {
            Utils.getOut().println(Utils.ROJO + "El nombre no existe en la lista" + Utils.RESET);
        }
    }


public static void Ejercicio3() throws IOException {

    if (nombres.isEmpty()) {
        Utils.getOut().println("Primero cargá la lista (Ejercicio 1)");
        return;
    }

    
    if (nombres.size() >= 3) {
        Utils.getOut().println("Nuevo valor para la posición 2:");
        String nuevo = Utils.getLector().readLine();
        nombres.set(2, nuevo);
    }

   
    Utils.getOut().println("Nombre a eliminar:");
    String eliminar = Utils.getLector().readLine();

    if (nombres.remove(eliminar)) {
        Utils.getOut().println("Eliminado correctamente");
    } else {
        Utils.getOut().println(Utils.ROJO + "No se encontró el nombre" + Utils.RESET);
    }

   
    Utils.getOut().println("Lista final:");
    Utils.getOut().println(nombres);
}



public static void Ejercicio4() {


    if (nombres.isEmpty()) {
        Utils.getOut().println("Lista vacía");
        return;
    }


    
    Utils.getOut().println(Utils.VERDE + "\nFOR clásico:" + Utils.RESET);
    for (int i = 0; i < nombres.size(); i++) { // 
        Utils.getOut().println(nombres.get(i));
    }


    
    Utils.getOut().println(Utils.AMARILLO + "\nFOR-EACH:" + Utils.RESET);
    for (String n : nombres) {
        Utils.getOut().println(n);
    }


    
    Utils.getOut().println(Utils.AZUL + "\nITERATOR:" + Utils.RESET);


    java.util.Iterator<String> it = nombres.iterator();


    while (it.hasNext()) {
        Utils.getOut().println(it.next());
    }
}


public static void Ejercicio5() throws IOException {

    Utils.getOut().println("Ingrese una frase:");
    String frase = Utils.getLector().readLine();

    String[] palabrasArray = frase.split(" ");
    ArrayList<String> palabras = new ArrayList<>();

    for (String p : palabrasArray) {
        palabras.add(p);
    }

    
    Utils.getOut().println("Cantidad de palabras: " + palabras.size());

    
    String masLarga = "";

    for (String p : palabras) {
        if (p.length() > masLarga.length()) {
            masLarga = p;
        }
    }

    Utils.getOut().println("Palabra más larga: " + masLarga);

   
    Utils.getOut().println("Ingrese una letra:");
    String letra = Utils.getLector().readLine().toLowerCase();

    int contador = 0;

    for (String p : palabras) {
        if (p.toLowerCase().contains(letra)) {
            contador++;
        }
    }

    Utils.getOut().println("Palabras que contienen '" + letra + "': " + contador);
}
public static void Ejercicio6() {

    if (nombres.isEmpty()) {
        Utils.getOut().println("Primero cargá la lista (Ejercicio 1)");
        return;
    }

    ArrayList<String> normalizada = new ArrayList<>();

    for (String n : nombres) {

        String aux = n.toLowerCase();  
        aux = aux.trim();               

       
        aux = aux.replace("a", "*")
                 .replace("e", "*")
                 .replace("i", "*")
                 .replace("o", "*")
                 .replace("u", "*");

        normalizada.add(aux);
    }

    Utils.getOut().println("Lista normalizada:");
    Utils.getOut().println(normalizada);
}

public static void Ejercicio7() throws IOException {

    diccionario.clear();

    Utils.getOut().println("Ingrese 5 palabras (español → inglés):");

    for (int i = 0; i < 5; i++) {

        Utils.getOut().print("Español: ");
        String esp = Utils.getLector().readLine();

        Utils.getOut().print("Inglés: ");
        String ing = Utils.getLector().readLine();

        diccionario.put(esp, ing);
    }

  
    Utils.getOut().println("\nDiccionario completo:");
    for (var entry : diccionario.entrySet()) {
        Utils.getOut().println(entry.getKey() + " → " + entry.getValue());
    }

   
    Utils.getOut().println("\nClaves:");
    Utils.getOut().println(diccionario.keySet());


    Utils.getOut().println("\nValores:");
    Utils.getOut().println(diccionario.values());
}

public static void Ejercicio8() throws IOException {

    if (diccionario.isEmpty()) {
        Utils.getOut().println("Primero cargá el diccionario (Ejercicio 7)");
        return;
    }

    Utils.getOut().println("Ingrese palabra en español:");
    String palabra = Utils.getLector().readLine();

    if (diccionario.containsKey(palabra)) {
        String traduccion = diccionario.get(palabra);
        Utils.getOut().println("Traducción: " + traduccion);
    } else {
        Utils.getOut().println(Utils.ROJO + "No existe en el diccionario" + Utils.RESET);
    }
}
public static void Ejercicio9() throws IOException {

    if (diccionario.isEmpty()) {
        Utils.getOut().println("Primero cargá el diccionario (Ejercicio 7)");
        return;
    }

    Utils.getOut().println("Ingrese una frase:");
    String frase = Utils.getLector().readLine().toLowerCase();

    String[] palabras = frase.split(" ");

    StringBuilder resultado = new StringBuilder();

    for (String p : palabras) {

        if (diccionario.containsKey(p)) {
            resultado.append(diccionario.get(p)).append(" ");
        } else {
            
            resultado.append("[???] ");
        }
    }

    Utils.getOut().println("Traducción:");
    Utils.getOut().println(resultado.toString().trim());
}


public static void Ejercicio10() throws IOException {

    Utils.getOut().println("Ingrese una frase:");
    String frase = Utils.getLector().readLine().toLowerCase();

    String[] palabras = frase.split(" ");

    java.util.HashMap<String, Integer> contador = new java.util.HashMap<>();

    for (String p : palabras) {

        if (contador.containsKey(p)) {
            contador.put(p, contador.get(p) + 1);
        } else {
            contador.put(p, 1);
        }
    }

    Utils.getOut().println("Frecuencia de palabras:");

    for (var entry : contador.entrySet()) {
        Utils.getOut().println(entry.getKey() + ": " + entry.getValue());
    }
}

public static void Ejercicio11() throws IOException {

    Utils.getOut().println("Ingrese números separados por espacio:");
    String linea = Utils.getLector().readLine();

    String[] partes = linea.split(" ");

    java.util.ArrayList<Integer> lista = new java.util.ArrayList<>();

    for (String p : partes) {
        lista.add(Integer.parseInt(p));
    }

    java.util.HashSet<Integer> sinDuplicados = new java.util.HashSet<>(lista);

    Utils.getOut().println("Lista original:");
    Utils.getOut().println(lista);

    Utils.getOut().println("Lista sin duplicados:");
    Utils.getOut().println(sinDuplicados);
}
public static void Ejercicio12() throws IOException {

    int op;

    do {
        Utils.getOut().println("\n--- SISTEMA DE ALUMNOS ---");
        Utils.getOut().println("1 - Agregar alumno");
        Utils.getOut().println("2 - Mostrar todos");
        Utils.getOut().println("3 - Buscar alumno");
        Utils.getOut().println("4 - Modificar nota");
        Utils.getOut().println("5 - Mostrar promedio");
        Utils.getOut().println("0 - Volver");

        op = Integer.parseInt(Utils.getLector().readLine());

        switch (op) {

        
            case 1:
                Utils.getOut().println("Nombre:");
                String nombre = Utils.getLector().readLine();

                Utils.getOut().println("Nota:");
                int nota = Integer.parseInt(Utils.getLector().readLine());

                alumnos.add(nombre);
                notas.put(nombre, nota);

                Utils.getOut().println("Alumno agregado");
                break;

            
            case 2:
                if (alumnos.isEmpty()) {
                    Utils.getOut().println("No hay alumnos");
                    break;
                }

                for (String a : alumnos) {

                    Integer n = notas.get(a); 
                    if (n == null) {
                        Utils.getOut().println(Utils.ROJO + a + " sin nota" + Utils.RESET);
                        continue;
                    }

                    if (n >= 6) {
                        Utils.getOut().println(Utils.VERDE + a + " - " + n + " (Aprobado)" + Utils.RESET);
                    } else {
                        Utils.getOut().println(Utils.ROJO + a + " - " + n + " (Desaprobado)" + Utils.RESET);
                    }
                }
                break;

      
            case 3:
                Utils.getOut().println("Nombre a buscar:");
                String buscar = Utils.getLector().readLine();

                if (notas.containsKey(buscar)) {
                    Utils.getOut().println("Nota: " + notas.get(buscar));
                } else {
                    Utils.getOut().println(Utils.ROJO + "No existe el alumno" + Utils.RESET);
                }
                break;

          
            case 4:
                Utils.getOut().println("Alumno:");
                String mod = Utils.getLector().readLine();

                if (notas.containsKey(mod)) {
                    Utils.getOut().println("Nueva nota:");
                    int nueva = Integer.parseInt(Utils.getLector().readLine());

                    notas.put(mod, nueva);
                    Utils.getOut().println("Nota actualizada");
                } else {
                    Utils.getOut().println(Utils.ROJO + "No existe el alumno" + Utils.RESET);
                }
                break;

            
            case 5:
                if (notas.isEmpty()) {
                    Utils.getOut().println("Sin datos");
                    break;
                }

                double suma = 0;

                for (int n : notas.values()) {
                    suma += n;
                }

                double promedio = suma / notas.size();

                Utils.getOut().println(Utils.AMARILLO + "Promedio: " + promedio + Utils.RESET);
                break;

            case 0:
                Utils.getOut().println("Volviendo...");
                break;

            default:
                Utils.getOut().println("Opción inválida");
        }

    } while (op != 0);
}
}