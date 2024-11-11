package team7.EpicEnergyServices.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import team7.EpicEnergyServices.Services.ComuneService;

@RestController
@RequestMapping("/importa-comuni")
public class ComuneController {

    @Autowired
    private ComuneService comuneService;

    @PostMapping
    public ResponseEntity<String> importaComuni(@RequestParam("file") MultipartFile file) {
        try {
            comuneService.importaComuni(file);
            return ResponseEntity.ok("Comuni importati con successo");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore durante l'importazione dei comuni.");
        }
    }

}
