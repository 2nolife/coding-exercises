package iconsltd;

import java.util.HashMap;
import java.util.Map;

import static iconsltd.Outcome.OutcomeType.FAILURE;
import static iconsltd.Outcome.OutcomeType.SUCCESS;

public class RegistrationServiceImpl implements RegistrationService {

    private static Outcome nullUserOutcome = new Outcome(FAILURE, "User details cannot be NULL");
    private static Outcome invalidEmailOutcome = new Outcome(FAILURE, "Email is not valid");
    private static Outcome invalidPwdOutcome = new Outcome(FAILURE, "Passwrd is not valid");

    private Map<String, UserDetails> userMap = new HashMap<>(); // storage

    public Outcome registerUser(UserDetails userDetails) {
      Outcome outcome = null;
      if (userDetails == null) return nullUserOutcome;

      UserDetails ud = sanitise(userDetails);
      if (outcome == null && !isEmailValid(ud.getEmail())) outcome = invalidEmailOutcome;
      if (outcome == null && !isPwdValid(ud.getPassword())) outcome = invalidPwdOutcome;

      if (outcome == null && userExists(ud.getEmail())) {
        outcome = new Outcome(FAILURE, String.format("%s is already registered", ud.getEmail()));
      }

      if (outcome == null) {
        userMap.put(ud.getEmail(), ud);
        outcome = new Outcome(SUCCESS, String.format("%s is registered", ud.getEmail()));
      }

      return outcome;
    }

    private boolean userExists(String email) {
      return userMap.containsKey(email);
    }

    private UserDetails sanitise(UserDetails userDetails) {
      return userDetails.withEmail(userDetails.getEmail().toLowerCase());
    }

    private boolean isEmailValid(String email) {
      String emailRegEx = "[a-z0-9]+@[[a-z0-9]]+\\.[a-z]{2,3}";
      return email != null && email.matches(emailRegEx);
    }

    private boolean isPwdValid(String pwd) {
      String pwdRegEx = "[a-zA-Z0-9!$]{8,}";
      return pwd != null && pwd.matches(pwdRegEx) && "!$".chars().anyMatch(c -> pwd.indexOf(c) != -1);
    }
}
