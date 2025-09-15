package vm.erik.sectors.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import vm.erik.sectors.model.Person;
import vm.erik.sectors.repository.PersonRepository;

@Repository
public class PersonRepositoryImpl implements PersonRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Person savePerson(Person person) {
        if (person.getId() == null) {
            em.persist(person);
        }
        else {em.merge(person);}
        return person;
    }
}
