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

import java.time.LocalDate;
import java.util.UUID;

@RestController
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/clienti/{id_cliente}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Cliente findById(@PathVariable("id_cliente") UUID id_cliente) {
        return clienteService.findById(id_cliente);
    }


    @DeleteMapping("/clienti/{id_cliente}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCliente(@PathVariable("id_cliente") UUID id_cliente) {
        clienteService.deleteCliente(id_cliente);
    }

    @PatchMapping("/me/{id_cliente}/logo")
    @ResponseStatus(HttpStatus.OK)
    public String updateLogo(@PathVariable("id_cliente") UUID id_cliente,
                             @RequestParam("logo") MultipartFile file,
                             @AuthenticationPrincipal Utente currentAuthenticatedUtente) {
        return this.clienteService.updateLogo(file, id_cliente, currentAuthenticatedUtente);
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
    public Page<Cliente> getClientiByUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ragioneSociale") String sortBy,
            @RequestParam(required = false) Double minFatturato,
            @RequestParam(required = false) Double maxFatturato,
            @RequestParam(required = false) LocalDate dataInserimento,
            @RequestParam(required = false) LocalDate dataUltimoContatto,
            @RequestParam(required = false) String parteRagioneSociale,
            @AuthenticationPrincipal Utente currentAuthenticatedUser) {

        return clienteService.getClienti(page, size, sortBy, minFatturato, maxFatturato,
                dataInserimento, dataUltimoContatto, parteRagioneSociale, currentAuthenticatedUser);
    }

    @PutMapping("/me/clienti/{id_cliente}")
    @ResponseStatus(HttpStatus.OK)
    public Cliente updateCliente(
            @PathVariable("id_cliente") UUID id_cliente,
            @RequestBody @Validated ClienteDTO payload,
            BindingResult validationResult,
            @AuthenticationPrincipal Utente currentAuthenticatedUtente) {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Errore nei dati forniti!");
        }
        return clienteService.findByIdAndUpdate(id_cliente, payload, currentAuthenticatedUtente);
    }
}
