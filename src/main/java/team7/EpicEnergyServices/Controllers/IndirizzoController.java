package team7.EpicEnergyServices.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team7.EpicEnergyServices.Entities.Indirizzo;
import team7.EpicEnergyServices.Services.IndirizzoService;
import team7.EpicEnergyServices.dto.IndirizzoDTO;

import java.util.UUID;

@RestController
@RequestMapping("/indirizzi")
public class IndirizzoController {

    @Autowired
    private IndirizzoService indirizzoService;

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Indirizzo> updateIndirizzo(@PathVariable UUID id, @RequestBody IndirizzoDTO indirizzoDTO) {
        Indirizzo updatedIndirizzo = indirizzoService.updateIndirizzo(id, indirizzoDTO);
        return ResponseEntity.ok(updatedIndirizzo);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteIndirizzo(@PathVariable UUID id) {
        indirizzoService.deleteIndirizzo(id);
        return ResponseEntity.noContent().build();
    }
}
