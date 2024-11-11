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
import team7.EpicEnergyServices.Entities.Utente;
import team7.EpicEnergyServices.Exceptions.BadRequestException;
import team7.EpicEnergyServices.Exceptions.NotFoundException;
import team7.EpicEnergyServices.Exceptions.UnauthorizedException;
import team7.EpicEnergyServices.Repositories.ClienteRepository;
import team7.EpicEnergyServices.dto.ClienteDTO;

import java.io.IOException;
import java.util.UUID;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private Cloudinary cloudinaryUploader;

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
                payload.sedeLegale(),
                payload.sedeOperativa()
        );
        newCliente.setFatturatoAnnuale(payload.fatturatoAnnuale());
        newCliente.setDataUltimoContatto(payload.dataUltimoContatto());
        newCliente.setTipoCliente(payload.tipoCliente());
        newCliente.setUtente(currentAuthenticatedUtente);

        return clienteRepository.save(newCliente);
    }

    public Cliente findByIdAndUpdate(UUID id_cliente, ClienteDTO payload) {
        Cliente cliente = this.findById(id_cliente);

        if (!cliente.getEmail().equals(payload.email()) && clienteRepository.existsByEmail(payload.email())) {
            throw new BadRequestException("La mail è già in uso");
        }

        cliente.setRagione_sociale(payload.ragione_sociale());
        cliente.setPartita_iva(payload.partita_iva());
        cliente.setEmail(payload.email());
        cliente.setPec(payload.pec());
        cliente.setTelefono(payload.telefono());
        cliente.setEmailContatto(payload.emailContatto());
        cliente.setNomeContatto(payload.nomeContatto());
        cliente.setCognomeContatto(payload.cognomeContatto());
        cliente.setTelefonoContatto(payload.telefonoContatto());
        cliente.setSedeLegale(payload.sedeLegale());
        cliente.setSedeOperativa(payload.sedeOperativa());
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