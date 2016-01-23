package introsde.finalproject.soap.model;

import introsde.finalproject.soap.dao.LifeCoachDao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * Persistence class for the "Reminder" database table.
 * @author yuly
 *
 */

@Entity
@Table(name="Reminder")
@NamedQueries({
	@NamedQuery(name="Reminder.findAll", query="SELECT r FROM Reminder r"),
})
@XmlType(propOrder = { "idReminder" ,"description", "reminderType", "weeklyDay", "monthlyDay", "specificDate", "dateRegistered"})
public class Reminder implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator="sqlite_reminder")
	@TableGenerator(name="sqlite_reminder", table="sequence", 
		pkColumnName="name", valueColumnName="seq",
		pkColumnValue="Reminder",
		initialValue=1, allocationSize=1)
	@Column(name="idReminder")
	private int idReminder;
	
	@Column(name="description")
	private String description;
	
	@Column(name="reminderType")
	private String reminderType;
	
	@Column(name="weeklyDay")
	private String weeklyDay;
	
	@Column(name="monthlyDay")
	private int monthlyDay;
	
	@Temporal(TemporalType.DATE)
	@Column(name="specificDate")
	private Date specificDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="dateRegistered")
	private Date dateRegistered;
	
	@ManyToOne
	@JoinColumn(name="idPerson",referencedColumnName="idPerson")
	private Person person;
	
	//constructor reminder class
	public Reminder(){}

	//getters and setters methods
	@XmlElement(name="rid", required=true)
	public int getIdReminder() {
		return idReminder;
	}

	public void setIdReminder(int idReminder) {
		this.idReminder = idReminder;
	}

	@XmlElement(required=true)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@XmlElement(name="type", required=true)
	public String getReminderType() {
		return reminderType;
	}

	public void setReminderType(String reminderType) {
		this.reminderType = reminderType;
	}

	@XmlElement(name="wDay")
	public String getWeeklyDay() {
		return weeklyDay;
	}

	public void setWeeklyDay(String weeklyDay) {
		this.weeklyDay = weeklyDay;
	}

	@XmlElement(name="mDay")
	public int getMonthlyDay() {
		return monthlyDay;
	}

	public void setMonthlyDay(int monthlyDay) {
		this.monthlyDay = monthlyDay;
	}

	@XmlElement(name="sDate")
	public Date getSpecificDate() {
		return specificDate;
	}

	public void setSpecificDate(Date specificDate) {
		this.specificDate = specificDate;
	}

	@XmlElement(name="created", required=true)
	public Date getDateRegistered() {
		return dateRegistered;
	}

	public void setDateRegistered(Date dateRegistered) {
		this.dateRegistered = dateRegistered;
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
		return "Reminder ( " + idReminder + " " + description + " " + reminderType + " "
				+ weeklyDay + " " + monthlyDay + " " + specificDate + " " + dateRegistered + " )";
	}
	
	//Database Query Operations
	public static Reminder getReminderById(int idReminder) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        em.getEntityManagerFactory().getCache().evictAll();
        Reminder r = em.find(Reminder.class, idReminder);
        LifeCoachDao.instance.closeConnections(em);
        return r;
    }
    
    public static List<Reminder> getAll() {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        em.getEntityManagerFactory().getCache().evictAll();
        List<Reminder> list = em.createNamedQuery("Reminder.findAll", Reminder.class).getResultList();
        LifeCoachDao.instance.closeConnections(em);
        return list;
    }
    
    //Database TRANSACTIONS Operations - CRUD
    public static Reminder saveReminder(Reminder r) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(r);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return r;
    }
    
    public static Reminder updateReminder(Reminder r) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        r=em.merge(r);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return r;
    }
    
    public static void removeReminder(Reminder r) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        r=em.merge(r);
        em.remove(r);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
    }  
}
