package com.zrb.baseappmvp.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zrb.baseappmvp.R;


/**
 * Created by zwl on 16/9/30.
 */

public class LoadingDialog {

    private static AutoDismissDialog mLoadingDialog;
    private static String mActivity_path;

    public static Dialog showLoading(Activity context, String msg, boolean cancelable) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        TextView loadingText = (TextView) view.findViewById(R.id.id_tv_loading_dialog_text);
        loadingText.setText(msg);

        mLoadingDialog = new AutoDismissDialog(context, R.style.CustomProgressDialog);
        mLoadingDialog.setCancelable(cancelable);
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mLoadingDialog.show();
        return mLoadingDialog;
    }

    public static Dialog showLoading(Activity context) {
        return showLoading(context,null);
    }

    public static Dialog showLoading(Activity context, AutoDismissDialog.AutoDismissListent autoDismissListent) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        TextView loadingText = (TextView) view.findViewById(R.id.id_tv_loading_dialog_text);
        loadingText.setText("加载中...");
        if (mLoadingDialog == null) {
            mActivity_path = context.toString();
            mLoadingDialog = new AutoDismissDialog(context, R.style.CustomProgressDialog);
            mLoadingDialog.setCancelable(true);
            mLoadingDialog.setCanceledOnTouchOutside(false);
            mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        } else {
            if (!context.toString().equals(mActivity_path)) {
                mActivity_path = context.toString();
                mLoadingDialog = new AutoDismissDialog(context, R.style.CustomProgressDialog);
                mLoadingDialog.setCancelable(true);
                mLoadingDialog.setCanceledOnTouchOutside(false);
                mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            }
        }
        mLoadingDialog.setAutoDismissListent(autoDismissListent);
        try {
            mLoadingDialog.show();
        } catch (Exception e) {

        }
        return mLoadingDialog;
    }

    /**
     * 关闭加载对话框
     */
    public static void disDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.cancel();
        }
    }
}
