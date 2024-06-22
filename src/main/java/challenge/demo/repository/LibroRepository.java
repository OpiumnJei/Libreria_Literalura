
package challenge.demo.repository;

import challenge.demo.model.Libro;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jerlinson G
 */
@Repository
public interface LibroRepository extends JpaRepository<Libro,Long>{
     Optional<Libro> findByTitulo(String titulo);
     List<Libro> findLibroByIdiomas(String idioma);
}
