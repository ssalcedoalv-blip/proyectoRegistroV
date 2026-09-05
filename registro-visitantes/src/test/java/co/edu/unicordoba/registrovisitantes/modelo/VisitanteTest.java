package co.edu.unicordoba.registrovisitantes.modelo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VisitanteTest {

    @Test
    void elContadorStaticEsCompartido() {
        int antes = Visitante.getTotalCreados();
        new Visitante("ana", 25);
        new Visitante("luis", 40);
        assertEquals(antes + 2, Visitante.getTotalCreados());
    }

    @Test
    void instanceofYCastSeguro() {
        Object o = new Visitante("marta", 16);
        assertTrue(o instanceof Visitante);

        // Patrón de tipo: si "o" es Visitante, se castea automáticamente a "v"
        if (o instanceof Visitante v) {
            assertFalse(v.esMayorDeEdad());
        }

        Object texto = "Hola";
        assertFalse(texto instanceof Visitante);
    }
}
