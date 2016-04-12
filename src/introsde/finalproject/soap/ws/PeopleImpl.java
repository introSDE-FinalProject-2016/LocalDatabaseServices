package introsde.finalproject.soap.ws;

import introsde.finalproject.soap.model.Goal;
import introsde.finalproject.soap.model.Measure;
import introsde.finalproject.soap.model.MeasureDefinition;
import introsde.finalproject.soap.model.Person;
import introsde.finalproject.soap.wrapper.GoalWrapper;
import introsde.finalproject.soap.wrapper.MeasureDefinitionWrapper;
import introsde.finalproject.soap.wrapper.MeasureWrapper;
import introsde.finalproject.soap.wrapper.PersonWrapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;

import org.apache.xmlbeans.impl.regex.REUtil;

/**
 * Service Implementation
 * 
 * @author yuly
 *
 */
@WebService(endpointInterface = "introsde.finalproject.soap.ws.People", serviceName = "PeopleService")
public class PeopleImpl implements People {

	/**
	 * Implementation CRUD Operation to Person
	 */

	/**
	 * Method #1: getPersonList() => List<Person> This method returns the list
	 * all the people in the database
	 */
	@Override
	public PersonWrapper getPersonList() {
		System.out.println("--> REQUEST: getPersonList()");
		PersonWrapper pWrapper = new PersonWrapper();
		pWrapper.setPersonList(Person.getAll());
		return pWrapper;
	}

	/**
	 * Method #2: getPerson(int idPerson) => Person This method returns all the
	 * personal information plus current measures of one person identified by
	 * {idPerson}
	 */
	@Override
	public Person getPerson(int idPerson) {
		System.out.println("--> REQUEST: getPerson(" + idPerson + ")");
		Person p = Person.getPersonById(idPerson);
		if (p != null) {
			System.out.println("Found Person by id = " + idPerson + " => "
					+ p.getFirstname());
		} else {
			System.out.println("Didn't find any Person with id = " + idPerson);
		}
		return p;
	}

	/**
	 * Method #3: createPerson(Person person, List<Measure> measure) => idPerson
	 * This method creates a new person and returns the id of the person created
	 */
	@Override
	public int createPerson(Person person, List<Measure> measure) {
		// phase #1: check if person includes some measures
		// if is true
		if (measure.isEmpty()) {
			System.out.println("--> REQUESTED: createPerson("
					+ person.getFirstname() + ") without measure");
			Person.savePerson(person);
			return person.getIdPerson();

		}// else if is false
		else {
			// removes the currentMeasure in the person and puts them in another
			// variable
			System.out.println("--> REQUESTED: createPerson("
					+ person.getFirstname() + ") with new measure");
			Person p = Person.savePerson(person);
			// creates the today date
			Calendar today = Calendar.getInstance();
			ArrayList<Integer> control = new ArrayList<>();
			for (int i = 0; i < measure.size(); i++) {
				// retrieves the name of the measures inserted by the client
				// (e.g. weight)
				String measureName = measure.get(i).getMeasureDefinition()
						.getMeasureName();

				// searches the measure definition associated with the name of
				// the measure
				MeasureDefinition temp = new MeasureDefinition();
				temp = MeasureDefinition
						.getMeasureDefinitionByName(measureName);

				if (!control.contains(temp.getIdMeasureDefinition())) {
					control.add(temp.getIdMeasureDefinition());
					measure.get(i).setPerson(p);
					measure.get(i).setTimestamp(today.getTime());
					measure.get(i).setMeasureDefinition(temp);
					Measure.saveMeasure(measure.get(i));
				}
			}
			Person.getPersonById(p.getIdPerson());
			return p.getIdPerson();
		}
	}

