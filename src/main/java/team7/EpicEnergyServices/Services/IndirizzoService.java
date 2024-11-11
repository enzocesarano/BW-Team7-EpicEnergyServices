package team7.EpicEnergyServices.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team7.EpicEnergyServices.Entities.Cliente;
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

    public Indirizzo createIndirizzo(IndirizzoDTO indirizzoDTO) {
        Indirizzo indirizzo = convertDtoToEntity(indirizzoDTO);
        return indirizzoRepository.save(indirizzo);
    }

    private Indirizzo convertDtoToEntity(IndirizzoDTO dto) {

        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new RuntimeException("Cliente non trovato con ID: " + dto.clienteId()));

        Comune comune = comuneRepository.findById(dto.comuneId())
                .orElseThrow(() -> new RuntimeException("Comune non trovato con ID: " + dto.comuneId()));

        return new Indirizzo(
                dto.via(),
                dto.civico(),
                dto.localita(),
                dto.cap(),
                comune,
                cliente
        );
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

        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new RuntimeException("Cliente non trovato con ID: " + dto.clienteId()));
        indirizzo.setCliente(cliente);

        Comune comune = comuneRepository.findById(dto.comuneId())
                .orElseThrow(() -> new RuntimeException("Comune non trovato con ID: " + dto.comuneId()));
        indirizzo.setComune(comune);

        return indirizzoRepository.save(indirizzo);
    }

    public void deleteIndirizzo(UUID id) {
        indirizzoRepository.deleteById(id);
    }
}
