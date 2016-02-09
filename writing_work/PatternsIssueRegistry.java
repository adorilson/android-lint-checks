public class PatternsIssueRegistry extends IssueRegistry {

	@Override
	public List<Issue> getIssues() {
		return Arrays.asList(
	            PatternsDetector.CHECKFRAGMENTACTIVITY,
	            PatternsDetector.USESACTIONBAR,
	            UsesFragmentTransaction.ISSUE,
	            PatternsDetector.USESTHEMEAPPCOMPATLIGHT,
	            ThemeDetector.USESTHEMEAPPCOMPATLIGHT2
	        );
	}
}

