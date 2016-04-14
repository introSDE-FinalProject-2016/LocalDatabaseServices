package introsde.finalproject.soap.localdbservices.ws;

import introsde.finalproject.soap.localdbservices.model.Goal;
import introsde.finalproject.soap.localdbservices.model.Measure;
import introsde.finalproject.soap.localdbservices.model.Person;
import introsde.finalproject.soap.localdbservices.wrapper.GoalWrapper;
import introsde.finalproject.soap.localdbservices.wrapper.MeasureDefinitionWrapper;
import introsde.finalproject.soap.localdbservices.wrapper.MeasureWrapper;
import introsde.finalproject.soap.localdbservices.wrapper.PersonWrapper;

import java.util.List;

import javax.jws.*;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

@WebService
@SOAPBinding(style=Style.DOCUMENT, use=Use.LITERAL)
public interface People {

	/**
	 * Definition CRUD Operation to Person
	 */
	@WebMethod(operationName="getPersonList")
	@WebResult(name="people") 
	public PersonWrapper getPersonList();
	
	@WebMethod(operationName="getPerson")
    @WebResult(name="person") 
    public Person getPerson(@WebParam(name="pid") int idPerson);
    
	@WebMethod(operationName="createPerson")
    @WebResult(name="pid") 
    public int createPerson(@WebParam(name="person") Person person, @WebParam(name="measure") List<Measure> measure);
    
	@WebMethod(operationName="updatePerson")
    @WebResult(name="pid") 
    public int updatePerson(@WebParam(name="person") Person person);
    
	@WebMethod(operationName="deletePerson")
    @WebResult(name="responsePersonCode") 
    public String deletePerson(@WebParam(name="pid") int idPerson);
	
	
	/**
	 * Definition CRUD Operation to Goal
	 */
	@WebMethod(operationName="createGoal")
	@WebResult(name="gid")
	public int createGoal(@WebParam(name="goal") Goal goal, @WebParam(name="pid") int idPerson);
	
	@WebMethod(operationName="getGoal")
	@WebResult(name="goals")
	public GoalWrapper getGoalByPersonMeasureName(@WebParam(name="pid") int idPerson, @WebParam(name="measureName") String measureName);
	
	@WebMethod(operationName="getGoalList")
	@WebResult(name="goals")
	public GoalWrapper getGoalList(@WebParam(name="pid") int idPerson);
	
	@WebMethod(operationName="updateGoal")
    @WebResult(name="gid") 
    public int updateGoal(@WebParam(name="goal") Goal goal);
    
	@WebMethod(operationName="deleteGoal")
    @WebResult(name="responseGoalCode") 
    public String deleteGoal(@WebParam(name="gid") int idGoal);
	
	
	/**
	 * Definition CRUD Operation to Measure
	 */
	@WebMethod(operationName="createMeasure")
	@WebResult(name="mid")
	public int createMeasure(@WebParam(name="measure") Measure measure, @WebParam(name="pid") int idPerson);
	
	@WebMethod(operationName="getMeasure")
	@WebResult(name="measuresHealth")
	public MeasureWrapper getMeasure(@WebParam(name="pid") int idPerson, @WebParam(name="measureName") String measureName);

	@WebMethod(operationName="getMeasureValue")
	@WebResult(name="value")
	public String getMeasureValue(@WebParam(name="pid") int idPerson, @WebParam(name="measureName") String measureName, @WebParam(name="mid") int idMeasure);
	
	@WebMethod(operationName="getMeasureList")
	@WebResult(name="historyHealth")
	public MeasureWrapper getMeasureHistoryProfile(@WebParam(name="pid") int idPerson);
	
	@WebMethod(operationName="updateMeasure")
    @WebResult(name="mid") 
    public int updateMeasure(@WebParam(name="measure") Measure measure);
	
	@WebMethod(operationName="deleteMeasure")
    @WebResult(name="responseMeasureCode") 
    public String deleteMeasure(@WebParam(name="mid") int idMeasure);
	
	/**
	 * Definition CRUD Operation to MeasureDefinition
	 */
	@WebMethod(operationName="getMeasureDefinitionNames")
    @WebResult(name="measureNames")
    public MeasureDefinitionWrapper getMeasureDefinitionNames();

}
