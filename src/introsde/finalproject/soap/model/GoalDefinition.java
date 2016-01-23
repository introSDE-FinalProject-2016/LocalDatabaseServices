package introsde.finalproject.soap.model;

import introsde.finalproject.soap.dao.LifeCoachDao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlValue;

/**
 * Persistence class for the "GoalDefinition" database table.
 * 
 * @author yuly
 *
 */

@Entity
@Table(name = "GoalDefinition")
@NamedQueries({ @NamedQuery(name = "GoalDefinition.findAll", query = "SELECT g FROM GoalDefinition g"), })
public class GoalDefinition implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "sqlite_goaldefinition")
	@TableGenerator(name = "sqlite_goaldefinition", table = "sequence", pkColumnName = "name", valueColumnName = "seq", pkColumnValue = "GoalDefinition", initialValue = 1, allocationSize = 1)
	@Column(name = "idGoalDefinition")
	private int idGoalDefinition;

	@Column(name = "description")
	private String description;

	@Column(name = "goalType")
	private String goalType;

	@Column(name = "goalValueType")
	private String goalValueType;

	@ManyToOne
	@JoinColumn(name = "idMeasureDefinition", referencedColumnName = "idMeasureDefinition")
	private MeasureDefinition measureDefinition;

	// constructor goalDefinition class
	public GoalDefinition() {
	}

	// getters and setters methods
	public int getIdGoalDefinition() {
		return idGoalDefinition;
	}

	public void setIdGoalDefinition(int idGoalDefinition) {
		this.idGoalDefinition = idGoalDefinition;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGoalType() {
		return goalType;
	}

	public void setGoalType(String goalType) {
		this.goalType = goalType;
	}

	public String getGoalValueType() {
		return goalValueType;
	}

	public void setGoalValueType(String goalValueType) {
		this.goalValueType = goalValueType;
	}

	public MeasureDefinition getMeasureDefinition() {
		return measureDefinition;
	}

	public void setMeasureDefinition(MeasureDefinition measureDefinition) {
		this.measureDefinition = measureDefinition;
	}

	// Database Query Operations
	public static GoalDefinition getGoaDefinitionlById(int idGoalDefinition) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		em.getEntityManagerFactory().getCache().evictAll();
		GoalDefinition gd = em.find(GoalDefinition.class, idGoalDefinition);
		LifeCoachDao.instance.closeConnections(em);
		return gd;
	}

	public static List<GoalDefinition> getAll() {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		em.getEntityManagerFactory().getCache().evictAll();
		List<GoalDefinition> list = em.createNamedQuery("GoalDefinition.findAll", GoalDefinition.class)
				.getResultList();
		LifeCoachDao.instance.closeConnections(em);
		return list;
	}

	// Database TRANSACTIONS Operations - CRUD
	public static GoalDefinition saveGoalDefinition(GoalDefinition gd) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(gd);
		tx.commit();
		LifeCoachDao.instance.closeConnections(em);
		return gd;
	}

	public static GoalDefinition updateGoalDefinition(GoalDefinition gd) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		gd = em.merge(gd);
		tx.commit();
		LifeCoachDao.instance.closeConnections(em);
		return gd;
	}

	public static void removeGoalDefinition(GoalDefinition gd) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		gd = em.merge(gd);
		em.remove(gd);
		tx.commit();
		LifeCoachDao.instance.closeConnections(em);
	}
}
