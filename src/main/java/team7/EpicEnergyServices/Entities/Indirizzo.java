package team7.EpicEnergyServices.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team7.EpicEnergyServices.Entities.Enums.TipoIndirizzo;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "indirizzi")
@JsonIgnoreProperties({"id_indirizzo"})
public class Indirizzo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id_indirizzo;

    private String via;
    private String civico;
    private String localita;
    private String cap;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    @JsonBackReference
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_comune")
    @JsonBackReference
    private Comune comune;

    @Enumerated(EnumType.STRING)
    private TipoIndirizzo tipoSede;

    @JsonCreator
    public Indirizzo(String via, String civico, String localita, String cap, Comune comune, TipoIndirizzo tipoSede) {
        this.via = via;
        this.civico = civico;
        this.localita = localita;
        this.cap = cap;
        this.comune = comune;
        this.tipoSede = tipoSede;
    }
}
