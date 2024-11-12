package team7.EpicEnergyServices.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team7.EpicEnergyServices.Entities.Provincia;

import java.util.Optional;

@Repository
public interface ProvinciaRepository extends JpaRepository<Provincia, Integer> {
    Optional<Provincia> findByNome(String nome);
}
