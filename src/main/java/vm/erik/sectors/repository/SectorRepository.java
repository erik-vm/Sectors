package vm.erik.sectors.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vm.erik.sectors.model.Sector;

import java.util.List;

@Repository
public interface SectorRepository extends JpaRepository<Sector, Long> {

    List<Sector> findByParentIsNullOrderByName();

    List<Sector> findByIsActiveTrueAndLevelLessThanEqualOrderByLevelAscNameAsc(int maxLevel);

    List<Sector> findByIsActiveTrueOrderByLevelAscNameAsc();

    @Query("SELECT COUNT(us) FROM UserSubmission us JOIN us.selectedSectors s WHERE s.id = :sectorId")
    Long countUserSubmissionsBySectorId(@Param("sectorId") Long sectorId);

    List<Sector> findByParentIsNullAndIsActiveTrueOrderByName();

}
