package ivanovvasil.u5d5w3Project.payloadsDTO;

import ivanovvasil.u5d5w3Project.entities.Prenotation;
import ivanovvasil.u5d5w3Project.entities.User;

import java.util.List;

public record UserProfileResponseDTO(User user, List<Prenotation> prenotation_list)
        implements ProfileResponseDTO {
}
