package com.donvigo.androidmanifestparser.manifest;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by vgaidarji on 13.03.14.
 */
@Root(strict = false)
public class IntentFilterEntry{
    @ElementList(entry = "action", inline = true, required = false)
    private List<IntentAction> actions;
    @ElementList(entry = "category", inline = true, required = false)
    private List<IntentCategory> categories;

    public List<IntentAction> getActions() {
        return actions;
    }

    public void setActions(List<IntentAction> actions) {
        this.actions = actions;
    }

    public List<IntentCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<IntentCategory> categories) {
        this.categories = categories;
    }
}