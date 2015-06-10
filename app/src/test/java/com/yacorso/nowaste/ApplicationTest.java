package com.yacorso.nowaste;

import android.widget.TextView;

import com.yacorso.nowaste.view.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class ApplicationTest {

    private MainActivity activity;

    @Before
    public void setup() throws Exception {
        activity = Robolectric.setupActivity(MainActivity.class);
        assertNotNull("MainActivity is not instantiated", activity);
    }

    @Test
    public void validateTextViewContent() throws Exception {
        TextView tvHelloWorld = (TextView) activity.findViewById(R.id.tvHello);
        assertNotNull("TextView could not be found", tvHelloWorld);
        assertTrue("TextView contains incorrect text", "Hello world!".equals(tvHelloWorld.getText().toString()));
    }
}
