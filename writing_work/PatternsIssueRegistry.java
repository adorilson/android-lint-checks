public class PatternsIssueRegistry extends IssueRegistry {

    @Override
    public List<Issue> getIssues() {
        return Arrays.asList(
                SuperClassDetector.FRAGMENT_ACTIVITY,
                SuperClassDetector.ACTIONBAR_ACTIVITY,
                UsesFragmentTransaction.ISSUE,
                ThemeDetector.USESTHEMEAPPCOMPATLIGHT2
            );
    }
}

