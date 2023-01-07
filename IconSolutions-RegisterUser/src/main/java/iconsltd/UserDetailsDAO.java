package iconsltd;

import java.util.Optional;

public interface UserDetailsDAO {

    public void storeUserDetails(UserDetails userDetails);
    
    public Optional<UserDetails> getUserDetails(String email);
}
