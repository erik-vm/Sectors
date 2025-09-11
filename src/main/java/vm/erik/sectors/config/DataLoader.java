package vm.erik.sectors.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import vm.erik.sectors.enums.RoleName;
import vm.erik.sectors.model.Role;
import vm.erik.sectors.model.Sector;
import vm.erik.sectors.repository.RoleRepository;
import vm.erik.sectors.repository.SectorRepository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final SectorRepository sectorRepository;
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        loadRoles();
        loadSectors();
        log.info("Data loading completed successfully");
    }

    private void loadRoles() {
        if (roleRepository.count() == 0) {
            log.info("Loading roles...");
            
            Role userRole = Role.builder()
                    .roleName(RoleName.USER)
                    .description("Regular user role")
                    .build();
            
            Role adminRole = Role.builder()
                    .roleName(RoleName.ADMIN)
                    .description("Administrator role")
                    .build();
            
            roleRepository.saveAll(Arrays.asList(userRole, adminRole));
            log.info("Roles loaded successfully");
        }
    }

    private void loadSectors() {
        if (sectorRepository.count() == 0) {
            log.info("Loading sectors...");
            
            Map<Integer, Sector> sectorMap = new HashMap<>();
            
            // Level 0: Root sectors
            sectorMap.put(1, createSector("Manufacturing", "Manufacturing sector", 0, 1, null));
            sectorMap.put(2, createSector("Service", "Service sector", 0, 2, null));
            sectorMap.put(3, createSector("Other", "Other sectors", 0, 3, null));
            
            // Save root sectors first
            sectorRepository.saveAll(sectorMap.values());
            
            // Level 1: Manufacturing subcategories
            sectorMap.put(19, createSector("Construction materials", "Construction materials", 1, 1, sectorMap.get(1)));
            sectorMap.put(18, createSector("Electronics and Optics", "Electronics and Optics", 1, 2, sectorMap.get(1)));
            sectorMap.put(6, createSector("Food and Beverage", "Food and Beverage", 1, 3, sectorMap.get(1)));
            sectorMap.put(13, createSector("Furniture", "Furniture", 1, 4, sectorMap.get(1)));
            sectorMap.put(12, createSector("Machinery", "Machinery", 1, 5, sectorMap.get(1)));
            sectorMap.put(11, createSector("Metalworking", "Metalworking", 1, 6, sectorMap.get(1)));
            sectorMap.put(9, createSector("Plastic and Rubber", "Plastic and Rubber", 1, 7, sectorMap.get(1)));
            sectorMap.put(5, createSector("Printing", "Printing", 1, 8, sectorMap.get(1)));
            sectorMap.put(7, createSector("Textile and Clothing", "Textile and Clothing", 1, 9, sectorMap.get(1)));
            sectorMap.put(8, createSector("Wood", "Wood", 1, 10, sectorMap.get(1)));
            
            // Level 1: Service subcategories
            sectorMap.put(25, createSector("Business services", "Business services", 1, 1, sectorMap.get(2)));
            sectorMap.put(35, createSector("Engineering", "Engineering", 1, 2, sectorMap.get(2)));
            sectorMap.put(28, createSector("Information Technology and Telecommunications", "Information Technology and Telecommunications", 1, 3, sectorMap.get(2)));
            sectorMap.put(22, createSector("Tourism", "Tourism", 1, 4, sectorMap.get(2)));
            sectorMap.put(141, createSector("Translation services", "Translation services", 1, 5, sectorMap.get(2)));
            sectorMap.put(21, createSector("Transport and Logistics", "Transport and Logistics", 1, 6, sectorMap.get(2)));
            
            // Level 1: Other subcategories
            sectorMap.put(37, createSector("Creative industries", "Creative industries", 1, 1, sectorMap.get(3)));
            sectorMap.put(29, createSector("Energy technology", "Energy technology", 1, 2, sectorMap.get(3)));
            sectorMap.put(33, createSector("Environment", "Environment", 1, 3, sectorMap.get(3)));
            
            // Save level 1 sectors
            sectorRepository.saveAll(Arrays.asList(
                sectorMap.get(19), sectorMap.get(18), sectorMap.get(6), sectorMap.get(13), sectorMap.get(12),
                sectorMap.get(11), sectorMap.get(9), sectorMap.get(5), sectorMap.get(7), sectorMap.get(8),
                sectorMap.get(25), sectorMap.get(35), sectorMap.get(28), sectorMap.get(22), sectorMap.get(141),
                sectorMap.get(21), sectorMap.get(37), sectorMap.get(29), sectorMap.get(33)
            ));
            
            // Level 2: Food and Beverage subcategories
            sectorMap.put(342, createSector("Bakery & confectionery products", "Bakery & confectionery products", 2, 1, sectorMap.get(6)));
            sectorMap.put(43, createSector("Beverages", "Beverages", 2, 2, sectorMap.get(6)));
            sectorMap.put(42, createSector("Fish & fish products", "Fish & fish products", 2, 3, sectorMap.get(6)));
            sectorMap.put(40, createSector("Meat & meat products", "Meat & meat products", 2, 4, sectorMap.get(6)));
            sectorMap.put(39, createSector("Milk & dairy products", "Milk & dairy products", 2, 5, sectorMap.get(6)));
            sectorMap.put(437, createSector("Other", "Other food products", 2, 6, sectorMap.get(6)));
            sectorMap.put(378, createSector("Sweets & snack food", "Sweets & snack food", 2, 7, sectorMap.get(6)));
            
            // Level 2: Furniture subcategories
            sectorMap.put(389, createSector("Bathroom/sauna", "Bathroom/sauna furniture", 2, 1, sectorMap.get(13)));
            sectorMap.put(385, createSector("Bedroom", "Bedroom furniture", 2, 2, sectorMap.get(13)));
            sectorMap.put(390, createSector("Children's room", "Children's room furniture", 2, 3, sectorMap.get(13)));
            sectorMap.put(98, createSector("Kitchen", "Kitchen furniture", 2, 4, sectorMap.get(13)));
            sectorMap.put(101, createSector("Living room", "Living room furniture", 2, 5, sectorMap.get(13)));
            sectorMap.put(392, createSector("Office", "Office furniture", 2, 6, sectorMap.get(13)));
            sectorMap.put(394, createSector("Other (Furniture)", "Other furniture", 2, 7, sectorMap.get(13)));
            sectorMap.put(341, createSector("Outdoor", "Outdoor furniture", 2, 8, sectorMap.get(13)));
            sectorMap.put(99, createSector("Project furniture", "Project furniture", 2, 9, sectorMap.get(13)));
            
            // Level 2: Machinery subcategories
            sectorMap.put(94, createSector("Machinery components", "Machinery components", 2, 1, sectorMap.get(12)));
            sectorMap.put(91, createSector("Machinery equipment/tools", "Machinery equipment/tools", 2, 2, sectorMap.get(12)));
            sectorMap.put(224, createSector("Manufacture of machinery", "Manufacture of machinery", 2, 3, sectorMap.get(12)));
            sectorMap.put(97, createSector("Maritime", "Maritime", 2, 4, sectorMap.get(12)));
            sectorMap.put(93, createSector("Metal structures", "Metal structures", 2, 5, sectorMap.get(12)));
            sectorMap.put(508, createSector("Other", "Other machinery", 2, 6, sectorMap.get(12)));
            sectorMap.put(227, createSector("Repair and maintenance service", "Repair and maintenance service", 2, 7, sectorMap.get(12)));
            
            // Save all level 2 sectors (continuing with batch saves for performance)
            sectorRepository.saveAll(Arrays.asList(
                sectorMap.get(342), sectorMap.get(43), sectorMap.get(42), sectorMap.get(40), sectorMap.get(39), sectorMap.get(437), sectorMap.get(378),
                sectorMap.get(389), sectorMap.get(385), sectorMap.get(390), sectorMap.get(98), sectorMap.get(101), sectorMap.get(392), sectorMap.get(394), sectorMap.get(341), sectorMap.get(99),
                sectorMap.get(94), sectorMap.get(91), sectorMap.get(224), sectorMap.get(97), sectorMap.get(93), sectorMap.get(508), sectorMap.get(227)
            ));
            
            // Continue with remaining sectors (Level 3 and other Level 2)...
            loadRemainingSectors(sectorMap);
            
            log.info("Sectors loaded successfully");
        }
    }
    
    private void loadRemainingSectors(Map<Integer, Sector> sectorMap) {
        // Level 3: Maritime subcategories
        sectorMap.put(271, createSector("Aluminium and steel workboats", "Aluminium and steel workboats", 3, 1, sectorMap.get(97)));
        sectorMap.put(269, createSector("Boat/Yacht building", "Boat/Yacht building", 3, 2, sectorMap.get(97)));
        sectorMap.put(230, createSector("Ship repair and conversion", "Ship repair and conversion", 3, 3, sectorMap.get(97)));
        
        // Level 2: Metalworking subcategories
        sectorMap.put(67, createSector("Construction of metal structures", "Construction of metal structures", 2, 1, sectorMap.get(11)));
        sectorMap.put(263, createSector("Houses and buildings", "Houses and buildings", 2, 2, sectorMap.get(11)));
        sectorMap.put(267, createSector("Metal products", "Metal products", 2, 3, sectorMap.get(11)));
        sectorMap.put(542, createSector("Metal works", "Metal works", 2, 4, sectorMap.get(11)));
        
        // Save these sectors first
        sectorRepository.saveAll(Arrays.asList(
            sectorMap.get(271), sectorMap.get(269), sectorMap.get(230),
            sectorMap.get(67), sectorMap.get(263), sectorMap.get(267), sectorMap.get(542)
        ));
        
        // Level 3: Metal works subcategories
        sectorMap.put(75, createSector("CNC-machining", "CNC-machining", 3, 1, sectorMap.get(542)));
        sectorMap.put(62, createSector("Forgings, Fasteners", "Forgings, Fasteners", 3, 2, sectorMap.get(542)));
        sectorMap.put(69, createSector("Gas, Plasma, Laser cutting", "Gas, Plasma, Laser cutting", 3, 3, sectorMap.get(542)));
        sectorMap.put(66, createSector("MIG, TIG, Aluminum welding", "MIG, TIG, Aluminum welding", 3, 4, sectorMap.get(542)));
        
        // Level 2: Plastic and Rubber subcategories
        sectorMap.put(54, createSector("Packaging", "Packaging", 2, 1, sectorMap.get(9)));
        sectorMap.put(556, createSector("Plastic goods", "Plastic goods", 2, 2, sectorMap.get(9)));
        sectorMap.put(559, createSector("Plastic processing technology", "Plastic processing technology", 2, 3, sectorMap.get(9)));
        sectorMap.put(560, createSector("Plastic profiles", "Plastic profiles", 2, 4, sectorMap.get(9)));
        
        // Save these sectors
        sectorRepository.saveAll(Arrays.asList(
            sectorMap.get(75), sectorMap.get(62), sectorMap.get(69), sectorMap.get(66),
            sectorMap.get(54), sectorMap.get(556), sectorMap.get(559), sectorMap.get(560)
        ));
        
        // Level 3: Plastic processing technology subcategories
        sectorMap.put(55, createSector("Blowing", "Blowing", 3, 1, sectorMap.get(559)));
        sectorMap.put(57, createSector("Moulding", "Moulding", 3, 2, sectorMap.get(559)));
        sectorMap.put(53, createSector("Plastics welding and processing", "Plastics welding and processing", 3, 3, sectorMap.get(559)));
        
        // Level 2: Printing subcategories
        sectorMap.put(148, createSector("Advertising", "Advertising", 2, 1, sectorMap.get(5)));
        sectorMap.put(150, createSector("Book/Periodicals printing", "Book/Periodicals printing", 2, 2, sectorMap.get(5)));
        sectorMap.put(145, createSector("Labelling and packaging printing", "Labelling and packaging printing", 2, 3, sectorMap.get(5)));
        
        // Level 2: Textile and Clothing subcategories
        sectorMap.put(44, createSector("Clothing", "Clothing", 2, 1, sectorMap.get(7)));
        sectorMap.put(45, createSector("Textile", "Textile", 2, 2, sectorMap.get(7)));
        
        // Level 2: Wood subcategories
        sectorMap.put(337, createSector("Other (Wood)", "Other wood products", 2, 1, sectorMap.get(8)));
        sectorMap.put(51, createSector("Wooden building materials", "Wooden building materials", 2, 2, sectorMap.get(8)));
        sectorMap.put(47, createSector("Wooden houses", "Wooden houses", 2, 3, sectorMap.get(8)));
        
        // Level 2: Information Technology and Telecommunications subcategories
        sectorMap.put(581, createSector("Data processing, Web portals, E-marketing", "Data processing, Web portals, E-marketing", 2, 1, sectorMap.get(28)));
        sectorMap.put(576, createSector("Programming, Consultancy", "Programming, Consultancy", 2, 2, sectorMap.get(28)));
        sectorMap.put(121, createSector("Software, Hardware", "Software, Hardware", 2, 3, sectorMap.get(28)));
        sectorMap.put(122, createSector("Telecommunications", "Telecommunications", 2, 4, sectorMap.get(28)));
        
        // Level 2: Transport and Logistics subcategories
        sectorMap.put(111, createSector("Air", "Air transport", 2, 1, sectorMap.get(21)));
        sectorMap.put(114, createSector("Rail", "Rail transport", 2, 2, sectorMap.get(21)));
        sectorMap.put(112, createSector("Road", "Road transport", 2, 3, sectorMap.get(21)));
        sectorMap.put(113, createSector("Water", "Water transport", 2, 4, sectorMap.get(21)));
        
        // Save final batch
        sectorRepository.saveAll(Arrays.asList(
            sectorMap.get(55), sectorMap.get(57), sectorMap.get(53),
            sectorMap.get(148), sectorMap.get(150), sectorMap.get(145),
            sectorMap.get(44), sectorMap.get(45),
            sectorMap.get(337), sectorMap.get(51), sectorMap.get(47),
            sectorMap.get(581), sectorMap.get(576), sectorMap.get(121), sectorMap.get(122),
            sectorMap.get(111), sectorMap.get(114), sectorMap.get(112), sectorMap.get(113)
        ));
    }
    
    private Sector createSector(String name, String description, int level, int sortOrder, Sector parent) {
        return Sector.builder()
                .name(name)
                .description(description)
                .level(level)
                .sortOrder(sortOrder)
                .parent(parent)
                .isActive(true)
                .build();
    }
}