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

/**
 * Persistence class for the "Measure" database table.
 * 
 * @author yuly
 *
 */

@Entity
@Table(name = "Measure")
@NamedQueries({ 
	@NamedQuery(name = "Measure.findAll", query = "SELECT m FROM Measure m"),

})
@XmlType(propOrder={"idMeasure", "measureDefinition", "value" , "dateRegistered"})
public class Measure implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "sqlite_measure")
	@TableGenerator(name = "sqlite_measure", table = "sequence", pkColumnName = "name", valueColumnName = "seq", pkColumnValue = "Measure", initialValue = 1, allocationSize = 1)
	@Column(name = "idMeasure")
	private int idMeasure;

	@Column(name = "value")
	private String value;

	@Temporal(TemporalType.DATE)
	@Column(name = "dateRegistered")
	private Date dateRegistered;

	@OneToOne
	@JoinColumn(name = "idMeasureDefinition", referencedColumnName = "idMeasureDefinition", nullable = false, insertable = true, updatable = true)
	private MeasureDefinition measureDefinition;

	@ManyToOne
	@JoinColumn(name = "idPerson", referencedColumnName = "idPerson", nullable = false)
	private Person person;

	// constructor measure class
	public Measure() {
	}

	// getters and setters methods
	@XmlElement(name = "mid")
	public int getIdMeasure() {
		return idMeasure;
	}

	public void setIdMeasure(int idMeasure) {
		this.idMeasure = idMeasure;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@XmlElement(name = "created")
	//@Convert(converter = DateConverter.class)
	//@XmlJavaTypeAdapter(DateAdapter.class)
	public String getDateRegistered() {
		//return dateRegistered;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(this.dateRegistered);
	}

	public void setDateRegistered(String dateRegistered) throws ParseException{
		//this.dateRegistered = dateRegistered;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = format.parse(dateRegistered);
        this.dateRegistered = date;
	}

	@XmlElement(name = "name")
	public MeasureDefinition getMeasureDefinition() {
		return measureDefinition;
	}

	public void setMeasureDefinition(MeasureDefinition measureDefinition) {
		this.measureDefinition = measureDefinition;
	}

	// we make this transient for JAXB to avoid and infinite loop on serialization
	@XmlTransient
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String toString() {
		return "Measure ( " + idMeasure + " " + value + " " + dateRegistered + " )";
	}
	
	// Database Query Operations
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
	
	//Database TRANSACTIONS Operations - CRUD
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
