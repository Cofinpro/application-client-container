package de.cofinpro.acc;

import javafx.application.Application;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;

public class Main {
	
	@EJB
	public static DepotStorage depotStorage;

	@EJB
	public static SecurityStorage securityStorage;

	@EJB
	public static Trader trader;
	
	@Resource(lookup = "jms/accConnectionFactory")
	public static TopicConnectionFactory connectionFactory;

	@Resource(lookup = "jms/depots")
	public static Topic topic;
	
	@Resource(lookup = "jms/securities")
	public static Topic securitiesTopic;
	
	public static void main(String[] args) {
		Application.launch(JavaFxMain.class, args);
	}

}
