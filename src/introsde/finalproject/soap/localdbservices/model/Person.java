package introsde.finalproject.soap.localdbservices.model;

import introsde.finalproject.soap.localdbservices.adapter.DateAdapter;
import introsde.finalproject.soap.localdbservices.converter.DateConverter;
import introsde.finalproject.soap.localdbservices.dao.LifeCoachDao;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Persistence class for the "Person" database table.
 * @author yuly
 *
 */

@Entity
@Table(name="Person")
@NamedQueries({
	@NamedQuery(name="Person.findAll", query="SELECT p FROM Person p"),	
	@NamedQuery(name="Person.currentHealth", query="SELECT m FROM Measure m WHERE m.timestamp IN "
			+ "(SELECT MAX(h.timestamp) "
			+ "FROM Measure h "
			+ "WHERE h.person = ?1 "
			+ "GROUP BY h.measureDefinition)"),
})
@XmlType(propOrder={"idPerson", "firstname", "lastname" , "birthdate", "email", "gender", "measure", "goal"})
@XmlAccessorType(XmlAccessType.NONE)
public class Person implements Serializable{

	private static final long serialVersionUID = 6936073577820155714L;

	@Id
	@GeneratedValue(generator="sqlite_person")
	@TableGenerator(name="sqlite_person", table="sqlite_sequence", 
		pkColumnName="name", valueColumnName="seq",
		pkColumnValue="Person")
	@Column(name="idPerson")
	private int idPerson;
	
	@Column(name="firstname")
	private String firstname;
	
	@Column(name="lastname")
	private String lastname;
	
	@Temporal(TemporalType.DATE)
	@Column(name="birthdate")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	//@Convert(converter = DateConverter.class)
	//@XmlJavaTypeAdapter(DateAdapter.class)
	private Date birthdate;
	
	@Column(name="email")
	private String email;
	
	@Column(name="gender")
    private String gender;
	
	//mappedBy must be equal to the name of the attribute in Measure that maps this relation
	@OneToMany(mappedBy="person", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private List<Measure> measure;
	
	//mappedBy must be equal to the name of the attribute in Goal that maps this relation
	@OneToMany(mappedBy="person", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private List<Goal> goal;
	
	// Constructor person class
	public Person(){
	}

	// Getters methods
	@XmlElement(name="pid")
	public int getIdPerson() {
		return idPerson;
	}

	@XmlElement
	public String getFirstname() {
		return firstname;
	}

	@XmlElement
	public String getLastname() {
		return lastname;
	}

	@XmlElement
	//@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	public Date getBirthdate() {
		return birthdate;
	}

	@XmlElement
	public String getEmail() {
		return email;
	}

	@XmlElement
	public String getGender() {
		return gender;
	}

	@XmlElementWrapper(name = "currentHealth")
	@XmlElement(name = "measure")
	public List<Measure> getMeasure() {
		//return measure;
		return this.getQueryCurrentHealth();
	}

	@XmlElementWrapper(name = "goals")
	@XmlElement(name = "goal")
	public List<Goal> getGoal() {
		return goal;
	}

	// Setters methods
	public void setIdPerson(int idPerson) {
		this.idPerson = idPerson;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setMeasure(List<Measure> measure) {
		this.measure = measure;
	}

	public void setGoal(List<Goal> goal) {
		this.goal = goal;
	}
	
	public String toString() {
		return "Person ( " + idPerson + ", " + firstname + ", " + lastname + ", "
				+ birthdate + ", " + email + ", " + gender + " )";
	}
	
	// Database operations 
    public static Person getPersonById(int idPerson) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        em.getEntityManagerFactory().getCache().evictAll();
        Person p = em.find(Person.class, idPerson);
        LifeCoachDao.instance.closeConnections(em);
        return p;
    }
    
    public static List<Person> getAll() {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        em.getEntityManagerFactory().getCache().evictAll();
        List<Person> list = em.createNamedQuery("Person.findAll", Person.class).getResultList();
        LifeCoachDao.instance.closeConnections(em);
        return list;
    }
    
    public static Person savePerson(Person p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return p;
    }
    
    public static Person updatePerson(Person p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p=em.merge(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return p;
    }
    
    public static void removePerson(Person p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p=em.merge(p);
        em.remove(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
    }  

    public List<Measure> getQueryCurrentHealth() {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
	    List<Measure> listCurrentMeasure = em.createNamedQuery("Person.currentHealth", Measure.class)
	    		.setParameter(1, this)
	    		.getResultList();
	    LifeCoachDao.instance.closeConnections(em);
	    return listCurrentMeasure;
	    
	}
    
}
