package de.cofinpro.acc;

import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@Remote(SecurityStorage.class)
public class SecurityStorageBean implements SecurityStorage {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Security> getAllSecurities() {
		return entityManager.createNamedQuery("findAllSecurities", Security.class).getResultList();
	}
	
}
