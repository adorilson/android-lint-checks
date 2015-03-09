package br.ufrn;


import static com.android.SdkConstants.ANDROID_URI;
import static com.android.SdkConstants.TAG_INTENT_FILTER;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.android.tools.lint.detector.api.Detector.JavaScanner;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.XmlContext;

public class PatternsDetector extends ResourceXmlDetector implements JavaScanner {
	private final static String FRAGMENT = "FragmentActivity";

	private String mainActivity;
	
	public static final Issue CHECKFRAGMENTACTIVITY = Issue.create(
            "MainActivityIsFragmentActivity", "The main activity should extends"
            		+ "FragmentActivity class",
            "Checks if the main activity definided in manifest file extends the"
            + "FragmentActivity class",
            Category.CORRECTNESS, 6, Severity.WARNING,
            new Implementation(
            		PatternsDetector.class,
            		EnumSet.of(Scope.ALL_JAVA_FILES, Scope.MANIFEST))
            );

	@Override
    public Collection<String> getApplicableElements() {
    	return Arrays.asList(
    			TAG_INTENT_FILTER
    			);
    }

	@Override
    public void visitElement(XmlContext context, Element element) {
    	// Discover the main activity
    	Node parentNode = element.getParentNode();
    	NamedNodeMap attr = parentNode.getAttributes();
    	Node mainActivity = attr.getNamedItemNS(ANDROID_URI, "name");
    	int lastDot = mainActivity.getNodeValue().lastIndexOf('.');
    	this.mainActivity = mainActivity.getNodeValue().substring(lastDot+1);
    }
}
