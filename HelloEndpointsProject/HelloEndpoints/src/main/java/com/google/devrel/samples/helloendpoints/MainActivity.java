package com.google.devrel.samples.helloendpoints;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.helloworld.Helloworld;
import com.google.api.services.helloworld.Helloworld.Greetings.Authed;
import com.google.api.services.helloworld.Helloworld.Greetings.GetGreeting;
import com.google.api.services.helloworld.Helloworld.Greetings.ListGreeting;
import com.google.api.services.helloworld.Helloworld.Greetings.Multiply;
import com.google.api.services.helloworld.model.Greeting;
import com.google.api.services.helloworld.model.GreetingCollection;
import com.google.common.base.Strings;

import java.io.IOException;

import com.google.devrel.samples.helloendpoints.R.id;

import static com.google.devrel.samples.helloendpoints.BuildConfig.DEBUG;

/**
 * Sample Android application for the Hello World tutorial for Google Cloud Endpoints. The sample
 * code shows many of the better practices described in the links below.
 *
 * @see <a href="https://developers.google.com/appengine/docs/java/endpoints">https://developers.google.com/appengine/docs/java/endpoints</a>
 * @see <a href="https://developers.google.com/appengine/docs/java/endpoints/consume_android">https://developers.google.com/appengine/docs/java/endpoints/consume_android</a>
 *
 */
public class MainActivity extends Activity {
  private static final String LOG_TAG = "MainActivity";

  /**
   * Activity result indicating a return from the Google account selection intent.
   */
  private static final int ACTIVITY_RESULT_FROM_ACCOUNT_SELECTION = 2222;

