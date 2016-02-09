# Android Lint Patterns and Guidelines Checks

This project features a custom lint check for Android patterns and guidelines.


## What kind of patterns and guidelines?

Patterns and guidelines presented in [Android documentation for
developers](http://developer.android.com/design/patterns/index.html)

For now, we intend to know if the applications are [Supporting Tablets and
Handsets](http://developer.android.com/guide/practices/tablets-and-handsets.html)

## Our checks

### MainActivityIsFragmentActivity

- Summary: The main activity should extends FragmentActivity class
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

### UsesFragmentManager

- Summary: Looks if the app uses a FragmentTransation
- Priority: 6 / 10
- Severity: Warning
- Category: Correctness

In order to use fragments API, the application should uses a FragmentTransaction.
This detector look for call to FragmentManager.beginTransaction method in activities classes.

#### How this is done?
1. For each class application, check if it extends android/support/v4/app/FragmentActivity
1. If yes, find the onCreate method and check, recursively, if FragmentManager.beginTransaction is called.

### ActivityShouldUsesThemeAppCompatLight

- Summary: Check if the activity uses the @style/Theme.AppCompat.Light theme
- Priority: 6 / 10
- Severity: FATAL
- Category: Correctness

#### How this is done?
1. For each activity that extends android/support/v7/app/ActionBarActivity checks if
this uses the @style/Theme.AppCompat.Light theme

## How to install and run the check?

- Config the requeriments
    - Clone the AndroidManifestParser's repository
    
    ```
    git clone https://github.com/adorilson/AndroidManifestParser.git
    ```
    - Open the project in Eclipse
    - Erase the class ManifestParser/src/main/java/com/donvigo/androidmanifestparser/MainActivity.java (yeap)

- Clone this repository

```
git clone https://github.com/adorilson/android-lint-checks.git
```

- Open the project in Eclipse (this is a Java Project, not Android Project)

- Configure the project classpath to include lint-api.jar. Add a ADT_HOME variable
in Properties > Java Build Path > Add Variable... setting to your Android SDK home.

- Export the project to a jar file. Don't forget the manifest file

- Copy the jar exported to your user's *android/lint/* directory. Note: On Unix based
systems this is most likely *~/.android/lint/*

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
