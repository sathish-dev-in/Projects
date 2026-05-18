package com.autonest.config;

import com.autonest.entity.postgres.*;
import com.autonest.enums.ServiceStatus;
import com.autonest.enums.ServiceType;
import com.autonest.enums.VehicleType;
import com.autonest.repository.postgres.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Seeds the database with initial demo data on startup.
 * Only runs if the database is empty.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;
    private final MechanicRepository mechanicRepository;
    private final ServiceOrderRepository serviceOrderRepository;
    private final PartRepository partRepository;

    @Override
    public void run(String... args) {
        if (customerRepository.count() > 0) {
            log.info("[DataInitializer] Database already seeded — skipping.");
            return;
        }

        log.info("[DataInitializer] Seeding database with initial data...");

        List<Mechanic> mechanics = seedMechanics();
        List<Customer> customers = seedCustomers();
        List<Vehicle> vehicles = seedVehicles(customers);
        seedServiceOrders(vehicles, mechanics);
        seedParts();

        log.info("[DataInitializer] Database seeding complete.");
    }

    private List<Mechanic> seedMechanics() {
        Mechanic m1 = Mechanic.builder()
                .firstName("Carlos").lastName("Rivera")
                .email("carlos.rivera@autonest.com").phone("9876543210")
                .specialization("Engine & Transmission").employeeId("EMP001")
                .build();

        Mechanic m2 = Mechanic.builder()
                .firstName("Diana").lastName("Chen")
                .email("diana.chen@autonest.com").phone("9876543211")
                .specialization("Electrical & AC Systems").employeeId("EMP002")
                .build();

        Mechanic m3 = Mechanic.builder()
                .firstName("James").lastName("Patel")
                .email("james.patel@autonest.com").phone("9876543212")
                .specialization("Brakes & Suspension").employeeId("EMP003")
                .build();

        List<Mechanic> saved = mechanicRepository.saveAll(List.of(m1, m2, m3));
        log.info("[DataInitializer] Seeded {} mechanics", saved.size());
        return saved;
    }

    private List<Customer> seedCustomers() {
        Customer c1 = Customer.builder()
                .firstName("Arjun").lastName("Sharma")
                .email("arjun.sharma@email.com").phone("9876501001")
                .address("12 MG Road").city("Bangalore")
                .build();

        Customer c2 = Customer.builder()
                .firstName("Priya").lastName("Nair")
                .email("priya.nair@email.com").phone("9876501002")
                .address("45 Anna Salai").city("Chennai")
                .build();

        Customer c3 = Customer.builder()
                .firstName("Rahul").lastName("Verma")
                .email("rahul.verma@email.com").phone("9876501003")
                .address("78 Baner Road").city("Pune")
                .build();

        Customer c4 = Customer.builder()
                .firstName("Sneha").lastName("Iyer")
                .email("sneha.iyer@email.com").phone("9876501004")
                .address("23 Linking Road").city("Mumbai")
                .build();

        Customer c5 = Customer.builder()
                .firstName("Vikram").lastName("Singh")
                .email("vikram.singh@email.com").phone("9876501005")
                .address("56 Connaught Place").city("Delhi")
                .build();

        List<Customer> saved = customerRepository.saveAll(List.of(c1, c2, c3, c4, c5));
        log.info("[DataInitializer] Seeded {} customers", saved.size());
        return saved;
    }

    private List<Vehicle> seedVehicles(List<Customer> customers) {
        Vehicle v1 = Vehicle.builder()
                .customer(customers.get(0))
                .make("Honda").model("City").year(2021)
                .licensePlate("KA01AB1234").vin("MRHGM185XAP000001")
                .vehicleType(VehicleType.SEDAN).color("White")
                .build();

        Vehicle v2 = Vehicle.builder()
                .customer(customers.get(1))
                .make("Hyundai").model("Creta").year(2022)
                .licensePlate("TN05CD5678").vin("MALCM41CLNM000002")
                .vehicleType(VehicleType.SUV).color("Silver")
                .build();

        Vehicle v3 = Vehicle.builder()
                .customer(customers.get(2))
                .make("Maruti").model("Swift").year(2020)
                .licensePlate("MH12EF9012").vin("MA3FJEB1S00000003")
                .vehicleType(VehicleType.HATCHBACK).color("Red")
                .build();

        Vehicle v4 = Vehicle.builder()
                .customer(customers.get(3))
                .make("Tata").model("Nexon").year(2023)
                .licensePlate("MH01GH3456").vin("MAT607052NTE00004")
                .vehicleType(VehicleType.SUV).color("Blue")
                .build();

        Vehicle v5 = Vehicle.builder()
                .customer(customers.get(4))
                .make("Toyota").model("Fortuner").year(2022)
                .licensePlate("DL02IJ7890").vin("MHFZX59G200000005")
                .vehicleType(VehicleType.SUV).color("Black")
                .build();

        List<Vehicle> saved = vehicleRepository.saveAll(List.of(v1, v2, v3, v4, v5));
        log.info("[DataInitializer] Seeded {} vehicles", saved.size());
        return saved;
    }

    private void seedServiceOrders(List<Vehicle> vehicles, List<Mechanic> mechanics) {
        ServiceOrder so1 = ServiceOrder.builder()
                .vehicle(vehicles.get(0))
                .mechanic(mechanics.get(0))
                .serviceType(ServiceType.OIL_CHANGE)
                .description("Full synthetic oil change with filter replacement and top-up of all fluids")
                .estimatedCost(BigDecimal.valueOf(2500.00))
                .status(ServiceStatus.COMPLETED)
                .actualCost(BigDecimal.valueOf(2700.00))
                .notes("Customer requested Mobil 1 synthetic oil")
                .build();

        ServiceOrder so2 = ServiceOrder.builder()
                .vehicle(vehicles.get(1))
                .mechanic(mechanics.get(1))
                .serviceType(ServiceType.AC_SERVICE)
                .description("AC gas recharge and compressor inspection")
                .estimatedCost(BigDecimal.valueOf(3500.00))
                .status(ServiceStatus.IN_PROGRESS)
                .notes("Compressor bearing making noise — check if replacement needed")
                .build();

        ServiceOrder so3 = ServiceOrder.builder()
                .vehicle(vehicles.get(2))
                .mechanic(mechanics.get(2))
                .serviceType(ServiceType.BRAKE_SERVICE)
                .description("Front brake pad replacement and rotor resurfacing")
                .estimatedCost(BigDecimal.valueOf(4200.00))
                .status(ServiceStatus.PENDING)
                .notes("Customer reports squealing from front brakes during braking")
                .build();

        serviceOrderRepository.saveAll(List.of(so1, so2, so3));
        log.info("[DataInitializer] Seeded 3 service orders");
    }

    private void seedParts() {
        List<Part> parts = List.of(
                Part.builder().name("Engine Oil Filter").partNumber("EOF-001")
                        .description("OEM compatible oil filter for Honda/Hyundai engines")
                        .unitPrice(BigDecimal.valueOf(350.00)).stockQuantity(45)
                        .minimumStockLevel(10).supplier("Bosch India").category("Filters").build(),

                Part.builder().name("Air Filter").partNumber("AF-002")
                        .description("High-performance air filter — universal fit")
                        .unitPrice(BigDecimal.valueOf(650.00)).stockQuantity(30)
                        .minimumStockLevel(8).supplier("K&N Filters").category("Filters").build(),

                Part.builder().name("Brake Pads (Front)").partNumber("BP-F-003")
                        .description("Ceramic front brake pads — low dust, quiet operation")
                        .unitPrice(BigDecimal.valueOf(1200.00)).stockQuantity(20)
                        .minimumStockLevel(5).supplier("Brembo").category("Brakes").build(),

                Part.builder().name("Brake Discs (Front)").partNumber("BD-F-004")
                        .description("Ventilated front brake discs — pair")
                        .unitPrice(BigDecimal.valueOf(2800.00)).stockQuantity(12)
                        .minimumStockLevel(4).supplier("Brembo").category("Brakes").build(),

                Part.builder().name("Spark Plugs (Set of 4)").partNumber("SP-005")
                        .description("Iridium spark plugs — set of 4")
                        .unitPrice(BigDecimal.valueOf(800.00)).stockQuantity(3)
                        .minimumStockLevel(5).supplier("NGK").category("Ignition").build(),

                Part.builder().name("Coolant (1L)").partNumber("CLT-006")
                        .description("OAT coolant concentrate — 1 litre")
                        .unitPrice(BigDecimal.valueOf(280.00)).stockQuantity(25)
                        .minimumStockLevel(10).supplier("Prestone").category("Fluids").build(),

                Part.builder().name("AC Refrigerant R134a").partNumber("ACR-007")
                        .description("R134a refrigerant canister — 500g")
                        .unitPrice(BigDecimal.valueOf(450.00)).stockQuantity(8)
                        .minimumStockLevel(3).supplier("Honeywell").category("AC Parts").build(),

                Part.builder().name("Timing Belt").partNumber("TB-008")
                        .description("OEM timing belt — compatible with 1.2L–1.5L engines")
                        .unitPrice(BigDecimal.valueOf(1800.00)).stockQuantity(2)
                        .minimumStockLevel(3).supplier("Gates").category("Engine").build(),

                Part.builder().name("Wiper Blades (Pair)").partNumber("WB-009")
                        .description("All-season wiper blades — 24\" and 18\"")
                        .unitPrice(BigDecimal.valueOf(420.00)).stockQuantity(18)
                        .minimumStockLevel(5).supplier("Bosch India").category("Accessories").build(),

                Part.builder().name("Battery 12V 55Ah").partNumber("BAT-010")
                        .description("Maintenance-free lead-acid battery")
                        .unitPrice(BigDecimal.valueOf(4500.00)).stockQuantity(6)
                        .minimumStockLevel(3).supplier("Amaron").category("Electrical").build()
        );

        partRepository.saveAll(parts);
        log.info("[DataInitializer] Seeded {} parts", parts.size());
    }
}