  private AuthorizationCheckTask mAuthTask;
  private String mEmailAccount = "";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Prevent the keyboard from being visible upon startup.
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mAuthTask.cancel(true);
    mAuthTask = null;
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == ACTIVITY_RESULT_FROM_ACCOUNT_SELECTION && resultCode == RESULT_OK) {
      // This path indicates the account selection activity resulted in the user selecting a
      // Google account and clicking OK.

      // Set the selected account.
      String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
      TextView emailAccountTextView = (TextView)this.findViewById(id.email_address_tv);
      emailAccountTextView.setText(accountName);

      // Fire off the authorization check for this account and OAuth2 scopes.
      performAuthCheck();
    }
  }

  private boolean isSignedIn() {
    if (!Strings.isNullOrEmpty(mEmailAccount)) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * This method is invoked when the "Get Greeting" button is clicked. See activity_main.xml for
   * the dynamic reference to this method.
   */
  public void onClickGetGreeting(View view) {
    View rootView = view.getRootView();
    TextView greetingIdInputTV = (TextView)rootView.findViewById(R.id.greeting_id_edit_text);
    if (greetingIdInputTV.getText()==null ||
        Strings.isNullOrEmpty(greetingIdInputTV.getText().toString())) {
      Toast.makeText(this, "Input a Greeting ID", Toast.LENGTH_SHORT).show();
      return;
    };

    String greetingIdString = greetingIdInputTV.getText().toString();
    int greetingId = Integer.parseInt(greetingIdString);

    // Use of an anonymous class is done for sample code simplicity. {@code AsyncTasks} should be
    // static-inner or top-level classes to prevent memory leak issues.
    // @see http://goo.gl/fN1fuE @26:00 for an great explanation.
    AsyncTask<Integer, Void, Greeting> getAndDisplayGreeting =
        new AsyncTask<Integer, Void, Greeting> () {
          @Override
          protected Greeting doInBackground(Integer... integers) {
            // Retrieve service handle using null credential since this is an unauthenticated call.
            Helloworld apiServiceHandle = AppConstants.getApiServiceHandle(null);

            try {
              GetGreeting getGreetingCommand = apiServiceHandle.greetings().getGreeting(integers[0]);
              Greeting greeting = getGreetingCommand.execute();
              return greeting;
            } catch (IOException e) {
              Log.e(LOG_TAG, "Exception during API call", e);
            }
            return null;
          }

          @Override
          protected void onPostExecute(Greeting greeting) {
            if (greeting!=null) {
              displayGreetings(greeting);
            } else {
              Log.e(LOG_TAG, "No greetings were returned by the API.");
            }
          }
        };

    getAndDisplayGreeting.execute(greetingId);
  }

  /**
   * This method is invoked when the "List Greetings" button is clicked. See activity_main.xml for
   * the dynamic reference to this method.
   */
  public void onClickListGreetings(View unused) {

    // Use of an anonymous class is done for sample code simplicity. {@code AsyncTasks} should be
    // static-inner or top-level classes to prevent memory leak issues.
    // @see http://goo.gl/fN1fuE @26:00 for an great explanation.
    AsyncTask<Void, Void, GreetingCollection> getAndDisplayGreeting =
        new AsyncTask<Void, Void, GreetingCollection> () {
          @Override
          protected GreetingCollection doInBackground(Void... unused) {
            // Retrieve service handle using null credential since this is an unauthenticated call.
            Helloworld apiServiceHandle = AppConstants.getApiServiceHandle(null);

            try {
              ListGreeting getGreetingCommand = apiServiceHandle.greetings().listGreeting();
              GreetingCollection greeting = getGreetingCommand.execute();
              return greeting;
            } catch (IOException e) {
              Log.e(LOG_TAG, "Exception during API call", e);
            }
            return null;
          }

          @Override
          protected void onPostExecute(GreetingCollection greeting) {
            if (greeting!=null && greeting.getItems()!=null) {
              displayGreetings(greeting.getItems().toArray(new Greeting[] {}));
            } else {
              Log.e(LOG_TAG, "No greetings were returned by the API.");
            }
          }
        };

    getAndDisplayGreeting.execute((Void)null);
  }

  /**
   * This method is invoked when the "Multiply Greeting" button is clicked. See activity_main.xml
   * for the dynamic reference to this method.
   */
  public void onClickSendGreetings(View view) {
    View rootView = view.getRootView();

    TextView greetingCountInputTV = (TextView)rootView.findViewById(id.greeting_count_edit_text);
    if (greetingCountInputTV.getText()==null ||
        Strings.isNullOrEmpty(greetingCountInputTV.getText().toString())) {
      Toast.makeText(this, "Input a Greeting Count", Toast.LENGTH_SHORT).show();
      return;
    };

    String greetingCountString = greetingCountInputTV.getText().toString();
    final int greetingCount = Integer.parseInt(greetingCountString);

    TextView greetingTextInputTV = (TextView)rootView.findViewById(id.greeting_text_edit_text);
    if (greetingTextInputTV.getText()==null ||
        Strings.isNullOrEmpty(greetingTextInputTV.getText().toString())) {
      Toast.makeText(this, "Input a Greeting Message", Toast.LENGTH_SHORT).show();
      return;
    };

    final String greetingMessageString = greetingTextInputTV.getText().toString();

    // Use of an anonymous class is done for sample code simplicity. {@code AsyncTasks} should be
    // static-inner or top-level classes to prevent memory leak issues.
    // @see http://goo.gl/fN1fuE @26:00 for an great explanation.
    AsyncTask<Void, Void, Greeting> sendGreetings = new AsyncTask<Void, Void, Greeting> () {
      @Override
      protected Greeting doInBackground(Void... unused) {
        // Retrieve service handle using null credential since this is an unauthenticated call.
        Helloworld apiServiceHandle = AppConstants.getApiServiceHandle(null);

        try {
          Greeting greeting = new Greeting();
          greeting.setMessage(greetingMessageString);

          Multiply multiplyGreetingCommand = apiServiceHandle.greetings().multiply(greetingCount,
              greeting);
          greeting = multiplyGreetingCommand.execute();
          return greeting;
        } catch (IOException e) {
          Log.e(LOG_TAG, "Exception during API call", e);
        }
        return null;
      }

      @Override
      protected void onPostExecute(Greeting greeting) {
        if (greeting!=null) {
          displayGreetings(greeting);
        } else {
          Log.e(LOG_TAG, "No greetings were returned by the API.");
        }
      }
    };

    sendGreetings.execute((Void)null);
  }

  /**
   * This method is invoked when the "Get Authenticated Greeting" button is clicked. See
   * activity_main.xml for the dynamic reference to this method.
   */
  public void onClickGetAuthenticatedGreeting(View unused) {
    if (!isSignedIn()) {
      Toast.makeText(this, "You must sign in for this action.", Toast.LENGTH_LONG).show();
      return;
    }

    // Use of an anonymous class is done for sample code simplicity. {@code AsyncTasks} should be
    // static-inner or top-level classes to prevent memory leak issues.
    // @see http://goo.gl/fN1fuE @26:00 for an great explanation.
    AsyncTask<Void, Void, Greeting> getAuthedGreetingAndDisplay =
        new AsyncTask<Void, Void, Greeting> () {
          @Override
          protected Greeting doInBackground(Void... unused) {
            if (!isSignedIn()) {
              return null;
            };

            if (!AppConstants.checkGooglePlayServicesAvailable(MainActivity.this)) {
              return null;
            }

            // Create a Google credential since this is an authenticated request to the API.
            GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(
                MainActivity.this, AppConstants.AUDIENCE);
            credential.setSelectedAccountName(mEmailAccount);

            // Retrieve service handle using credential since this is an authenticated call.
            Helloworld apiServiceHandle = AppConstants.getApiServiceHandle(credential);

            try {
              Authed getAuthedGreetingCommand = apiServiceHandle.greetings().authed();
              Greeting greeting = getAuthedGreetingCommand.execute();
              return greeting;
            } catch (IOException e) {
              Log.e(LOG_TAG, "Exception during API call", e);
            }
            return null;
          }

          @Override
          protected void onPostExecute(Greeting greeting) {
            if (greeting!=null) {
              displayGreetings(greeting);
            } else {
              Log.e(LOG_TAG, "No greetings were returned by the API.");
            }
          }
        };

    getAuthedGreetingAndDisplay.execute((Void)null);
  }

  private void displayGreetings(Greeting... greetings) {
    String msg;
    if (greetings==null || greetings.length < 1) {
      msg = "Greeting was not present";
      Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    } else {
      if (DEBUG) {
        Log.d(LOG_TAG, "Displaying " + greetings.length + " greetings.");
      }
      for (Greeting greeting : greetings) {
        if (!Strings.isNullOrEmpty(greeting.getMessage())) {
          Toast.makeText(this, greeting.getMessage(), Toast.LENGTH_LONG).show();
        } else {
          Toast.makeText(this, "<empty message>", Toast.LENGTH_LONG).show();
        }
      }
    }
  }

  /**
   * This method is invoked when the "Sign In" button is clicked. See activity_main.xml for the
   * dynamic reference to this method.
   */
  public void onClickSignIn(View view) {
    TextView emailAddressTV = (TextView) view.getRootView().findViewById(id.email_address_tv);
    // Check to see how many Google accounts are registered with the device.
    int googleAccounts = AppConstants.countGoogleAccounts(this);
    if (googleAccounts == 0) {
      // No accounts registered, nothing to do.
      Toast.makeText(this, R.string.toast_no_google_accounts_registered,
          Toast.LENGTH_LONG).show();
    } else if (googleAccounts == 1) {
      // If only one account then select it.
      Toast.makeText(this, R.string.toast_only_one_google_account_registered,
          Toast.LENGTH_LONG).show();
      AccountManager am = AccountManager.get(this);
      Account[] accounts = am.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
      if (accounts != null && accounts.length > 0) {
        // Select account and perform authorization check.
        mEmailAccount = accounts[0].name;
        emailAddressTV.setText(accounts[0].name);
        performAuthCheck();
      }
    } else {
      // More than one Google Account is present, a chooser is necessary.

      // Reset selected account.
      emailAddressTV.setText("");

      // Invoke an {@code Intent} to allow the user to select a Google account.
      Intent accountSelector = AccountPicker.newChooseAccountIntent(null, null,
          new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, false,
          "Select the account to access Google Compute Engine API.", null, null, null);
      startActivityForResult(accountSelector,
          ACTIVITY_RESULT_FROM_ACCOUNT_SELECTION);
    }

  }

  /**
   * Schedule the authorization check in an {@code Tasks}.
   */
  public void performAuthCheck() {
    if (Strings.isNullOrEmpty(mEmailAccount)) {
      // Email address hasn't been selected so we have nothing to check.
      return;
    }

    // Cancel previously running tasks.
    if (mAuthTask != null) {
      try {
        mAuthTask.cancel(true);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return;
    }

    // Start task to check authorization.
    mAuthTask = new AuthorizationCheckTask();
    mAuthTask.execute("unused");
  }

  /**
   * Verifies OAuth2 token access for the application and Google account combination with
   * the {@code AccountManager} and the Play Services installed application. If the appropriate
   * OAuth2 access hasn't been granted (to this application) then the task may fire an
   * {@code Intent} to request that the user approve such access. If the appropriate access does
   * exist then the button that will let the user proceed to the next activity is enabled.
   */
  class AuthorizationCheckTask extends AsyncTask<String, Integer, Boolean> {
    @Override
    protected Boolean doInBackground(String... unused) {
      Log.i(LOG_TAG, "Background task started.");

      if (!AppConstants.checkGooglePlayServicesAvailable(MainActivity.this)) {
        return false;
      }

      // Ensure only one task is running at a time.
      mAuthTask = this;

      // Ensure an email was selected.
      if (Strings.isNullOrEmpty(mEmailAccount)) {
        publishProgress(R.string.toast_no_google_account_selected);
        // Failure.
        return false;
      }

      if (DEBUG) {
        Log.d(LOG_TAG, "Attempting to get AuthToken for account: " + mEmailAccount);
      }

      try {
        // If the application has the appropriate access then a token will be retrieved, otherwise
        // an error will be thrown.
        GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(
            MainActivity.this, AppConstants.AUDIENCE);
        credential.setSelectedAccountName(mEmailAccount);

        String accessToken = credential.getToken();
        if (DEBUG) {
          Log.d(LOG_TAG, "AccessToken retrieved");
        }

        // Success.
        return true;
      } catch (GoogleAuthException unrecoverableException) {
        Log.e(LOG_TAG, "Exception checking OAuth2 authentication.", unrecoverableException);
        publishProgress(R.string.toast_exception_checking_authorization);
        // Failure.
        return false;
      } catch (IOException ioException) {
        Log.e(LOG_TAG, "Exception checking OAuth2 authentication.", ioException);
        publishProgress(R.string.toast_exception_checking_authorization);
        // Failure or cancel request.
        return false;
      }
    }

    @Override
    protected void onProgressUpdate(Integer... stringIds) {
      // Toast only the most recent.
      Integer stringId = stringIds[0];
      Toast.makeText(MainActivity.this, stringId, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPreExecute() {
      mAuthTask = this;
    }

    @Override
    protected void onPostExecute(Boolean success) {
      TextView emailAddressTV = (TextView) MainActivity.this.findViewById(id.email_address_tv);
      if (success) {
        // Authorization check successful, set internal variable.
        mEmailAccount = emailAddressTV.getText().toString();
      } else {
        // Authorization check unsuccessful, reset TextView to empty.
        emailAddressTV.setText("");
      }
      mAuthTask = null;
    }

    @Override
    protected void onCancelled() {
      mAuthTask = null;
    }
  }

}
