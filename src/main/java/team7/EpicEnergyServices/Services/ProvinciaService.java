package team7.EpicEnergyServices.Services;

import org.apache.commons.io.FileUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team7.EpicEnergyServices.Entities.Comune;
import team7.EpicEnergyServices.Entities.Provincia;
import team7.EpicEnergyServices.Exceptions.NotFoundException;
import team7.EpicEnergyServices.Repositories.ComuneRepository;
import team7.EpicEnergyServices.Repositories.ProvinciaRepository;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;


@Service
public class ProvinciaService {

    @Autowired
    private ProvinciaRepository provinciaRepository;

    @Autowired
    private ComuneRepository comuneRepository;

    public void importaProvince(MultipartFile file) throws IOException {
        try {
            File tempFile = File.createTempFile("province", ".csv");
            file.transferTo(tempFile);

            List<String> lines = FileUtils.readLines(tempFile, StandardCharsets.UTF_8);
            boolean firstLine = true;
            for (String line : lines) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] dati = line.split(";");
                String sigla = dati[0].trim();
                String provincia = dati[1].trim();
                String regione = dati[2].trim();

                if (sigla.matches("(?i).*roma.*")) {
                    sigla = "RM";
                }

                if (provincia.matches("(?i).*verbania.*")) {
                    provincia = "Verbano-Cusio-Ossola";
                }

                if (provincia.matches("(?i).*Monza-Brianza.*")) {
                    provincia = "Monza e della Brianza";
                }

                if (provincia.matches("(?i).*Reggio-Emilia.*")) {
                    provincia = "Reggio nell'Emilia";
                }

                if (provincia.matches("(?i).*La-Spezia.*")) {
                    provincia = "La Spezia";
                }

                if (provincia.matches("(?i).*forli-cesena.*")) {
                    provincia = "Forlì-Cesena";
                }

                if (provincia.matches("(?i).*pesaro-urbino.*")) {
                    provincia = "Pesaro e Urbino";
                }

                if (provincia.matches("(?i).*ascoli-piceno.*")) {
                    provincia = "Ascoli Piceno";
                }

                if (provincia.matches("(?i).*reggio-calabria.*")) {
                    provincia = "Reggio Calabria";
                }

                if (provincia.matches("(?i).*vibo-valentia.*")) {
                    provincia = "Vibo Valentia";
                }

                Provincia provinciaEntity = new Provincia();
                provinciaEntity.setSigla(sigla);
                provinciaEntity.setNome(provincia);
                provinciaEntity.setRegione(regione);
                provinciaRepository.save(provinciaEntity);
            }

            Provincia sudSardegna = new Provincia();
            sudSardegna.setSigla("SU");
            sudSardegna.setNome("Sud Sardegna");
            sudSardegna.setRegione("Sardegna");
            provinciaRepository.save(sudSardegna);

        } catch (IOException e) {
            e.printStackTrace();
            throw new BadRequestException("Errore nel caricamento del file CSV.");
        }
    }

    public Provincia findByNome(String nome) {
        return this.provinciaRepository.findByNome(nome).orElseThrow(() -> new NotFoundException("La provincia non è stata trovata!"));
    }


    public Page<Comune> findAllByProvincia_Nome(int page, int size, String sortBy, String nomeProvincia) {
        if (size > 10) size = 10;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return comuneRepository.findAllByProvincia_Nome(nomeProvincia, pageable);
    }

}



