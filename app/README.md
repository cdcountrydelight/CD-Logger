# CDLogger Library

CDLogger is a logging utility library for Android applications that allows you to easily log user
events , activities and fragments.

## Features

- Log custom events with event details.
- Automatically log activity and fragment opening events.
- Provide customizable options for logging activity and fragment events.

## Installation

To use CDLogger in your Android project, follow these steps:

### Step 1: Switch to project view

Change file view mode from android to project, then navigate to app folder.

### Step 2: Add the aar file to libs folder

Add the `cdlogger-debug.aar` file in your app/libs folder (if libs folder not present create new).

### Step 3: Add the aar file in your build.gradle(app) file.

To add all aar files inside libs folder in project use:-.

```kotlin
implementation fileTree (dir: 'libs', include: ['*.aar'])
```

or if you only want to add cd-logger library use :-

```kotlin
implementation files ('libs/aar-file-name.aar')
```

## Usage

To start using CDLogger in your application, follow these steps:

### Step 1: Initialize CDLogger in your Application class

In your `Application` class, initialize the `CDLogger` instance using the `CDLogger.Builder` class:

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        CDLogger.Builder(this)
            .logActivityOpeningEvent(true) // Optional: Enable logging of activity opening events
            .logFragmentOpeningEvent(true) // Optional: Enable logging of fragment opening events
            .build()
    }
}
```

### Step 2: Log Events

You can now use the `CDLogger` class to log events anywhere in your application:

```kotlin
// Log a custom event
val event = Event(eventName = "ButtonClicked", eventData = mapOf("buttonId" to "loginButton"))
CDLogger.logEvent(event)

// Add user details
val userDetails = mutableMapOf(
    "userId" to "123456",
    "userName" to "John Doe",
    // Add more user details as needed
)
CDLogger.addUserDetails(userDetails)
```

## Dependencies

CDLogger has the following dependencies:

- Gson: `com.google.code.gson:gson:2.10.1`
- Room: `androidx.room:room-runtime:2.6.1`
- WorkManager: `androidx.work:work-runtime-ktx:2.9.0`
- Ktor: `io.ktor:ktor-client-android:2.3.7`

Make sure to include these dependencies in your app's `build.gradle` file.
