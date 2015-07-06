package br.ufrn;


import static com.android.SdkConstants.ANDROID_URI;
import static com.android.SdkConstants.TAG_INTENT_FILTER;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;

import lombok.ast.AstVisitor;
import lombok.ast.ClassDeclaration;
import lombok.ast.ForwardingAstVisitor;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.android.tools.lint.client.api.JavaParser.ResolvedClass;
import com.android.tools.lint.client.api.JavaParser.ResolvedNode;
import com.android.tools.lint.detector.api.Detector.JavaScanner;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.XmlContext;
import com.donvigo.androidmanifestparser.manifest.ActivityEntry;
import com.donvigo.androidmanifestparser.manifest.AndroidManifest;

public class PatternsDetector extends ResourceXmlDetector implements JavaScanner {
	private static final String CLASS_V7_ACTIONBARACTIVITY = "android.support.v7.app.ActionBarActivity";
	private static final String CLASS_V4_FRAGMENTACTIVITY = "android.support.v4.app.FragmentActivity";
	private static final String MAIN = "android.intent.action.MAIN";
	private static final String THEME_APPCOMPAT_LIGHT = "@style/Theme.AppCompat.Light";

	private String mainActivity = null;
	private static AndroidManifest manifest = null;
	private boolean DEBUG = false;
	
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
            		+ CLASS_V7_ACTIONBARACTIVITY + " class",
            "Checks if the main activity defined in manifest file extends the "
            + CLASS_V7_ACTIONBARACTIVITY + " class",
            Category.CORRECTNESS, 6, Severity.WARNING,
            new Implementation(
            		PatternsDetector.class,
            		EnumSet.of(Scope.ALL_JAVA_FILES, Scope.MANIFEST))
            );
	
	public static final Issue USESTHEMEAPPCOMPATLIGHT = Issue.create(
            "ActivityShouldUsesThemeAppCompatLight", "The activity should extends the "
            		+ THEME_APPCOMPAT_LIGHT + " theme",
            "Checks if the activity uses the "
            + CLASS_V7_ACTIONBARACTIVITY + " theme",
            Category.CORRECTNESS, 6, Severity.FATAL,
            new Implementation(
            		PatternsDetector.class,
            		EnumSet.of(Scope.ALL_JAVA_FILES, Scope.MANIFEST))
            ).addMoreInfo("http://developer.android.com/guide/topics/ui/actionbar.html#Adding");
	
	@Override
    public Collection<String> getApplicableElements() {
    	return Arrays.asList(
    			TAG_INTENT_FILTER
    			);
    }

	public void beforeCheckProject(Context context) {
		
		File file = context.getProject().getManifestFiles().iterator().next();
		
		manifest = AndroidManifest.getManifestFromXML(file.getAbsolutePath());
		
		if(DEBUG){
			System.out.println("\n === beforeCheckProject ===");
			System.out.println(manifest);
		}
		
		super.beforeCheckProject(context);
	}
	
	@Override
    public void visitElement(XmlContext context, Element element) {
		
		if(DEBUG){
			System.out.println("\n=== visitElement ===");
			System.out.println(element);
		}
		
    	// Discover the main activity
		// The main activity is the one with 
		// <action android:name="android.intent.action.MAIN" /> tag in
		// AndroidManifest file
		NodeList child = element.getChildNodes();
		for (int i=0; i<child.getLength(); i++) {
			Node c = child.item(i);
			NamedNodeMap attr = c.getAttributes();
			if (attr==null){
				continue;
			}
			Node node = attr.getNamedItemNS(ANDROID_URI, "name");
			if(node==null){
				continue;
			}
			String value = node.getNodeValue();
			if (value.equalsIgnoreCase(MAIN)){
				Node parentNode = element.getParentNode();
				attr = parentNode.getAttributes();
				Node mainActivity = attr.getNamedItemNS(ANDROID_URI, "name");
				int lastDot = mainActivity.getNodeValue().lastIndexOf('.');
				this.mainActivity = mainActivity.getNodeValue().substring(lastDot+1);
				break;
			}
		}
    }

	@Override
	public AstVisitor createJavaVisitor(JavaContext context) {
		return new PerformanceVisitor(context, this.mainActivity);
	}

	private static class PerformanceVisitor extends ForwardingAstVisitor {
		private final boolean DEBUG = false;
		private final JavaContext mContext;
		private String ACTIVITY;
		
		public PerformanceVisitor(JavaContext context, String activity) {
			mContext = context;
			this.ACTIVITY = activity;
		}
		
		@Override
		public boolean visitClassDeclaration(ClassDeclaration node) {
			ResolvedNode rNode = mContext.resolve(node);
			ResolvedClass rClass = (ResolvedClass) rNode;
			
			if(DEBUG){
				System.out.println("\n === visitClassDeclaration ===");
				System.out.println(rNode.getName());
			}
			
			if (node.astName().toString().equals(ACTIVITY)){
				boolean isFragmentActivity = rClass.isSubclassOf(CLASS_V4_FRAGMENTACTIVITY, false);
				if (!isFragmentActivity){
					report(node);
				}else{
					// TODO Check if the app really uses fragment
					// Identify some code patterns, maybe
					if(DEBUG){
						System.out.println("Extends " + CLASS_V4_FRAGMENTACTIVITY);
					}
				}
				
				boolean isActionBarActivity = rClass.isSubclassOf(CLASS_V7_ACTIONBARACTIVITY, false);
				if (!isActionBarActivity){
					report_actionbar(node);
				}else{
					 checkTheme(node);
				}
			}
			
			return super.visitClassDeclaration(node);
			
		}

		private void checkTheme(ClassDeclaration node) {
			
			ResolvedNode rNode = mContext.resolve(node);
			String name = null;
			name = rNode.getName().replace(manifest.getPackageName(), "");
			
			if(DEBUG){
				System.out.println("\n === checkTheme ===");
				System.out.println(manifest.getPackageName());
				System.out.println("rNode.getName(): " + rNode.getName());
				System.out.println("name: " + name);
			}
			
			ActivityEntry activity = null;
			activity = manifest.getApplication().getActivity(name);
			
			if(DEBUG){
				if (activity!=null){
					System.out.println("activity.getName(): " + activity.getName());
					System.out.println("activity.getTheme(): " + activity.getTheme());
				}
			}
			
			if(activity.getTheme()==null){
				mContext.report(USESTHEMEAPPCOMPATLIGHT, mContext.getLocation(node), "Should uses the @style/Theme.AppCompat.Light style");
			}else{
				if (!activity.getTheme().equals(THEME_APPCOMPAT_LIGHT)){
					mContext.report(USESTHEMEAPPCOMPATLIGHT, mContext.getLocation(node), "Should uses the @style/Theme.AppCompat.Light style");
				}
			}
		}

		private void report(ClassDeclaration node) {
			String message = ACTIVITY + " class should extends " + CLASS_V4_FRAGMENTACTIVITY;
			Location location = mContext.getLocation(node.astName());
			mContext.report(CHECKFRAGMENTACTIVITY, node, location, message);
		}

		private void report_actionbar(ClassDeclaration node) {
			String message = ACTIVITY + " class should extends " + CLASS_V7_ACTIONBARACTIVITY;
			Location location = mContext.getLocation(node.astName());
			mContext.report(USESACTIONBAR, node, location, message);
		}
	}
}

