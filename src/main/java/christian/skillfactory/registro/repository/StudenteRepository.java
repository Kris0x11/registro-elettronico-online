package christian.skillfactory.registro.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import christian.skillfactory.registro.model.RegistroEntity;
import christian.skillfactory.registro.model.StudenteEntity;

public interface StudenteRepository extends JpaRepository<StudenteEntity,Long>
{
	
	Page<StudenteEntity> findByRegstudenti_NomeClasse(String id, Pageable pageable);
	Optional<StudenteEntity> findByAccountId(Long id);
	List<StudenteEntity> findByRegstudenti(RegistroEntity registro);
}
