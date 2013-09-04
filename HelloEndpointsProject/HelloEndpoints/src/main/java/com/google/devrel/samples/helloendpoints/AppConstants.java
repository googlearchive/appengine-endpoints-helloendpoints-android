/* Copyright 2013 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.devrel.samples.helloendpoints;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.helloworld.Helloworld;

import javax.annotation.Nullable;

/**
 * Application constants and simple utilities.
 */
public class AppConstants {
  /**
   * Your WEB CLIENT ID from the API Access screen of the Developer Console for your project. This
   * is NOT the Android client id from that screen.
   *
   * @see <a href="https://developers.google.com/console">https://developers.google.com/console</a>
   */
  public static final String WEB_CLIENT_ID = "your_web_client_id";

  /**
   * The audience is defined by the web client id, not the Android client id.
   */
  public static final String AUDIENCE = "server:client_id:" + WEB_CLIENT_ID;

  /**
   * Class instance of the JSON factory.
   */
  public static final JsonFactory JSON_FACTORY = new AndroidJsonFactory();

  /**
   * Class instance of the HTTP transport.
   */
  public static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();

  /**
   * Count Google accounts on the device.
   */
  public static int countGoogleAccounts(Context context) {
    AccountManager am = AccountManager.get(context);
    Account[] accounts = am.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
    if (accounts == null || accounts.length < 1) {
      return 0;
    } else {
      return accounts.length;
    }
  }

  /**
   * Retrieve a Helloworld api service handle to access the API.
   */
  public static Helloworld getApiServiceHandle(@Nullable GoogleAccountCredential credential) {
    // Use a builder to help formulate the API request.
    Helloworld.Builder helloWorld = new Helloworld.Builder(AppConstants.HTTP_TRANSPORT,
        AppConstants.JSON_FACTORY, credential);
    return helloWorld.build();
  }

  /**
   * Check that Google Play services APK is installed and up to date.
   */
  public static boolean checkGooglePlayServicesAvailable(Activity activity) {
    final int connectionStatusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
    if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
      showGooglePlayServicesAvailabilityErrorDialog(activity, connectionStatusCode);
      return false;
    }
    return true;
  }

  /**
   * Called if the device does not have Google Play Services installed.
   */
  public static void showGooglePlayServicesAvailabilityErrorDialog(final Activity activity,
      final int connectionStatusCode) {
    final int REQUEST_GOOGLE_PLAY_SERVICES = 0;
    activity.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
            connectionStatusCode, activity, REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
      }
    });
  }
}
