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
import team7.EpicEnergyServices.Exceptions.NotFoundException;
import team7.EpicEnergyServices.Repositories.ClienteRepository;
import team7.EpicEnergyServices.Repositories.FatturaRepository;
import team7.EpicEnergyServices.dto.FatturaDTO;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class FatturaService {
    @Autowired
    private FatturaRepository fR;
    @Autowired
    private ClienteRepository cR;

    public Fattura save(FatturaDTO body, UUID cliente_Id) throws Throwable {
        Cliente cliente = (Cliente) cR.findById(cliente_Id).orElseThrow(() -> new NotFoundException("Utente  non trovato"));

        Fattura newFattura = new Fattura(body.data(), body.importo(), body.statoFattura(), cliente);

        return this.fR.save(newFattura);
    }

    public Page<Fattura> findAll(int page, int size, String sortBy) {
        if (size > 100) size = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.fR.findAll(pageable);
    }

    public Fattura findById(UUID fatturaId) {
        return this.fR.findById(fatturaId).orElseThrow(() -> new NotFoundException("fattura non trovata"));
    }

    public Fattura findByIdAndUpdateStato(UUID fatturaId, FatturaDTO body) {
        Fattura found = this.findById(fatturaId);


        found.setStatoFattura(body.statoFattura());


        return this.fR.save(found);
    }


    public Page<Fattura> findbyCliente(int page, int size, String sortBy, UUID clienteID) {
        if (size > 100) size = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.fR.findByCliente(pageable, clienteID);
    }

    public Page<Fattura> findFattureByStatoFattura(StatoFattura statoFattura, int page, int size, String sortBy) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortBy));
        return fR.findByStatoFattura(statoFattura, pageRequest);
    }

    public Page<Fattura> findbyDataFattura(int page, int size, String sortBy, LocalDate data) {
        if (size > 100) size = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.fR.findByDataFattura(pageable, data);
    }

    public Page<Fattura> findByImporto(int page, int size, String sortBy, double minimo, double massimo) {
        if (size > 100) size = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.fR.findByImportoBetween(pageable, minimo, massimo);
    }


    public void findByIdAndDelete(UUID fatturaId) {
        Fattura found = this.findById(fatturaId);
        this.fR.delete(found);
    }

}
