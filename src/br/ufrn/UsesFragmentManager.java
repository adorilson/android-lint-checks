package br.ufrn;

import java.io.File;
import java.util.Collections;
import java.util.List;

import lombok.ast.AstVisitor;
import lombok.ast.ForwardingAstVisitor;
import lombok.ast.MethodDeclaration;
import lombok.ast.MethodInvocation;
import lombok.ast.Node;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.tools.lint.client.api.JavaParser.ResolvedMethod;
import com.android.tools.lint.client.api.JavaParser.ResolvedNode;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Detector.JavaScanner;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.Speed;

public class UsesFragmentManager extends Detector implements JavaScanner {
	private static final String ON_CREATE = "onCreate";   //$NON-NLS-1$
	
	private static final Implementation IMPLEMENTATION = new Implementation(
            UsesFragmentManager.class,
            Scope.JAVA_FILE_SCOPE);
	/** Missing call to super */
    public static final Issue ISSUE = Issue.create(
            "UsesFragmentManager", //$NON-NLS-1$
            "Missing getFragmentSupport Call",
            "Is need to call getFragmentSupport to sure that activity uses fragment",
            Category.CORRECTNESS,
            9,
            Severity.WARNING,
            IMPLEMENTATION);

    /** Constructs a new {@link UsesFragmentManager} check */
    public UsesFragmentManager() {
    }
    
    @Override
    public boolean appliesTo(@NonNull Context context, @NonNull File file) {
        return true;
    }
    @NonNull
    @Override
    public Speed getSpeed() {
        return Speed.FAST;
    }
    
    //---- Implements JavaScanner ----
    @Override
    public List<Class<? extends Node>> getApplicableNodeTypes() {
        return Collections.<Class<? extends Node>>singletonList(MethodDeclaration.class);
    }
    
    @Override
    public AstVisitor createJavaVisitor(@NonNull final JavaContext context) {
        return new ForwardingAstVisitor() {
            @Override
            public boolean visitMethodDeclaration(MethodDeclaration node) {
                ResolvedNode resolved = context.resolve(node);
                if (resolved instanceof ResolvedMethod) {
                    ResolvedMethod method = (ResolvedMethod) resolved;
                    checkUsesFM(context, node, method);
                }
                return false;
            }
        };
    }
    
    private static void checkUsesFM(@NonNull JavaContext context,
            @NonNull MethodDeclaration declaration,
            @NonNull ResolvedMethod method) {
    	ResolvedMethod superMethod = null; //= new ResolvedMethod();
        String name = method.getName();
        if (ON_CREATE.equals(name)){
            if (!GetFMVisitor.callsSuper(context, declaration, superMethod)) {
                String methodName = method.getName();
                String message = "OnCreate method should call getFragmentSupport";
                Location location = context.getLocation(declaration.astMethodName());
                context.report(ISSUE, declaration, location, message);
            }
        }
    }
    
/** Visits a method and determines whether the method calls its super method */
	private static class GetFMVisitor extends ForwardingAstVisitor {
		private final JavaContext mContext;
		private final ResolvedMethod mMethod;
		private boolean mCallsSuper;
		
		public GetFMVisitor(JavaContext context, ResolvedMethod method) {
			mContext = context;
            mMethod = method;
		}

		public static boolean callsSuper(
				@NonNull JavaContext context,
				@NonNull MethodDeclaration methodDeclaration,
				@NonNull ResolvedMethod method) {
			GetFMVisitor visitor = new GetFMVisitor(context, method);
			methodDeclaration.accept(visitor);
			return visitor.mCallsSuper;
		}
	}

@Override
public void visitMethod(JavaContext context, AstVisitor visitor,
		MethodInvocation node) {
	// TODO Auto-generated method stub
	super.visitMethod(context, visitor, node);
}
	
	
	
}
