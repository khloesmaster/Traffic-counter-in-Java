package hu.unideb.fksz.model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import hu.unideb.fksz.TrafficCounterLogger;

public class UserDAO {
	private static String UNIDEBDB = "unidebdb";
	private static EntityManager entityManager;
	private static EntityManagerFactory entityManagerFactory;

	static {
		entityManagerFactory = Persistence.createEntityManagerFactory(UNIDEBDB);
		entityManager = entityManagerFactory.createEntityManager();
	}

	public static void close() {
		entityManager.close();
		entityManagerFactory.close();
	}

	public static void registerUser(User user) {
		entityManager.getTransaction().begin();
		if (!entityManager.contains(user)) {
			if (user.getRole().equals("admin")) {
				TrafficCounterLogger.warnMessage("Can't register an admin!");
			} else {
				entityManager.persist(user);
			}
		} else {
			TrafficCounterLogger.warnMessage("User already registered!");
		}
		entityManager.getTransaction().commit();
	}

	public static void removeUser(User user) {
		entityManager.getTransaction().begin();
		if (entityManager.contains(user)) {
			if (!user.getRole().equals("admin")) {
				entityManager.remove(user);
			} else {
				TrafficCounterLogger.warnMessage("Can't delete an admin!");
			}
		} else {
			TrafficCounterLogger.warnMessage("Can't delete nonexistent user!");
		}
		entityManager.getTransaction().commit();
	}
	
	public static List<User> users() {
		entityManager.getTransaction().begin();
		
		Query monitorsQuery = entityManager.createQuery("select * from traffic_counter_users"
				+ "where user_role <> admin");
		
		List<User> monitors = (List<User>)monitorsQuery.getResultList();
		entityManager.getTransaction().commit();
		return monitors;
	}
}
