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
