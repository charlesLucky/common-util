public class User {
	String name;
	String age;
	String gender;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public User(String name, String age, String gender) {
		super();
		this.name = name;
		this.age = age;
		this.gender = gender;
	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

}
