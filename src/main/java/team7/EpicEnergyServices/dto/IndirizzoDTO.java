package team7.EpicEnergyServices.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import team7.EpicEnergyServices.Entities.Enums.TipoIndirizzo;

public record IndirizzoDTO(


        @NotEmpty(message = "La via è obbligatoria!")
        @Size(min = 3, max = 50, message = "La via deve contenere dai 3 ai 50 caratteri")
        String via,

        @NotEmpty(message = "Il civico è obbligatorio!")
        @Size(max = 10, message = "Il civico non può superare i 10 caratteri")
        String civico,

        @NotEmpty(message = "La località è obbligatoria!")
        @Size(min = 3, max = 50, message = "La localita deve contenere tra i 3 e i 50 caratteri")
        String localita,

        @NotEmpty(message = "Il CAP è obbligatorio!")
        @Size(min = 5, max = 5, message = "Il CAP può essere SOLO di 5 numeri")
        String cap,

        @NotNull(message = "Il comune è obbligatorio!")
        String comune,

        @NotNull(message = "Il tipo di sede è obbligatorio")
        TipoIndirizzo sede
) {

}
