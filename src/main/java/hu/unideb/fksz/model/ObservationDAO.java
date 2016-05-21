package hu.unideb.fksz.model;

/*
 * #%L
 * Traffic-counter
 * %%
 * Copyright (C) 2016 FKSZSoft
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

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

	public static void close() {
		entityManager.close();
		entityManagerFactory.close();
	}

	public static List<Observation> observationsOf(User user) {
		Query monitorIdQuery = entityManager
				.createQuery("select u from User u" + " WHERE USER_NAME='" + user.getName() + "'");
		int monitorId = ((User) monitorIdQuery.getSingleResult()).getId();

		TypedQuery<Observation> observationsQuery = entityManager
				.createQuery("select obs from Observation obs" + " where MONITOR_ID=" + monitorId, Observation.class);
		List<Observation> observations = observationsQuery.getResultList();
		return observations;
	}
}
