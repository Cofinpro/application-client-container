package de.cofinpro.acc;

import javax.annotation.Resource;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
@Remote(DepotStorage.class)
public class DepotStorageBean implements DepotStorage {

	@PersistenceContext
	private EntityManager entityManager;

	@Resource(lookup = "jms/accConnectionFactory")
	private ConnectionFactory connectionFactory;

	@Resource(lookup = "jms/depots")
	private Topic topic;

	@Override
	public Depot register(String eMailAddress, String password) {
		Depot depot = new Depot(eMailAddress, password);
		entityManager.persist(depot);
		notifyClients(depot);
		return depot;
	}

	private void notifyClients(Depot depot) {
		System.out.println("DepotStorageBean.notifyClients()");
		try {
			Connection connection = connectionFactory.createConnection();
			Session session = connection.createSession(true, 0);
			MessageProducer messageProducer = session.createProducer(topic);
			connection.start();
			messageProducer.send(session.createObjectMessage(depot));
			System.out.println("Sending successful!");
		} catch (JMSException e) {
			System.out.println(e);
		}
	}

	@Override
	public boolean eMailAddressUsed(String eMailAddress) {
		TypedQuery<Number> namedQuery = entityManager.createNamedQuery("eMailAddressUsed", Number.class);
		namedQuery.setParameter("eMailAddress", eMailAddress);
		Number count = namedQuery.getSingleResult();
		return count.intValue() > 0;
	}

}
