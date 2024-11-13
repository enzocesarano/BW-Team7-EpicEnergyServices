package team7.EpicEnergyServices.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team7.EpicEnergyServices.Entities.Cliente;
import team7.EpicEnergyServices.Entities.Enums.StatoFattura;
import team7.EpicEnergyServices.Entities.Fattura;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface FatturaRepository extends JpaRepository<Fattura, UUID> {

    Page<Fattura> findAllByCliente(Cliente cliente, Pageable pageable);

    Page<Fattura> findByStatoFattura(StatoFattura stato_fattura, Pageable pageable);

    Page<Fattura> findByDataFattura(LocalDate dataFattura, Pageable pageable);

    @Query("SELECT f FROM Fattura f WHERE YEAR(f.dataFattura) = :anno")
    Page<Fattura> findByAnno(int anno, Pageable pageable);

    Page<Fattura> findByImportoBetween(Double minImporto, Double maxImporto, Pageable pageable);
}
