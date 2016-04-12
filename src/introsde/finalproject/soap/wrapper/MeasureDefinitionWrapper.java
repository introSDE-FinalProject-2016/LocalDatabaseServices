package introsde.finalproject.soap.wrapper;

import introsde.finalproject.soap.model.MeasureDefinition;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Wrapper used when are listened the measureName of all the MeasureDefinition
 *
 */
@XmlRootElement(name="measureNames")
public class MeasureDefinitionWrapper {
	
	@XmlElement(name="measureName")
	@JsonProperty("measureName")
	public List<MeasureDefinition> measureDefinitionList = new ArrayList<MeasureDefinition>();
	
	public void setMeasureDefinitionList(List<MeasureDefinition> measureDefinitionList) {
		this.measureDefinitionList = measureDefinitionList;
	}
}
