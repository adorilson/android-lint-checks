public class SuperClassDetector extends Detector implements JavaScanner {
        
        @Override
        public List<String> applicableSuperClasses() {
                return Collections.singletonList(SdkConstants.CLASS_ACTIVITY);
        }
        
        @Override
        public void checkClass(JavaContext context, ClassDeclaration declaration,
                        Node node, ResolvedClass resolvedClass) {
                
                // checking if the activity extends CLASS_V4_FRAGMENTACTIVITY
                boolean isFragmentActivity = resolvedClass.isSubclassOf(
                                                CLASS_V4_FRAGMENTACTIVITY, false);
                if (!isFragmentActivity){
                        ResolvedNode rNode = context.resolve(node);
                        Location location = context.getLocation(node);
                        String message = rNode.getName() + " class should extends "
                                                        + CLASS_V4_FRAGMENTACTIVITY;
                        context.report(FRAGMENT_ACTIVITY, node, location, message);
                }
                
                
                
                // checking if the activity extends CLASS_V7_ACTIONBARACTIVITY
                boolean isActionBarActivity = resolvedClass.isSubclassOf(
                                                CLASS_V7_ACTIONBARACTIVITY, false);
                if (!isActionBarActivity){
                        ResolvedNode rNode = context.resolve(node);
                        Location location = context.getLocation(node);
                        String message = rNode.getName() + " class should extends "
                                                        + CLASS_V7_ACTIONBARACTIVITY;
                        context.report(ACTIONBAR_ACTIVITY, node, location, message);
                }

                super.checkClass(context, declaration, node, resolvedClass);
        }
}
