package introsde.finalproject.soap.localdbservices.wrapper;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import introsde.finalproject.soap.localdbservices.model.Person;


/**
 * Wrapper used when are listened the people of all the Person
 *
 */
@XmlRootElement(name="People")
public class PersonWrapper {
	
	@XmlElement(name="person")
	@JsonProperty("person")
	public List<Person> personList = new ArrayList<Person>();

	public void setPersonList(List<Person> personList) {
		this.personList = personList;
	}
}
