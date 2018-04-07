package daemontus.widget.demo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import daemontus.widget.SwapLayout;

public class SwapLayoutActivity extends AppCompatActivity implements SwapLayout.SwappedOut {

    private int counter = 0;

    @Nullable
    private View showNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap_layout);

        final SwapLayout layout = findViewById(R.id.swap_layout);
        layout.swapChild(nextText());

        Button swap = findViewById(R.id.swap);
        Button clear = findViewById(R.id.clear);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.swapChild(null, SwapLayoutActivity.this);
            }
        });

        swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View child = showNext;
                showNext = null;
                if (child == null) {
                    child = nextText();
                }
                layout.swapChild(child, SwapLayoutActivity.this);
            }
        });
    }

    @Override
    public void onSwappedOut(@NonNull View child) {
        showNext = child;
    }

    private TextView nextText() {
        counter += 1;
        TextView text = new TextView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        text.setLayoutParams(params);
        text.setGravity(Gravity.CENTER);
        text.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        text.setText(getString(R.string.content, counter));
        return text;
    }
}
