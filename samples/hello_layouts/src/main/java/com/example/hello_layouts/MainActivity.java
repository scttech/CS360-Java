package com.example.hello_layouts;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import android.widget.RadioGroup;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RadioGroup rgLayouts;
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        View root = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rgLayouts = findViewById(R.id.rg_layouts);
        rgLayouts.setOnCheckedChangeListener((group, checkedId) -> {
            Fragment frag;
            if (checkedId == R.id.rb_horizontal) {
                frag = new HorizontalFragment();
            } else if (checkedId == R.id.rb_vertical) {
                frag = new VerticalFragment();
            } else if (checkedId == R.id.rb_constraint){
                frag = new ConstraintFragment();
            } else {
                frag = new GridFragment();
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, frag)
                    .commit();
        });

        // default selection
        rgLayouts.check(R.id.rb_horizontal);
    }
}
