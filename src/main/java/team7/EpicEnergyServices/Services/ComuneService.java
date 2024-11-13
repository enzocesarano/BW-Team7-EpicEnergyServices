package team7.EpicEnergyServices.Services;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team7.EpicEnergyServices.Entities.Comune;
import team7.EpicEnergyServices.Exceptions.NotFoundException;
import team7.EpicEnergyServices.Repositories.ComuneRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class ComuneService {

    @Autowired
    private ComuneRepository comuneRepository;

    @Autowired
    private ProvinciaService provinciaService;

    public void importaComuni(MultipartFile file) throws IOException {
        try {
            File tempFile = File.createTempFile("comuni", ".csv");
            file.transferTo(tempFile);

            List<String> lines = FileUtils.readLines(tempFile, "UTF-8");

            String codiceProvinciaCorrente = "";
            int progressivoComune = 1;

            boolean firstLine = true;
            for (String line : lines) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] dati = line.split(";");
                String codiceProvincia = dati[0].trim();
                String progressivo = dati[1].trim();
                String denominazione = dati[2].trim();


                if (!codiceProvincia.equals(codiceProvinciaCorrente)) {
                    codiceProvinciaCorrente = codiceProvincia;
                    progressivoComune = 1;
                }

                if (progressivo.equals("#RIF!")) {
                    progressivo = String.valueOf(progressivoComune);
                }

                progressivoComune++;

                Comune comune = new Comune();
                comune.setCodiceProvincia(codiceProvincia);
                comune.setProgressivoComune(progressivo);
                comune.setDenominazione(denominazione);

                comuneRepository.save(comune);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Comune findById(UUID id_comune) {
        return comuneRepository.findById(id_comune)
                .orElseThrow(() -> new NotFoundException("Cliente con ID " + id_comune + " non trovato"));
    }

}
