package kupchinskii.ruslan.armpullup;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

public class info extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        ((TextView) findViewById(R.id.tv_info_1)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) findViewById(R.id.tv_info_2)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) findViewById(R.id.tv_info_3)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) findViewById(R.id.tv_info_4)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) findViewById(R.id.tv_info_5)).setMovementMethod(LinkMovementMethod.getInstance());

    }

    public void OnClickClose(View view) {
        finish();
    }

}
