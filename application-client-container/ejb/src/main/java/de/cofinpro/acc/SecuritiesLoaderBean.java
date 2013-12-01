package de.cofinpro.acc;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Startup
@Singleton
public class SecuritiesLoaderBean {

	@PersistenceContext
	private EntityManager entityManager;

	@PostConstruct
	public void loadSecurities() {
		Long securitiesCount = entityManager.createNamedQuery("countSecurities", Long.class).getSingleResult();
		if(securitiesCount > 0) {
			System.out.println("SecuritiesLoaderBean: Securities already exist.");
			return;
		}
		
		System.out.println("SecuritiesLoaderBean.loadSecurities()");
		Security security = new Security();
		security.setIsin("DE0123");
		security.getRates().add(new Rate(new Date(), 103.57f));
		entityManager.persist(security);

		System.out.println("Id: " + security.getId());
	}
	
}
