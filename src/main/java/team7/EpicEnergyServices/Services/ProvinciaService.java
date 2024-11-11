package team7.EpicEnergyServices.Services;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team7.EpicEnergyServices.Entities.Provincia;
import team7.EpicEnergyServices.Repositories.ProvinciaRepository;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;


@Service
public class ProvinciaService {

    @Autowired
    private ProvinciaRepository provinciaRepository;

    private String csvFilePath = "province-italiane.csv";

    public void importaProvince(MultipartFile file) {
        try {
            File csvFile = new File(csvFilePath);

            List<String> lines = FileUtils.readLines(csvFile, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (line.startsWith("Sigla;Provincia;Regione")) {
                    continue;
                }
                String[] dati = line.split(";");
                Provincia provincia = new Provincia();
                provincia.setSigla(dati[0]);
                provincia.setNome(dati[1]);
                provincia.setRegione(dati[2]);
                provinciaRepository.save(provincia);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
