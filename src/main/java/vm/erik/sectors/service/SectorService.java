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


    List<Sector> getActiveSectorsHierarchy();

    String handleSectorCreation(Sector sector, BindingResult result, Long parentId, Model model);


    String handleSectorUpdate(Long id, Sector sector, BindingResult result, Long parentId, Model model);
}
