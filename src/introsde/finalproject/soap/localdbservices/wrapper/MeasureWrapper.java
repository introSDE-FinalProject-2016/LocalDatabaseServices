package introsde.finalproject.soap.localdbservices.wrapper;

import introsde.finalproject.soap.localdbservices.model.Measure;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Wrapper used when are listened the measure of all the Measure
 *
 */
@XmlRootElement(name="HealthProfile-history")
public class MeasureWrapper {

	@XmlElement(name="measure")
	@JsonProperty("measure")
	public List<Measure> measureList = new ArrayList<Measure>();
	
	public void setMeasureList(List<Measure> measureList) {
		this.measureList = measureList;
	}
}