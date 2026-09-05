# registro-visitantes

Taller 01 — *Del `new` al contenedor*. Proyecto Spring Boot que compara
**atributos/métodos de instancia** con **atributos/métodos `static`**, y
los conecta con el concepto de **bean singleton** de Spring.

## 1. Requisitos

- Java 21 o superior (tú tienes Java 24: funciona sin problema).
- Maven (el proyecto trae `./mvnw`, no necesitas instalarlo aparte).

## 2. Ejecutar el proyecto

Desde la carpeta `registro-visitantes/`:

```bash
./mvnw spring-boot:run
```

En Windows, usa `mvnw.cmd spring-boot:run` (si no tienes el wrapper,
puedes correrlo también desde tu IDE: botón derecho sobre
`RegistroVisitantesApplication.java` → Run).

La API queda escuchando en `http://localhost:8080`.

## 3. Probar los endpoints (Paso 5)

```bash
# Registrar un visitante
curl -X POST "http://localhost:8080/api/visitantes?nombre=ana%20maria%20perez&edad=25"

# Registrar dos más
curl -X POST "http://localhost:8080/api/visitantes?nombre=luis%20gomez&edad=40"
curl -X POST "http://localhost:8080/api/visitantes?nombre=pedro%20diaz&edad=16"

# Ver la lista completa
curl http://localhost:8080/api/visitantes

# Ver los conteos (instancia vs static)
curl http://localhost:8080/api/visitantes/conteos

# Normalizar un texto con el método static de TextoUtil
curl "http://localhost:8080/api/visitantes/normalizar?texto=pedro%20jose%20DIAZ"
```

Con los tres visitantes de arriba (25, 40 y 16 años), `/conteos` debería
responder algo como:

```json
{
  "registradosEnElServicio": 3,
  "creadosEnLaClase": 3,
  "edadMinima": 18
}
```

## 4. El experimento del "fantasma" (Paso 6)

```bash
curl -X POST http://localhost:8080/api/visitantes/fantasma
```

Este endpoint crea un `Visitante` con `new` pero **no lo guarda** en la
lista del servicio. Compara `registradosEnElServicio` (no cambia) contra
`creadosEnLaClase` (sí sube): esa diferencia es la evidencia de que
`totalCreados` vive en la **clase** (`static`), mientras que la lista
`registrados` vive en el **objeto** `VisitanteService` (que Spring
mantiene como singleton).

## 5. Errores intencionales (Paso 7)

Ver el archivo `paso7-errores-experimentales.md`. Ahí están los dos
fragmentos de código para pegar temporalmente, capturar el error, y
luego borrar antes de subir el proyecto.

## 6. Pruebas unitarias (Paso 8, opcional)

```bash
./mvnw test
```

## 7. Guía rápida para el informe (PDF, máx. 3 páginas)

**a) Instancia vs. clase — justificación de cada decisión**

| Miembro | Tipo | Por qué |
|---|---|---|
| `Visitante.id`, `nombre`, `edad` | instancia | cada visitante tiene su propio nombre y edad |
| `Visitante.totalCreados` | static | es un conteo global, compartido por todos los objetos |
| `Visitante.EDAD_MINIMA` | static final | una regla de negocio única, no cambia por objeto |
| `Visitante.esMayorDeEdad()` | instancia | necesita `this.edad`, el dato de un objeto concreto |
| `Visitante.getTotalCreados()` | static | solo expone el contador compartido, sin usar `this` |
| `TextoUtil.normalizarNombre()` | static | resultado puro, depende solo del parámetro, como `Math.pow()` |
| `VisitanteService.registrados` | instancia (del bean) | pero el bean es singleton, así que en la práctica se comparte |

**b) Pregunta central del Paso 6**

`creadosEnLaClase` sube porque `totalCreados` es `static`: existe una
sola vez en la clase `Visitante` y se incrementa en **cada** `new`,
sin importar si el objeto se guarda o no. `registradosEnElServicio` no
sube porque cuenta elementos de la lista `registrados`, que es un
atributo de instancia del bean `VisitanteService`; si el objeto creado
nunca se agrega a esa lista (como el "fantasma"), no se refleja ahí.

**c) Los dos errores del Paso 7**

Documenta con capturas: el Error A (campo `static` con `@Autowired`)
compila y falla en ejecución (`null` silencioso); el Error B (`this` en
un método `static`) ni siquiera compila.

**d) `static` vs. bean singleton — una diferencia y una semejanza**

- *Semejanza:* ambos representan "una sola instancia/valor compartido"
  que todo el mundo ve y puede modificar.
- *Diferencia:* un atributo `static` pertenece a la **clase** cargada
  por la JVM (existe siempre, incluso sin Spring); un bean singleton es
  un **objeto normal** que el contenedor de Spring decide crear una sola
  vez y reutilizar — por eso sí se puede inyectar por constructor, y por
  eso NO tiene sentido combinarlo con `static` (de ahí el Error A).

## 8. Estructura del proyecto

```
registro-visitantes/
├── pom.xml
├── README.md
├── paso7-errores-experimentales.md
└── src/
    ├── main/java/co/edu/unicordoba/registrovisitantes/
    │   ├── RegistroVisitantesApplication.java
    │   ├── modelo/Visitante.java
    │   ├── util/TextoUtil.java
    │   ├── servicio/VisitanteService.java
    │   └── controlador/VisitanteController.java
    ├── main/resources/application.properties
    └── test/java/co/edu/unicordoba/registrovisitantes/modelo/VisitanteTest.java
```
