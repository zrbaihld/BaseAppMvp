package com.zrb.baseappmvp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by zrb on 2017/8/4.
 */

public class AutoDismissDialog extends Dialog {
    private AutoDismissListent autoDismissListent;

    public AutoDismissDialog(@NonNull Context context) {
        super(context);
        init(context);
    }

    public AutoDismissDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    protected AutoDismissDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void init(Context context) {
    }


    public void setAutoDismissListent(AutoDismissListent autoDismissListent) {
        this.autoDismissListent = autoDismissListent;
    }

    @Override
    public void show() {
        super.show();
        Observable.timer(10, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (autoDismissListent != null && isShowing())
                            autoDismissListent.dismiss();
                        dismiss();
                    }
                });
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public interface AutoDismissListent {
        void dismiss();
    }
}
