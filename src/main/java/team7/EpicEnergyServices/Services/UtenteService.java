package team7.EpicEnergyServices.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import team7.EpicEnergyServices.Entities.Utente;
import team7.EpicEnergyServices.Exceptions.BadRequestException;
import team7.EpicEnergyServices.Exceptions.NotFoundException;
import team7.EpicEnergyServices.Repositories.UtenteRepository;
import team7.EpicEnergyServices.dto.UtenteDTO;

import java.util.UUID;

@Service
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;


    public Utente save(UtenteDTO body) {
        this.utenteRepository.findByEmail(body.email()).ifPresent(dipendente -> {
                    throw new BadRequestException("Email " + body.email() + " già in uso");
                }
        );
        Utente newUtente = new Utente(body.username(), body.email(), body.password(), body.nome(), body.cognome());
        newUtente.setAvatar("https://ui-avatars.com/api/?name=" + body.nome() + "+" + body.cognome());
        return this.utenteRepository.save(newUtente);
    }

    public Page<Utente> findAll(int page, int size, String sortBy){
        if (size > 100) size = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.utenteRepository.findAll(pageable);
    }

    public Utente findById(UUID userId) {
        return this.utenteRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
    }

    public Utente findByIdAndUpdate(UUID userId, UtenteDTO body) {
        Utente userFound = this.findById(userId);

        if (!userFound.getEmail().equals(body.email())) {
            this.utenteRepository.findByEmail(body.email()).ifPresent(
                    user -> {
                        throw new BadRequestException("Email " + body.email() + " già in uso!");
                    }
            );
        }

        userFound.setNome(body.nome());
        userFound.setCognome(body.cognome());
        userFound.setEmail(body.email());
        userFound.setUsername(body.username());
        return this.utenteRepository.save(userFound);
    }

    public void findByIdAndDelete(UUID userId) {
        Utente userFound = this.findById(userId);
        this.utenteRepository.delete(userFound);
    }

    public Utente finByEmail(String email){
        return this.utenteRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("l'untente con la mail " + email + " non è stato trovato"));
    }
}
