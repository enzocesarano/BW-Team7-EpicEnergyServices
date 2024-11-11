package team7.EpicEnergyServices.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import team7.EpicEnergyServices.Entities.Cliente;

import java.util.UUID;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
}
