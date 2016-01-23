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
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Persistence class for the "Person" database table.
 * @author yuly
 *
 */

@Entity
@Table(name="Person")
@NamedQueries({
	@NamedQuery(name="Person.findAll", query="SELECT p FROM Person p"),
	/*@NamedQuery(name="Person.currentHealth", query="SELECT m FROM Measure m "
			+ "WHERE m.person = ?1 "
			+ "GROUP BY m.measureDefinition "
			+ "HAVING m.dateRegistered = MAX(m.dateRegistered)"),*/
	@NamedQuery(name="Person.currentHealth",
		query="SELECT m FROM Measure m WHERE m.dateRegistered IN (SELECT MAX(h.dateRegistered) FROM Measure h WHERE h.person = ?1 GROUP BY h.measureDefinition)")
})
//@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"idPerson", "firstname", "lastname" , "birthdate", "username", "password", "email", "measures"})
public class Person implements Serializable{

	private static final long serialVersionUID = 6936073577820155714L;

	@Id
	@GeneratedValue(generator="sqlite_person")
	@TableGenerator(name="sqlite_person", table="sequence", 
		pkColumnName="name", valueColumnName="seq",
		pkColumnValue="Person",
		initialValue=1, allocationSize=1)
	@Column(name="idPerson")
	private int idPerson;
	
	@Column(name="firstname", nullable=false)
	private String firstname;
	
	@Column(name="lastname", nullable=false)
	private String lastname;
	
	@Temporal(TemporalType.DATE)
	@Column(name="birthdate", nullable=false)
	private Date birthdate;
	
	@Column(name="username", nullable=false)
	private String username;
	
	@Column(name="password", nullable=false)
	private String password;
	
	@Column(name="email", nullable=false)
	private String email;
	
	//mappedBy must be equal to the name of the attribute in Measure that maps this relation
	@OneToMany(mappedBy="person", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private List<Measure> measures;
	
	//mappedBy must be equal to the name of the attribute in Measure that maps this relation
	@OneToMany(mappedBy="person", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private List<Goal> goals;
	
	//mappedBy must be equal to the name of the attribute in Measure that maps this relation
	@OneToMany(mappedBy="person", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private List<Reminder> reminders;
	
	//constructor person class
	public Person(){}
	
	//getters and setters methods
	@XmlElement(name="pid")
	public int getIdPerson() {
		return idPerson;
	}
	
	public void setIdPerson(int idPerson) {
		this.idPerson = idPerson;
	}
	
	@XmlElement
	public String getFirstname() {
		return firstname;
	}
	
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	@XmlElement
	public String getLastname() {
		return lastname;
	}
	
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	@XmlElement
	//@Convert(converter = DateConverter.class)
	//@XmlJavaTypeAdapter(DateAdapter.class)
	public String getBirthdate() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(this.birthdate);
	}
	
	public void setBirthdate(String birthdate) throws ParseException{
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = format.parse(birthdate);
        this.birthdate = date;
    }
	
	@XmlElement
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	@XmlElement
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	@XmlElement
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	// the XmlElementWrapper defines the name of node in which the list of Measure elements
    // will be inserted
    @XmlElementWrapper(name = "currentHealthProfile")
    @XmlElement(name="measure")
	public List<Measure> getMeasures() {
		//return measures;
    	return this.getCurrentHealthById();
	}
	
	public void setMeasures(List<Measure> measures) {
		this.measures = measures;
	}
	
	@XmlTransient
	public List<Goal> getGoals() {
		return goals;
	}
	
	public void setGoals(List<Goal> goals) {
		this.goals = goals;
	}
	
	@XmlTransient
	public List<Reminder> getReminders() {
		return reminders;
	}
	
	public void setReminders(List<Reminder> reminders) {
		this.reminders = reminders;
	}
	
	public String toString() {
		return "Person ( " + idPerson + " " + firstname + " " + lastname + " "
				+ birthdate + " " + username + " " + password + " " + email + " )";
	}
	
	//Database Query Operations
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
        for(Person p: list){
        	System.out.println(p.toString());
        }
        LifeCoachDao.instance.closeConnections(em);
        return list;
    }
    
    public List<Measure> getCurrentHealthById(){
    	EntityManager em = LifeCoachDao.instance.createEntityManager();
        em.getEntityManagerFactory().getCache().evictAll();
        List<Measure> list = em.createNamedQuery("Person.currentHealth", Measure.class)
        		.setParameter(1, this)
        		.getResultList();
        LifeCoachDao.instance.closeConnections(em);
        return list;
    }
    
    //Database TRANSACTIONS Operations - CRUD
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

}
