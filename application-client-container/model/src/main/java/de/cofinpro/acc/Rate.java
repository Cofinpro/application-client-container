package de.cofinpro.acc;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@SuppressWarnings("serial")
public class Rate implements Serializable {

	@Id
	@GeneratedValue
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	private float rate;

	@SuppressWarnings("unused")
	private Rate() {
		// For JPA only
	}
	
	public Rate(Date date, float rate) {
		this.date = date;
		this.rate = rate;
	}

	public Date getDate() {
		return date;
	}

	public float getRate() {
		return rate;
	}

	
	
}
