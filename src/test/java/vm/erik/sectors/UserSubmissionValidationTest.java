package vm.erik.sectors;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import vm.erik.sectors.enums.RoleName;
import vm.erik.sectors.model.*;
import vm.erik.sectors.repository.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.sql.init.mode=never"
})
@Transactional
public class UserSubmissionValidationTest {

    @Autowired
    private Validator validator;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SectorRepository sectorRepository;

    @Autowired
    private UserSubmissionRepository userSubmissionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;
    private Sector testSector;

    @BeforeEach
    void setUp() {
        userSubmissionRepository.deleteAll();
        userRepository.deleteAll();
        sectorRepository.deleteAll();
        roleRepository.deleteAll();
        personRepository.deleteAll();

        Role userRole = Role.builder()
                .roleName(RoleName.USER)
                .description("User role")
                .build();
        userRole.setCreatedAt(LocalDateTime.now());
        userRole.setUpdatedAt(LocalDateTime.now());
        roleRepository.save(userRole);

        Person person = Person.builder()
                .firstName("Test")
                .lastName("User")
                .build();
        person.setCreatedAt(LocalDateTime.now());
        person.setUpdatedAt(LocalDateTime.now());
        person = personRepository.save(person);

        testUser = User.builder()
                .username("testuser")
                .email("testuser@example.com")
                .password(passwordEncoder.encode("testpass"))
                .person(person)
                .roles(Collections.singleton(userRole))
                .isActive(true)
                .isLocked(false)
                .build();
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setUpdatedAt(LocalDateTime.now());
        testUser = userRepository.save(testUser);

        testSector = Sector.builder()
                .name("Manufacturing")
                .level(0)
                .isActive(true)
                .sortOrder(1)
                .build();
        testSector.setCreatedAt(LocalDateTime.now());
        testSector.setUpdatedAt(LocalDateTime.now());
        testSector = sectorRepository.save(testSector);
    }

