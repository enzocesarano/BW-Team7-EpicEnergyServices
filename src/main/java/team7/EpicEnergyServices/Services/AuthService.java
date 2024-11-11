package team7.EpicEnergyServices.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import team7.EpicEnergyServices.Entities.Utente;
import team7.EpicEnergyServices.Exceptions.UnauthorizedException;
import team7.EpicEnergyServices.Tools.JWT;
import team7.EpicEnergyServices.dto.UtenteLoginDTO;

@Service
public class AuthService {
    @Autowired
    private UtenteService userService;
    @Autowired
    private JWT jwt;
    @Autowired
    private PasswordEncoder bcryptencoder;

    public String checkCredenzialiAndToken(UtenteLoginDTO body){

        Utente userFound = this.userService.finByEmail(body.email());
        if (bcryptencoder.matches(body.password(), userFound.getPassword())){
            String accessToken = jwt.createToken(userFound);
            return accessToken;
        }else {
            throw new UnauthorizedException("credenziali inserite errate");
        }
    }

}
