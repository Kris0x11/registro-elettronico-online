package christian.skillfactory.registro.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import christian.skillfactory.registro.model.ProfessoreEntity;
import christian.skillfactory.registro.model.RegistroEntity;
import christian.skillfactory.registro.model.StudenteEntity;

public interface RegistroRepository  extends JpaRepository<RegistroEntity,String>{

	List<RegistroEntity> findByListaProf(ProfessoreEntity professore);
	
}
