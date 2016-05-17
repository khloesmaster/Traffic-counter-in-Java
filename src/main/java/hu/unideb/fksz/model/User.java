package hu.unideb.fksz.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "traffic_counter_users")
public class User {

	@Id
	@Column(name = "user_id")
	@SequenceGenerator(name = "IdGenerator", sequenceName = "user_id_generator", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IdGenerator")
	private int id;

	@Column(name = "user_name")
	private String name;

	@Column(name = "user_password")
	private String password;

	@Column(name = "user_role")
	private String role;


	@Override
	public String toString() {
		return getName();
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public User(String name, String password, String role) {
		this.name = name;
		this.password = password;
		this.role = role;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User() {
	}
}
