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

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "traffic_counter_observations")
public class Observation {

	@Id
	@Column(name = "observation_id")
	@SequenceGenerator(name = "IdGenerator", sequenceName = "OBSERVATION_ID_GENERATOR", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IdGenerator")
	private int observationId;

	@Column(name = "monitor_id")
	private int monitorId;

	@Column(name = "observed_video_title")
	private String observedVideoTitle;

	@Column(name = "traffic_count")
	private int trafficCount;

	@Column(name = "computer_traffic_count")
	private int computerTrafficCount;

	@Column(name = "observation_date")
	private Timestamp observationDate;


	public int getComputerTrafficCount() {
		return computerTrafficCount;
	}

	public void setComputerTrafficCount(int computerTrafficCount) {
		this.computerTrafficCount = computerTrafficCount;
	}

	public int getObservationId() {
		return observationId;
	}

	public void setObservationId(int observationId) {
		this.observationId = observationId;
	}

	public int getMonitor_id() {
		return monitorId;
	}

	public void setMonitor_id(int monitorId) {
		this.monitorId = monitorId;
	}

	public String getObservedVideoTitle() {
		return observedVideoTitle;
	}

	public void setObservedVideoTitle(String observedVideoTitle) {
		this.observedVideoTitle = observedVideoTitle;
	}

	public int getTrafficCount() {
		return trafficCount;
	}

	public void setTrafficCount(int trafficCount) {
		this.trafficCount = trafficCount;
	}

	public Timestamp getObservationDate() {
		return observationDate;
	}

	public void setObservationDate(Timestamp observationDate) {
		this.observationDate = observationDate;
	}

	public Observation(int observationId, int monitorId, String observedVideoTitle, int trafficCount, Timestamp observationDate) {
		this.observationId = observationId;
		this.monitorId = monitorId;
		this.observedVideoTitle = observedVideoTitle;
		this.trafficCount = trafficCount;
		this.observationDate = observationDate;
	}

	public Observation() {
	}
}
