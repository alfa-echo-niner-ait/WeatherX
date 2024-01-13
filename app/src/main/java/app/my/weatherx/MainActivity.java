package app.my.weatherx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottom_nav_view;

    private HomeFragment homeFragment;
    private HistoryFragment historyFragment;
    private AboutFragment aboutFragment;
    private Fragment activeFragment;

    private int PERMIT_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);

        homeFragment = new HomeFragment(MainActivity.this);
        historyFragment = new HistoryFragment();
        aboutFragment = new AboutFragment();
        activeFragment = homeFragment;

        // Add all fragments initially
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, homeFragment)
                .add(R.id.container, historyFragment)
                .hide(historyFragment) // Hide initially
                .add(R.id.container, aboutFragment)
                .hide(aboutFragment) // Hide initially
                .commit();

        bottom_nav_view = findViewById(R.id.bottom_nav);
        bottom_nav_view.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_home:
                        switchFragment(homeFragment);
                        return true;

                    case R.id.item_about:
                        switchFragment(aboutFragment);
                        return true;

                    case R.id.item_history:
                        switchHistoryFragment(historyFragment);
                        return true;
                }
                return false;
            }
        });
    }

    private void switchFragment(Fragment targetFragment) {
        // Hide the current active fragment
        getSupportFragmentManager().beginTransaction().hide(activeFragment).commit();

        // Show the selected fragment
        if (targetFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().show(targetFragment).commit();
        }
        else {
            getSupportFragmentManager().beginTransaction().add(R.id.container, targetFragment).commit();
        }

        // Update the active fragment
        activeFragment = targetFragment;
    }

    private void switchHistoryFragment(HistoryFragment historyFragment) {
        // Hide the current active fragment
        getSupportFragmentManager().beginTransaction().hide(activeFragment).commit();

        // Show the selected fragment
        if (historyFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().show(historyFragment).commit();
        }
        else {
            getSupportFragmentManager().beginTransaction().add(R.id.container, historyFragment).commit();
        }

        // Update the HistoryFragment when a city is added in HomeFragment
        historyFragment.updateCityList();
        // Update the active fragment
        activeFragment = historyFragment;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PERMIT_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Please grant permission to continue!", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

}