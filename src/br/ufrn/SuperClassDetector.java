package br.ufrn;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import lombok.ast.ClassDeclaration;
import lombok.ast.Node;

import com.android.SdkConstants;
import com.android.tools.lint.client.api.JavaParser.ResolvedClass;
import com.android.tools.lint.client.api.JavaParser.ResolvedNode;
import com.android.tools.lint.detector.api.Detector.JavaScanner;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;

public class SuperClassDetector extends Detector implements JavaScanner {
	
	private static final String CLASS_V7_ACTIONBARACTIVITY = "android.support.v7.app.ActionBarActivity";
	private static final String CLASS_V4_FRAGMENTACTIVITY = "android.support.v4.app.FragmentActivity";

	public static final Issue FRAGMENT_ACTIVITY = Issue.create(
            "ActivityIsFragmentActivity",
            "The activities should extends " + CLASS_V4_FRAGMENTACTIVITY + " class",
            "Checks if the activities extends the " + CLASS_V4_FRAGMENTACTIVITY + " class",
            Category.CORRECTNESS, 6, Severity.WARNING,
            new Implementation(
            		SuperClassDetector.class,
            		EnumSet.of(Scope.ALL_JAVA_FILES))
            );

	public static final Issue ACTIONBAR_ACTIVITY = Issue.create(
            "AppShouldUsesActionBar",
            "The activities should extends the " + CLASS_V7_ACTIONBARACTIVITY + " class",
            "Checks if the activities extends the " + CLASS_V7_ACTIONBARACTIVITY + " class",
            Category.CORRECTNESS, 8, Severity.WARNING,
            new Implementation(
            		SuperClassDetector.class,
            		EnumSet.of(Scope.ALL_JAVA_FILES))
            );
	
	@Override
	public List<String> applicableSuperClasses() {
		return Collections.singletonList(SdkConstants.CLASS_ACTIVITY);
	}
	
	@Override
	public void checkClass(JavaContext context, ClassDeclaration declaration,
			Node node, ResolvedClass resolvedClass) {
		
		// checking if the class extends CLASS_V4_FRAGMENTACTIVITY
		boolean isFragmentActivity = resolvedClass.isSubclassOf(CLASS_V4_FRAGMENTACTIVITY, false);
		if (!isFragmentActivity){
			ResolvedNode rNode = context.resolve(node);
			Location location = context.getLocation(node);
			String message = rNode.getName() + " class should extends " + CLASS_V4_FRAGMENTACTIVITY;
			context.report(FRAGMENT_ACTIVITY, node, location, message);
		}
		
		// checking if the class extends CLASS_V7_ACTIONBARACTIVITY
		boolean isActionBarActivity = resolvedClass.isSubclassOf(CLASS_V7_ACTIONBARACTIVITY, false);
		if (!isActionBarActivity){
			ResolvedNode rNode = context.resolve(node);
			Location location = context.getLocation(node);
			String message = rNode.getName() + " class should extends " + CLASS_V7_ACTIONBARACTIVITY;
			context.report(ACTIONBAR_ACTIVITY, node, location, message);
		}

		super.checkClass(context, declaration, node, resolvedClass);
	}
}