	/**
	 * Method #4: updatePerson(Person person) =>idPerson This method update the
	 * personal information of the person identified by his/her idPerson
	 */
	public int updatePerson(Person person) {
		try {
			System.out.println("--> REQUEST: updatePerson(" + person.toString()
					+ ")");
			Person existing = Person.getPersonById(person.getIdPerson());
			int personId;

			if (existing == null) {
				// the person is not found
				System.out.println("Didn't find any Person with  id = "
						+ person.getIdPerson());
				personId = -2;

			} else {
				System.out.println("Found Person by id = "
						+ person.getIdPerson());
				person.setIdPerson(existing.getIdPerson());
				// checks if the client sent a name in order to update the
				// person
				// if there is no name, remain the previous name, the same
				// happens
				// with Lastname and Birthdate
				if (person.getFirstname() == null) {
					person.setFirstname(existing.getFirstname());
				}
				if (person.getLastname() == null) {
					person.setLastname(existing.getLastname());
				}
				if (person.getBirthdate() == null) {
					person.setBirthdate(existing.getBirthdate());
				}
				if (person.getEmail() == null) {
					person.setEmail(existing.getEmail());
				}
				if (person.getGender() == null) {
					person.setGender(existing.getGender());
				}

				person.setMeasure(existing.getMeasure());
				Person.updatePerson(person);
				personId = person.getIdPerson();
			}

			return personId;

		} catch (Exception e) {
			System.out.println("Person not updated due the exception: " + e);
			return -1;
		}
	}

	/**
	 * Method #5: deletePerson(int idPerson) => String message This method
	 * cancel from the database the person identified by id and also his/her
	 * measures
	 */
	@Override
	public String deletePerson(int idPerson) {
		try {
			System.out.println("---> REQUEST: deletePerson(" + idPerson + ")");
			String result = "";
			Person p = Person.getPersonById(idPerson);
			if (p != null) {
				if (!p.getMeasure().isEmpty()) {
					for (int i = 0; i < p.getMeasure().size(); i++) {
						Measure.removeMeasure(p.getMeasure().get(i));
					}
					Person.removePerson(p);
					result = "Person with id: " + idPerson
							+ " deleted with his/her measures";
					return result;
				}
				Person.removePerson(p);
				result = "Person with id: " + idPerson + " deleted";
				return result;
			} else {
				result = "Person with id: " + idPerson + " not found!";
				return result;
			}
		} catch (Exception e) {
			return "Person not deleted due to the exception: " + e;
		}
	}

	/**
	 * Implementation CRUD Operation to Goal
	 */

	/**
	 * Method #1: createGoal(Goal goal, int idPerson) => idGoal This method
	 * creates a new goal for a specified person identified by id
	 */
	@Override
	public int createGoal(Goal goal, int idPerson) {
		try {
			System.out.println("--> REQUESTED: createGoal(" + goal.getType()
					+ ", " + idPerson + ")");
			String goalType = goal.getType();

			// searches the measure definition associated with the name of
			// the measure
			MeasureDefinition temp = new MeasureDefinition();
			temp = MeasureDefinition.getMeasureDefinitionByName(goalType);

			// search person by idPerson
			Person p = Person.getPersonById(idPerson);

			if (p != null) {
				System.out.println("Found Person by id = " + idPerson + " => "
						+ p.getIdPerson());
				goal.setPerson(p);
				goal.setMeasureDefinition(temp);
				Goal.saveGoal(goal);
				return goal.getIdGoal();
			} else {
				System.out
						.println("Din't find any Person with id = " + idPerson);
				return -2;
			}
		} catch (Exception e) {
			System.out.println("Goal not saved due the exception: " + e);
			return -1;
		}
	}

