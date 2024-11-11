package team7.EpicEnergyServices.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team7.EpicEnergyServices.Entities.Fattura;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface FatturaRepository extends JpaRepository<Fattura, UUID> {
    Page<Fattura> findByCliente(Pageable pageable, UUID cliente_id);

    Page<Fattura> findByData(Pageable pageable, LocalDate data);

    Page<Fattura> findByImporto(Pageable pageable, Double minimo, Double massimo);

    Page<Fattura> findByAnno(Pageable pageable, LocalDate anno);

    Page<Fattura> findByStato(Pageable pageable, String stato);

}
