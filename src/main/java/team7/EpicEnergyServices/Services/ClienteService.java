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

    public Page<Cliente> findAllByUtente(int page, int size, String sortBy, Utente currentAuthenticatedUtente) {
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

        Cliente newCliente = new Cliente(
                payload.ragioneSociale(),
                payload.partita_iva(),
                payload.email(),
                payload.pec(),
                payload.telefono(),
                payload.emailContatto(),
                payload.nomeContatto(),
                payload.cognomeContatto(),
                payload.telefonoContatto(),
                null
        );

        newCliente.setDataUltimoContatto(payload.dataUltimoContatto());
        newCliente.setTipoCliente(payload.tipoCliente());
        newCliente.setUtente(currentAuthenticatedUtente);
        newCliente.setLogoAziendale("https://ui-avatars.com/api/?name=" + payload.ragioneSociale().replace(" ", ""));
        clienteRepository.save(newCliente);
        List<Indirizzo> sedeOperativaList = payload.sede().stream()
                .map(sede -> {
                    if (sede.comune() == null) {
                        throw new BadRequestException("Comune non fornito per la sede operativa");
                    }
                    Comune comuneOperativo = this.comuneService.findById(sede.comune());
                    Indirizzo indirizzoOperativo = new Indirizzo(
                            sede.via(),
                            sede.civico(),
                            sede.localita(),
                            sede.cap(),
                            comuneOperativo,
                            sede.sede()
                    );
                    indirizzoOperativo.setCliente(newCliente);
                    indirizzoRepository.save(indirizzoOperativo);
                    return indirizzoOperativo;
                })
                .collect(Collectors.toList());
        newCliente.setSedi(sedeOperativaList);
        return clienteRepository.save(newCliente);
    }


    public Cliente findByIdAndUpdate(UUID id_cliente, ClienteDTO payload, Utente currentAuthenticatedUtente) {
        Cliente cliente = this.findById(id_cliente);
        if (!cliente.getEmail().equals(payload.email()) && clienteRepository.existsByEmail(payload.email())) {
            throw new BadRequestException("La mail è già in uso");
        }

        cliente.setRagioneSociale(payload.ragioneSociale());
        cliente.setPartita_iva(payload.partita_iva());
        cliente.setEmail(payload.email());
        cliente.setPec(payload.pec());
        cliente.setTelefono(payload.telefono());
        cliente.setEmailContatto(payload.emailContatto());
        cliente.setNomeContatto(payload.nomeContatto());
        cliente.setCognomeContatto(payload.cognomeContatto());
        cliente.setTelefonoContatto(payload.telefonoContatto());
        cliente.setDataUltimoContatto(payload.dataUltimoContatto());
        cliente.setTipoCliente(payload.tipoCliente());
        cliente.setUtente(currentAuthenticatedUtente);
        cliente.setLogoAziendale("https://ui-avatars.com/api/?name=" + payload.ragioneSociale().replace(" ", ""));

        List<Indirizzo> sediList = payload.sede().stream()
                .map(sede -> {
                    if (sede.comune() == null) {
                        throw new BadRequestException("Comune non fornito per la sede");
                    }

                    Comune comuneOperativo = this.comuneService.findById(sede.comune());

                    Indirizzo indirizzo = new Indirizzo(
                            sede.via(),
                            sede.civico(),
                            sede.localita(),
                            sede.cap(),
                            comuneOperativo,
                            sede.sede()
                    );
                    indirizzo.setCliente(cliente);
                    indirizzoRepository.save(indirizzo);
                    return indirizzo;
                })
                .collect(Collectors.toList());
        cliente.setSedi(sediList);
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