package team7.EpicEnergyServices.Entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "indirizzi")
public class Indirizzo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id_indirizzo;

    private String via;
    private String civico;
    private String località;
    private String cap;

    @ManyToOne
    private Comune comune;

    public Indirizzo(String via, String civico, String località, String cap, Comune comune) {
        this.via = via;
        this.civico = civico;
        this.località = località;
        this.cap = cap;
        this.comune = comune;
    }
}
