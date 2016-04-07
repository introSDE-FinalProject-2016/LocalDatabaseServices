package introsde.finalproject.soap.ws;

import introsde.finalproject.soap.model.Measure;
import introsde.finalproject.soap.model.MeasureDefinition;
import introsde.finalproject.soap.model.Person;
import introsde.finalproject.soap.wrapper.PersonWrapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.jws.WebService;

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
	 * Method #1: getPersonList() => List<Person>
	 * Returns the list all the people in the database
	 */
	@Override
	public PersonWrapper getPersonList() {
		System.out.println("---> REQUEST: getPersonList()");
		PersonWrapper pWrapper = new PersonWrapper();
		pWrapper.setPersonList(Person.getAll());
		return pWrapper;
	}
	
	/**
	 * Method #2: getPerson(int idPerson) => Person
	 * Returns all the personal information plus current measures of one person identified by {idPerson}
	 */
	@Override
	public Person getPerson(int idPerson) {
		System.out.println("---> REQUEST: getPerson(" + idPerson + ")");
		Person p = Person.getPersonById(idPerson);
		if (p != null) {
			System.out.println("---> Found Person by id = " + idPerson + " => "
					+ p.getFirstname());
		} else {
			System.out.println("---> Didn't find any Person with  id = " + idPerson);
		}
		return p;
	}

	@Override
	public int createPerson(Person person, List<Measure> measure) {
		//phase #1: check if person includes some measures
		//if is true
		if(measure == null){
			System.out.println("REQUESTED: createPerson(" + person.getFirstname() + ") without measure");
    		Person.savePerson(person);
    		return person.getIdPerson();
    		
		}//else if is false
		else{
			//removes the currentMeasure in the person and puts them in another variable
    		System.out.println("REQUESTED: createPerson(" +  person.getFirstname() + ") with new measure");
    		Person p = Person.savePerson(person);
    		//creates the today date
    		Calendar today = Calendar.getInstance();
			ArrayList<Integer> control = new ArrayList<>();
			for(int i=0; i<measure.size(); i++){
				//retrieves the name of the measures inserted by the client (e.g. weight)
    			String measureName = measure.get(i).getMeasureDefinition().getMeasureName();
    			
    			//searches the measure definition associated with the name of the measure
    			MeasureDefinition temp = new MeasureDefinition();
    			temp = MeasureDefinition.getMeasureDefinitionByName(measureName);
				
				if(!control.contains(temp.getIdMeasureDefinition())){
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
	
	
	/*@Override
	public int createPerson(Person person) {
		try {
			System.out.println("---> Creating new Person without measures");
			Person.savePerson(person);
			return person.getIdPerson();
		} catch (Exception e) {
			System.out.println("Person not saved due to the exeception: " + e);
			return -1;
		}
	}

	@Override
	public int updatePerson(Person person) {
		try {
			int id = person.getIdPerson();
			if (id != 0) {
				if (Person.getPersonById(id) != null) {
					Person x = Person.getPersonById(id);
					person.setMeasures(x.getMeasures());
					
					if (person.getFirstname() == null) {
						person.setFirstname(x.getFirstname());
					}

					if (person.getLastname() == null) {
						person.setLastname(x.getLastname());
					}

					if (person.getBirthdate() == null) {
						person.setBirthdate(x.getBirthdate());
					}

					if (person.getEmail() == null) {
						person.setEmail(x.getEmail());
					}

					if (person.getUsername() == null) {
						person.setUsername(x.getUsername());
					}

					if (person.getPassword() == null) {
						person.setPassword(x.getPassword());
					}

					Person.updatePerson(person);
					return person.getIdPerson();
				} else {
					System.out.println("Does not exist a Person with id: " + id);
					return -2;
				}
			} else {
				System.out.println("personId is equals to " + id);
				return -2;
			}
		} catch (Exception e) {
			System.out.println("Person not updated due the exception: " + e);
			return -1;
		}
	}

	@Override
	public int deletePerson(int id) {
		try {
			System.out.println("---> Deleting Person by id = " + id);
			Person p = Person.getPersonById(id);
			if (p != null) {
				Person.removePerson(p);
				return 1;
			} else {
				System.out.println("Does not exist a Person with id: " + id);
				return -2;
			}
		} catch (Exception e) {
			System.out.println("Person not deleted due to the exception: " + e);
			return -1;
		}
	}*/

	

}
