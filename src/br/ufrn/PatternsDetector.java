package br.ufrn;


import static com.android.SdkConstants.ANDROID_URI;
import static com.android.SdkConstants.TAG_INTENT_FILTER;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;

import lombok.ast.AstVisitor;
import lombok.ast.ClassDeclaration;
import lombok.ast.ForwardingAstVisitor;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.android.tools.lint.detector.api.Detector.JavaScanner;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.XmlContext;

public class PatternsDetector extends ResourceXmlDetector implements JavaScanner {
	private final static String FRAGMENT = "FragmentActivity";
	private final static String ACTIONBARACTIVITY = "ActionBarActivity";

	private String mainActivity;
	
	public static final Issue CHECKFRAGMENTACTIVITY = Issue.create(
            "MainActivityIsFragmentActivity", "The main activity should extends"
            		+ "FragmentActivity class",
            "Checks if the main activity defined in manifest file extends the"
            + "FragmentActivity class",
            Category.CORRECTNESS, 6, Severity.WARNING,
            new Implementation(
            		PatternsDetector.class,
            		EnumSet.of(Scope.ALL_JAVA_FILES, Scope.MANIFEST))
            );

	public static final Issue USESACTIONBAR = Issue.create(
            "AppShouldUsesActionBar", "The main activity should extends the "
            		+ ACTIONBARACTIVITY + " class",
            "Checks if the main activity defined in manifest file extends the "
            + ACTIONBARACTIVITY + " class",
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
		// TODO If we have more than one element with intent-filter tag?
    	Node parentNode = element.getParentNode();
    	NamedNodeMap attr = parentNode.getAttributes();
    	Node mainActivity = attr.getNamedItemNS(ANDROID_URI, "name");
    	int lastDot = mainActivity.getNodeValue().lastIndexOf('.');
    	this.mainActivity = mainActivity.getNodeValue().substring(lastDot+1);
    }

	@Override
	public AstVisitor createJavaVisitor(JavaContext context) {
		return new PerformanceVisitor(context, this.mainActivity);
	}

	private static class PerformanceVisitor extends ForwardingAstVisitor {
		private final JavaContext mContext;
		private String ACTIVITY;

		public PerformanceVisitor(JavaContext context, String activity) {
			mContext = context;
			this.ACTIVITY = activity;
		}

		@Override
		public boolean visitClassDeclaration(ClassDeclaration node) {
			if (node.astName().toString().equals(ACTIVITY)){
				if (!node.astExtending().toString().equals(FRAGMENT)){
					report(node);
				}
				if (!node.astExtending().toString().equals(ACTIONBARACTIVITY)){
					report_actionbar(node);
				}
			}
			return super.visitClassDeclaration(node);
		}

		private void report(ClassDeclaration node) {
			String message = ACTIVITY + " class should extends " + FRAGMENT;
			Location location = mContext.getLocation(node.astName());
			mContext.report(CHECKFRAGMENTACTIVITY, node, location, message);
		}

		private void report_actionbar(ClassDeclaration node) {
			String message = ACTIVITY + " class should extends " + ACTIONBARACTIVITY;
			Location location = mContext.getLocation(node.astName());
			mContext.report(USESACTIONBAR, node, location, message);
		}
	}
}
