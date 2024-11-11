package team7.EpicEnergyServices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    public NotFoundException(UUID id) {
        super("il record con id " + id + " non è stato trovato");
    }
    public NotFoundException(String message) {
        super(message);
    }
}
