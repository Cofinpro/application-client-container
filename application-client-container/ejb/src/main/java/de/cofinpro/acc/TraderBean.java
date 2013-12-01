package de.cofinpro.acc;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@Remote(Trader.class)
public class TraderBean implements Trader {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Depot buy(Depot depot, Security securityToBuy, int count) {
		depot.addBuyOrder(securityToBuy, count);
		Depot mergedDeport = entityManager.merge(depot);
		entityManager.persist(mergedDeport);
		return mergedDeport;
	}

}
