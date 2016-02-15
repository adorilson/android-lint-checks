package br.ufrn;

import java.util.Arrays;
import java.util.Collection;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import com.android.annotations.NonNull;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.checks.ManifestDetector;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.XmlContext;

import static com.android.SdkConstants.TAG_ACTIVITY;
import static com.android.SdkConstants.ATTR_THEME;
import static com.android.SdkConstants.ATTR_NAME;
import static com.android.SdkConstants.ANDROID_URI;

public class ThemeDetector extends ManifestDetector{
	
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
            		Scope.MANIFEST_SCOPE)
            ).addMoreInfo("http://developer.android.com/guide/topics/ui/actionbar.html#Adding");

	// ---- Implements Detector.XmlScanner (ManifestDetector implements this) ----
    @Override
    public Collection<String> getApplicableElements() {
        return Arrays.asList(
                TAG_ACTIVITY
        );
    }

    @Override
    public void visitElement(@NonNull XmlContext context, @NonNull Element element) {
    	
    	if (DEBUG){
    		System.out.println("=== ThemeDetector check ===");
    		System.out.println("visitElement(...)");
    	}

    	Attr theme = element.getAttributeNodeNS(ANDROID_URI, ATTR_THEME);
        
        if(DEBUG){
        	ResourceFolderType folderType = context.getResourceFolderType();
            int phase = context.getPhase();
            String tag = element.getTagName();
            Attr nameNode = element.getAttributeNodeNS(ANDROID_URI, ATTR_NAME);
            
        	System.out.println("nameNode = " + nameNode);
        	System.out.println("theme = " + theme);
        	System.out.println("folderType = " + folderType);
        	System.out.println("phase = " + phase);
        	System.out.println("tag= " + tag);
        }
        
        if(theme==null){
			context.report(USESTHEMEAPPCOMPATLIGHT2, context.getLocation(element),
					"Should uses the @style/Theme.AppCompat.Light style");
		}else{
			if (!theme.getValue().equals(THEME_APPCOMPAT_LIGHT)){
				context.report(USESTHEMEAPPCOMPATLIGHT2, context.getLocation(element),
						"Should uses the @style/Theme.AppCompat.Light style");
			}
		}
    }
}