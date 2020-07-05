package com.vikas.springboot.model;

public class Leader {

	private String lastName;
	private String firstName;

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Leader(String firstName, String lastName) {
		super();
		this.lastName = lastName;
		this.firstName = firstName;
	}

	public Leader() {
		super();
	}

	@Override
	public String toString() {
		return "Leader [lastName=" + lastName + ", firstName=" + firstName + "]";
	}

}
