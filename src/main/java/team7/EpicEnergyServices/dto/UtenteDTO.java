package team7.EpicEnergyServices.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UtenteDTO(
        @NotEmpty(message = "Lo username è obbligatorio!")
        @Size(min = 2, max = 40, message = "Lo user deve essere compreso tra 2 e 40 caratteri!")
        String username,
        @NotEmpty(message = "Il nome è obbligatorio!")
        @Size(min = 2, max = 40, message = "Il nome deve essere compreso tra 2 e 40 caratteri!")
        String nome,
        @NotEmpty(message = "Il cognome proprio è obbligatorio!")
        @Size(min = 2, max = 40, message = "Il cognome deve essere compreso tra 2 e 40 caratteri!")
        String cognome,
        @NotEmpty(message = "L'email è un campo obbligatorio!")
        @Email(message = "L'email inserita non è un'email valida")
        String email,
        @NotEmpty(message = "password obbligatoria")
        String password) {
}
