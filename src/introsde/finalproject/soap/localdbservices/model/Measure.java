package introsde.finalproject.soap.localdbservices.model;

import introsde.finalproject.soap.localdbservices.adapter.DateAdapter;
import introsde.finalproject.soap.localdbservices.converter.DateConverter;
import introsde.finalproject.soap.localdbservices.dao.LifeCoachDao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Persistence class for the "Measure" database table.
 * 
 * @author yuly
 *
 */

@Entity
@Cacheable(false)
@Table(name = "Measure")
@NamedQueries({
	@NamedQuery(name = "Measure.findAll", query = "SELECT m FROM Measure m"),
	@NamedQuery(name="Measure.findByIdPersonAndMeasureName", query="SELECT m FROM Measure m WHERE m.person = ?1 AND m.measureDefinition = ?2"),
	@NamedQuery(name="Measure.findByIdPerson", query="SELECT m FROM Measure m WHERE m.person = ?1"),
	@NamedQuery(name="Measure.findByIdMeasure", query="SELECT m FROM Measure m WHERE m.person = ?1 AND m.measureDefinition = ?2 AND m.idMeasure = ?3")
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
	@JsonProperty("mid")
	public int getIdMeasure() {
		return idMeasure;
	}

	@XmlElement(name = "value")
	public String getValue() {
		return value;
	}

	@XmlElement(name = "created")
	@JsonProperty("created")
	@Convert(converter = DateConverter.class)
	@XmlJavaTypeAdapter(DateAdapter.class)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	public Date getTimestamp() {
		return timestamp;
	}

	@XmlElement(name = "name")
	@JsonProperty("name")
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

	/**
	 * Given a specified person and measureName, the function returns the list of the measure
	 * associated to pid and measureName
	 * @param p
	 * @param m
	 * @return
	 */
	public static List<Measure> getMeasureByMeasureName(Person p, MeasureDefinition md) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        TypedQuery<Measure> query = em.createNamedQuery(
        		"Measure.findByIdPersonAndMeasureName", Measure.class);
		query.setParameter(1, p);
		query.setParameter(2, md);
		List<Measure> list = query.getResultList();
		for(Measure m : list){
        	System.out.println(m.toString());
        }
		LifeCoachDao.instance.closeConnections(em);
        return list;
    }
	
	/**
	 * Given a specified person, the function returns the list of the measure
	 * associated to pid
	 * @param p
	 * @return
	 */
	public static List<Measure> getMeasureByPerson(Person p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        TypedQuery<Measure> query = em.createNamedQuery(
        		"Measure.findByIdPerson", Measure.class);
		query.setParameter(1, p);
		List<Measure> list = query.getResultList();
		for(Measure m : list){
        	System.out.println(m.toString());
        }
		LifeCoachDao.instance.closeConnections(em);
        return list;
    }
	
	/**
	 * Given a specified person, measureName and idMeasure, the function returns a measure 
	 * associated to pid, measureName and mid
	 * @param person
	 * @param measureDef
	 * @return
	 */
	public static Measure getMeasureValueByMid(Person p, MeasureDefinition md, int mid){
		EntityManager em = LifeCoachDao.instance.createEntityManager();
        TypedQuery<Measure> query = em.createNamedQuery(
        		"Measure.findByIdMeasure", Measure.class);
		query.setParameter(1, p);
		query.setParameter(2, md);
		query.setParameter(3, mid);
		Measure measure = query.getSingleResult();
		System.out.println(measure.toString());
		LifeCoachDao.instance.closeConnections(em);
		return measure;
	}
}
