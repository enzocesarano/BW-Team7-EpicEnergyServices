package team7.EpicEnergyServices.Entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team7.EpicEnergyServices.Entities.Enums.StatoFattura;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "fatture")
public class Fattura {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id_fattura;

    private LocalDate dataFattura;
    private double importo;

    @Enumerated(EnumType.STRING)
    private StatoFattura statoFattura;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    public Fattura(LocalDate dataFattura, double importo, StatoFattura statoFattura, Cliente cliente) {
        this.dataFattura = dataFattura;
        this.importo = importo;
        this.statoFattura = statoFattura;
        this.cliente = cliente;
    }

}
