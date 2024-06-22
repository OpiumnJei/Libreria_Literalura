package challenge.demo.principal;

import challenge.demo.model.Autor;
import challenge.demo.model.Datos;
import challenge.demo.model.DatosAutor;
import challenge.demo.model.DatosLibros;
import challenge.demo.model.Libro;
import challenge.demo.repository.AutorRepository;
import challenge.demo.repository.LibroRepository;
import challenge.demo.service.ConsumoAPI;
import challenge.demo.service.ConversorDatos;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Jerlinson G
 */
@Component

public class Principal {

    private Scanner sc = new Scanner(System.in);
    private String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConversorDatos conversor = new ConversorDatos();
    private List<Libro> libros;
    private List<Autor> autor;

    //inyecciones
    @Autowired
    private LibroRepository libroRepo;
    @Autowired
    private AutorRepository autorRepo;

    public void mostrarMenu() {
        var opcion = 0;
        while (true) {
            System.out.println(
                    """
                   - - - - - - - - - - - - 
                  |Bienvenido a Literalura|
                   - - - - - - - - - - - - 
                
                      Elige una opcion.
                    
                1. Buscar libro.
                2. Mostrar libros almacenados.
                3. Mostrar autores almacenados.
                4. Buscar autor vivo en un determinado año.
                5. Mostrar Libros por idioma
                6. Salir.  
                    
                  """);

            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1 ->
                    buscarLibro();
                case 2 ->
                    mostrarLibrosAlamcenados();
                case 3 ->
                    mostrarAutoresAlmacenado();
                case 4 ->
                    buscarAutorPorAnio();
                case 5 ->
                    encontrarLibroPorIdioma();
                case 6 ->
                    System.exit(0);
                default ->
                    System.out.println("Opción inválida");
            }

        }
    }

    private  Datos getDatosLibro() {
        System.out.println("Ingrese el nombre del libro que desea buscar");
        var tituloLibro = sc.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "+"));
        return conversor.obtenerDatos(json, Datos.class);
    }

    private String getAutoresVivosEnAnio(String anio) {
        List<Autor> autores = autorRepo.findAutoresVivosEnAnio(anio);
        if (autores.isEmpty()) {
            return "No hay autores vivos en el año " + anio;
        } else {
            String autoresStr = autores.stream()
                    .map(Autor::toString)
                    .collect(Collectors.joining("\n"));
            return autoresStr;
        }
    }

    private String getLibroPorIdioma(String idioma) {
        List<Libro> libro = libroRepo.findLibroByIdiomas(idioma);
        if (libro.isEmpty()) {
            return "Libro no encontrado";
        } else {
            String idiomaLibro = libro.stream()
                    .map(Libro::toString)
                    .collect(Collectors.joining("\n"));
            return idiomaLibro;
        }
    }

    private  void buscarLibro() {
        Datos datos = getDatosLibro();

        if (datos.resultados().isEmpty()) {
            System.out.println("Libro no encontrados");
        } else {
            for (DatosLibros datosLibros : datos.resultados()) {
                // Verificar si el libro ya existe en la base de datos
                Optional<Libro> libroExistente = libroRepo.findByTitulo(datosLibros.titulo());

                if (libroExistente.isPresent()) {
                    System.out.println("El libro ya existe en la base de datos: " + libroExistente.get());
                    continue;
                }

                // Si el libro no existe, procedemos a verificar/guardar el autor y el libro
                Autor autor = null;
                if (!datosLibros.autor().isEmpty()) {
                    DatosAutor datosAutor = datosLibros.autor().get(0);
                    Optional<Autor> autorExistente = autorRepo.findByNombre(datosAutor.nombre());

                    if (autorExistente.isPresent()) {
                        autor = autorExistente.get();
                        System.out.println("El autor ya existe en la base de datos: " + autor);
                    } else {
                        autor = new Autor(datosAutor);
                        autorRepo.save(autor);
                    }

                    // Guardar datos del libro con el autor
                    Libro libro = new Libro(datosLibros, autor);
                    autor.addLibro(libro);
                    libroRepo.save(libro);
                    System.out.println(libro);
                    break;
                } else {
                    // En caso de que no haya autores, solo guarda el libro
                    Libro libro = new Libro(datosLibros, null);
                    libroRepo.save(libro);
                    System.out.println("Libro guardado sin autor: " + libro);
                    break;
                }
            }
        }

    }

    private  void mostrarLibrosAlamcenados() {
        libros = libroRepo.findAll();

        libros.stream()
                .forEach(System.out::println);
    }

    private  void mostrarAutoresAlmacenado() {
        autor = autorRepo.findAll();

        autor.stream()
                .forEach(System.out::println);
    }

    private  void buscarAutorPorAnio() {
        String anioAutor;
        System.out.println("Digite el anio del autor a buscar: ");
        anioAutor = sc.nextLine();

        System.out.println(getAutoresVivosEnAnio(anioAutor));
    }

    private  void encontrarLibroPorIdioma() {
        System.out.println("""
                            
                            Seleccione un idioma: 
                            
                            es - español
                            en - ingles
                            fr - frances
                           
                            """);
        String idiomaLibro = null;
        System.out.println("Ingrese el idioma del libro: ");
        idiomaLibro = sc.nextLine();

        System.out.println(getLibroPorIdioma(idiomaLibro).toLowerCase());
    }
}


/*
nota:
ya resolvi el problema de la dublicidad, ahora me falta establecer la relacion entre la clase Libro y autor, para asignar 
a la clase Autor un atributo del tipo List con los nombres de los libros escritos por el autor
 */
