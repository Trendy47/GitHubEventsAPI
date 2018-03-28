package com.trendydev.githubevents;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    private EditText ownerEditText, repoEditText, eventTypeEditText;
    private FrameLayout fragContainer;
    private Button matchButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ownerEditText = findViewById(R.id.ownerEditText);
        repoEditText = findViewById(R.id.repoEditText);
        eventTypeEditText = findViewById(R.id.eventEditText);

        fragContainer = findViewById(R.id.fragContainer);

        matchButton = findViewById(R.id.matchButton);
        matchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMatchesFragment();
            }
        });

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }

    private void openMatchesFragment() {
        String ownerString = ownerEditText.getText().toString();
        String repoString = repoEditText.getText().toString();
        String eventString = eventTypeEditText.getText().toString();

        Bundle bundle = new Bundle();
        bundle.putString("owner", ownerString);
        bundle.putString("repo", repoString);
        bundle.putString("event", eventString);

        MatchingEventsFragment fragment = new MatchingEventsFragment();
        fragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        fragContainer.setVisibility(View.VISIBLE);
        matchButton.setVisibility(View.GONE);
        backButton.setVisibility(View.VISIBLE);
    }

    private void goBack() {
        getFragmentManager().popBackStack();
        fragContainer.setVisibility(View.GONE);
        matchButton.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.GONE);
    }
}
