package challenge.demo.repository;

import challenge.demo.model.Autor;
// import challenge.demo.model.Libro;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Jerlinson G
 */
public interface AutorRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findByNombre(String nombre);
    
    @Query("SELECT a FROM Autor a WHERE SUBSTRING(a.fechaDeNacimiento, 1, 4) <= :anio AND SUBSTRING(a.fechaDeFallecimiento, 1, 4) >= :anio")
    List<Autor> findAutoresVivosEnAnio(@Param("anio") String anio);
    
    
}
