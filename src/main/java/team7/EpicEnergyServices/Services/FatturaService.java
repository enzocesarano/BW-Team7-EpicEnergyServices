package team7.EpicEnergyServices.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import team7.EpicEnergyServices.Entities.Cliente;
import team7.EpicEnergyServices.Entities.Enums.StatoFattura;
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

    public FatturaService(FatturaRepository fatturaRepository) {
        this.fatturaRepository = fatturaRepository;
    }

    public Page<Fattura> getFatture(int page, int size, String sortBy, Integer anno, LocalDate dataFattura,
                                    StatoFattura stato_fattura, Double minImporto, Double maxImporto, Cliente cliente) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));


        if (cliente != null) {
            return fatturaRepository.findAllByCliente(cliente, pageable);
        } else if (stato_fattura != null) {
            return fatturaRepository.findByStatoFattura(stato_fattura, pageable);
        } else if (dataFattura != null) {
            return fatturaRepository.findByDataFattura(dataFattura, pageable);
        } else if (anno != null) {
            return fatturaRepository.findByAnno(anno, pageable);
        } else if (minImporto != null && maxImporto != null) {
            return fatturaRepository.findByImportoBetween(minImporto, maxImporto, pageable);
        }

        return fatturaRepository.findAll(pageable);
    }

    public Fattura save(FatturaDTO body, UUID cliente_Id) throws Throwable {
        Cliente cliente = (Cliente) cR.findById(cliente_Id).orElseThrow(() -> new NotFoundException("Utente  non trovato"));

        Fattura newFattura = new Fattura(body.importo(), cliente);
        return this.fR.save(newFattura);
    }

    public Page<Fattura> findAll(int page, int size) {
        if (size > 100) size = 100;
        Pageable pageable = PageRequest.of(page, size);
        return this.fR.findAll(pageable);
    }

    public Fattura findById(UUID fatturaId) {
        return this.fR.findById(fatturaId).orElseThrow(() -> new NotFoundException("fattura non trovata"));
    }

    public Fattura findByIdAndUpdateStato(UUID fatturaId, StatoFatturaDTO body, Utente currentAuthenticatedUtente) {
        Fattura found = this.findById(fatturaId);
        if (!found.getCliente().getUtente().getId_utente().equals(currentAuthenticatedUtente.getId_utente())) {
            throw new UnauthorizedException("Non hai i permessi per modificare questa fattura!");
        }
        found.setStatoFattura(body.stato_fattura());
        return this.fR.save(found);
    }


    public void findByIdAndDelete(UUID fatturaId) {
        Fattura found = this.findById(fatturaId);
        this.fR.delete(found);
    }
}
