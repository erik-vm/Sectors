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
}
