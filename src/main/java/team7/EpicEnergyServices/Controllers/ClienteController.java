package team7.EpicEnergyServices.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team7.EpicEnergyServices.Entities.Cliente;
import team7.EpicEnergyServices.Entities.Utente;
import team7.EpicEnergyServices.Services.ClienteService;

import java.util.UUID;

@RestController
@RequestMapping("/clienti")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<Cliente> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ragioneSociale") String sortBy) {
        return clienteService.findAll(page, size, sortBy);
    }

    @GetMapping("/{id_cliente}")
    @ResponseStatus(HttpStatus.OK)
    public Cliente findById(@PathVariable UUID id_cliente) {
        return clienteService.findById(id_cliente);
    }


    @DeleteMapping("/{id_cliente}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCliente(@PathVariable UUID id_cliente) {
        clienteService.deleteCliente(id_cliente);
    }

    @PatchMapping("/me/{id_cliente}/logo")
    @ResponseStatus(HttpStatus.OK)
    public String uploadAvatar(@RequestParam("avatar") MultipartFile file, @PathVariable UUID id_dipendente, @AuthenticationPrincipal Utente currentAuthenticatedUtente) {
        return this.clienteService.updateAvatar(file, id_dipendente, currentAuthenticatedUtente);
    }
}
