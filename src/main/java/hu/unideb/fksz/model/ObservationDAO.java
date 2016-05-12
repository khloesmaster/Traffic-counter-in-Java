package hu.unideb.fksz.model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ObservationDAO {
	
	private static String UNIDEBDB = "unidebdb";
	private static EntityManager entityManager;
	private static EntityManagerFactory entityManagerFactory;
	
	static {
		entityManagerFactory = Persistence.createEntityManagerFactory(UNIDEBDB);
		entityManager = entityManagerFactory.createEntityManager();
	}
	
	public static void insertObservation(Observation observation) {
		entityManager.getTransaction().begin();
		entityManager.persist(observation);
		entityManager.getTransaction().commit();
	}
	
	public static void removeObservation(Observation observation) {
		
	}
	
} 
