package team7.EpicEnergyServices.dto;

import jakarta.validation.constraints.*;
import team7.EpicEnergyServices.Entities.Enums.TipoCliente;
import team7.EpicEnergyServices.Entities.Indirizzo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ClienteDTO(
        @NotEmpty(message = "La ragione sociale è obbligatoria.")
        @Size(min = 3, max = 100, message = "La ragione sociale deve contenere dai 3 ai 100 caratteri.")
        String ragione_sociale,

        @NotEmpty(message = "La partita IVA è obbligatoria.")
        @Pattern(regexp = "\\d{11}", message = "La partita IVA deve contenere esattamente 11 cifre.")
        String partita_iva,

        @NotEmpty(message = "L'email è obbligatoria.")
        @Email(message = "Inserisci un'email valida.")
        String email,

        @NotNull(message = "La data dell'ultimo contatto è obbligatoria.")
        LocalDate dataUltimoContatto,

        @NotNull(message = "Il fatturato annuale è obbligatorio.")
        @DecimalMin(value = "0.0")
        BigDecimal fatturatoAnnuale,

        @NotEmpty(message = "La PEC è obbligatoria.")
        @Email(message = "Inserisci una PEC valida.")
        String pec,

        @NotEmpty(message = "Il numero di telefono è obbligatorio.")
        @Pattern(regexp = "\\+?\\d{7,15}", message = "Inserisci un numero di telefono valido, con prefisso internazionale se necessario.")
        String telefono,

        @NotEmpty(message = "L'email di contatto è obbligatoria.")
        @Email(message = "Inserisci un'email di contatto valida.")
        String emailContatto,

        @NotEmpty(message = "Il nome del contatto è obbligatorio.")
        @Size(min = 2, max = 50, message = "Il nome del contatto deve contenere dai 2 ai 50 caratteri.")
        String nomeContatto,

        @NotEmpty(message = "Il cognome del contatto è obbligatorio.")
        @Size(min = 2, max = 50, message = "Il cognome del contatto deve contenere dai 2 ai 50 caratteri.")
        String cognomeContatto,

        @NotEmpty(message = "Il telefono del contatto è obbligatorio.")
        @Pattern(regexp = "\\+?\\d{7,15}", message = "Inserisci un numero di telefono di contatto valido.")
        String telefonoContatto,

        @NotNull(message = "La sede legale è obbligatoria.")
        Indirizzo sedeLegale,

        @NotEmpty(message = "La lista delle sedi operative non può essere vuota.")
        List<Indirizzo> sedeOperativa,

        @NotNull(message = "Il tipo cliente è obbligatorio.")
        TipoCliente tipoCliente) {
}
