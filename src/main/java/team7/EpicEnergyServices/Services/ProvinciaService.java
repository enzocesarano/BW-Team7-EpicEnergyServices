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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ProvinciaService {

    private static final Map<String, String> provinciaSiglaCorretta = new HashMap<>();

    static {
        provinciaSiglaCorretta.put("Agrigento", "AG");
        provinciaSiglaCorretta.put("Alessandria", "AL");
        provinciaSiglaCorretta.put("Ancona", "AN");
        provinciaSiglaCorretta.put("Aosta", "AO");
        provinciaSiglaCorretta.put("L'Aquila", "AQ");
        provinciaSiglaCorretta.put("Arezzo", "AR");
        provinciaSiglaCorretta.put("Ascoli-Piceno", "AP");
        provinciaSiglaCorretta.put("Asti", "AT");
        provinciaSiglaCorretta.put("Avellino", "AV");
        provinciaSiglaCorretta.put("Bari", "BA");
        provinciaSiglaCorretta.put("Barletta-Andria-Trani", "BT");
        provinciaSiglaCorretta.put("Belluno", "BL");
        provinciaSiglaCorretta.put("Benevento", "BN");
        provinciaSiglaCorretta.put("Bergamo", "BG");
        provinciaSiglaCorretta.put("Biella", "BI");
        provinciaSiglaCorretta.put("Bologna", "BO");
        provinciaSiglaCorretta.put("Bolzano", "BZ");
        provinciaSiglaCorretta.put("Brescia", "BS");
        provinciaSiglaCorretta.put("Brindisi", "BR");
        provinciaSiglaCorretta.put("Cagliari", "CA");
        provinciaSiglaCorretta.put("Caltanissetta", "CL");
        provinciaSiglaCorretta.put("Campobasso", "CB");
        provinciaSiglaCorretta.put("Carbonia Iglesias", "CI");
        provinciaSiglaCorretta.put("Caserta", "CE");
        provinciaSiglaCorretta.put("Catania", "CT");
        provinciaSiglaCorretta.put("Catanzaro", "CZ");
        provinciaSiglaCorretta.put("Chieti", "CH");
        provinciaSiglaCorretta.put("Como", "CO");
        provinciaSiglaCorretta.put("Cosenza", "CS");
        provinciaSiglaCorretta.put("Cremona", "CR");
        provinciaSiglaCorretta.put("Crotone", "KR");
        provinciaSiglaCorretta.put("Cuneo", "CN");
        provinciaSiglaCorretta.put("Enna", "EN");
        provinciaSiglaCorretta.put("Fermo", "FM");
        provinciaSiglaCorretta.put("Ferrara", "FE");
        provinciaSiglaCorretta.put("Firenze", "FI");
        provinciaSiglaCorretta.put("Foggia", "FG");
        provinciaSiglaCorretta.put("Forli-Cesena", "FC");
        provinciaSiglaCorretta.put("Frosinone", "FR");
        provinciaSiglaCorretta.put("Genova", "GE");
        provinciaSiglaCorretta.put("Gorizia", "GO");
        provinciaSiglaCorretta.put("Grosseto", "GR");
        provinciaSiglaCorretta.put("Imperia", "IM");
        provinciaSiglaCorretta.put("Isernia", "IS");
        provinciaSiglaCorretta.put("La-Spezia", "SP");
        provinciaSiglaCorretta.put("Latina", "LT");
        provinciaSiglaCorretta.put("Lecce", "LE");
        provinciaSiglaCorretta.put("Lecco", "LC");
        provinciaSiglaCorretta.put("Livorno", "LI");
        provinciaSiglaCorretta.put("Lodi", "LO");
        provinciaSiglaCorretta.put("Lucca", "LU");
        provinciaSiglaCorretta.put("Macerata", "MC");
        provinciaSiglaCorretta.put("Mantova", "MN");
        provinciaSiglaCorretta.put("Massa-Carrara", "MS");
        provinciaSiglaCorretta.put("Matera", "MT");
        provinciaSiglaCorretta.put("Medio Campidano", "VS");
        provinciaSiglaCorretta.put("Messina", "ME");
        provinciaSiglaCorretta.put("Milano", "MI");
        provinciaSiglaCorretta.put("Modena", "MO");
        provinciaSiglaCorretta.put("Monza-Brianza", "MB");
        provinciaSiglaCorretta.put("Napoli", "NA");
        provinciaSiglaCorretta.put("Novara", "NO");
        provinciaSiglaCorretta.put("Nuoro", "NU");
        provinciaSiglaCorretta.put("Ogliastra", "OG");
        provinciaSiglaCorretta.put("Olbia Tempio", "OT");
        provinciaSiglaCorretta.put("Oristano", "OR");
        provinciaSiglaCorretta.put("Padova", "PD");
        provinciaSiglaCorretta.put("Palermo", "PA");
        provinciaSiglaCorretta.put("Parma", "PR");
        provinciaSiglaCorretta.put("Pavia", "PV");
        provinciaSiglaCorretta.put("Perugia", "PG");
        provinciaSiglaCorretta.put("Pesaro-Urbino", "PU");
        provinciaSiglaCorretta.put("Pescara", "PE");
        provinciaSiglaCorretta.put("Piacenza", "PC");
        provinciaSiglaCorretta.put("Pisa", "PI");
        provinciaSiglaCorretta.put("Pistoia", "PT");
        provinciaSiglaCorretta.put("Pordenone", "PN");
        provinciaSiglaCorretta.put("Potenza", "PZ");
        provinciaSiglaCorretta.put("Prato", "PO");
        provinciaSiglaCorretta.put("Ragusa", "RG");
        provinciaSiglaCorretta.put("Ravenna", "RA");
        provinciaSiglaCorretta.put("Reggio-Calabria", "RC");
        provinciaSiglaCorretta.put("Reggio-Emilia", "RE");
        provinciaSiglaCorretta.put("Rieti", "RI");
        provinciaSiglaCorretta.put("Rimini", "RN");
        provinciaSiglaCorretta.put("Roma", "RM");
        provinciaSiglaCorretta.put("Rovigo", "RO");
        provinciaSiglaCorretta.put("Salerno", "SA");
        provinciaSiglaCorretta.put("Sassari", "SS");
        provinciaSiglaCorretta.put("Savona", "SV");
        provinciaSiglaCorretta.put("Siena", "SI");
        provinciaSiglaCorretta.put("Siracusa", "SR");
        provinciaSiglaCorretta.put("Sondrio", "SO");
        provinciaSiglaCorretta.put("Taranto", "TA");
        provinciaSiglaCorretta.put("Teramo", "TE");
        provinciaSiglaCorretta.put("Terni", "TR");
        provinciaSiglaCorretta.put("Torino", "TO");
        provinciaSiglaCorretta.put("Trapani", "TP");
        provinciaSiglaCorretta.put("Trento", "TN");
        provinciaSiglaCorretta.put("Treviso", "TV");
        provinciaSiglaCorretta.put("Trieste", "TS");
        provinciaSiglaCorretta.put("Udine", "UD");
        provinciaSiglaCorretta.put("Varese", "VA");
        provinciaSiglaCorretta.put("Venezia", "VE");
        provinciaSiglaCorretta.put("Verbania", "VB");
        provinciaSiglaCorretta.put("Vercelli", "VC");
        provinciaSiglaCorretta.put("Verona", "VR");
        provinciaSiglaCorretta.put("Vibo-Valentia", "VV");
        provinciaSiglaCorretta.put("Vicenza", "VI");
        provinciaSiglaCorretta.put("Viterbo", "VT");
    }

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

                if (provinciaSiglaCorretta.containsKey(provincia)) {
                    sigla = provinciaSiglaCorretta.get(provincia);
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



