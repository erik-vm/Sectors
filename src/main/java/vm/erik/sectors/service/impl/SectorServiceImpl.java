package vm.erik.sectors.service.impl;

import org.springframework.stereotype.Service;
import vm.erik.sectors.model.Sector;
import vm.erik.sectors.service.SectorService;

import java.util.List;
import java.util.UUID;

@Service
public class SectorServiceImpl implements SectorService {
    @Override
    public List<Sector> getAllActiveSectors() {
        return List.of();
    }

    @Override
    public List<Sector> getSectorsByIds(List<UUID> sectorIds) {
        return List.of();
    }

    @Override
    public List<Sector> getRootSectors() {
        return List.of();
    }

    @Override
    public Sector getSectorById(UUID sectorId) {
        return null;
    }
}