	/**
	 * Method #2: getGoalByMeasure(int idPerson, int idMeasureDefinition) =>
	 * List<Goal> This method retrieve information about goal for a specified
	 * person and measure definition
	 */
	@Override
	public GoalWrapper getGoalByPersonMeasureName(int idPerson,
			String measureName) {
		System.out.println("--> REQUESTED: getGoalByPersonMeasureDef("
				+ idPerson + ", " + measureName + ")");
		
		Person p = Person.getPersonById(idPerson);
		// searches the measure definition associated with the name of
		// the measure
		MeasureDefinition md = new MeasureDefinition();
		md = MeasureDefinition.getMeasureDefinitionByName(measureName);
		
		List<Goal> goalList = null;
		GoalWrapper lgwrapper = new GoalWrapper();
		
		if ((p != null) && (md != null)) {
			System.out.println("Found Person by id = " + idPerson + " => "
					+ p.getIdPerson());
			System.out
					.println("Found Measure by name = "
							+ md.getMeasureName());
			
			goalList = Goal.getGoalByMeasureDef(p, md);
			lgwrapper.setGoalList(goalList);
			
			return lgwrapper;
			
		} else {
			System.out.println("Didn't find any Person with  idPerson = "
					+ idPerson + " and measureName = "
					+ measureName);
			lgwrapper.setGoalList(goalList);
			return lgwrapper;
		}
	}

	/**
	 * Method #3: getGoalList(int idPerson) => List<Goal> This method retrieve
	 * information about goal for a specified person
	 */
	@Override
	public GoalWrapper getGoalList(int idPerson) {
		System.out.println("--> REQUESTED: getGoalList(" + idPerson + ")");
		Person p = Person.getPersonById(idPerson);
		List<Goal> goalList = null;
		GoalWrapper lgwrapper = new GoalWrapper();
		if (p != null) {
			System.out.println("Found Person by id = " + idPerson + " => "
					+ p.getIdPerson());
			goalList = Goal.getGoalByPerson(p);
			lgwrapper.setGoalList(goalList);
			return lgwrapper;
		} else {
			System.out.println("Didn't find any Person with  id = " + idPerson);
			lgwrapper.setGoalList(goalList);
			return lgwrapper;
		}
	}

	/**
	 * Method #4: updateGoal(Goal goal) => idGoal This method update the goal
	 * information of the goal identified by its idGoal
	 */
	@Override
	public int updateGoal(Goal goal) {
		try {
			System.out.println("--> REQUEST: updateGoal(" + goal.toString()
					+ ")");
			Goal existing = Goal.getGoalById(goal.getIdGoal());
			int goalId;

			if (existing == null) {
				// the person is not found
				System.out.println("Din't find any Goal with id = "
						+ goal.getIdGoal());
				goalId = -2;

			} else {
				System.out.println("Found Goal by id = " + goal.getIdGoal());
				goal.setIdGoal(existing.getIdGoal());
				// checks if the client sent a name in order to update the goal
				// if there is no value, remain the previous value, the same
				// happens
				// with Type, StartDateGoal, EndDateGoal and Achieved
				if (goal.getValue() == 0) {
					goal.setValue(existing.getValue());
				}
				if (goal.getType() == null) {
					goal.setType(existing.getType());
				}
				if (goal.getStartDateGoal() == null) {
					goal.setStartDateGoal(existing.getStartDateGoal());
				}
				if (goal.getEndDateGoal() == null) {
					goal.setEndDateGoal(existing.getEndDateGoal());
				}
				if (goal.getAchieved() == null) {
					goal.setAchieved(existing.getAchieved());
				}
				goal.setPerson(existing.getPerson());
				goal.setMeasureDefinition(existing.getMeasureDefinition());

				Goal.updateGoal(goal);
				goalId = goal.getIdGoal();
			}

			return goalId;

		} catch (Exception e) {
			System.out.println("Goal not updated due the exception: " + e);
			return -1;
		}

	}

	/**
	 * Method #5: deleteGoal(int idGoal) => String message This method cancel
	 * from the database the goal identified by id
	 */
	@Override
	public String deleteGoal(int idGoal) {
		try {
			System.out.println("---> REQUEST: deleteGoal(" + idGoal + ")");
			String result = "";
			Goal g = Goal.getGoalById(idGoal);
			if (g != null) {
				Goal.removeGoal(g);
				result = "Goal with id: " + idGoal + " deleted";
				return result;
			} else {
				result = "Goal with id: " + idGoal + " not found!";
				return result;
			}
		} catch (Exception e) {
			return "Goal not deleted due to the exception: " + e;
		}
	}

