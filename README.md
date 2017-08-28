appengine-endpoints-helloendpoints-android
==========================================

![status: inactive](https://img.shields.io/badge/status-inactive-red.svg)

This project is no longer actively developed or maintained.

For new work on this check out [android-docs-samples](https://github.com/GoogleCloudPlatform/android-docs-samples).

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
app will use as an API server. Proceed with optional authentication steps
during setup.

1. Install [Android Studio][7].

1. Download code
`git clone https://github.com/GoogleCloudPlatform/appengine-endpoints-helloendpoints-android.git`

1. Import the project:
    1. Open AndroidStudio and choose "Import Project."
    1. Select the build.gradle file in the HelloEndpointsProject directory and
use default values for the rest of the import screens.
    1. NOTE: If you recieve an import error indicating that "play-services", a
required dependency, could not be found that is OK. It will be resolved by the
following step.

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

1. Deploy the Android App
    1. Deploy your app via normal Android deployment procedures.

***NOTE: At this point you can deploy the app and all anonymous API calls will work. 
The authenticated API call will not work; continue on to configure the Android
application to securely communicate with your backend sample.***

1. Register an Android Application to access your Google Cloud Endpoints
backend project.
    1. Navigate to the [Google Cloud Console][8]. Select the Google Cloud 
Endpoints Backend project that was deployed as in Step 1.
    1. Navigate to the "APIs & Auth," item of the left menu bar.
    1. Choose the "Credentials" submenu item.
    1. Click "Create new Client ID" and select "Installed Application" and "Android."
    1. Use com.google.devrel.samples.helloendpoints as the package name.
    1. Retrieve your debugging keystore fingerprint from ADB. On a Mac this
command would typically look like this with an empty password:
    `keytool -list -v -keystore ~/.android/debug.keystore`
    1. Click "Create Client ID."

1. Associate your Google Cloud Endpoints backend project to the Android
application.
    1. Update the file `HelloEngpoints/src/main/java/com/google/devrel/samples/helloendpoints/AppConstants.java`.
`WEB_CLIENT_ID` field should be updated with the web application client ID
that is defined in the "APIs & Auth" -> "Credentials" screen of your Google Cloud Endpoints
backend project. You will be using the Client ID specified for "Compute Engine and App Engine."
    1. You will similarly need to update your Google Cloud Endpoints backend
sample with the Android application client ID as well. **Do not** forget to
recompile your backend app (such as with `mvn clean install` for the Java one).

1. Deploy the Android App
    1. Deploy your app via normal Android deployment procedures.
    1. You will need to use a version 17 or later "Google APIs" enabled AVD
definition if you are using an emulator instead of a physical device. Physical
devices need only Google Play (Services) installed to work.

1. A quick note about the Client IDs used above for Authentication.
    -Client IDs identify applications (client or backend) to Google.
The authenticated APIs in this sample utilize OAuth2 authentication. OAuth2 tokens
are issued to specific Client IDs and therefore the Client IDs can be used
for restricting access to your APIs.
    -When you create a Google Cloud Console Project a default Client ID
is created and named for use by the project. When you upload an App Engine
project it uses that Client ID. This becomes the `WEB_CLIENT_ID` referenced
in the Android/iOS/Backend samples.
    -When you register the iOS or Android Applications in the Google
Cloud Console you are creating another Client ID. This Client ID is the
one requesting an OAuth2 token from Google for authentication purposes.
When the API is used in an authenticated manner an OAuth2 access token is
sent and opened by Google Cloud Endpoints. The Client ID is extracted from
the token and compared to the backend's declared acceptable Client ID list. 

[1]: https://developers.google.com/appengine
[2]: http://developer.android.com/reference/packages.html
[3]: https://developers.google.com/appengine/docs/java/endpoints/
[4]: https://code.google.com/apis/console
[5]: https://localhost:8888/
[6]: https://github.com/GoogleCloudPlatform/appengine-endpoints-tictactoe-java/blob/master/war/js/render.js
[7]: http://developer.android.com/sdk/installing/studio.html
[8]: http://cloud.google.com/console
[9]: https://github.com/GoogleCloudPlatform/appengine-endpoints-helloendpoints-java-maven
