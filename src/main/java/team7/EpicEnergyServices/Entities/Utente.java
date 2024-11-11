package team7.EpicEnergyServices.Entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team7.EpicEnergyServices.Entities.Enums.TipoUtente;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "utenti")
public class Utente {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id_utente;

    private String username;
    private String email;
    private String password;
    private String nome;
    private String cognome;
    private String avatar;

    @Enumerated(EnumType.STRING)
    private TipoUtente tipoUtente;

    public Utente(String username, String email, String password, String nome, String cognome) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
        this.tipoUtente = TipoUtente.USER;
    }
}
