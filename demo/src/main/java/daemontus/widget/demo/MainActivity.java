package daemontus.widget.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button swapLayout = findViewById(R.id.swap_layout);
        swapLayout.setOnClickListener(openActivity(SwapLayoutActivity.class));

        Button swapImageButton = findViewById(R.id.swap_image_button);
        swapImageButton.setOnClickListener(openActivity(SwapImageButtonActivity.class));

        Button loopViewPager = findViewById(R.id.loop_view_pager);
        loopViewPager.setOnClickListener(openActivity(LoopViewPagerActivity.class));
    }

    private View.OnClickListener openActivity(final Class<?> activity) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, activity));
            }
        };
    }
}
