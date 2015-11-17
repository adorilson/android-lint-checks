private static class PerformanceVisitor extends ForwardingAstVisitor {
        private static final String CLASS_V4_FRAGMENTACTIVITY = "android.support.v4.app.FragmentActivity";
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
			
			// TODO Replace this ACTIVITY for a list of activities name 
			if (node.astName().toString().equals(ACTIVITY)){
				boolean isFragmentActivity = rClass.isSubclassOf(CLASS_V4_FRAGMENTACTIVITY, false);
				if (!isFragmentActivity){
					report(node);
				}

			return super.visitClassDeclaration(node);
		}

		private void report(ClassDeclaration node) {
			String message = ACTIVITY + " class should extends " + CLASS_V4_FRAGMENTACTIVITY;
			Location location = mContext.getLocation(node.astName());
			mContext.report(CHECKFRAGMENTACTIVITY, node, location, message);
		}

	}
}
