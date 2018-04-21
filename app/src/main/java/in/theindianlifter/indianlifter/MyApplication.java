package in.theindianlifter.indianlifter;

import android.app.Application;
import android.support.v7.widget.AppCompatTextView;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by rajatdhamija on 21/04/18.
 */

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Montserrat-Regular.ttf")
                .addCustomStyle(AppCompatTextView.class, android.R.attr.textViewStyle)
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
