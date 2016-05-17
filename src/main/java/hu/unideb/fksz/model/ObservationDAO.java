package hu.unideb.fksz.model;

import java.util.List;

import javax.persistence.Query;

import hu.unideb.fksz.TrafficCounterLogger;

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
		entityManager.getTransaction().begin();
		if (entityManager.contains(observation)) {
			entityManager.remove(observation);
		} else {
			TrafficCounterLogger.warnMessage("Can't remove nonexistent observation!");
		}
		entityManager.getTransaction().commit();
	}

	public static void close() {
		entityManager.close();
		entityManagerFactory.close();
	}

	public static List<Observation> observationsOf(User user) {

		entityManager.getTransaction().begin();

		Query monitorIdQuery = entityManager
				.createQuery("select user_id from TRAFFIC_COUNTER_USERS" + " WHERE USER_NAME = " + user.getName());
		int monitorId = (int) monitorIdQuery.getSingleResult();

		Query observationsQuery = entityManager
				.createQuery("select obs.OBSERVATION_ID, obs.MONITOR_ID, obs.OBSERVED_VIDEO_TITLE, "
						+ "obs.TRAFFIC_COUNT, obs.OBSERVATION_DATE" + "from TRAFFIC_COUNTER_OBSERVATIONS obs"
						+ "where obs.MONITOR_ID =" + monitorId);
		List<Observation> observations = (List<Observation>) observationsQuery.getResultList();
		entityManager.getTransaction().commit();
		return observations;
	}
}
