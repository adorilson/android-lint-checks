package com.donvigo.androidmanifestparser.manifest;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by vgaidarji on 13.03.14.
 */
@Root(strict = false)
public class ApplicationEntry{
	@Attribute(name = "label")
    private String label;
	
    @ElementList(entry = "activity", inline = true)
    private List<ActivityEntry> activities;

    public List<ActivityEntry> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityEntry> activities) {
        this.activities = activities;
    }

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
