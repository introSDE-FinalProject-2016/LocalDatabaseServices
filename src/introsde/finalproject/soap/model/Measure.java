package introsde.finalproject.soap.model;

import introsde.finalproject.soap.adapter.DateAdapter;
import introsde.finalproject.soap.converter.DateConverter;
import introsde.finalproject.soap.dao.LifeCoachDao;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Persistence class for the "Measure" database table.
 * 
 * @author yuly
 *
 */

@Entity
@Table(name = "Measure")
@NamedQueries({
	@NamedQuery(name = "Measure.findAll", query = "SELECT m FROM Measure m")
})
@XmlType(propOrder={"idMeasure", "measureDefinition", "value" , "timestamp"})
@XmlAccessorType(XmlAccessType.NONE)
public class Measure implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "sqlite_measure")
	@TableGenerator(name = "sqlite_measure", table = "sqlite_sequence", 
		pkColumnName = "name", valueColumnName = "seq", 
		pkColumnValue = "Measure")
	@Column(name = "idMeasure")
	private int idMeasure;

	@Column(name = "value")
	private String value;

	@Temporal(TemporalType.DATE)
	@Column(name = "timestamp")
	private Date timestamp;

	@OneToOne
	@JoinColumn(name = "idMeasureDefinition", referencedColumnName = "idMeasureDefinition", insertable = true, updatable = true)
	private MeasureDefinition measureDefinition;

	@ManyToOne
	@JoinColumn(name = "idPerson", referencedColumnName = "idPerson")
	private Person person;

	// Constructor measure class
	public Measure() {
	}

	// Getters methods
	@XmlElement(name = "mid")
	public int getIdMeasure() {
		return idMeasure;
	}

	@XmlElement(name = "value")
	public String getValue() {
		return value;
	}

	@XmlElement(name = "created")
	public Date getTimestamp() {
		return timestamp;
	}

	@XmlElement(name = "measure")
	@JsonProperty("measure")
	public MeasureDefinition getMeasureDefinition() {
		return measureDefinition;
	}

	// We make this transient for JAXB to avoid and infinite loop on
	// serialization
	@XmlTransient
	public Person getPerson() {
		return person;
	}

	// Setters methods
	public void setIdMeasure(int idMeasure) {
		this.idMeasure = idMeasure;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public void setMeasureDefinition(MeasureDefinition measureDefinition) {
		this.measureDefinition = measureDefinition;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String toString() {
		return "Measure ( " + idMeasure + ", " + measureDefinition + ", "
				+ value + ", " + timestamp + " )";
	}
	
	// Database operations
	public static Measure getMeasureById(int idMeasure) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		em.getEntityManagerFactory().getCache().evictAll();
		Measure m = em.find(Measure.class, idMeasure);
		LifeCoachDao.instance.closeConnections(em);
		return m;
	}

	public static List<Measure> getAll() {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		em.getEntityManagerFactory().getCache().evictAll();
		List<Measure> list = em.createNamedQuery("Measure.findAll",
				Measure.class).getResultList();
		LifeCoachDao.instance.closeConnections(em);
		return list;
	}

	public static Measure saveMeasure(Measure m) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(m);
		tx.commit();
		LifeCoachDao.instance.closeConnections(em);
		return m;
	}

	public static Measure updateMeasure(Measure m) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		m = em.merge(m);
		tx.commit();
		LifeCoachDao.instance.closeConnections(em);
		return m;
	}

	public static void removeMeasure(Measure m) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		m = em.merge(m);
		em.remove(m);
		tx.commit();
		LifeCoachDao.instance.closeConnections(em);
	}

}
