
package challenge.demo.model;

import jakarta.persistence.*;
import java.util.List;


/**
 *
 * @author Jerlinson G
 */
@Entity
@Table(name = "libros")
public class Libro {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String titulo;
    
//    private String  autor;
     
    private String  idiomas;

    private Double numeroDeDescargas;
    
    
//    @JoinColumn(name = "autor_id", nullable = false)
    @ManyToOne
    private Autor autor;
    
    public Libro() {
    }
   

    public Libro(DatosLibros d, Autor autor) {
        this.titulo = d.titulo();
       this.autor = autor;
//        this.autor = d.autor().isEmpty() ? "Desconocido" : d.autor().get(0).nombre();
        this.idiomas = d.idiomas().isEmpty() ? "Idioma desconocido" : d.idiomas().get(0);
        this.numeroDeDescargas = d.numeroDeDescargas();
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }


    public Double getDescargas() {
        return numeroDeDescargas;
    }

    public void setDescargas(Double descargas) {
        this.numeroDeDescargas = descargas;
    }
    
   

    public String getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(String idiomas) {
        this.idiomas = idiomas;
    }
    
    @Override
    public String toString() {
        
        return   "       LIBRO: \n"+
                 "\nTitulo: " + titulo + "\n" 
                +"Autor: " + autor.getNombre() + "\n"
                +"Idioma: " + idiomas + "\n"
                +"Descargas: " + numeroDeDescargas + "\n";
              
    }

}
