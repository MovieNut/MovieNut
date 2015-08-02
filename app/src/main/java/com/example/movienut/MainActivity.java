package com.example.movienut;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.io.IOException;

import bolts.AppLinks;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;


public class MainActivity extends Activity {
    private TextView mTextDetails;
    private CallbackManager mCallbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d("Success: ", "Displaying Home");
            goHome();
        }

        @Override
        public void onCancel() {
            mTextDetails.setText("Login canceled.");
            Log.d("Cancel: ", "Login was canceled by user");
        }

        @Override
        public void onError(FacebookException e) {
            mTextDetails.setText("Error encountered.");
            Log.d("Error: ", "Error logging into facebook " + e);
        }


    };

    private void displayWelcomeMsg(Profile profile) {
        if (profile != null) {
            mTextDetails.setText("Welcome " + profile.getName() +"!");
        } else {
            mTextDetails.setText("");
        }
    }

    public MainActivity() {
    }

    @Override
    public void onStart() {
        super.onStart();

        Branch branch = Branch.getInstance();
        branch.initSession(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {
                    // params are the deep linked params associated with the link that the user clicked before showing up
                    Log.d("BranchConfigTest", "deep link data: " + referringParams.toString());
                }
            }
        }, this.getIntent().getData(), this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        if (isLoggedIn()) {
            goHome();
        }
        setContentView(R.layout.activity_main);
        initLogin();
    }

    //go to home
    private void goHome() {
        Intent i = new Intent(this, Home.class);
        startActivity(i);
    }

    private void initLogin() {
        mCallbackManager = CallbackManager.Factory.create();
        mTextDetails = (TextView) findViewById(R.id.mTextDetails);
        initLoginButton();
        initAccessTokens();
        initProfileTracker();


    }

    private void initAccessTokens() {
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
                mTextDetails.setText("");
            }
        };
        accessTokenTracker.startTracking();
    }

    private void initProfileTracker() {
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {

            }
        };

        profileTracker.startTracking();
    }

    private void initLoginButton() {
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile");
        loginButton.registerCallback(mCallbackManager, mCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void buttonToGoHome(View v) throws IOException {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
        Profile profile = Profile.getCurrentProfile();
        displayWelcomeMsg(profile);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        accessTokenTracker.startTracking();
        profileTracker.startTracking();


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


}