	/**
	 * Implementation CRUD Operation to Measure
	 */

	/**
	 * Method #1: createMeasure(Measure measure, int idPerson) => idMeasure This
	 * method creates a new measure for a specified person identified by id
	 */
	@Override
	public int createMeasure(Measure measure, int idPerson) {
		try {
			System.out.println("--> REQUESTED: createMeasure("
					+ measure.getMeasureDefinition().getMeasureName() + ", "
					+ idPerson + ")");

			String measureName = measure.getMeasureDefinition()
					.getMeasureName();
			Calendar today = Calendar.getInstance();

			// searches the measure definition associated with the name of
			// the measure
			MeasureDefinition temp = new MeasureDefinition();
			temp = MeasureDefinition.getMeasureDefinitionByName(measureName);

			// search person identified by idPerson
			Person p = Person.getPersonById(idPerson);

			if (p != null) {
				System.out.println("Found Person by id = " + idPerson + " => "
						+ p.getIdPerson());

				if (measure.getTimestamp() == null) {
					measure.setTimestamp(today.getTime());
				}

				measure.setPerson(p);
				measure.setMeasureDefinition(temp);
				Measure.saveMeasure(measure);
				return measure.getIdMeasure();

			} else {
				System.out
						.println("Din't find any Person with id = " + idPerson);
				return -2;
			}

		} catch (Exception e) {
			System.out.println("Measure not saved due the exception: " + e);
			return -1;
		}
	}

	/**
	 * Method #2: getMeasure(int idPerson, String measureName) => List<Goal>
	 * This method retrieve information about measure for a specified person and
	 * measureName
	 */
	@Override
	public MeasureWrapper getMeasure(int idPerson, String measureName) {

		System.out.println("--> REQUESTED: getMeasure(" + idPerson + ", "
				+ measureName + ")");
		Person p = Person.getPersonById(idPerson);
		// searches the measure definition associated with the name of
		// the measure
		MeasureDefinition temp = new MeasureDefinition();
		temp = MeasureDefinition.getMeasureDefinitionByName(measureName);

		List<Measure> measureList = null;
		MeasureWrapper lmwrapper = new MeasureWrapper();

		if ((p != null) && (temp != null)) {
			System.out.println("Found Person by id = " + idPerson + " => "
					+ p.getIdPerson());
			System.out.println("Found MeasureDefinition by measureName = "
					+ temp.getMeasureName());
			measureList = Measure.getMeasureByMeasureName(p, temp);
			if (measureList.isEmpty()) {
				System.out.println("Didn't find any measureName = "
						+ temp.getMeasureName()
						+ " for a specified person with id = " + idPerson);
				lmwrapper.setMeasureList(measureList);
				return lmwrapper;
			}
			lmwrapper.setMeasureList(measureList);
			return lmwrapper;

		} else {
			System.out.println("Didn't find any Person with  idPerson = "
					+ idPerson + " and measureName = " + measureName);
			lmwrapper.setMeasureList(measureList);
			return lmwrapper;
		}
	}

	/**
	 * Method #3: getMeasureValue(int idPerson, String measureName, int
	 * idMeasure) => String value This method retrieve value of measureName
	 * identified by mid for a specified person identified by idPerson
	 */
	@Override
	public String getMeasureValue(int idPerson, String measureName,
			int idMeasure) {
		System.out.println("--> REQUESTED: getMeasureValue(" + idPerson + ", "
				+ measureName + ", " + idMeasure + ")");

		Person p = Person.getPersonById(idPerson);
		// searches the measure definition associated with the name of
		// the measure
		MeasureDefinition temp = new MeasureDefinition();
		temp = MeasureDefinition.getMeasureDefinitionByName(measureName);

		Measure measure = null;
		String value = null;
		try{
			System.out.println("Found Person by id = " + idPerson + " => "
					+ p.getIdPerson());
			System.out.println("Found MeasureDefinition by measureName = "
					+ temp.getMeasureName());
			System.out.println("Found Measure by id = "
					+ idMeasure);
			
			measure = Measure.getMeasureValueByMid(p, temp, idMeasure);
			value = measure.getValue();
			System.out.println("Found value => " + value);
			
		}catch(Exception ex){
			throw new RuntimeException("Didn't find any Person with  idPerson = "
					+ idPerson + ", measureName = " + measureName + " and idMeasure = " + idMeasure);
		}
		return value;
	}

