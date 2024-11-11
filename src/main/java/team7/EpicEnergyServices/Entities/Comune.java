package team7.EpicEnergyServices.Entities;

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
    private Provincia provincia;

    @OneToMany
    private List<Indirizzo> indirizzi;

    public Comune(String progressivoComune, String denominazione, Provincia provincia) {
        this.progressivoComune = progressivoComune;
        this.denominazione = denominazione;
        this.provincia = provincia;
    }
}
