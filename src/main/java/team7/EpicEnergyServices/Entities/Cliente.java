package team7.EpicEnergyServices.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team7.EpicEnergyServices.Entities.Enums.TipoCliente;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "clienti")
@JsonIgnoreProperties({"id_cliente", "fatturatoAnnuale", "utente", "fatture"})
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id_cliente;

    @Column(name = "ragione_sociale")
    private String ragioneSociale;
    private String partita_iva;
    private String email;

    @Setter(AccessLevel.NONE)
    private LocalDate dataInserimento;
    private LocalDate dataUltimoContatto;
    private double fatturatoAnnuale;
    private String pec;
    private String telefono;
    private String emailContatto;
    private String nomeContatto;
    private String cognomeContatto;
    private String telefonoContatto;
    private String logoAziendale;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Indirizzo> sedi;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Fattura> fatture;

    @ManyToOne
    @JoinColumn(name = "id_utente")
    private Utente utente;

    @Enumerated(EnumType.STRING)
    private TipoCliente tipoCliente;

    public Cliente(String ragioneSociale, String partita_iva, String email, String pec, String telefono, String emailContatto, String nomeContatto, String cognomeContatto, String telefonoContatto, List<Indirizzo> sedi) {
        this.ragioneSociale = ragioneSociale;
        this.partita_iva = partita_iva;
        this.email = email;
        this.pec = pec;
        this.telefono = telefono;
        this.emailContatto = emailContatto;
        this.nomeContatto = nomeContatto;
        this.cognomeContatto = cognomeContatto;
        this.telefonoContatto = telefonoContatto;
        this.sedi = sedi;
        this.dataInserimento = LocalDate.now();
        this.fatturatoAnnuale = 0;
    }
}
