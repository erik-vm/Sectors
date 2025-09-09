package vm.erik.sectors.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vm.erik.sectors.model.Person;

import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {
}
