package team7.EpicEnergyServices.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team7.EpicEnergyServices.Entities.Indirizzo;
import team7.EpicEnergyServices.Services.IndirizzoService;
import team7.EpicEnergyServices.dto.IndirizzoDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/indirizzi")
public class IndirizzoController {

    @Autowired
    private IndirizzoService indirizzoService;

    @PostMapping
    public ResponseEntity<Indirizzo> createIndirizzo(@RequestBody IndirizzoDTO indirizzoDTO) {
        Indirizzo indirizzo = indirizzoService.createIndirizzo(indirizzoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(indirizzo);
    }

    @GetMapping
    public ResponseEntity<List<Indirizzo>> getAllIndirizzi() {
        List<Indirizzo> indirizzi = indirizzoService.getAllIndirizzi();
        return ResponseEntity.ok(indirizzi);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Indirizzo> getIndirizzoById(@PathVariable UUID id) {
        Optional<Indirizzo> indirizzo = indirizzoService.getIndirizzoById(id);

        if (indirizzo.isPresent()) {
            return ResponseEntity.ok(indirizzo.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Indirizzo> updateIndirizzo(@PathVariable UUID id, @RequestBody IndirizzoDTO indirizzoDTO) {
        Indirizzo updatedIndirizzo = indirizzoService.updateIndirizzo(id, indirizzoDTO);
        return ResponseEntity.ok(updatedIndirizzo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIndirizzo(@PathVariable UUID id) {
        indirizzoService.deleteIndirizzo(id);
        return ResponseEntity.noContent().build();
    }
}
