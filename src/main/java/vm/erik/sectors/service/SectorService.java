package vm.erik.sectors.service;

import vm.erik.sectors.model.Sector;

import java.util.List;
import java.util.UUID;

public interface SectorService {


    List<Sector> getAllActiveSectors();

    List<Sector> getSectorsByIds(List<UUID> sectorIds);

    List<Sector> getRootSectors();

    Sector getSectorById(UUID sectorId);
}
