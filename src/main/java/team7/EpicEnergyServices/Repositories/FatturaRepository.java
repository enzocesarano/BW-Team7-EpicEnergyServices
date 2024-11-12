package team7.EpicEnergyServices.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team7.EpicEnergyServices.Entities.Enums.StatoFattura;
import team7.EpicEnergyServices.Entities.Fattura;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface FatturaRepository extends JpaRepository<Fattura, UUID> {
    Page<Fattura> findByCliente(Pageable pageable, UUID cliente_id);

    Page<Fattura> findByDataFattura(Pageable pageable, LocalDate dataFattura);

    Page<Fattura> findByImportoBetween(Pageable pageable, Double minimo, Double massimo);


    Page<Fattura> findByStatoFattura(StatoFattura statoFattura, Pageable pageable);

}
