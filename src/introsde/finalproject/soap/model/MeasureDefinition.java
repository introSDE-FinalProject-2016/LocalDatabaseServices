package introsde.finalproject.soap.model;

import introsde.finalproject.soap.dao.LifeCoachDao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlValue;

/**
 * Persistence class for the "MeasureDefinition" database table.
 * 
 * @author yuly
 *
 */

@Entity
@Table(name = "MeasureDefinition")
@NamedQueries({
		@NamedQuery(name = "MeasureDefinition.findAll", query = "SELECT md FROM MeasureDefinition md"),
		@NamedQuery(name = "MeasureDefinition.getMeasureDefinitionByName", query = "SELECT md FROM MeasureDefinition md WHERE md.measureName = ?1 "),
		
})
@XmlAccessorType(XmlAccessType.NONE)
public class MeasureDefinition implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "sqlite_measuredefinition")
	@TableGenerator(name = "sqlite_measuredefinition", table = "sqlite_sequence",
		pkColumnName = "name", valueColumnName = "seq", 
		pkColumnValue = "MeasureDefinition")
	@Column(name = "idMeasureDefinition")
	private int idMeasureDefinition;

	@Column(name = "measureName")
	@XmlValue
	private String measureName;

	@Column(name = "measureType")
	private String measureType;

	@Column(name = "startValue")
	private String startValue;

	@Column(name = "endValue")
	private String endValue;

	@Column(name = "alarmLevel")
	private String alarmLevel;

	// Constructor measureDefinition class
	public MeasureDefinition() {
	}

	// Getters methods
	public int getIdMeasureDefinition() {
		return idMeasureDefinition;
	}

	public String getMeasureName() {
		return measureName;
	}

	public String getMeasureType() {
		return measureType;
	}

	public String getStartValue() {
		return startValue;
	}

	public String getEndValue() {
		return endValue;
	}

	public String getAlarmLevel() {
		return alarmLevel;
	}

	// Setters methods
	public void setIdMeasureDefinition(int idMeasureDefinition) {
		this.idMeasureDefinition = idMeasureDefinition;
	}

	public void setMeasureName(String measureName) {
		this.measureName = measureName;
	}

	public void setMeasureType(String measureType) {
		this.measureType = measureType;
	}

	public void setStartValue(String startValue) {
		this.startValue = startValue;
	}

	public void setEndValue(String endValue) {
		this.endValue = endValue;
	}

	public void setAlarmLevel(String alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	public String toString() {
		return "MeasureDefinition ( " + idMeasureDefinition + ", "
				+ measureName + ", " + measureType + ", " + startValue + ", "
				+ endValue + ", " + alarmLevel + " )";
	}

	// Database operations
	public static MeasureDefinition getMeasureDefinitionById(
			int idMeasureDefinition) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		em.getEntityManagerFactory().getCache().evictAll();
		MeasureDefinition md = em.find(MeasureDefinition.class,
				idMeasureDefinition);
		LifeCoachDao.instance.closeConnections(em);
		return md;
	}

	public static List<MeasureDefinition> getAll() {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		em.getEntityManagerFactory().getCache().evictAll();
		List<MeasureDefinition> list = em.createNamedQuery(
				"MeasureDefinition.findAll", MeasureDefinition.class)
				.getResultList();
		LifeCoachDao.instance.closeConnections(em);
		return list;
	}

	public static MeasureDefinition saveMeasureDefinition(MeasureDefinition md) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(md);
		tx.commit();
		LifeCoachDao.instance.closeConnections(em);
		return md;
	}

	public static MeasureDefinition updateMeasureDefinition(MeasureDefinition md) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		md = em.merge(md);
		tx.commit();
		LifeCoachDao.instance.closeConnections(em);
		return md;
	}

	public static void removeMeasureDefinition(MeasureDefinition md) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		md = em.merge(md);
		em.remove(md);
		tx.commit();
		LifeCoachDao.instance.closeConnections(em);
	}

	/**
	 * Given a measure name, the function returns the corresponding
	 * MeasureDefinition object.
	 * @param measureName
	 * @return MeasureDefinition
	 */
	public static MeasureDefinition getMeasureDefinitionByName(
			String measureName) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		try {
			MeasureDefinition p = em
					.createNamedQuery(
							"MeasureDefinition.getMeasureDefinitionByName",
							MeasureDefinition.class)
					.setParameter(1, measureName).getSingleResult();
			LifeCoachDao.instance.closeConnections(em);
			return p;
		} catch (Exception e) {
			return null;
		}
	}

}
