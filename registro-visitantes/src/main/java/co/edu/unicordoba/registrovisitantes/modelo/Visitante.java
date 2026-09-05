package co.edu.unicordoba.registrovisitantes.modelo;

import co.edu.unicordoba.registrovisitantes.util.TextoUtil;

public class Visitante {

 
    private final int id;
    private final String nombre;
    private final int edad;

    private static int totalCreados;

    public static final int EDAD_MINIMA = 18;

    static {
        totalCreados = 0;
    }

    public Visitante(String nombre, int edad) {
        totalCreados++;                  // SIN "this": pertenece a la CLASE
        this.id = totalCreados;          // CON "this": pertenece a ESTE objeto
        this.nombre = TextoUtil.normalizarNombre(nombre);
        this.edad = edad;
    }

    public boolean esMayorDeEdad() {
        return this.edad >= EDAD_MINIMA;
    }

    public static int getTotalCreados() {
        return totalCreados;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getEdad() {
        return edad;
    }
}
