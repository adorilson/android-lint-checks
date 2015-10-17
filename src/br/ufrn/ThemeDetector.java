package br.ufrn;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;


import org.w3c.dom.Element;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector.JavaScanner;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.Speed;
import com.android.tools.lint.detector.api.XmlContext;
import com.donvigo.androidmanifestparser.manifest.AndroidManifest;

import static com.android.SdkConstants.TAG_STYLE;
import static com.android.SdkConstants.ATTR_PARENT;
import static com.android.resources.ResourceFolderType.VALUES;

public class ThemeDetector extends LayoutDetector{
	
	private static final String THEME_APPCOMPAT_LIGHT = "@style/Theme.AppCompat.Light";
	
	private boolean DEBUG = true;
	
	/** The main issue discovered by this detector */
	public static final Issue USESTHEMEAPPCOMPATLIGHT2 = Issue.create(
            "ActivityShouldUsesThemeAppCompatLight2", "The activity should extends the "
            		+ THEME_APPCOMPAT_LIGHT + " theme",
            "Checks if the activity uses the "
            + THEME_APPCOMPAT_LIGHT + " theme",
            Category.CORRECTNESS, 6, Severity.FATAL,
            new Implementation(
            		ThemeDetector.class,
            		EnumSet.of(Scope.ALL_RESOURCE_FILES))
            ).addMoreInfo("http://developer.android.com/guide/topics/ui/actionbar.html#Adding");
	
	@NonNull
    @Override
    public Speed getSpeed() {
        return Speed.FAST;
    }

    @Override
    public boolean appliesTo(@NonNull ResourceFolderType folderType) {
    	return folderType == VALUES;
    }
    
	
    @Override
    public void afterCheckProject(@NonNull Context context) {
        // Process checks in two phases: THIS REALLY NEED ??????????
        // Phase 1: Gather styles and includes (styles are encountered after the layouts
        // so we can't do it in a single phase, and includes can be affected by includes from
        // layouts we haven't seen yet)
        // Phase 2: Process layouts, using gathered style and include data, and mark layouts
        // not known.
        //
    	if (DEBUG){
    		System.out.println("themedetector");
    		System.out.println("afterCheckProject");
    	}
    	
        if (context.getPhase() == 1) {
            //checkSizeSetInTheme();
            context.requestRepeat(this, Scope.ALL_RESOURCES_SCOPE);
        }
    }
    
    @Override
	public void beforeCheckProject(Context context) {
		if(DEBUG){
			System.out.println("themedetector");
			System.out.println("\n === beforeCheckProject ===");
		}
		
		super.beforeCheckProject(context);
	}
    
 // ---- Implements XmlScanner ----
    @Override
    public Collection<String> getApplicableElements() {
    	if(DEBUG){
    		System.out.println("themedetector");
    		System.out.println("getApplicableElements()");
    	}
    	
    	return Arrays.asList(
    			TAG_STYLE
    			);
    }

    
    @Override
    public void visitElement(XmlContext context, Element element) {
    	ResourceFolderType folderType = context.getResourceFolderType();
        int phase = context.getPhase();
        String tag = element.getTagName();
        
        if(DEBUG){
        	System.out.println("ThemeDetector check");
        	System.out.println("visitElement(...)");
        	System.out.println("folderType = " + folderType);
        	System.out.println("phase = " + phase);
        	System.out.println("tag= " + tag);
        }
        
    }
}
