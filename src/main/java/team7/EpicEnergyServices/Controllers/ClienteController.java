package team7.EpicEnergyServices.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team7.EpicEnergyServices.Entities.Cliente;
import team7.EpicEnergyServices.Entities.Utente;
import team7.EpicEnergyServices.Exceptions.BadRequestException;
import team7.EpicEnergyServices.Services.ClienteService;
import team7.EpicEnergyServices.dto.ClienteDTO;

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

    @PostMapping(value = "/me/clienti", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente createCliente(
            @RequestBody @Validated ClienteDTO payload,
            BindingResult validationResult,
            @AuthenticationPrincipal Utente currentAuthenticatedUtente) {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Errore nei dati forniti!");
        }
        return clienteService.saveCliente(payload, currentAuthenticatedUtente);
    }

    @GetMapping("/me/clienti")
    @ResponseStatus(HttpStatus.OK)
    public Page<Cliente> findAllByUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ragioneSociale") String sortBy,
            @AuthenticationPrincipal Utente currentAuthenticatedUtente) {
        return clienteService.findAllByUtente(page, size, sortBy, currentAuthenticatedUtente);
    }

    @PutMapping("/me/clienti/{id_cliente}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('USER')")
    public Cliente updateCliente(
            @PathVariable UUID id_cliente,
            @RequestBody @Validated ClienteDTO payload,
            BindingResult validationResult,
            @AuthenticationPrincipal Utente currentAuthenticatedUtente) {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Errore nei dati forniti!");
        }
        return clienteService.findByIdAndUpdate(id_cliente, payload, currentAuthenticatedUtente);
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
