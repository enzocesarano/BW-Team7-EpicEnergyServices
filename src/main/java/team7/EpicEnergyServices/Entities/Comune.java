package team7.EpicEnergyServices.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "comuni")
public class Comune {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id_comune;

    private String codiceProvincia;
    private String progressivoComune;
    private String denominazione;

    @ManyToOne
    @JoinColumn(name = "id_provincia")
    private Provincia provincia;

    @OneToMany(mappedBy = "comune")
    @JsonManagedReference
    private List<Indirizzo> indirizzi;

    public Comune(String progressivoComune, String denominazione, Provincia provincia) {
        this.progressivoComune = progressivoComune;
        this.denominazione = denominazione;
        this.provincia = provincia;
    }
}
