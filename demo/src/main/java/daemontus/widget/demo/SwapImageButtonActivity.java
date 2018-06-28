package daemontus.widget.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import daemontus.widget.SwapImageButton;

public class SwapImageButtonActivity extends AppCompatActivity {

    private static final SwapImageButton.State STATE_A = new SwapImageButton.State(
            R.drawable.ic_state_a, R.drawable.ic_state_a_background, R.string.state_a
    );

    private static final SwapImageButton.State STATE_B = new SwapImageButton.State(
            R.drawable.ic_state_b, R.drawable.ic_state_b_background, R.string.state_b
    );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap_image_button);

        final SwapImageButton button = findViewById(R.id.swap_image_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SwapImageButtonActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
                // Enable this to see swapping + interaction at the same time.
                //SwapImageButton.State newState = button.getState() == STATE_A ? STATE_B : STATE_A;
                //button.setState(newState);
            }
        });

        findViewById(R.id.state_none).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setState(null);
            }
        });

        findViewById(R.id.state_A).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setState(STATE_A);
            }
        });

        findViewById(R.id.state_B).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setState(STATE_B);
            }
        });

    }
}
