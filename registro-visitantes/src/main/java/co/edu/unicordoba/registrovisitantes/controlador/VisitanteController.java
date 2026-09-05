package co.edu.unicordoba.registrovisitantes.controlador;

import co.edu.unicordoba.registrovisitantes.modelo.Visitante;
import co.edu.unicordoba.registrovisitantes.servicio.VisitanteService;
import co.edu.unicordoba.registrovisitantes.util.TextoUtil;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/visitantes")
public class VisitanteController {

    private final VisitanteService servicio;

   
    public VisitanteController(VisitanteService servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    public Visitante registrar(@RequestBody Map<String, Object> body) {
        String nombre = (String) body.get("nombre");
        int edad = Integer.parseInt(body.get("edad").toString());
        return servicio.registrar(nombre, edad);
    }

    @GetMapping
    public List<Visitante> listar() {
        return servicio.listar();
    }

    @GetMapping("/conteos")
    public Map<String, Object> conteos() {
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("registradosEnElServicio", servicio.contarRegistrados());
        respuesta.put("creadosEnLaClase", servicio.contarCreadosEnLaClase());
        respuesta.put("edadMinima", Visitante.EDAD_MINIMA);
        return respuesta;
    }

    @GetMapping("/normalizar")
    public Map<String, String> normalizar(@RequestParam String texto) {
        return Map.of("normalizado", TextoUtil.normalizarNombre(texto));
    }

    
    @PostMapping("/fantasma")
    public Map<String, Object> fantasma() {
        new Visitante("objeto fantasma", 30); // se crea... y se pierde (nadie lo referencia)

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("registradosEnElServicio", servicio.contarRegistrados());
        respuesta.put("creadosEnLaClase", Visitante.getTotalCreados());
        return respuesta;
    }
}
