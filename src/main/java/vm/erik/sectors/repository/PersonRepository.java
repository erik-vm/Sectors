package vm.erik.sectors.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vm.erik.sectors.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

}
