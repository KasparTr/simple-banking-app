package com.danabijak.demo.banking.domain.users.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.danabijak.demo.banking.domain.transactions.entity.TransactionalEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


//@NoArgsConstructor
//@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class User extends TransactionalEntity{
	
    private String username;
    private String password;
    @OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    private List<Role> roles;
    private boolean active;
    
    
    User(){
    	super("unk");
    }
    
	public User(String username, String password, List<Role> roles, boolean active) {
		super(username);
		this.username = username;
		this.password = password;
		this.roles = roles;
		this.active = active;
	}
	
	public User(String username, String password){
		super(username);
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
    
    

}