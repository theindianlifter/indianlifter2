package in.theindianlifter.indianlifter.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.indicators.BallTrianglePathIndicator;

import in.theindianlifter.indianlifter.R;

/**
 * Created by rajatdhamija
 * 22/04/18.
 */

public final class ProgressDialog {
    private static final float DIM_AMOUNT = 0.6f;
    private static Dialog progressDialog;

    private ProgressDialog() {
    }

    public static boolean isShowing() {
        return progressDialog != null && progressDialog.isShowing();
    }

    public static void showProgressDialog(final Context context) {
        try {
            progressDialog = new Dialog(context,
                    android.R.style.Theme_Translucent_NoTitleBar);
            progressDialog.setContentView(R.layout.dialog_progress);
            AVLoadingIndicatorView avi = progressDialog.findViewById(R.id.avi);
            avi.setIndicator(new BallTrianglePathIndicator());

            Window dialogWindow = progressDialog.getWindow();
            WindowManager.LayoutParams layoutParams = dialogWindow
                    .getAttributes();
            layoutParams.dimAmount = DIM_AMOUNT;
            dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dismissProgressDialog() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                try {
                    progressDialog.dismiss();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                progressDialog = null;
            }
        }
    }
}
