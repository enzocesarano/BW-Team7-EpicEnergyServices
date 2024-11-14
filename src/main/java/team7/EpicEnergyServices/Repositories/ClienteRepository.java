package team7.EpicEnergyServices.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import team7.EpicEnergyServices.Entities.Cliente;
import team7.EpicEnergyServices.Entities.Utente;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID>, JpaSpecificationExecutor<Cliente> {
    boolean existsByEmail(String email);

    Optional<Cliente> findByEmail(String email);

    Page<Cliente> findAllByUtente(Utente utente, Pageable pageable);

//    Page<Cliente> findByFatturatoAnnualeBetween(Double minFatturato, Double maxFatturato, Pageable pageable);
//
//    Page<Cliente> findByDataInserimento(LocalDate dataInserimento, Pageable pageable);
//
//    Page<Cliente> findByDataUltimoContatto(LocalDate dataUltimoContatto, Pageable pageable);
//
//    Page<Cliente> findByRagioneSocialeContainingIgnoreCase(String parteNome, Pageable pageable);
//
//    Page<Cliente> findByProvinciaSedi(String provinciaSedi, Pageable pageable);
}
