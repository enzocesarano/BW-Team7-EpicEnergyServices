package team7.EpicEnergyServices.Services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team7.EpicEnergyServices.Entities.Cliente;
import team7.EpicEnergyServices.Entities.Comune;
import team7.EpicEnergyServices.Entities.Indirizzo;
import team7.EpicEnergyServices.Entities.Utente;
import team7.EpicEnergyServices.Exceptions.BadRequestException;
import team7.EpicEnergyServices.Exceptions.NotFoundException;
import team7.EpicEnergyServices.Exceptions.UnauthorizedException;
import team7.EpicEnergyServices.Repositories.ClienteRepository;
import team7.EpicEnergyServices.Repositories.IndirizzoRepository;
import team7.EpicEnergyServices.dto.ClienteDTO;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private Cloudinary cloudinaryUploader;

    @Autowired
    private IndirizzoService indirizzoService;

    @Autowired
    private IndirizzoRepository indirizzoRepository;

    @Autowired
    private ComuneService comuneService;

    public Page<Cliente> findAll(int page, int size, String sortBy) {
        if (size > 15) size = 15;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return clienteRepository.findAll(pageable);
    }

    public Page<Cliente> findAllByUser(int page, int size, String sortBy, Utente currentAuthenticatedUtente) {
        if (size > 15) size = 15;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return clienteRepository.findAllByUtente(currentAuthenticatedUtente, pageable);
    }

    public Cliente findById(UUID id_cliente) {
        return clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new NotFoundException("Cliente con ID " + id_cliente + " non trovato"));
    }

    public Cliente saveCliente(ClienteDTO payload, Utente currentAuthenticatedUtente) {
        if (clienteRepository.existsByEmail(payload.email())) {
            throw new BadRequestException("La mail è già in uso");
        }

        Comune comuneSedeLegale = this.comuneService.findById(payload.sedeLegale().comune());

        Indirizzo newIndirizzo = new Indirizzo(
                payload.sedeLegale().via(),
                payload.sedeLegale().civico(),
                payload.sedeLegale().localita(),
                payload.sedeLegale().cap(),
                comuneSedeLegale
        );
        indirizzoRepository.save(newIndirizzo);

        List<Indirizzo> sedeOperativaList = payload.sedeOperativa().stream()
                .map(sede -> {
                    if (sede.comune() == null) {
                        throw new BadRequestException("Comune non fornito per la sede operativa");
                    }

                    // Recupera il comune per ogni sede operativa utilizzando l'ID
                    Comune comuneOperativo = this.comuneService.findById(sede.comune());

                    // Crea l'indirizzo operativo
                    Indirizzo indirizzoOperativo = new Indirizzo(
                            sede.via(),
                            sede.civico(),
                            sede.localita(),
                            sede.cap(),
                            comuneOperativo
                    );

                    // Salva l'indirizzo operativo nel database
                    indirizzoRepository.save(indirizzoOperativo);

                    return indirizzoOperativo;
                })
                .collect(Collectors.toList());

        // Crea il nuovo cliente e associa la sede legale e gli indirizzi operativi
        Cliente newCliente = new Cliente(
                payload.ragione_sociale(),
                payload.partita_iva(),
                payload.email(),
                payload.pec(),
                payload.telefono(),
                payload.emailContatto(),
                payload.nomeContatto(),
                payload.cognomeContatto(),
                payload.telefonoContatto(),
                newIndirizzo,  // Associa la sede legale
                sedeOperativaList // Associa la lista degli indirizzi operativi
        );

        // Imposta altri dati del cliente
        newCliente.setFatturatoAnnuale(payload.fatturatoAnnuale());
        newCliente.setDataUltimoContatto(payload.dataUltimoContatto());
        newCliente.setTipoCliente(payload.tipoCliente());
        newCliente.setUtente(currentAuthenticatedUtente);

        // Salva il cliente nel database
        return clienteRepository.save(newCliente);
    }

    public Cliente findByIdAndUpdate(UUID id_cliente, ClienteDTO payload) {
        Cliente cliente = this.findById(id_cliente);

        if (!cliente.getEmail().equals(payload.email()) && clienteRepository.existsByEmail(payload.email())) {
            throw new BadRequestException("La mail è già in uso");
        }

        Indirizzo indirizzo = this.indirizzoService.getIndirizzoById(payload.sedeLegale().comune())
                .orElseThrow(() -> new NotFoundException("L'indirizzo con " + payload.sedeLegale() + " non è stato trovato!"));

        List<Indirizzo> sedeOperativaList = payload.sedeOperativa().stream()
                .map(id -> this.indirizzoService.getIndirizzoById(id.comune())
                        .orElseThrow(() -> new NotFoundException("L'indirizzo con ID " + id.comune() + " non è stato trovato!"))
                ).toList();

        cliente.setRagione_sociale(payload.ragione_sociale());
        cliente.setPartita_iva(payload.partita_iva());
        cliente.setEmail(payload.email());
        cliente.setPec(payload.pec());
        cliente.setTelefono(payload.telefono());
        cliente.setEmailContatto(payload.emailContatto());
        cliente.setNomeContatto(payload.nomeContatto());
        cliente.setCognomeContatto(payload.cognomeContatto());
        cliente.setTelefonoContatto(payload.telefonoContatto());
        cliente.setSedeLegale(indirizzo);
        cliente.setSedeOperativa(sedeOperativaList);
        cliente.setFatturatoAnnuale(payload.fatturatoAnnuale());
        cliente.setDataUltimoContatto(payload.dataUltimoContatto());
        cliente.setTipoCliente(payload.tipoCliente());

        return clienteRepository.save(cliente);
    }

    public void deleteCliente(UUID id_cliente) {
        Cliente cliente = this.findById(id_cliente);
        clienteRepository.delete(cliente);
    }

    public Cliente findByEmail(String email) {
        return clienteRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Il cliente con email " + email + " non è stato trovato"));
    }

    public String updateAvatar(MultipartFile file, UUID id_cliente, Utente currentAuthenticatedUtente) {
        Cliente cliente = this.findById(id_cliente);
        if (!cliente.getUtente().getId_utente().equals(currentAuthenticatedUtente.getId_utente())) {
            throw new UnauthorizedException("Non hai i permessi per modificare questo cliente!");
        }
        String url = null;
        try {
            url = (String) cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        } catch (IOException e) {
            throw new BadRequestException("Ci sono stati problemi con l'upload dell'avatar!");
        }
        cliente.setLogoAziendale(url);
        this.clienteRepository.save(cliente);
        return url;
    }
}