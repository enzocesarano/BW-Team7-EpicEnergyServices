package team7.EpicEnergyServices.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team7.EpicEnergyServices.Entities.Indirizzo;
import team7.EpicEnergyServices.Repositories.IndirizzoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class IndirizzoService {

    @Autowired
    private IndirizzoRepository indirizzoRepository;

    public Indirizzo createIndirizzo(Indirizzo indirizzo) {
        return indirizzoRepository.save(indirizzo);
    }

    public List<Indirizzo> getAllIndirizzi() {
        return indirizzoRepository.findAll();
    }

    public Optional<Indirizzo> getIndirizzoById(UUID id) {
        return indirizzoRepository.findById(id);
    }

    public void deleteIndirizzo(UUID id) {
        indirizzoRepository.deleteById(id);
    }
}
