# CDLogger Library

CDLogger is an Android library designed to facilitate automatic event logging within applications.
It offers seamless logging of various events such as activity openings, fragment openings,
application crashes, and custom events. This library streamlines the logging process by
automatically capturing these events without requiring manual intervention, thereby enhancing the
efficiency of logging operations. Furthermore, CDLogger provides functionality for logging user
details alongside events, enabling developers to gather comprehensive data insights.

![Platform](https://img.shields.io/badge/Platform-Android-lightseagreen) &nbsp;
![API Level](https://img.shields.io/badge/API-21%2B-steelblue) &nbsp;
[![](https://jitpack.io/v/cdcountrydelight/CD-Logger.svg)](https://jitpack.io/#cdcountrydelight/CD-Logger)
&nbsp;
![Language](https://img.shields.io/badge/Language-Kotlin-orange)

## Key Features

CDLogger provides the following features:

- **Activity Opening Events:** Automatically logs the opening of activities, including those
  extending `AppCompatActivity` or `FragmentActivity`.
- **Fragment Opening Events:** Automatically logs the opening of fragments.
- **Application Crashes:** Automatically captures and logs application crashes.
- **Custom Events:** Provides functions to log custom events with optional user details.

## Requirements

CDLogger has the following dependencies:

- Gson: `com.google.code.gson:gson:2.10.1`
- Room: `androidx.room:room-runtime:2.6.1`
- WorkManager: `androidx.work:work-runtime-ktx:2.9.0`
- Ktor: `io.ktor:ktor-client-android:2.3.7`

Ensure that your app's build.gradle file specifies the following minimum versions for compatibility
with CDLogger.

## Getting Started

To integrate CDLogger into your Android project, follow these steps:

### Step 1: Add the JitPack repository to your gradle file

Add it in your `settings.gradle` file at the end of repositories:

```kotlin
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
    maven(url = "https://jitpack.io")
  }
}
```

### Step 2: Add the dependency

Add it in your `build.gradle` (module level) file:

```kotlin
dependencies {
  implementation("com.github.cdcountrydelight:CD-Logger:<latest-version>")
}
```

## Usage

To start using CDLogger in your application, follow these steps:

### Step 1: Get Google Space Details

Before initializing CDLogger, you need to obtain the details of the Google space where you want to
log events. Follow these steps to get the space ID, space key, and space token:

    1. Navigate to "Gmail" > "Chats" > "New chat" > "Create a space".

    2. Provide the space details such as space name, description, and restrictions.

    3. After creating the space, go to "App & Integration" > "Webhooks" to get the space URL.
    
    4. Extract the space ID, space key, and space token from the URL. 

    For example, for:
    https://chat.googleapis.com/v1/spaces/AAA/messages?key=AIzaSyDdI0hCZtE6&token=12eQX3gUIi7DdVJsv50ozfQwCX

        - Space ID: AAA
        - Space Key: AIzaSyDdI0hCZtE6
        - Space Token: 12eQX3gUIi7DdVJsv50ozfQwCX

### Step 2: Initialize CDLogger in your Application class

In your `Application` class, initialize the `CDLogger` instance using the `CDLogger.Builder` class:

```kotlin
class MyApplication : Application() {
  override fun onCreate() {
    super.onCreate()

    CDLogger.Builder(
      application = this,
      spaceDetails = SpaceDetails(
        spaceId = "space_id",
        spaceKey = "space_key",
        spaceToken = "space_token"
      )
    )
      .logActivityOpeningEvent(true)
      .logFragmentOpeningEvent(true)
      .logCrashEvent(true)
      .registerLoggerFailureCallbacks(object : ILoggerFailureCallback {
        override fun onLoggerFailure(failureTag: String, exception: Exception) {
          Log.e("Logger Exception", "$failureTag , $exception")
        }
      })
      .build()
  }
}
```

1. `logActivityOpeningEvent(logActivityOpeningEvent: Boolean):` Sets whether to automatically log
   activity opening events.By default, activity opening events are not logged.

2. `logFragmentOpeningEvent(logFragmentOpeningEvent: Boolean):` Sets whether to automatically log
   fragment opening events.By default, fragment opening events are not logged.

3. `logCrashEvent(logCrashEvent: Boolean):` Sets whether to automatically log crash events.By
   default, crash events are automatically logged.

4. `registerLoggerFailureCallbacks(callback: ILoggerFailureCallback):` Registers a callback to
   handle logger failure events.

### Step 3: Log Events

To incorporate user details for the purpose of user identification, adhere to the following
procedure:

```kotlin
val userDetails = mutableMapOf(
  "userId" to "123456",
  "userName" to "John Doe",
  // Add more user details as needed
)
CDLogger.addUserDetails(userDetails)

```

To log custom events, utilize any of the following available methods:

```kotlin
1.CDLogger.logEvent(eventName: String, eventData: MutableMap< String, Any >)

2.CDLogger.logEvent(eventName: String, eventMessage: String)
```

To check whether logger is initialized use:-

```kotlin
CDLogger.isLoggerInitialized()
```


