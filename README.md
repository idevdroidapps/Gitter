# Gitter
GitHub Repo API demo

## Specifications / Requirements
As a result from a search, the app should:
- Do Stuff

## Built With
* [Kotlin](https://kotlinlang.org/)
* [Android Architecture Components](https://developer.android.com/topic/libraries/architecture)
* [GitHub API](https://docs.github.com/en/free-pro-team@latest/rest)

## Instructions
This demo uses the Gradle build system.

1. Download the demo by cloning this repository.
2. In Android Studio, create a new project and choose the "Import Project" option.
3. Select the root directory that you downloaded with this repository.
4. If prompted for a gradle configuration, accept the default settings.
- Alternatively use the gradlew build command to build the project directly.

## Issues Encountered
- Use of the DataBinding Library can often require to invalidate project cache and rebuild the project in order to generate implementation classes.
- Android Studio produces a warning during the build process: "WARNING: API 'BaseVariant.getApplicationIdTextResource' is obsolete" which is caused by current SafeArgs plugin.
## Testing
An Instrumented test has been recorded to demonstrate the MainActivity functionality
Instrumented tests much be run on a physical device

## Author
* **James Campbell** - *Senior Android Engineer* -