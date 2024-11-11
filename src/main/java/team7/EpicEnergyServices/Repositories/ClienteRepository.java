package team7.EpicEnergyServices.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import team7.EpicEnergyServices.Entities.Cliente;
import team7.EpicEnergyServices.Entities.Utente;

import java.util.Optional;
import java.util.UUID;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    boolean existsByEmail(String email);

    Optional<Cliente> findByEmail(String email);

    Page<Cliente> findAllByUtente(Utente utente, Pageable pageable);
}
