package team7.EpicEnergyServices.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import team7.EpicEnergyServices.Entities.Comune;

import java.util.UUID;

public interface ComuneRepository extends JpaRepository<Comune, UUID> {
}
