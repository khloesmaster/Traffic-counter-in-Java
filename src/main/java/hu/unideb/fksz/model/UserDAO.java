package hu.unideb.fksz.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import hu.unideb.fksz.TrafficCounterLogger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserDAO {
	private final static String UNIDEBDB = "unidebdb";
	public final static String MONITOR_ROLE = "monitor";
	public final static String ADMIN_ROLE = "admin";
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

	public static boolean logInUser(User user) {
		entityManager.getTransaction().begin();

		TypedQuery<User> query =  entityManager.
				createQuery("Select u from User u where"
				+" user_name ='" + user.getName()+ "'", User.class);
		List<User> users = query.getResultList();

		for(int i = 0; i < users.size(); ++i) {
			System.out.println(users.get(i).getName()+ " " + users.get(i).getPassword());
		}
		System.out.println("User:" + user.getName() + " " + user.getPassword());
		if (users.size() > 0) {
			if (users.get(0).getPassword().equals(user.getPassword())) {
				if (users.get(0).getRole().equals(user.getRole())) {
					return true;
				} else {
					TrafficCounterLogger.warnMessage("User's role not correct!");
					return false;
				}
			} else {
				TrafficCounterLogger.warnMessage("Incorrect password!");
				return false;
			}
		}
		entityManager.flush();
		entityManager.getTransaction().commit();

		return false;
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

		TypedQuery<User> monitorsQuery = entityManager.createQuery("select u from User u"
				+ "where user_role='monitor'", User.class);

		List<User> monitors = monitorsQuery.getResultList();
		System.out.println(monitors.size());
		entityManager.getTransaction().commit();
		return monitors;
	}

	public static ObservableList<String> usersString() {
		List<User> users = UserDAO.users();

		List<String> usersString = new ArrayList<String>();
		for(int i = 0; i < users.size(); i++) {
			usersString.add(users.get(i).toString());
		}
		return FXCollections.observableArrayList(usersString);
	}
}
