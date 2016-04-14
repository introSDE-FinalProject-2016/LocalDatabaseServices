package introsde.finalproject.soap.localdbservices.model;

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

/**
 * Persistence class for the "Goal" database table.
 * 
 * @author yuly
 *
 */

@Entity
@Table(name = "Goal")
@NamedQueries({ 
	@NamedQuery(name = "Goal.findAll", query = "SELECT g FROM Goal g"),
	@NamedQuery(name = "Goal.findByIdPersonAndIdMeasureDef", query = "SELECT g FROM Goal g WHERE g.person = ?1 AND g.measureDefinition = ?2"),
	@NamedQuery(name = "Goal.findByIdPerson", query = "SELECT g FROM Goal g WHERE g.person = ?1")
	})
@XmlType(propOrder={"idGoal", "type", "value" , "startDateGoal", "endDateGoal", "achieved"})
@XmlAccessorType(XmlAccessType.NONE)
public class Goal implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "sqlite_goal")
	@TableGenerator(name = "sqlite_goal", table = "sqlite_sequence", 
		pkColumnName = "name", valueColumnName = "seq", 
		pkColumnValue = "Goal")
	@Column(name = "idGoal")
	private int idGoal;

	@Column(name = "value")
	private int value;

	@Column(name = "type")
	private String type;

	@Temporal(TemporalType.DATE)
	@Column(name = "startDateGoal")
	private Date startDateGoal;

	@Temporal(TemporalType.DATE)
	@Column(name = "endDateGoal")
	private Date endDateGoal;

	@Column(name = "achieved")
	private Boolean achieved;

	@ManyToOne
	@JoinColumn(name = "idPerson", referencedColumnName = "idPerson")
	private Person person;

	@ManyToOne
	@JoinColumn(name = "idMeasureDefinition", referencedColumnName = "idMeasureDefinition")
	private MeasureDefinition measureDefinition;

	// Constructor goal class
	public Goal() {
	}

	// Getters methods
	@XmlElement(name="gid")
	public int getIdGoal() {
		return idGoal;
	}

	@XmlElement
	public int getValue() {
		return value;
	}

	@XmlElement
	public String getType() {
		return type;
	}

	@XmlElement
	public Date getStartDateGoal() {
		return startDateGoal;
	}

	@XmlElement
	public Date getEndDateGoal() {
		return endDateGoal;
	}

	@XmlElement
	public Boolean getAchieved() {
		return achieved;
	}

	// We make this transient for JAXB to avoid and infinite loop on
	// serialization
	@XmlTransient
	public Person getPerson() {
		return person;
	}

	public MeasureDefinition getMeasureDefinition() {
		return measureDefinition;
	}

	// Setters methods
	public void setIdGoal(int idGoal) {
		this.idGoal = idGoal;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setStartDateGoal(Date startDateGoal) {
		this.startDateGoal = startDateGoal;
	}

	public void setEndDateGoal(Date endDateGoal) {
		this.endDateGoal = endDateGoal;
	}

	public void setAchieved(Boolean achieved) {
		this.achieved = achieved;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public void setMeasureDefinition(MeasureDefinition measureDefinition) {
		this.measureDefinition = measureDefinition;
	}

	public String toString() {
		return "Goal ( " + idGoal + ", " + value + ", "
				+ type + ", " + startDateGoal + ", " 
				+ endDateGoal + ", "+ achieved + " )";
	}
	
	// Database operations
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
		List<Goal> list = em.createNamedQuery("Goal.findAll", Goal.class)
				.getResultList();
		LifeCoachDao.instance.closeConnections(em);
		return list;
	}

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
	
	/**
	 * Given a specified person and measure definition, the function returns the list of the goals
	 * associated to idPerson and idMeasureDefinition.
	 * @param p
	 * @param m
	 * @return
	 */
	public static List<Goal> getGoalByMeasureDef(Person p, MeasureDefinition md) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        List<Goal> list = em.createNamedQuery("Goal.findByIdPersonAndIdMeasureDef", Goal.class)
        		.setParameter(1, p)
        		.setParameter(2, md)
        		.getResultList();
        for(Goal g : list){
        	System.out.println(g.toString());
        }
        LifeCoachDao.instance.closeConnections(em);
        return list;
    }
	
	/**
	 * Given a specified person, the function returns the list of the goals
	 * associated to idPerson.
	 * @param p
	 * @return
	 */
	public static List<Goal> getGoalByPerson(Person p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        List<Goal> list = em.createNamedQuery("Goal.findByIdPerson", Goal.class)
        		.setParameter(1, p)
        		.getResultList();
        for(Goal g : list){
        	System.out.println(g.toString());
        }
        LifeCoachDao.instance.closeConnections(em);
        return list;
    }
}
