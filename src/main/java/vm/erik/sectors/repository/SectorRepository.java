package vm.erik.sectors.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vm.erik.sectors.model.Sector;

import java.util.List;

@Repository
public interface SectorRepository extends JpaRepository<Sector, Long> {

    List<Sector> findByParentIsNullOrderByName();

}
