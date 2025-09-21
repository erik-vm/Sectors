package vm.erik.sectors.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vm.erik.sectors.model.Sector;
import vm.erik.sectors.repository.SectorRepository;
import vm.erik.sectors.service.SectorService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SectorServiceImpl implements SectorService {

    private final SectorRepository sectorRepository;

    @Override
    public List<Sector> getAllSectorsHierarchy() {
        return sectorRepository.findByParentIsNullOrderByName();
    }

    @Override
    public Sector getSectorById(Long id) {
        return sectorRepository.findById(id).orElse(null);
    }

    @Override
    public List<Sector> getActiveSectorsByMaxLevel(int maxLevel) {
        return sectorRepository.findByIsActiveTrueAndLevelLessThanEqualOrderByLevelAscNameAsc(maxLevel);
    }

    @Override
    public Sector saveSector(Sector sector) {
        return sectorRepository.save(sector);
    }

    @Override
    public void deactivateSector(Long sectorId) {
        Sector sector = getSectorById(sectorId);
        if (sector != null) {
            sector.setIsActive(false);
            sectorRepository.save(sector);

            // Recursively deactivate children
            for (Sector child : sector.getChildren()) {
                deactivateSector(child.getId());
            }
        }
    }

    @Override
    public void activateSector(Long sectorId) {
        Sector sector = getSectorById(sectorId);
        if (sector != null) {
            sector.setIsActive(true);
            sectorRepository.save(sector);

            // Activate parent if it exists (so hierarchy remains consistent)
            if (sector.getParent() != null && !sector.getParent().getIsActive()) {
                activateSector(sector.getParent().getId());
            }
        }
    }

    @Override
    public Long getSectorUsageCount(Long sectorId) {
        return sectorRepository.countUserSubmissionsBySectorId(sectorId);
    }

    @Override
    public List<Sector> getActiveSectorsForUserSelection() {
        return sectorRepository.findByIsActiveTrueOrderByLevelAscNameAsc();
    }

    @Override
    public List<Sector> getActiveSectorsHierarchy() {
        return sectorRepository.findByParentIsNullAndIsActiveTrueOrderByName();
    }
}
