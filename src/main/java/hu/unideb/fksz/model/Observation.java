package hu.unideb.fksz.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

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
	
	@Column(name = "observation_date")
	private Timestamp observationDate;
	
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
