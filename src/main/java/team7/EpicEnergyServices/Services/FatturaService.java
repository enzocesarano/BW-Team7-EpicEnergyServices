package team7.EpicEnergyServices.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import team7.EpicEnergyServices.Entities.Cliente;
import team7.EpicEnergyServices.Entities.Enums.StatoFattura;
import team7.EpicEnergyServices.Entities.Enums.TipoUtente;
import team7.EpicEnergyServices.Entities.Fattura;
import team7.EpicEnergyServices.Entities.Utente;
import team7.EpicEnergyServices.Exceptions.NotFoundException;
import team7.EpicEnergyServices.Exceptions.UnauthorizedException;
import team7.EpicEnergyServices.Repositories.ClienteRepository;
import team7.EpicEnergyServices.Repositories.FatturaRepository;
import team7.EpicEnergyServices.dto.FatturaDTO;
import team7.EpicEnergyServices.dto.StatoFatturaDTO;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class FatturaService {
    private final FatturaRepository fatturaRepository;
    @Autowired
    private FatturaRepository fR;
    @Autowired
    private ClienteRepository cR;
    @Autowired
    private ClienteService cS;

    public FatturaService(FatturaRepository fatturaRepository) {
        this.fatturaRepository = fatturaRepository;
    }

    public Page<Fattura> getFatture(int page, int size, String sortBy, Integer anno, LocalDate dataFattura,
                                    StatoFattura statoFattura, Double minImporto, Double maxImporto, String cliente, Utente currentAuthenticatedUtente) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Specification<Fattura> spec = Specification.where(null);

        if (currentAuthenticatedUtente != null) {
            if (currentAuthenticatedUtente.getTipoUtente() != TipoUtente.ADMIN) {
                spec = spec.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("cliente").get("utente").get("id_utente"), currentAuthenticatedUtente.getId_utente()));
            }
        }

        if (cliente != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("cliente").get("ragioneSociale")),
                    "%" + cliente.toLowerCase() + "%"));
        }
        if (statoFattura != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("statoFattura"), statoFattura));
        }
        if (dataFattura != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("dataFattura"), dataFattura));
        }
        if (anno != null) {
            spec = spec.and((root, query, criteriaBuilder) -> {
                LocalDate startOfYear = LocalDate.of(anno, 1, 1);
                LocalDate endOfYear = LocalDate.of(anno, 12, 31);
                return criteriaBuilder.between(root.get("dataFattura"), startOfYear, endOfYear);
            });
        }
        if (minImporto != null && maxImporto != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("importo"), minImporto, maxImporto));
        }

        return fatturaRepository.findAll(spec, pageable);
    }

    public Fattura save(FatturaDTO body, UUID cliente_Id, Utente currentAuthenticatedUtente) {
        Cliente cliente = cR.findById(cliente_Id).orElseThrow(() -> new NotFoundException("Utente  non trovato"));

        if (currentAuthenticatedUtente != null) {
            if (currentAuthenticatedUtente.getTipoUtente() != TipoUtente.ADMIN) {
                if (!cliente.getUtente().getId_utente().equals(currentAuthenticatedUtente.getId_utente())) {
                    throw new UnauthorizedException("Non hai i permessi per salvare questa fattura!");
                }
            }
        }

        Fattura newFattura = new Fattura(body.importo(), cliente);


        double sommaFattureAnnoCorrente = cliente.getFatture().stream()
                .filter(fattura -> fattura.getDataFattura().getYear() == LocalDate.now().getYear())
                .mapToDouble(Fattura::getImporto)
                .sum();

        cliente.setFatturatoAnnuale(sommaFattureAnnoCorrente + newFattura.getImporto());
        this.fR.save(newFattura);
        this.cR.save(cliente);
        return newFattura;
    }

    public Page<Fattura> findAll(int page, int size) {
        if (size > 100) size = 100;
        Pageable pageable = PageRequest.of(page, size);
        return this.fR.findAll(pageable);
    }

    public Fattura findById(UUID fatturaId, Utente currentAuthenticatedUtente) {
        Fattura fattura = this.fR.findById(fatturaId).orElseThrow(() -> new NotFoundException("fattura non trovata"));

        if (currentAuthenticatedUtente != null) {
            if (currentAuthenticatedUtente.getTipoUtente() != TipoUtente.ADMIN) {
                if (!fattura.getCliente().getUtente().getId_utente().equals(currentAuthenticatedUtente.getId_utente())) {
                    throw new UnauthorizedException("Non hai i permessi per vedere questa fattura!");
                }
            }
        }
        return fattura;
    }

    public Fattura findByIdAndUpdateStato(UUID fatturaId, StatoFatturaDTO body, Utente currentAuthenticatedUtente) {
        Fattura found = this.findById(fatturaId, currentAuthenticatedUtente);

        if (currentAuthenticatedUtente != null) {
            if (currentAuthenticatedUtente.getTipoUtente() != TipoUtente.ADMIN) {
                if (!found.getCliente().getUtente().getId_utente().equals(currentAuthenticatedUtente.getId_utente())) {
                    throw new UnauthorizedException("Non hai i permessi per modificare questa fattura!");
                }
            }
        }

        found.setStatoFattura(body.stato_fattura());
        return this.fR.save(found);
    }


    public void findByIdAndDelete(UUID fatturaId, Utente currentAuthenticatedUtente) {
        Fattura found = this.findById(fatturaId, currentAuthenticatedUtente);
        Cliente cliente = this.cS.findById(found.getCliente().getId_cliente());
        if (currentAuthenticatedUtente != null) {
            if (currentAuthenticatedUtente.getTipoUtente() != TipoUtente.ADMIN) {
                if (!found.getCliente().getUtente().getId_utente().equals(currentAuthenticatedUtente.getId_utente())) {
                    throw new UnauthorizedException("Non hai i permessi per eliminare questa fattura!");
                }
            }
        }


        cliente.setFatturatoAnnuale(cliente.getFatturatoAnnuale() - found.getImporto());
        this.cR.save(cliente);
        this.fR.delete(found);
    }
}
