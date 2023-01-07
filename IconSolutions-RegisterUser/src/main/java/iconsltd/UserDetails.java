package iconsltd;

/**
 * The details of the user registration.
 */
public class UserDetails {

	private final String name;
	private final String email;
	private String password;

	public UserDetails(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public UserDetails withEmail(String email) {
		return new UserDetails(this.name, email, this.password);
	}

}
