package co.edu.unicordoba.registrovisitantes.servicio;

import co.edu.unicordoba.registrovisitantes.modelo.Visitante;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VisitanteService {


    private final List<Visitante> registrados = new ArrayList<>();

    public Visitante registrar(String nombre, int edad) {
        Visitante v = new Visitante(nombre, edad); 
        registrados.add(v);
        return v;
    }

    public List<Visitante> listar() {
        return List.copyOf(registrados); 
    }

    public int contarRegistrados() {
        return registrados.size();
    }

    public int contarCreadosEnLaClase() {
        return Visitante.getTotalCreados();
    }
}
