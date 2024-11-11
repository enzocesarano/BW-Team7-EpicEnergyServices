package team7.EpicEnergyServices.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import team7.EpicEnergyServices.Services.ProvinciaService;

@RestController
@RequestMapping("/importa-province")
public class ProvinciaController {

    @Autowired
    private ProvinciaService provinciaService;

    @PostMapping
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

}
