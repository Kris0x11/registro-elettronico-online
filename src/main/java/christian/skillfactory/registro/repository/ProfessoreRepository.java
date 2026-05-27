package christian.skillfactory.registro.repository;
import christian.skillfactory.registro.model.ProfessoreEntity;
import christian.skillfactory.registro.model.StudenteEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessoreRepository extends JpaRepository<ProfessoreEntity,Long>{
	
	Optional<ProfessoreEntity> findByAccountId(Long id);

}
