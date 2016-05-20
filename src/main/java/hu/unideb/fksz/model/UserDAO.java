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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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
		try {
			TypedQuery<User> query = entityManager
					.createQuery("Select u from User u where" + " user_name ='" + user.getName() + "'", User.class);
			List<User> users = query.getResultList();

			if (users.size() > 0) {
				if (users.get(0).getPassword().equals(user.getPassword())) {
					if (users.get(0).getRole().equals(user.getRole())) {
						user.setId(users.get(0).getId());
						System.out.println("User:" + user.getName() + " " + user.getPassword() + " " + user.getId());
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
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			TrafficCounterLogger.errorMessage(e.toString());
		}

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
		//TODO SELECT USER
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
		try {
			TypedQuery<User> monitorsQuery = entityManager
					.createQuery("select u from User u where user_role='monitor'", User.class);
			List<User> monitors = monitorsQuery.getResultList();
			return monitors;

		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			TrafficCounterLogger.errorMessage(e.toString());
			return null;
		}
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