    @Test
    @DisplayName("Should fail validation when name is blank")
    void testValidationFailsWithBlankName() {
        UserSubmission submission = UserSubmission.builder()
                .name("")  // Blank name
                .agreeToTerms(true)
                .selectedSectors(new java.util.HashSet<>(Set.of(testSector)))
                .user(testUser)
                .build();

        Set<ConstraintViolation<UserSubmission>> violations = validator.validate(submission);

        assertFalse(violations.isEmpty(), "Should have validation violations");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")),
                "Should have name validation violation");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Name is required")),
                "Should have correct error message for name");
    }

    @Test
    @DisplayName("Should fail validation when name is null")
    void testValidationFailsWithNullName() {
        UserSubmission submission = UserSubmission.builder()
                .name(null)  // Null name
                .agreeToTerms(true)
                .selectedSectors(new java.util.HashSet<>(Set.of(testSector)))
                .user(testUser)
                .build();

        Set<ConstraintViolation<UserSubmission>> violations = validator.validate(submission);

        assertFalse(violations.isEmpty(), "Should have validation violations");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")),
                "Should have name validation violation");
    }

    @Test
    @DisplayName("Should allow false terms agreement at validation level (business logic handles this)")
    void testValidationAllowsFalseTermsAgreement() {
        UserSubmission submission = UserSubmission.builder()
                .name("Test User")
                .agreeToTerms(false)  // Terms not agreed - should be valid at entity level
                .selectedSectors(new java.util.HashSet<>(Set.of(testSector)))
                .user(testUser)
                .build();

        Set<ConstraintViolation<UserSubmission>> violations = validator.validate(submission);

        assertTrue(violations.isEmpty(), "Entity validation should pass with false terms - business logic handles this requirement");
    }

    @Test
    @DisplayName("Should fail validation when terms are null")
    void testValidationFailsWithNullTermsAgreement() {
        UserSubmission submission = UserSubmission.builder()
                .name("Test User")
                .agreeToTerms(null)  // Terms null
                .selectedSectors(new java.util.HashSet<>(Set.of(testSector)))
                .user(testUser)
                .build();

        Set<ConstraintViolation<UserSubmission>> violations = validator.validate(submission);

        assertFalse(violations.isEmpty(), "Should have validation violations");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("agreeToTerms")),
                "Should have agreeToTerms validation violation");
    }

    @Test
    @DisplayName("Should fail validation when no sectors are selected")
    void testValidationFailsWithoutSectors() {
        UserSubmission submission = UserSubmission.builder()
                .name("Test User")
                .agreeToTerms(true)
                .selectedSectors(new java.util.HashSet<>())  // Empty sectors
                .user(testUser)
                .build();

        Set<ConstraintViolation<UserSubmission>> violations = validator.validate(submission);

        assertFalse(violations.isEmpty(), "Should have validation violations");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("selectedSectors")),
                "Should have selectedSectors validation violation");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("select at least one sector")),
                "Should have correct error message for sectors");
    }

    @Test
    @DisplayName("Should fail validation when sectors are null")
    void testValidationFailsWithNullSectors() {
        UserSubmission submission = UserSubmission.builder()
                .name("Test User")
                .agreeToTerms(true)
                .selectedSectors(null)
                .user(testUser)
                .build();

        Set<ConstraintViolation<UserSubmission>> violations = validator.validate(submission);

        assertFalse(violations.isEmpty(), "Should have validation violations");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("selectedSectors")),
                "Should have selectedSectors validation violation");
    }

    @Test
    @DisplayName("Should pass validation when all conditions are met")
    void testValidationPassesWithValidSubmission() {
        UserSubmission submission = UserSubmission.builder()
                .name("Test User")
                .agreeToTerms(true)
                .selectedSectors(new java.util.HashSet<>(Set.of(testSector)))
                .user(testUser)
                .isActive(true)
                .build();

        Set<ConstraintViolation<UserSubmission>> violations = validator.validate(submission);

        assertTrue(violations.isEmpty(), "Should have no validation violations: " + violations);
    }

    @Test
    @DisplayName("Should successfully save submission when valid")
    void testSuccessfulSubmissionSave() {
        UserSubmission submission = UserSubmission.builder()
                .name("Test User Submission")
                .agreeToTerms(true)
                .selectedSectors(new java.util.HashSet<>(Set.of(testSector)))
                .user(testUser)
                .isActive(true)
                .build();
        submission.setCreatedAt(LocalDateTime.now());
        submission.setUpdatedAt(LocalDateTime.now());

        UserSubmission saved = userSubmissionRepository.save(submission);

        assertNotNull(saved.getId(), "Saved submission should have an ID");
        assertEquals("Test User Submission", saved.getName());
        assertTrue(saved.getAgreeToTerms());
        assertTrue(saved.getIsActive());
        assertEquals(1, saved.getSelectedSectors().size());
        assertTrue(saved.getSelectedSectors().contains(testSector));
        assertEquals(testUser.getId(), saved.getUser().getId());
    }

    @Test
    @DisplayName("Should find submission by user")
    void testFindSubmissionByUser() {
        // Create and save a submission
        UserSubmission submission = UserSubmission.builder()
                .name("Find Me Submission")
                .agreeToTerms(true)
                .selectedSectors(new java.util.HashSet<>(Set.of(testSector)))
                .user(testUser)
                .isActive(true)
                .build();
        submission.setCreatedAt(LocalDateTime.now());
        submission.setUpdatedAt(LocalDateTime.now());
        userSubmissionRepository.save(submission);

        var submissions = userSubmissionRepository.findByUserOrderByCreatedAtDesc(testUser);

        assertFalse(submissions.isEmpty(), "Should find at least one submission");
        assertEquals(1, submissions.size());
        assertEquals("Find Me Submission", submissions.get(0).getName());
        assertEquals(testUser.getId(), submissions.get(0).getUser().getId());
    }

    @Test
    @DisplayName("Should update submission sectors correctly")
    void testUpdateSubmissionSectors() {
        Sector newSector = Sector.builder()
                .name("IT Services")
                .level(0)
                .isActive(true)
                .sortOrder(2)
                .build();
        newSector.setCreatedAt(LocalDateTime.now());
        newSector.setUpdatedAt(LocalDateTime.now());
        newSector = sectorRepository.save(newSector);

        UserSubmission submission = UserSubmission.builder()
                .name("Update Me Submission")
                .agreeToTerms(true)
                .selectedSectors(new java.util.HashSet<>(Set.of(testSector)))
                .user(testUser)
                .isActive(true)
                .build();
        submission.setCreatedAt(LocalDateTime.now());
        submission.setUpdatedAt(LocalDateTime.now());
        submission = userSubmissionRepository.save(submission);

        submission.setSelectedSectors(new java.util.HashSet<>(Set.of(newSector)));
        submission.setUpdatedAt(LocalDateTime.now());
        submission = userSubmissionRepository.save(submission);

        UserSubmission updated = userSubmissionRepository.findById(submission.getId()).orElse(null);
        assertNotNull(updated);
        assertEquals(1, updated.getSelectedSectors().size());
        assertTrue(updated.getSelectedSectors().contains(newSector));
        assertFalse(updated.getSelectedSectors().contains(testSector));
    }

    @Test
    @DisplayName("Should validate submission with multiple sectors")
    void testValidationWithMultipleSectors() {
        Sector sector2 = Sector.builder()
                .name("IT Services")
                .level(0)
                .isActive(true)
                .sortOrder(2)
                .build();
        sector2.setCreatedAt(LocalDateTime.now());
        sector2.setUpdatedAt(LocalDateTime.now());
        sector2 = sectorRepository.save(sector2);

        Sector sector3 = Sector.builder()
                .name("Food & Beverage")
                .level(1)
                .isActive(true)
                .sortOrder(1)
                .parent(testSector)
                .build();
        sector3.setCreatedAt(LocalDateTime.now());
        sector3.setUpdatedAt(LocalDateTime.now());
        sector3 = sectorRepository.save(sector3);

        UserSubmission submission = UserSubmission.builder()
                .name("Multi Sector Submission")
                .agreeToTerms(true)
                .selectedSectors(new java.util.HashSet<>(Set.of(testSector, sector2, sector3)))
                .user(testUser)
                .isActive(true)
                .build();

        Set<ConstraintViolation<UserSubmission>> violations = validator.validate(submission);

        assertTrue(violations.isEmpty(), "Should have no validation violations with multiple sectors");

        submission.setCreatedAt(LocalDateTime.now());
        submission.setUpdatedAt(LocalDateTime.now());
        UserSubmission saved = userSubmissionRepository.save(submission);

        assertEquals(3, saved.getSelectedSectors().size());
        assertTrue(saved.getSelectedSectors().contains(testSector));
        assertTrue(saved.getSelectedSectors().contains(sector2));
        assertTrue(saved.getSelectedSectors().contains(sector3));
    }
}