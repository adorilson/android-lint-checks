package br.ufrn;

import java.util.List;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.tools.lint.detector.api.ClassContext;

public abstract class LintUtils {

	public static final String BEGINTRANSACTION_OWNER = "android/support/v4/app/FragmentManager";
	public static final String BEGINTRANSACTION_METHOD = "beginTransaction";
	public static final String FRAGMENT_ACTIVITY_V4 = "android/support/v4/app/FragmentActivity";
	public static final String ON_CREATE_SIG = "(Landroid/os/Bundle;)V";
	
	public static void showParents(ClassContext context, ClassNode classNode){
		String className = classNode.name;
        while (className != null) {
            System.out.println(className);
            className = context.getClient().getSuperClass(context.getProject(), className);
            
        }
	}
	
	@Nullable
	public static MethodNode findMethod(
			@NonNull List<MethodNode> methods,
			@NonNull String name,
			@NonNull String desc) {
		for (MethodNode method : methods) {
			if (name.equals(method.name)
					&& desc.equals(method.desc)) {
				return method;
			}
		}
		return null;
	}
}

