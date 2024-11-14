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
import team7.EpicEnergyServices.Entities.Cliente;
import team7.EpicEnergyServices.Entities.Fattura;
import team7.EpicEnergyServices.Entities.Utente;
import team7.EpicEnergyServices.Exceptions.BadRequestException;
import team7.EpicEnergyServices.Services.ClienteService;
import team7.EpicEnergyServices.Services.FatturaService;
import team7.EpicEnergyServices.Services.UtenteService;
import team7.EpicEnergyServices.dto.ClienteDTO;
import team7.EpicEnergyServices.dto.StatoFatturaDTO;
import team7.EpicEnergyServices.dto.UtenteDTO;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/utenti")
public class UtenteController {
    @Autowired
    private UtenteService utenteService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private FatturaService fatturaService;


    //++++++++++++++++++ permessi Admin+++++++++++++++++++++++

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Utente> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                @RequestParam(defaultValue = "id") String sortBy) {
        return this.utenteService.findAll(page, size, sortBy);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Utente findById(@PathVariable UUID userId) {
        return this.utenteService.findById(userId);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Utente findByIdAndUpdate(@PathVariable UUID userId, @RequestBody @Validated UtenteDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Errore nel payload!");
        }
        return this.utenteService.findByIdAndUpdate(userId, body);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID userId) {
        this.utenteService.findByIdAndDelete(userId);
    }


    //++++++++++++++++++ permessi dell'utente+++++++++++++++++++++++


    @GetMapping("/me")
    public Utente getProfile(@AuthenticationPrincipal Utente currentAuthenticatedUser) {
        return currentAuthenticatedUser;
    }

    @PutMapping("/me")
    public Utente updateProfile(@AuthenticationPrincipal Utente currentAuthenticatedUser, @RequestBody @Validated UtenteDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Errore nel payload!");
        }
        return this.utenteService.findByIdAndUpdate(currentAuthenticatedUser.getId_utente(), body);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal Utente currentAuthenticatedUser) {
        this.utenteService.findByIdAndDelete(currentAuthenticatedUser.getId_utente());
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

    @PutMapping("/me/{fatturaId}")
    public Fattura findByIdAndUpdate(@PathVariable UUID fatturaId,
                                     @RequestBody @Validated StatoFatturaDTO body,
                                     BindingResult validationResult,
                                     @AuthenticationPrincipal Utente currentAuthenticatedUtente) {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Ci sono stati errori nel payload!");
        }
        return this.fatturaService.findByIdAndUpdateStato(fatturaId, body, currentAuthenticatedUtente);
    }
}
