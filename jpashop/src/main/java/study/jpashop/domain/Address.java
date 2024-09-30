package study.jpashop.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Address {

	@Column(length = 20)
	private String city;
	
	@Column(length = 10)
	private String street;
	
	@Column(length = 5)
	private String zipcode;
	
	public String fullAddress () {
		return getCity() + " - " + getStreet() + " - " + getZipcode();
	}
	
	public String getCity() {
		return city;
	}
	public String getStreet() {
		return street;
	}
	public String getZipcode() {
		return zipcode;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getCity(), getStreet(), getZipcode());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		return Objects.equals(getCity(), other.getCity()) && Objects.equals(getStreet(), other.getStreet())
				&& Objects.equals(getZipcode(), other.getZipcode());
	}
	
}
