package de.cofinpro.acc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@SuppressWarnings("serial")
@NamedQueries({ @NamedQuery(name ="findAllSecurities", query ="SELECT s FROM Security s"),
	@NamedQuery(name ="countSecurities", query = "SELECT count(s) FROM Security s")})
public class Security implements Serializable {

	@Id
	@GeneratedValue
	private Long id;

	private String isin;

	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	private List<Rate> rates = new ArrayList<Rate>();

	public Security() {
	}

	public Long getId() {
		return id;
	}

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

	public List<Rate> getRates() {
		return rates;
	}

	public void setRates(List<Rate> rates) {
		this.rates = rates;
	}

	public Rate getLatestRate() {
		if(rates == null || rates.size() == 0) {
			return null;
		}
		return rates.get(rates.size() - 1);
	}
	
}
