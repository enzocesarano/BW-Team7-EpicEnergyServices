package team7.EpicEnergyServices.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team7.EpicEnergyServices.Entities.Enums.StatoFattura;
import team7.EpicEnergyServices.Entities.Fattura;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface FatturaRepository extends JpaRepository<Fattura, UUID> {
    @Query("SELECT f FROM Fattura f WHERE f.cliente = :id_cliente")
    Page<Fattura> findByCliente(Pageable pageable, UUID id_cliente);

    @Query("SELECT f FROM Fattura f WHERE f.dataFattura = :dataFattura")
    Page<Fattura> findByDataFattura(Pageable pageable, LocalDate dataFattura);

    @Query("SELECT f FROM Fattura f WHERE f.importo BETWEEN :minimo AND :massimo")
    Page<Fattura> findByImportoBetween(Pageable pageable, Double minimo, Double massimo);

    @Query("SELECT f FROM Fattura f WHERE f.stato_fattura = :statoFattura")
    Page<Fattura> findByStatoFattura(StatoFattura statoFattura, Pageable pageable);
    
    @Query("SELECT f FROM Fattura f WHERE YEAR(f.dataFattura) = :anno")
    Page<Fattura> findByAnno(int anno, Pageable pageable);

}
