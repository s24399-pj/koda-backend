package pl.pjwstk.kodabackend.security.user;

import io.swagger.v3.oas.annotations.tags.Tag;
import pl.pjwstk.kodabackend.security.user.model.AppUserDto;

import java.security.Principal;

@Tag(name = "User Internal Management", description = "Internal user management API")
public interface UserInternalController {

    AppUserDto getUserDetails(Principal principal);
}
