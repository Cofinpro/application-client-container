package de.cofinpro.acc;

public interface Trader {

	Depot buy(Depot depot, Security securityToBuy, int count);
	
}
