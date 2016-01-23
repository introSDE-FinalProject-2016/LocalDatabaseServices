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
 * Persistence class for the "Goal" database table.
 * 
 * @author yuly
 *
 */

@Entity
@Table(name = "Goal")
@NamedQueries({ @NamedQuery(name = "Goal.findAll", query = "SELECT g FROM Goal g"), })
@XmlType(propOrder = { "idGoal", "goalDefinition", "value", "dateRegistered" })
public class Goal implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "sqlite_goal")
	@TableGenerator(name = "sqlite_goal", table = "sequence", pkColumnName = "name", valueColumnName = "seq", pkColumnValue = "Goal", initialValue = 1, allocationSize = 1)
	@Column(name = "idGoal")
	private int idGoal;

	@Column(name = "value")
	private int value;

	@Temporal(TemporalType.DATE)
	@Column(name = "dateRegistered")
	private Date dateRegistered;

	@ManyToOne
	@JoinColumn(name = "idGoalDefinition", referencedColumnName = "idGoalDefinition")
	private GoalDefinition goalDefinition;

	@ManyToOne
	@JoinColumn(name = "idPerson", referencedColumnName = "idPerson")
	private Person person;

	// constructor goal class
	public Goal() {
	}

	// getters and setters methods
	@XmlElement(name = "gid")
	public int getIdGoal() {
		return idGoal;
	}

	public void setIdGoal(int idGoal) {
		this.idGoal = idGoal;
	}

	@XmlElement(name = "value")
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@XmlElement(name = "created")
	public Date getDateRegistered() {
		return dateRegistered;
	}

	public void setDateRegistered(Date dateRegistered) {
		this.dateRegistered = dateRegistered;
	}

	@XmlElement(name = "goalDefinition")
	public GoalDefinition getGoalDefinition() {
		return goalDefinition;
	}

	public void setGoalDefinition(GoalDefinition goalDefinition) {
		this.goalDefinition = goalDefinition;
	}

	// we make this transient for JAXB to avoid and infinite loop on
	// serialization
	@XmlTransient
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String toString() {
		return "Goal ( " + idGoal + " " + value + " " + dateRegistered + " )";
	}

	// Database Query Operations
	public static Goal getGoalById(int idGoal) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		em.getEntityManagerFactory().getCache().evictAll();
		Goal g = em.find(Goal.class, idGoal);
		LifeCoachDao.instance.closeConnections(em);
		return g;
	}

	public static List<Goal> getAll() {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		em.getEntityManagerFactory().getCache().evictAll();
		List<Goal> list = em.createNamedQuery("Goal.findAll",
				Goal.class).getResultList();
		LifeCoachDao.instance.closeConnections(em);
		return list;
	}

	// Database TRANSACTIONS Operations - CRUD
	public static Goal saveGoal(Goal g) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(g);
		tx.commit();
		LifeCoachDao.instance.closeConnections(em);
		return g;
	}

	public static Goal updateGoal(Goal g) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		g = em.merge(g);
		tx.commit();
		LifeCoachDao.instance.closeConnections(em);
		return g;
	}

	public static void removeGoal(Goal g) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		g = em.merge(g);
		em.remove(g);
		tx.commit();
		LifeCoachDao.instance.closeConnections(em);
	}
}
