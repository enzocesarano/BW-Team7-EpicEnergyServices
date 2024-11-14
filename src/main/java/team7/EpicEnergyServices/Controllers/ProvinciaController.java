package team7.EpicEnergyServices.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team7.EpicEnergyServices.Entities.Comune;
import team7.EpicEnergyServices.Services.ProvinciaService;

@RestController
@RequestMapping("/province")
public class ProvinciaController {

    @Autowired
    private ProvinciaService provinciaService;

    @PostMapping("/importa-province")
    public ResponseEntity<String> importaProvince(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Il file è vuoto.");
        }

        try {
            provinciaService.importaProvince(file);
            return ResponseEntity.status(HttpStatus.OK).body("File importato con successo.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore: " + e.getMessage());
        }
    }

    @GetMapping("/{nome}")
    @ResponseStatus(HttpStatus.OK)
    public Page<Comune> findAllByNome(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "denominazione") String sortBy,
            @PathVariable String nome
    ) {
        return provinciaService.findAllByProvincia_Nome(page, size, sortBy, nome);
    }

}
