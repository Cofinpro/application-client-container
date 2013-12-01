package de.cofinpro.acc;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@SuppressWarnings("serial")
@NamedQuery(name="eMailAddressUsed", query="SELECT COUNT(d) FROM Depot d WHERE d.eMailAddress = :eMailAddress")
public class Depot implements Serializable {

	@Id
	@GeneratedValue
	private Long id;
	
	private String eMailAddress;
	private String password;

	private BigDecimal balance;
	
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE })
	private List<Order> orders = new ArrayList<Order>();
	
	@SuppressWarnings("unused")
	private Depot() {
		// For JPA only
	}
	
	public Depot(String eMailAddress, String password) {
		this.eMailAddress = eMailAddress;
		this.password = password;
		balance = new BigDecimal("25000");
	}

	public String geteMailAddress() {
		return eMailAddress;
	}

	public String getPassword() {
		return password;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void addBuyOrder(Security securityToBuy, int count) {
		orders.add(new Order(securityToBuy, count, securityToBuy.getLatestRate()));
		BigDecimal rate = new BigDecimal(Float.toString(securityToBuy.getLatestRate().getRate()));
		BigDecimal orderValue = rate.multiply(new BigDecimal(Integer.toString(count)));
		balance = balance.subtract(orderValue);
	}
	
}
