package challenge.demo.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Jerlinson G
 */
@Entity
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String nombre;
    private String fechaDeNacimiento;
    private String fechaDeFallecimiento;
    
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Libro> libros = new ArrayList<>();

    public Autor() {
    }

    public Autor(DatosAutor d) {
        this.nombre = d.nombre();
        this.fechaDeNacimiento = d.fechaDeNacimiento();
        this.fechaDeFallecimiento = d.fechaDeFallecimiento();
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNacimiento() {
        return fechaDeNacimiento;
    }

    public void setNacimiento(String nacimiento) {
        this.fechaDeNacimiento = nacimiento;
    }

    public String getFallecimiento() {
        return fechaDeFallecimiento;
    }

    public void setFallecimiento(String fallecimiento) {
        this.fechaDeFallecimiento = fallecimiento;
    }
       public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }
      public void addLibro(Libro libro) {
        libros.add(libro);
        libro.setAutor(this);
    }
    

       @Override
    public String toString() {
          List<String> titulosLibros = libros.stream()
                                            .map(Libro::getTitulo)
                                            .collect(Collectors.toList());
                    
        return  
                "nombre: " + nombre 
                + "\nnacimiento: " + fechaDeNacimiento 
                + "\nfallecimiento: " + fechaDeFallecimiento
                + "\nLibros: " + titulosLibros +"\n";
          
    }
}
