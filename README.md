# CDLogger Library

CDLogger is a logging utility library for Android applications that simplifies the process of
logging user events, activities, and fragments.

## Features

- Log custom events with detailed event information.
- Automatically log activity and fragment opening events.
- Customizable options for logging activity and fragment events.

## Installation

To integrate CDLogger into your Android project, follow these steps:

### Step 1: Download the AAR File

Download the `cdlogger-debug.aar` file
from  [here](cdlogger/build/outputs/aar/cdlogger-debug.aar).

### Step 2: Include AAR File

1. Navigate to your project's `app` folder.
2. Create a `libs` folder if it doesn't already exist.
3. Place the `cdlogger-debug.aar` file in the `libs` folder.

### Step 3: Modify `build.gradle` (app)

Add the following line to your app-level `build.gradle` file to include the AAR file:

```kotlin
implementation files ('libs/cdlogger-debug.aar')
```

or to add all aar files inside libs folder use:-

```kotlin
implementation fileTree (dir: 'libs', include: ['*.aar'])
```

### Step 4: Sync Gradle

After modifying your `build.gradle` file, sync your project with Gradle to apply the changes:

1. Click on the "Sync Project with Gradle Files" icon in the toolbar.
2. Alternatively, go to "File" > "Sync Project with Gradle Files" in the menu.

This will ensure that the CDLogger library is properly included in your project and any changes made
to the dependencies are applied.

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
