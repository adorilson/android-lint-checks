package br.ufrn;

import java.util.Arrays;
import java.util.List;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;

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
