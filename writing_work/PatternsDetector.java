public class PatternsDetector extends ResourceXmlDetector implements JavaScanner {
	
	private static final String MAIN = "android.intent.action.MAIN";
	// TODO Replace this mainActivity for a list of activities name 
	private String mainActivity = null;
	
	@Override
    public Collection<String> getApplicableElements() {
        // filtering the elements that will be target from visitElement method
        // <intent-filter>
    	return Arrays.asList(TAG_INTENT_FILTER);
    }

	@Override
    public void visitElement(XmlContext context, Element element) {
    	// Discover the main activity. The main activity is the one with 
		// <action android:name="android.intent.action.MAIN" /> tag in
		// AndroidManifest file
		NodeList child = element.getChildNodes();
		for (int i=0; i<child.getLength(); i++) {
			Node c = child.item(i);
			NamedNodeMap attr = c.getAttributes();
			if (attr==null){ continue;}
			Node node = attr.getNamedItemNS(ANDROID_URI, "name");
			if(node==null){	continue; }
			String value = node.getNodeValue();
			if (value.equalsIgnoreCase(MAIN)){
				Node parentNode = element.getParentNode();
				attr = parentNode.getAttributes();
				Node mainActivity = attr.getNamedItemNS(ANDROID_URI, "name");
				int lastDot = mainActivity.getNodeValue().lastIndexOf('.');
				this.mainActivity = mainActivity.getNodeValue().substring(lastDot+1);
				break;
			}
		}
    }

	@Override
	public AstVisitor createJavaVisitor(JavaContext context) {
		return new PerformanceVisitor(context, this.mainActivity);
	}
}

