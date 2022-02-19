package simplehttp.app;

public class User {
	private Long id;
	private String name;
	private String password;
	
	public User() {
	}
	
	public User(Long id, String name, String password) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
	}
	public Long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getPassword() {
		return password;
	}
	
	
}
