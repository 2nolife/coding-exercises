package iconsltd;

/**
 * This service is used in registration of a user to our system.
 */
public interface RegistrationService {

    /**
     * Attempt to register the user.
     * @param userDetails the details of the user,
     * @return the outcome of the registration.
     */
    public Outcome registerUser(UserDetails userDetails);
}
