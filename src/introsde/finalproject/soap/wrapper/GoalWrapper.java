package introsde.finalproject.soap.wrapper;

import introsde.finalproject.soap.model.Goal;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Wrapper used when are listened the goals of all the Goal
 *
 */
@XmlRootElement(name="goals")
public class GoalWrapper {

	@XmlElement(name="goal")
	@JsonProperty("goal")
	public List<Goal> goalList = new ArrayList<Goal>();
	
	public void setGoalList(List<Goal> goalList) {
		this.goalList = goalList;
	}
}
