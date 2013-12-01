package de.cofinpro.acc;

import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;
import javax.ejb.Schedule;
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
public class SecurityRateGeneratorBean {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Resource(lookup = "jms/accConnectionFactory")
	private ConnectionFactory connectionFactory;

	@Resource(lookup = "jms/securities")
	private Topic topic;
	
	@Schedule(hour = "*", minute = "*", second = "0")
	public void generateSecurityRates() {
		System.out.println("SecurityRateGeneratorBean.generateSecurityRates()");
		
		TypedQuery<Security> query = entityManager.createNamedQuery("findAllSecurities", Security.class);
		List<Security> securities = query.getResultList();
		for (Security security : securities) {
			security.getRates().add(createNewRateFor(security));
			entityManager.persist(security);
		}

		notifyClients();
	}

	private Rate createNewRateFor(Security security) {
		Rate latestRate = security.getLatestRate();
		float rateChange = determineRateChange(latestRate.getRate());
		return new Rate(new Date(), rateChange);
	}

	private float determineRateChange(float latestRate) {
		Random random = new Random(System.currentTimeMillis() + (int) latestRate);
		int sign = 1;
		if(random.nextBoolean()) {
			sign = -1;
		}
		float rateChange = (latestRate * 0.1f) * sign * (random.nextInt(100) / 100f);
		return latestRate + rateChange;		
	}

	private void notifyClients() {
		System.out.println("SecurityRateGeneratorBean.notifyClients()");
		try {
			Connection connection = connectionFactory.createConnection();
			Session session = connection.createSession(true, 0);
			MessageProducer messageProducer = session.createProducer(topic);
			connection.start();
			messageProducer.send(session.createTextMessage("Securities updated."));
			messageProducer.close();
			session.close();
			connection.close();
			System.out.println("Sending successful!");
		} catch (JMSException e) {
			System.out.println(e);
		}
	}
	
}
