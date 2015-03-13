package com.skaliak.tutorialapp;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import android.accounts.Account;

import java.io.IOException;


public class OauthTest extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth_test);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_oauth_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
    String mEmail; // Received from newChooseAccountIntent(); passed to getCookie()

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("oauthtest", "onactivityresult");
        Log.d("oauthtest", ""+requestCode);
        if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
            // Receiving a result from the AccountPicker
            if (resultCode == RESULT_OK) {
                Log.d("oauthtest", "ok result");
                mEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                String typ = data.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);
                Account account = new Account(mEmail, typ);
                Log.d("oauthtest", "mEmail value: " + mEmail);
                // With the account name acquired, go get the auth token
                AccountManager accountManager = AccountManager.get(getApplicationContext());
                accountManager.getAuthToken(account, "ah", false, new GetAuthTokenCallback(), null);

                //new GetUsernameTask(this, mEmail, "oauth2:https://www.googleapis.com/auth/userinfo.profile ").execute();
            } else if (resultCode == RESULT_CANCELED) {
                Log.d("oauthtest", "cancelled");
                // The account picker dialog closed without selecting an account.
                // Notify users that they must pick an account to proceed.
                Toast.makeText(this, "you shoulda picked an account", Toast.LENGTH_SHORT).show();
            }
        }
        // Later, more code will go here to handle the result from some exceptions...
        Log.d("oauthtest", "onactivityresult end");
    }

    private class GetAuthTokenCallback implements AccountManagerCallback {
        public void run(AccountManagerFuture result) {
            Log.d("oauthtest", "getAuthTokenCAllback");
            Bundle bundle;
            try {
                bundle = (Bundle) result.getResult();
                Intent intent = (Intent)bundle.get(AccountManager.KEY_INTENT);
                if(intent != null) {
                    // User input required
                    startActivity(intent);
                } else {
                    Log.d("oauthtest", "got an auth token? the accountmanager way");
                    onGetAuthToken(bundle);
                }
            } catch (OperationCanceledException e) {
                e.printStackTrace();
                Log.d("oauthtest", "op cancelled exception " + e.getMessage());
            } catch (AuthenticatorException e) {
                e.printStackTrace();
                Log.d("oauthtest", "auth exception " + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("oauthtest", "IO exception " + e.getMessage());
            }
        }
    }

    protected void onGetAuthToken(Bundle bundle) {
        Log.d("oauthtest", "onGetAuthToken");
        String auth_token = bundle.getString(AccountManager.KEY_AUTHTOKEN);
        new GetCookieTask().execute(auth_token);
    }

    //https://developer.android.com/google/auth/http-auth.html
    private void pickUserAccount() {
        Log.d("oauthtest", "start pickuseraccount");
        String[] accountTypes = new String[]{"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }

    public void doLogin(View view) {
        Log.d("oauthtest", "button clicked");
        pickUserAccount();
    }

    //http://blog.notdot.net/2010/05/Authenticating-against-App-Engine-from-an-Android-app
    private class GetCookieTask extends AsyncTask<String, Void, Boolean> {
        protected Boolean doInBackground(String... tokens) {
            Log.d("oauthtest", "trying to get cookie...");
            DefaultHttpClient http_client = new DefaultHttpClient();
            try {
                // Don't follow redirects
                http_client.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);

                HttpGet http_get = new HttpGet("http://monspotting.appspot.com/_ah/login?continue=http://localhost/&auth=" + tokens[0]);
                HttpResponse response;
                response = http_client.execute(http_get);
                Log.d("oauthtest", "status code was " + response.getStatusLine().getStatusCode());

                if(response.getStatusLine().getStatusCode() != 302) {
                    // Response should be a redirect
                    Log.d("oauthtest", "response was not a redirect");
                    return false;
                }

                for(Cookie cookie : http_client.getCookieStore().getCookies()) {
                    if(cookie.getName().equals("ACSID")) {
                        Log.d("oauthtest", "got a cookie!");
                        Log.d("oauthtest", cookie.toString());
                        String cookie_str = cookie.getName() + "=" + cookie.getValue();
                        Log.d("oauthtest", "cookie_str: " + cookie_str);
                        DataSinglet.getInstance().logIn(cookie_str);
                        return true;
                    }
                }
            } catch (ClientProtocolException e) {
                Log.d("oauthtest", "client Protocol exception " + e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("oauthtest", "IO exception " + e.getMessage());
                e.printStackTrace();
            } finally {
                http_client.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, true);
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            //start next activity here?
            Intent intent = new Intent(getBaseContext(), NewMonster.class);
            Log.d("oauthtest", "sending intent to switch to PlanStep3b");
            startActivity(intent);
        }
    }


    //rename this
    public class GetUsernameTask extends AsyncTask {
        Activity mActivity;
        String mScope;
        String mEmail;

        GetUsernameTask(Activity activity, String name, String scope) {
            this.mActivity = activity;
            this.mScope = scope;
            this.mEmail = name;
        }

        /**
         * Executes the asynchronous job. This runs when you call execute()
         * on the AsyncTask instance.
         */
        @Override
        protected Object doInBackground(Object... params) {
            Log.d("oauthtest", "doInBackground");
            try {
                String token = fetchTokenNoCatch();
                Log.d("oauthtest", "fetchtoken returned");
                if (token != null) {
                    // Insert the good stuff here.
                    // Use the token to access the user's Google data.
                    Log.d("oauthtest", "the token is: " + token);

                    new GetCookieTask().execute(token);
                    //we want to save the cookie, not the token
                    //DataSinglet.getInstance().logIn(token);

//                    Intent intent = new Intent(getBaseContext(), NewMonster.class);
//                    Log.d("oauthtest", "sending intent to switch to PlanStep3b");
//                    startActivity(intent);
                }
            } catch (IOException e) {
                // The fetchToken() method handles Google-specific exceptions,
                // so this indicates something went wrong at a higher level.
                // TIP: Check for network connectivity before starting the AsyncTask.
                Log.d("oauthtest", "exception after fetchToken?");
                Log.d("oauthtest", e.getMessage());
            }
            return null;
        }


        //ha ha nice try -- catching is mandatory
        protected String fetchTokenNoCatch() throws IOException {
            Log.d("oauthtest", "fetchtoken");
            try {
                return GoogleAuthUtil.getToken(mActivity, mEmail, mScope);
            } catch (UserRecoverableAuthException userRecoverableException) {
                // GooglePlayServices.apk is either old, disabled, or not present
                // so we need to show the user some UI in the activity to recover.
                //mActivity.handleException(userRecoverableException);
                Log.d("oauthtest", "userRecoverableException");
                Log.d("oauthtest", userRecoverableException.getMessage());
                Log.d("oauthtest", "starting activity again...");
                startActivityForResult(userRecoverableException.getIntent(), REQUEST_CODE_PICK_ACCOUNT);
            } catch (GoogleAuthException fatalException) {
                // Some other type of unrecoverable exception has occurred.
                // Report and log the error as appropriate for your app.
                Log.d("oauthtest", "fatalexception");
                Log.d("oauthtest", fatalException.getMessage());
            }
            return null;
        }
    }
}