	/**
	 * Method #4: getMeasureHistoryProfile(int idPerson) => List<Measure> This
	 * method retrieve information about measure for a specified person
	 */
	@Override
	public MeasureWrapper getMeasureHistoryProfile(int idPerson) {
		Person p = Person.getPersonById(idPerson);

		List<Measure> measureList = null;

		MeasureWrapper lmwrapper = new MeasureWrapper();

		if (p != null) {
			System.out.println("Found Person by id = " + idPerson + " => "
					+ p.getIdPerson());
			measureList = Measure.getMeasureByPerson(p);
			lmwrapper.setMeasureList(measureList);
			return lmwrapper;
		} else {
			System.out.println("Didn't find any Person with  id = " + idPerson);
			lmwrapper.setMeasureList(measureList);
			return lmwrapper;
		}
	}

	/**
	 * Method #5: updateMeasure(Measure measure) => mid This method update the
	 * measure information of the measure identified by its mid
	 */
	@Override
	public int updateMeasure(Measure measure) {
		try {
			System.out.println("--> REQUEST: updateMeasure("
					+ measure.toString() + ")");
			Measure existing = Measure.getMeasureById(measure.getIdMeasure());
			int measureId;

			if (existing == null) {
				// the person is not found
				System.out.println("Din't find any Measure with id: "
						+ measure.getIdMeasure());
				measureId = -2;

			} else {
				System.out.println("Found Measure by id = "
						+ measure.getIdMeasure());
				Calendar today = Calendar.getInstance();

				measure.setIdMeasure(existing.getIdMeasure());

				// checks if the client sent a name in order to update the goal
				// if there is no value, remain the previous value, the same
				// happens
				// with timestamp
				if (measure.getValue() == null) {
					measure.setValue(existing.getValue());
				}
				if (measure.getTimestamp() == null) {
					measure.setTimestamp(today.getTime());
				}

				measure.setPerson(existing.getPerson());
				measure.setMeasureDefinition(existing.getMeasureDefinition());

				Measure.updateMeasure(measure);
				measureId = measure.getIdMeasure();
			}

			return measureId;

		} catch (Exception e) {
			System.out.println("Measure not updated due the exception: " + e);
			return -1;
		}
	}

	/**
	 * Method #6: deleteMeasure(int idMeasure) => String message This method
	 * cancel from the database the measure identified by id
	 */
	@Override
	public String deleteMeasure(int idMeasure) {
		try {
			System.out
					.println("---> REQUEST: deleteMeasure(" + idMeasure + ")");
			String result = "";
			Measure m = Measure.getMeasureById(idMeasure);
			if (m != null) {
				Measure.removeMeasure(m);
				result = "Measure with id: " + idMeasure + " deleted";
				return result;
			} else {
				result = "Measure with id: " + idMeasure + " not found!";
				return result;
			}
		} catch (Exception e) {
			return "Measure not deleted due to the exception: " + e;
		}
	}

	/**
	 * Implementation CRUD Operation to Measure
	 */

	/**
	 * Method #1: getMeasureNames() => List<MeasureDefinition> 
	 * This method retrieves all information about
	 * the provided measure definitions
	 */
	@Override
	public MeasureDefinitionWrapper getMeasureDefinitionNames(){
		MeasureDefinitionWrapper lmdWrapper = new MeasureDefinitionWrapper();
		lmdWrapper.setMeasureDefinitionList(MeasureDefinition.getAll());
		return lmdWrapper;
	}
}
