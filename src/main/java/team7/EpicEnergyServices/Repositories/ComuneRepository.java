package team7.EpicEnergyServices.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team7.EpicEnergyServices.Entities.Comune;

import java.util.UUID;

@Repository
public interface ComuneRepository extends JpaRepository<Comune, UUID> {
}
