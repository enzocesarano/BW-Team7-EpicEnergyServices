package team7.EpicEnergyServices.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team7.EpicEnergyServices.Entities.Enums.StatoFattura;
import team7.EpicEnergyServices.Entities.Fattura;
import team7.EpicEnergyServices.Exceptions.BadRequestException;
import team7.EpicEnergyServices.Services.FatturaService;
import team7.EpicEnergyServices.dto.FatturaDTO;

import java.time.LocalDate;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/fattura")
public class FatturaController {
    @Autowired
    private FatturaService fatturaService;

    @GetMapping
    public Page<Fattura> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return this.fatturaService.findAll(page, size);
    }

    @GetMapping("/{id_cliente}/fatture")
    public Page<Fattura> findByCliente(@PathVariable UUID id_cliente,
                                       @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size
    ) {
        return this.fatturaService.findbyCliente(page, size, id_cliente);
    }

    @GetMapping("/anno")
    public Page<Fattura> findByAnno(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam int anno) {
        return this.fatturaService.findByAnno(page, size, anno);
    }

    @GetMapping("/dataFattura")
    public Page<Fattura> getFattureByData(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam LocalDate data) {
        return fatturaService.findbyDataFattura(page, size, sortBy, data);
    }

    @GetMapping("/stato")
    public Page<Fattura> getFattureByStato(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) StatoFattura statoFattura) {
        return this.fatturaService.findFattureByStatoFattura(statoFattura, page, size);

    }

    @GetMapping("/importo")
    public Page<Fattura> getFattureByImportoRange(
            @RequestParam double minImporto,
            @RequestParam double maxImporto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        return fatturaService.findByImporto(page, size, sortBy, minImporto, maxImporto);
    }

    @GetMapping("/{fatturaId}")
    public Fattura findById(@PathVariable UUID fatturaId) {
        return this.fatturaService.findById(fatturaId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Fattura save(@RequestBody @Validated FatturaDTO body, UUID cliente_id, BindingResult validationResult) throws Throwable {

        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati errori nel payload! " + message);
        }

        return this.fatturaService.save(body, cliente_id);
    }

    @PutMapping("/{fatturaId}")
    public Fattura findByIdAndUpdate(@PathVariable UUID fatturaId, @RequestBody @Validated FatturaDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Ci sono stati errori nel payload!");
        }
        return this.fatturaService.findByIdAndUpdateStato(fatturaId, body);
    }

    @DeleteMapping("/{fatturaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID fatturaId) {
        this.fatturaService.findByIdAndDelete(fatturaId);
    }
}
