package de.cofinpro.acc;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ORDERS")
public class Order implements Serializable {

	@Id
	@GeneratedValue
	private Long id;
	
	private Security security;
	private int count;
	private Rate rate;

	@SuppressWarnings("unused")
	private Order() {
	}
	
	public Order(Security security, int count, Rate rate) {
		this.security = security;
		this.count = count;
		this.rate = rate;
	}

	public Security getSecurity() {
		return security;
	}

	public int getCount() {
		return count;
	}

	public Rate getRate() {
		return rate;
	}
}
