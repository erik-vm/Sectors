package vm.erik.sectors.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import vm.erik.sectors.model.Sector;
import vm.erik.sectors.repository.SectorRepository;
import vm.erik.sectors.service.SectorService;
import vm.erik.sectors.validation.ValidationService;

import java.util.List;

@Service
@Transactional
public class SectorServiceImpl implements SectorService {

    private final SectorRepository sectorRepository;
    private final ValidationService validationService;

    public SectorServiceImpl(SectorRepository sectorRepository, ValidationService validationService) {
        this.sectorRepository = sectorRepository;
        this.validationService = validationService;
    }

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
    public List<Sector> getActiveSectorsHierarchy() {
        return sectorRepository.findByParentIsNullAndIsActiveTrueOrderByName();
    }

    @Override
    public String handleSectorCreation(Sector sector, BindingResult result, Long parentId, Model model) {
        // Preserve parent information
        if (parentId != null) {
            Sector parent = getSectorById(parentId);
            if (parent != null) {
                sector.setParent(parent);
                sector.setLevel(parent.getLevel() + 1);
            }
        }

        if (validationService.handleValidationErrors(result, model, sector, "sector")) {
            model.addAttribute("parentSectors", getActiveSectorsByMaxLevel(1));
            return "admin/sector-form";
        }

        if (parentId != null) {
            Sector parent = getSectorById(parentId);
            if (parent != null && parent.getLevel() < 2) {
                sector.setParent(parent);
                sector.setLevel(parent.getLevel() + 1);
            } else {
                validationService.addCustomErrorToModel(model, "Invalid parent sector or maximum depth reached");
                model.addAttribute("parentSectors", getActiveSectorsByMaxLevel(1));
                return "admin/sector-form";
            }
        } else {
            sector.setLevel(0);
            sector.setParent(null);
        }

        sector.setIsActive(true);
        saveSector(sector);
        return "redirect:/admin/sectors";
    }

    @Override
    public String handleSectorUpdate(Long id, Sector sector, BindingResult result, Long parentId, Model model) {
        if (validationService.handleValidationErrors(result, model, sector, "sector")) {
            model.addAttribute("parentSectors", getActiveSectorsByMaxLevel(1));
            return "admin/sector-form";
        }

        Sector existingSector = getSectorById(id);
        if (existingSector == null) {
            validationService.addCustomErrorToModel(model, "Sector not found");
            return "redirect:/admin/sectors";
        }

        // Update basic fields
        existingSector.setName(sector.getName());
        existingSector.setDescription(sector.getDescription());
        existingSector.setSortOrder(sector.getSortOrder());

        // Handle parent changes carefully
        if (parentId != null && (existingSector.getParent() == null || !parentId.equals(existingSector.getParent().getId()))) {
            Sector newParent = getSectorById(parentId);
            if (newParent != null && newParent.getLevel() < 2) {
                existingSector.setParent(newParent);
                existingSector.setLevel(newParent.getLevel() + 1);
            }
        } else if (parentId == null && existingSector.getParent() != null) {
            existingSector.setParent(null);
            existingSector.setLevel(0);
        }

        saveSector(existingSector);
        return "redirect:/admin/sectors";
    }
}
