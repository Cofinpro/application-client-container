package de.cofinpro.acc;

public interface DepotStorage {

	Depot register(String eMailAddress, String password);

	boolean eMailAddressUsed(String eMailAddress);
}
