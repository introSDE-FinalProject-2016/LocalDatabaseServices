package introsde.finalproject.soap.ws;

import introsde.finalproject.soap.model.Person;
import introsde.finalproject.soap.wrapper.PersonWrapper;

import java.util.List;

import javax.jws.WebService;

/**
 * Service Implementation
 * @author yuly
 *
 */
@WebService(endpointInterface = "introsde.finalproject.soap.ws.People",
serviceName="PeopleService")
public class PeopleImpl implements People{

	/** ================================= 
	   	 Implementation CRUD Operation to Person              
	   	================================= **/
	
	@Override
	public PersonWrapper getPersonList() {
		PersonWrapper pWrapper = new PersonWrapper();
		pWrapper.setPersonList(Person.getAll());
		return pWrapper;
	}

	@Override
	public int addPerson(Person person) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Person readPerson(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updatePerson(Person person) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deletePerson(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

}
