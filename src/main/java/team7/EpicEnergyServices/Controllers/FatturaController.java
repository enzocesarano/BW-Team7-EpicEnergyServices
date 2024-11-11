package team7.EpicEnergyServices.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team7.EpicEnergyServices.Entities.Fattura;
import team7.EpicEnergyServices.Services.FatturaService;

@RestController
@RequestMapping("/fattura")
public class FatturaController {
    @Autowired
    private FatturaService fatturaService;

    @GetMapping
    public Page<Fattura> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(defaultValue = "id") String sortBy) {
        return this.fatturaService.findAll(page, size, sortBy);
    }

    @GetMapping("/{eventId}")
    public Event findById(@PathVariable UUID eventId) {
        return this.es.findById(eventId);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('GESTORE_EVENTI')")
    @ResponseStatus(HttpStatus.CREATED)
    public Event save(@RequestBody @Validated NewEventDTO body, UUID organizzatore_id, BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati errori nel payload! " + message);
        }

        return this.es.save(body, organizzatore_id);
    }

    @PutMapping("/{eventId}")
    @PreAuthorize("hasAuthority('GESTORE_EVENTI')")
    public Event findByIdAndUpdate(@PathVariable UUID eventId, @RequestBody @Validated NewEventDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Ci sono stati errori nel payload!");
        }
        return this.es.findByIdAndUpdate(eventId, body);
    }

    @DeleteMapping("/{eventId}")
    @PreAuthorize("hasAuthority('GESTORE_EVENTI')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID eventId) {
        this.es.findByIdAndDelete(eventId);
    }
}
