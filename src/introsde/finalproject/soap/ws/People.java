package introsde.finalproject.soap.ws;

import introsde.finalproject.soap.model.Person;
import introsde.finalproject.soap.wrapper.PersonWrapper;

import java.util.List;

import javax.jws.*;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

@WebService
@SOAPBinding(style=Style.DOCUMENT, use=Use.LITERAL)
public interface People {

	/**
	 *  ================================= 
  	 		Definition CRUD Operation to Person              
  		================================= **/
	
	@WebMethod(operationName="createPerson")
    @WebResult(name="personId") 
    public int addPerson(@WebParam(name="person") Person person);
 
    @WebMethod(operationName="getPerson")
    @WebResult(name="person") 
    public Person readPerson(@WebParam(name="personId") int id);
    
    @WebMethod(operationName="updatePerson")
    @WebResult(name="personId") 
    public int updatePerson(@WebParam(name="person") Person person);
    
    @WebMethod(operationName="deletePerson")
    @WebResult(name="responsePersonCode") 
    public int deletePerson(@WebParam(name="personId") int id);
    
    @WebMethod(operationName="getPersonList")
	@WebResult(name="people") 
	public PersonWrapper getPersonList();
    
    /*@WebMethod(operationName="getVitalSigns")
    @WebResult(name="vitalSigns") 
    public ListMeasureWrapper getVitalSigns(@WebParam(name="personId") int id);
    
    @WebMethod(operationName="getCurrentHealth")
    @WebResult(name="currentHealth") 
    public ListMeasureWrapper getCurrentHealth(@WebParam(name="personId") int id);*/
	
	
	
}
