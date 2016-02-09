package br.ufrn;

import java.util.ListIterator;

import lombok.ast.ClassDeclaration;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.android.annotations.NonNull;
import com.android.tools.lint.client.api.JavaParser.ResolvedClass;
import com.android.tools.lint.client.api.JavaParser.ResolvedNode;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;

import static br.ufrn.LintUtils.showParents;
import static br.ufrn.LintUtils.findMethod;
import static br.ufrn.LintUtils.BEGINTRANSACTION_METHOD;
import static br.ufrn.LintUtils.BEGINTRANSACTION_OWNER;
import static br.ufrn.LintUtils.FRAGMENT_ACTIVITY_V4;
import static br.ufrn.LintUtils.ON_CREATE_SIG;

/**
 * Checks if the subclasses android.support.v4.app.FragmentActivity calls
 * FragmentManager.beginTransaction method
 */
public class UsesFragmentTransaction extends Detector implements Detector.ClassScanner{
		private static final Implementation IMPLEMENTATION = new Implementation(
				UsesFragmentTransaction.class,
			Scope.CLASS_FILE_SCOPE);

	public static final Issue ISSUE = Issue.create(
			"UsesFragmentTransaction", //$NON-NLS-1$
			"Looks if the app uses a FragmentTransation",
			"In order to use fragments API, the application should uses a FragmentTransaction. " +
			"This detector look for call to FragmentManager.beginTransaction method in activities classes.",
			Category.CORRECTNESS,
			9,
			Severity.WARNING,
			IMPLEMENTATION).addMoreInfo("http://developer.android.com/guide/components/fragments.html#Adding");

	/** Print diagnostics during analysis (display flow control graph etc).
	 * Make sure you add the asm-debug or asm-util jars to the runtime classpath
	 * as well since the opcode integer to string mapping display routine looks for
	 * it via reflection. */
	private static boolean DEBUG = false;

	/** Constructs a new {@link UsesFragmentTransaction} */
	public UsesFragmentTransaction() {
	}

	@Override
	public void checkClass(ClassContext context, ClassNode classNode) {
		super.checkClass(context, classNode);
		
		Location location = context.getLocation(classNode);

		if(DEBUG){
			System.out.println("\n=== checkClass ===");
			System.out.println("classNode.name:" + classNode.name);
			System.out.println("classNode.superName: " + classNode.superName);
			System.out.println("location = " + location);
			System.out.println("parents = [" );
				showParents(context, classNode);
			System.out.println("]" );
		}
		
		if (!context.getDriver().isSubclassOf(classNode, FRAGMENT_ACTIVITY_V4)) {
			return;
		}
		
		if(DEBUG){
			System.out.println(classNode.name + " is subclass of " + FRAGMENT_ACTIVITY_V4);
		}
		
		boolean callsBeginTransaction = checkIfCallBeginTransation(classNode);

		if(DEBUG){
			System.out.println("Calls beginTransaction " + callsBeginTransaction);
		}

		if (!callsBeginTransaction){
			context.report(ISSUE, location,
					"You should call FragmentManager.beginTransaction");
		}
	}
	
	@SuppressWarnings("unchecked") // ASM API
	private boolean checkIfCallBeginTransation(ClassNode classNode) {
		MethodNode onCreate = findMethod(classNode.methods, "onCreate", ON_CREATE_SIG);
		return checkIfCallBeginTransation_aux(classNode, onCreate);
	}
	
	@SuppressWarnings("unchecked") // ASM API
	private boolean checkIfCallBeginTransation_aux(ClassNode classNode, 
													@NonNull MethodNode method) {
		if(DEBUG){
			System.out.println("Checking... " + method.name + " " + method.desc);
			System.out.println("classNode.name = " + classNode.name);
		}

		ListIterator<AbstractInsnNode> iterator = method.instructions.iterator();
		while (iterator.hasNext()) {
			AbstractInsnNode insnNode = iterator.next();
			if (insnNode.getType() == AbstractInsnNode.METHOD_INSN) {
				MethodInsnNode methodInsnNode = (MethodInsnNode) insnNode;
				if(DEBUG){
					System.out.println("owner:" + methodInsnNode.owner);
					System.out.println("name:" + methodInsnNode.name);
					System.out.println("desc:" + methodInsnNode.desc);
				}

				if (methodInsnNode.name.equals(BEGINTRANSACTION_METHOD) &&
						methodInsnNode.owner.equals(BEGINTRANSACTION_OWNER)){
					return true; 
				}

				if (methodInsnNode.owner.equals(classNode.name)){
					MethodNode child = findMethod(classNode.methods,
													methodInsnNode.name,
													methodInsnNode.desc);

					if(child!=null){
						return checkIfCallBeginTransation_aux(classNode, child);
					}
				}
			}
		}
		return false;
	} 
}