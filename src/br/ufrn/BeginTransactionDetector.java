package br.ufrn;


/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//import static com.android.tools.lint.detector.api.LintConstants.ANDROID_APP_ACTIVITY;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.donvigo.androidmanifestparser.manifest.ActivityEntry;
import com.donvigo.androidmanifestparser.manifest.AndroidManifest;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

/**
 * Checks for problems with wakelocks (such as failing to release them)
 * which can lead to unnecessary battery usage.
 */
public class BeginTransactionDetector extends ResourceXmlDetector implements Detector.JavaScanner, Detector.ClassScanner{
    /** Problems using wakelocks */
	
	private static final Implementation IMPLEMENTATION = new Implementation(
            BeginTransactionDetector.class,
            Scope.CLASS_FILE_SCOPE);
	
    public static final Issue ISSUE = Issue.create(
        "BeginTransationDetector", //$NON-NLS-1$
        "Looks for problems with fragments usage",
        "In order to use fragments API, the application should uses a FragmentTransaction." +
        "This detector look for call to FragmentManager.beginTransaction method in activities classes." +
        "\n" +
        "See " +
        "",
        Category.CORRECTNESS,
        9,
        Severity.WARNING,
        IMPLEMENTATION);
    
    private static final String BEGINTRANSACTION_OWNER = "android/support/v4/app/FragmentManager";
    private static final String BEGINTRANSACTION_METHOD = "beginTransaction";
    private static final String CLASS_V4_FRAGMENTACTIVITY = "android.support.v4.app.FragmentActivity";
    
    private HashMap<String, ActivityEntry> activities = null;
    
    /** Print diagnostics during analysis (display flow control graph etc).
     * Make sure you add the asm-debug or asm-util jars to the runtime classpath
     * as well since the opcode integer to string mapping display routine looks for
     * it via reflection. */
    private static boolean DEBUG = true;
    
    /** Constructs a new {@link WakelockDetector} */
    public BeginTransactionDetector() {
    }
    
    /*
    @Override
    public void afterCheckProject(@NonNull Context context) {
        if (!mHasBeginTransaction && context.getDriver().getPhase() == 1) {
            // Gather positions of the acquire calls
            context.getDriver().requestRepeat(this, Scope.CLASS_FILE_SCOPE);
        }
    }
	*/
    
 // TODO Replace the HashMap activities by this. Is possible in next versions Lint?
    
    @Override
	public List<String> applicableSuperClasses() {
    	// TODO Use this method instead of discovery activities in manifest file
    	return  Collections.singletonList(CLASS_V4_FRAGMENTACTIVITY);
	}
    
    @Override
    public void checkClass(ClassContext context, ClassNode classNode) {
    	super.checkClass(context, classNode);
    	
    	location = context.getLocation(classNode);
    	
    	
    	if(DEBUG){
    		System.out.println("=== checkClass ===");
    		System.out.println("classNode.name:" + classNode.name);
    		System.out.println("classNode.superName: " + classNode.superName);
    		System.out.println("location = " + location);
    	}
    	
    	//context.
    	
    }
    
    
    public void beforeCheckProject(Context context) {
    	if(DEBUG){
    		System.out.println("=== beforeCheckProject ===");
    	}
    	
    	activities = new HashMap<String, ActivityEntry>();
    	
    	for (Iterator<File> iterator = context.getProject().getManifestFiles().iterator(); iterator.hasNext();) {
			File file = (File) iterator.next();
			AndroidManifest manifest = AndroidManifest.getManifestFromXML(file.getAbsolutePath());
			for(Iterator<ActivityEntry> it = manifest.getApplication().getActivities().iterator(); it.hasNext();){
				ActivityEntry entry = (ActivityEntry) it.next();
				int lastDot = entry.getName().lastIndexOf('.');
				String activityName = entry.getName().substring(lastDot+1);
				activities.put(activityName, entry);
			}
		}
    	
    	if(DEBUG){
    		System.out.println("=== Activities founded ===");
    		for(Iterator<Entry<String, ActivityEntry>> it = activities.entrySet().iterator(); it.hasNext(); ){
    			Entry<String, ActivityEntry> entry = (Entry<String, ActivityEntry>)it.next();
    			System.out.println(entry.getValue().getName() + "->" + entry.getKey());
    		}
    	}
    };
    
    
    
    @Override
	public void beforeCheckFile(Context context) {
    	super.beforeCheckFile(context);
    	
    	if(DEBUG){
    		System.out.println(">> Starting with... " + context.file.getName());
    	}
    	
    	mHasBeginTransaction = false;
    	location = null;
	}
	
    
    @Override
	public void afterCheckFile(Context context) {
    	super.afterCheckFile(context);
    	
    	String fileName = context.file.getName();
    	int dotIndex = fileName.indexOf('.');
    	String className = fileName.substring(0, dotIndex);
    	
    	if (activities.containsKey(className)){
	    	if(DEBUG){
	    		System.out.println("\n === afterCheckFile(Context context) ===");
	    		System.out.println("context.file.getName(): " + context.file.getName());
	    		System.out.println("mHasBeginTransaction: " + mHasBeginTransaction);
	    		System.out.println("activities.containsKey(className): " + activities.containsKey(className));
	    	}
	    	
	    	if (!mHasBeginTransaction){
	    		if(DEBUG){
	    			System.out.println("Reporting error to " + location);
	    		}
	    			
	    		context.report(ISSUE, location,
	    			"You should call FragmentManager.beginTransaction");
	    	}
    	}
    	
    	if(DEBUG){
    		System.out.println("<< Finishing with... " + context.file.getName());
    	}
    	
    	
	}
	

	// ---- Implements ClassScanner ----
    /** Whether any {@code beginTransaction()} calls have been encountered */
    private boolean mHasBeginTransaction;
    
    private Location location;
    
    
    @Override
    @Nullable
    public List<String> getApplicableCallNames() {
    	return Arrays.asList(BEGINTRANSACTION_METHOD);
    }
    
    @Override
    public void checkCall(@NonNull ClassContext context, @NonNull ClassNode classNode,
    		@NonNull MethodNode method, @NonNull MethodInsnNode call){
		
    	String className = classNode.name.substring(classNode.name.lastIndexOf("/")+1);
		
		if(DEBUG){
			System.out.println("=== checkCall === ");
			System.out.println("classNode.name: " + classNode.name);
    		System.out.println("classNode.superName: " + classNode.superName);
    		System.out.println("MethodNode: " + method.name);
    		System.out.println("MethodInsnNode: " + call.owner + "." + call.name);
    		System.out.println("\nclassName: " + className);
    		System.out.println("activities.containsKey(className): " + activities.containsKey(className));
    	}
		
		// If not a activity, do nothing
		if (!activities.containsKey(className)){
    		return;
    	}
		
		location = context.getLocation(classNode);
		
		if(DEBUG){
			System.out.println("location setted to: " + location);
		}
		
    	if(call.owner.equals(BEGINTRANSACTION_OWNER)){
    		if(call.name.equals(BEGINTRANSACTION_METHOD)){
    			mHasBeginTransaction = true;
    			if(DEBUG){
    				System.out.println("mHasBeginTransaction was setted to true...");
    			}
    		}
    	}
    	
    }
    
}
