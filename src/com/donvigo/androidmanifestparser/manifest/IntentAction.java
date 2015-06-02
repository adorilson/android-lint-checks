package com.donvigo.androidmanifestparser.manifest;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Created by vgaidarji on 13.03.14.
 */
@Root(strict = false)
public class IntentAction{
    @Attribute(name = "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}