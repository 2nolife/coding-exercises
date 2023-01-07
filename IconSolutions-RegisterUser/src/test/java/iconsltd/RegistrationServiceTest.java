package iconsltd;

import org.junit.jupiter.api.Test;

import static iconsltd.Outcome.OutcomeType.FAILURE;
import static iconsltd.Outcome.OutcomeType.SUCCESS;
import static org.junit.jupiter.api.Assertions.*;

public class RegistrationServiceTest {

    private RegistrationService rs = new RegistrationServiceImpl();
    private UserDetails properUser = new UserDetails("John Smith", "aa@aa.com", "MyPassrord!");
    private UserDetails properUser2 = new UserDetails("John Smith", "JohnSmith@aa.com", "MyPassrord$");
    private UserDetails invalidEmailUser = new UserDetails("John Smith", "aa@aa", "MyPassrord!");
    private UserDetails invalidPwdUser = new UserDetails("John Smith", "aa@aa.com", "MyPassrord");

    @Test
    public void registerUser() throws Exception {

    }

    @Test
    public void registerServiceShouldReturnNonForAnyArgument() throws Exception {
        assertNotNull(rs.registerUser(null));
    }

    @Test
    public void registerServiceShouldReturnFailedOutcomeForInvalidCases() throws Exception {
        assertEquals(new Outcome(FAILURE, "Email is not valid"), rs.registerUser(invalidEmailUser));
        assertEquals(new Outcome(FAILURE, "Passwrd is not valid"), rs.registerUser(invalidPwdUser));
    }

    @Test
    public void registerServiceShouldReturnSuccessOutcomeForValidCases() throws Exception {
        assertEquals(new Outcome(SUCCESS, "aa@aa.com is registered"), rs.registerUser(properUser));
        assertEquals(new Outcome(SUCCESS, "johnsmith@aa.com is registered"), rs.registerUser(properUser2));
    }

    @Test
    public void registerServiceShouldReturnFailedForDoubleRegistration() throws Exception {
        assertEquals(new Outcome(SUCCESS, "johnsmith@aa.com is registered"), rs.registerUser(properUser2));
        assertEquals(new Outcome(FAILURE, "johnsmith@aa.com is already registered"), rs.registerUser(properUser2));
    }



}
