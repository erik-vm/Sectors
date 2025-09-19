package vm.erik.sectors.config;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import vm.erik.sectors.enums.RoleName;
import vm.erik.sectors.model.Person;
import vm.erik.sectors.model.Role;
import vm.erik.sectors.model.User;
import vm.erik.sectors.repository.PersonRepository;
import vm.erik.sectors.repository.RoleRepository;
import vm.erik.sectors.repository.UserRepository;

import java.util.Set;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository, PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
        initializeAdminUser();
        initializeTestUser();
    }

    @Transactional
    protected void initializeRoles() {
        log.info("Initializing roles...");


        if (!roleRepository.existsByRoleName(RoleName.USER)) {
            Role userRole = new Role(RoleName.USER, "Standard user with basic access permissions");
            roleRepository.save(userRole);
            log.info("Created USER role");
        }


        if (!roleRepository.existsByRoleName(RoleName.ADMIN)) {
            Role adminRole = new Role(RoleName.ADMIN, "Administrator with elevated permissions");
            roleRepository.save(adminRole);
            log.info("Created ADMIN role");
        }

        log.info("Roles initialization completed. Total roles: {}", roleRepository.count());
    }

    @Transactional
    protected void initializeAdminUser() {
        log.info("Initializing admin user...");


        if (!userRepository.existsUserByUsername("admin")) {

            Role adminRole = roleRepository.findByRoleName(RoleName.ADMIN);
            if (adminRole == null) {
                log.error("ADMIN role not found! Make sure roles are initialized first.");
                return;
            }


            Person adminPerson = Person.builder()
                    .firstName("Erik")
                    .lastName("Vainumäe")
                    .build();
            adminPerson = personRepository.save(adminPerson);
            log.info("Created admin person: {} {}", adminPerson.getFirstName(), adminPerson.getLastName());


            User adminUser = User.builder()
                    .username("admin")
                    .email("admin@erik.vm")
                    .password(passwordEncoder.encode("admin"))
                    .person(adminPerson)
                    .roles(Set.of(adminRole))
                    .isActive(true)
                    .isLocked(false)
                    .isExpired(false)
                    .credentialsExpired(false)
                    .build();

            adminUser = userRepository.save(adminUser);
            log.info("Created admin user: {} with ID: {}", adminUser.getUsername(), adminUser.getId());
        } else {
            log.info("Admin user already exists, skipping creation");
        }
    }

    @Transactional
    protected void initializeTestUser() {
        log.info("Initializing test user...");


        if (!userRepository.existsUserByUsername("testuser")) {

            Role userRole = roleRepository.findByRoleName(RoleName.USER);
            if (userRole == null) {
                log.error("USER role not found! Make sure roles are initialized first.");
                return;
            }

            Person userPerson = Person.builder()
                    .firstName("Test")
                    .lastName("User")
                    .build();
            userPerson = personRepository.save(userPerson);
            log.info("Created regular user person: {} {}", userPerson.getFirstName(), userPerson.getLastName());


            User testUser = User.builder()
                    .username("testuser")
                    .email("testuser@erik.vm")
                    .password(passwordEncoder.encode("testuser"))
                    .person(userPerson)
                    .roles(Set.of(userRole))
                    .isActive(true)
                    .isLocked(false)
                    .isExpired(false)
                    .credentialsExpired(false)
                    .build();

            testUser = userRepository.save(testUser);
            log.info("Created regular user: {} with ID: {}", testUser.getUsername(), testUser.getId());
        } else {
            log.info("Regular user already exists, skipping creation");
        }
    }
}
