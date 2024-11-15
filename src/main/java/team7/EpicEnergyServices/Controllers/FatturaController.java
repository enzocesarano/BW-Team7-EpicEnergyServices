package team7.EpicEnergyServices.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team7.EpicEnergyServices.Entities.Cliente;
import team7.EpicEnergyServices.Entities.Enums.StatoFattura;
import team7.EpicEnergyServices.Entities.Fattura;
import team7.EpicEnergyServices.Entities.Utente;
import team7.EpicEnergyServices.Exceptions.BadRequestException;
import team7.EpicEnergyServices.Services.FatturaService;
import team7.EpicEnergyServices.dto.FatturaDTO;
import team7.EpicEnergyServices.dto.StatoFatturaDTO;

import java.time.LocalDate;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class FatturaController {
    @Autowired
    private FatturaService fatturaService;

    @GetMapping("/me/fatture")
    public Page<Fattura> getFatture(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dataFattura") String sortBy,
            @RequestParam(required = false) Integer anno,
            @RequestParam(required = false) LocalDate dataFattura,
            @RequestParam(required = false) StatoFattura stato_fattura,
            @RequestParam(required = false) Double minImporto,
            @RequestParam(required = false) Double maxImporto,
            @RequestParam(required = false) Cliente cliente,
            @AuthenticationPrincipal Utente currentAuthenticatedUtente) {

        return fatturaService.getFatture(page, size, sortBy, anno, dataFattura, stato_fattura, minImporto, maxImporto, cliente, currentAuthenticatedUtente);
    }

    @GetMapping("me/{fatturaId}")
    public Fattura findById(@PathVariable UUID fatturaId, @AuthenticationPrincipal Utente currentAuthenticatedUtente) {
        return this.fatturaService.findById(fatturaId, currentAuthenticatedUtente);
    }

    @PostMapping("/me/clienti/{id_cliente}/fatture")
    @ResponseStatus(HttpStatus.CREATED)
    public Fattura save(@RequestBody @Validated FatturaDTO body, @PathVariable UUID cliente_id, BindingResult validationResult, @AuthenticationPrincipal Utente currentAuthenticatedUtente) throws Throwable {

        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati errori nel payload! " + message);
        }

        return this.fatturaService.save(body, cliente_id, currentAuthenticatedUtente);
    }

//    @PutMapping("/{fatturaId}")
//    public Fattura findByIdAndUpdate(@PathVariable UUID fatturaId, @RequestBody @Validated FatturaDTO body, BindingResult validationResult) {
//        if (validationResult.hasErrors()) {
//            validationResult.getAllErrors().forEach(System.out::println);
//            throw new BadRequestException("Ci sono stati errori nel payload!");
//        }
//        return this.fatturaService.findByIdAndUpdateStato(fatturaId, body);
//    }

    @DeleteMapping("/me/{fatturaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID fatturaId, @AuthenticationPrincipal Utente currentAuthenticatedUtente) {
        this.fatturaService.findByIdAndDelete(fatturaId, currentAuthenticatedUtente);
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
