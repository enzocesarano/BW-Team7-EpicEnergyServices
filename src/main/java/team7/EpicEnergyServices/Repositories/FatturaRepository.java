package team7.EpicEnergyServices.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import team7.EpicEnergyServices.Entities.Fattura;

import java.util.UUID;

@Repository
public interface FatturaRepository extends JpaRepository<Fattura, UUID>, JpaSpecificationExecutor<Fattura> {
}
