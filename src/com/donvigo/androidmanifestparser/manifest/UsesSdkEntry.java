package com.donvigo.androidmanifestparser.manifest;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(strict = false)
public class UsesSdkEntry{
	@Attribute(name = "minSdkVersion", required=false)
    private String minSdkVersion;
	@Attribute(name = "targetSdkVersion", required=false)
    private String targetSdkVersion;
	
    public String getMinSdkVersion() {
		return minSdkVersion;
	}

	public void setMinSdkVersion(String minSdkVersion) {
		this.minSdkVersion = minSdkVersion;
	}

	public String getTargetSdkVersion() {
		return targetSdkVersion;
	}

	public void setTargetSdkVersion(String targetSdkVersion) {
		this.targetSdkVersion = targetSdkVersion;
	}
}
