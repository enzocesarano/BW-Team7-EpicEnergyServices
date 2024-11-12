package team7.EpicEnergyServices.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import team7.EpicEnergyServices.Entities.Enums.StatoFattura;

import java.time.LocalDate;
import java.util.UUID;

public record FatturaDTO(
        @NotNull(message = "L'importo è obbligatorio")
        @DecimalMin(value = "1.00", message = "L'importo deve essere minimo 1")
        @DecimalMax(value = "9999999.99", message = "L'importo deve essere massimo di 9 cifre")
        Double importo,
        LocalDate data,
        StatoFattura statoFattura,
        UUID cliente_id) {
}
