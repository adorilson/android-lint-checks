package br.ufrn;

import java.util.Arrays;
import java.util.List;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;

public class PatternsIssueRegistry extends IssueRegistry {

	@Override
	public List<Issue> getIssues() {
		return Arrays.asList(
	            PatternsDetector.CHECKFRAGMENTACTIVITY,
	            PatternsDetector.USESACTIONBAR,
	            UsesFragmentTransaction.ISSUE,
	            PatternsDetector.USESTHEMEAPPCOMPATLIGHT
	        );
	}
}
