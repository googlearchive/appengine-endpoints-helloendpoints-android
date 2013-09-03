appengine-endpoints-helloendpoints-android
==========================================

This application is a HelloWorld Android sample application that connects to a
HelloWorld Google Cloud Endpoints backend sample.

## Products
- [App Engine][1]

## Language
- [Android][2]

## APIs
- [Google Cloud Endpoints][3]

## Setup Instructions
1. Deploy a Google Cloud Endpoints [backend sample][9] that this client
app will use as an API server.

1. Install [Android Studio][7].

1. Import the project:
    1. Open AndroidStudio and choose "Import Project."
    1. Select the build.gradle file in the HelloEndpointsProject directory and
use default values for the rest of the import screens.
    1. NOTE: If you recieve an import error indicating that "play-services", a
required dependency, could not be found comment out the Google Play Services
dependency line of build.gradle. This is on line 25 of
HelloEndpointsProject/HelloEndpoints.build.gradle. Import the project again.
Comment the dependency back in after the following step.

1. Check dependencies (even if the project compiles) using the Android SDK
Manager. Ensure the following packages are installed and up-to-date:
    1. Android Support Repository
    1. Android Support Library
    1. Google Play Services
    1. Google Repository

1. Close and reopen your project. Clean dependencies using the Build menu
by choosing "Rebuild Project."

1. Update the file `HelloEndpointsProject/HelloEndpoints/src/main/java/com/google/api/services/helloworld/Helloworld.java`
with the location of your deployed API. `DEFAULT_ROOT_URL` should include
your App Engine app ID.

***NOTE: At this point you can deploy the app and all anonymous API calls will work. 
The authenticated API call will not work; continue on to configure the Android
application to securely communicate with your backend sample.***

1. Register an Android Application to access your Google Cloud Endpoints
backend project.
    1. Use the [Developer Console][8] to navigate to the Google Cloud Endpoints
backend project. This is the project referenced in step 1.
    1. Navigate to the "API Access," menu of your Google project.
    1. Create a new Client ID of the "Installed Application" type and select
Android.
    1. Use com.google.devrel.samples.helloendpoints as the package name.
    1. Retrieve your debugging keystore fingerprint from ADB. On a Mac this
command would typically look like this with an empty password:
    `keytool -list -v -keystore ~/.android/debug.keystore`

1. Associate your Google Cloud Endpoints backend project to the Android
application.
    1. Update the file `HelloEngpoints/src/main/java/com/google/devrel/samples/helloendpoints/AppConstants.java`.
`WEB_CLIENT_ID` field should be updated with the web application client ID
that is defined in the "API Access" screen of your Google Cloud Endpoints
backend sample.
    1. You will conversely need to update your Google Cloud Endpoints backend
sample with the Android application client ID as well.

1. Deploy the Android App
    1. Deploy your app via normal Android deployment procedures.
    1. You will need to use a version 17 or later "Google APIs" enabled AVD
definition if you are using an emulator instead of a physical device. Physical
devices need only have Google Play installed to work.

[1]: https://developers.google.com/appengine
[2]: http://developer.android.com/reference/packages.html
[3]: https://developers.google.com/appengine/docs/java/endpoints/
[4]: https://code.google.com/apis/console
[5]: https://localhost:8888/
[6]: https://github.com/GoogleCloudPlatform/appengine-endpoints-tictactoe-java/blob/master/war/js/render.js
[7]: http://developer.android.com/sdk/installing/studio.html
[8]: http://developer.google.com/console
[9]: https://github.com/GoogleCloudPlatform/appengine-endpoints-helloendpoints-java-maven
