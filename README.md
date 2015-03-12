# Android Lint Patterns and Guidelines Checks

This project features a custom lint check for Android patterns and guidelines.


## What kind of patterns and guidelines?

Patterns and guidelines presented in [Android documentation for
developers](http://developer.android.com/design/patterns/index.html)

For now, we intend to know if the applications are [Supporting Tablets and
Handsets](http://developer.android.com/guide/practices/tablets-and-handsets.html)

## Our checks

### MainActivityIsFragmentActivity

- Summary: The main activity should extendsFragmentActivity class
- Priority: 6 / 10
- Severity: Warning
- Category: Correctness

Checks if the main activity defined in manifest file extends the FragmentActivity
class.

#### How this is done?
1. Look for in manifest file by the main activity. This will the element has a
intent-filter tag.
1. Check if main activity founded extends FragmentActivity class.

### AppShouldUsesActionBar

- Summary: The app should uses action bar
- Priority: 6 / 10
- Severity: Warning
- Category: Correctness

Checks if the application uses the action bar.

#### How this is done?
1. Look for in manifest file by the main activity. This will the element has a
intent-filter tag.
1. Check if main activity founded extends ActionBarActivity class.

## How to install and run the check?

- Clone this repository

```
git clone https://github.com/adorilson/android-lint-checks.git
```

- Open the project in Eclipse (this is a Java Project, not Android Project)

- Configure the project classpath to include lint-api.jar. Add a ADT_HOME variable
in Properties > Java Build Path > Add Variable... setting to /sdk/tools/lib/lint-api.jar 
- Export the project to a jar file. Don't forget the manifest file
- Copy the jar exported to your user's *android/lint/* directory. Note: On Unix
based systems this is most likely *~/.android/lint/*
- Test if lint "see" your check

```
ADT_HOME/sdk/tools/lint --show MainActivityIsFragmentActivity
MainActivityIsFragmentActivity
------------------------------
Summary: The main activity should extendsFragmentActivity class

Priority: 6 / 10
Severity: Warning
Category: Correctness

Checks if the main activity defined in manifest file extends
theFragmentActivity class
```

- Run the check

    a) In Gradle project, at project home
    ```
        ./gradlew lint
    ```
    b) In non-gradle project, at project home
    ```
        <ADT_HOME>/sdk/tools/lint --check MainActivityIsFragmentActivity .
    ```