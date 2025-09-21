package vm.erik.sectors.service;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import vm.erik.sectors.model.Sector;

import java.util.List;

public interface SectorService {

    List<Sector> getAllSectorsHierarchy();

    Sector getSectorById(Long id);

    List<Sector> getActiveSectorsByMaxLevel(int maxLevel);

    Sector saveSector(Sector sector);

    void deactivateSector(Long sectorId);

    void activateSector(Long sectorId);

    Long getSectorUsageCount(Long sectorId);

    List<Sector> getActiveSectorsForUserSelection();

    List<Sector> getActiveSectorsHierarchy();

    /**
     * Handles sector creation with validation - returns view name to render
     */
    String handleSectorCreation(Sector sector, BindingResult result, Long parentId, Model model);

    /**
     * Handles sector update with validation - returns view name to render
     */
    String handleSectorUpdate(Long id, Sector sector, BindingResult result, Long parentId, Model model);
}
