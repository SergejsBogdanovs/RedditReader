package lv.st.sbogdano.redditreader.ui.posts;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.dean.jraw.auth.AuthenticationManager;

import lv.st.sbogdano.redditreader.R;
import lv.st.sbogdano.redditreader.ui.login.Authentication;
import lv.st.sbogdano.redditreader.ui.login.LoginActivity;

public class PostsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAuthenticationState();
    }

    private void checkAuthenticationState() {
        try {
            Authentication authentication = Authentication.getInstance(this);
            String state = AuthenticationManager.get().checkAuthState().toString();
            switch (state) {
                case "READY":
                    // TODO implement ready state
                    break;
                case "NONE":
                    startActivity(new Intent(this, LoginActivity.class));
                    break;
                case "NEED_REFRESH":
                    authentication.refreshAuthenTokenAsync();
                    break;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
