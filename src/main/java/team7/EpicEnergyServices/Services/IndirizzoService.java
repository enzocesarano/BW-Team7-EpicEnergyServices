package team7.EpicEnergyServices.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team7.EpicEnergyServices.Entities.Comune;
import team7.EpicEnergyServices.Entities.Indirizzo;
import team7.EpicEnergyServices.Repositories.ClienteRepository;
import team7.EpicEnergyServices.Repositories.ComuneRepository;
import team7.EpicEnergyServices.Repositories.IndirizzoRepository;
import team7.EpicEnergyServices.dto.IndirizzoDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class IndirizzoService {

    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    ComuneRepository comuneRepository;
    @Autowired
    private IndirizzoRepository indirizzoRepository;

    public Indirizzo createIndirizzo(IndirizzoDTO indirizzo) {
        Comune comune = comuneRepository.findById(indirizzo.comune())
                .orElseThrow(() -> new RuntimeException("Comune non trovato con ID: " + indirizzo.comune()));

        Indirizzo indirizzo1 = new Indirizzo(
                indirizzo.via(),
                indirizzo.civico(),
                indirizzo.localita(),
                indirizzo.cap(),
                comune,
                indirizzo.sede()
        );
        return indirizzoRepository.save(indirizzo1);
    }


    public List<Indirizzo> getAllIndirizzi() {
        return indirizzoRepository.findAll();
    }

    public Optional<Indirizzo> getIndirizzoById(UUID id) {
        return indirizzoRepository.findById(id);
    }

    public Indirizzo updateIndirizzo(UUID id, IndirizzoDTO dto) {
        Indirizzo indirizzo = indirizzoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Indirizzo non trovato con ID: " + id));

        indirizzo.setVia(dto.via());
        indirizzo.setCivico(dto.civico());
        indirizzo.setLocalita(dto.localita());
        indirizzo.setCap(dto.cap());

        Comune comune = comuneRepository.findById(dto.comune())
                .orElseThrow(() -> new RuntimeException("Comune non trovato con ID: " + dto.comune()));
        indirizzo.setComune(comune);

        return indirizzoRepository.save(indirizzo);
    }

    public void deleteIndirizzo(UUID id) {
        indirizzoRepository.deleteById(id);
    }
}
