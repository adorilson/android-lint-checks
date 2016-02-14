public class UsesFragmentTransaction extends Detector implements Detector.ClassScanner{
        
    /** Constructs a new {@link UsesFragmentTransaction} */
    public UsesFragmentTransaction() {
    }

    @Override
    public void checkClass(ClassContext context, ClassNode classNode) {
        super.checkClass(context, classNode);
        
        Location location = context.getLocation(classNode);

        if (!context.getDriver().isSubclassOf(classNode, FRAGMENT_ACTIVITY_V4)) {
            return;
        }
        
        boolean callsBeginTransaction = checkIfCallBeginTransation(classNode);

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
        
        ListIterator<AbstractInsnNode> iterator = method.instructions.iterator();
        while (iterator.hasNext()) {
            AbstractInsnNode insnNode = iterator.next();
            if (insnNode.getType() == AbstractInsnNode.METHOD_INSN) {
                MethodInsnNode methodInsnNode = (MethodInsnNode) insnNode;
                
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
