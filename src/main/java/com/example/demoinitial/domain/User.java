package com.example.demoinitial.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table( name="user" )
public class User extends BaseEntity {


	@Column( unique=true, nullable=false )
	private String email;

	@Column( nullable=false )
	private String password;

	private String fullName;

	public boolean accountNonExpired;

	public boolean accountNonLocked;

	public boolean credentialsNonExpired;

	public boolean enabled;

	@ManyToMany( cascade = CascadeType.REFRESH, fetch = FetchType.EAGER )
	@JoinTable(
			name = "user_role",
			joinColumns = {@JoinColumn(name="user_id")},
			inverseJoinColumns = {@JoinColumn(name="role_id")}
	)
	private Set<Role> roles = new HashSet<Role>();

	public User() {}

	public User(String fullName, String email) {
		this.fullName = fullName;
		this.email = email;
		this.enabled = true;
		this.accountNonLocked = true;
		this.credentialsNonExpired = true;
		this.accountNonExpired = true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", password=" + password + "]";
	}

}
