package com.willkernel.app.wklib.utils;

import android.content.Context;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.willkernel.app.wklib.R;
import com.willkernel.app.wklib.WKApp;

public class ToastUtil {

    private static ToastUtil td;

    public static void show(int resId) {
        show(WKApp.getContext().getString(resId));
    }

    public static void show(String msg) {
        if (td == null) {
            td = new ToastUtil(WKApp.getContext());
        }
        td.setText(msg);
        td.create().show();
    }

    public static void shortShow(String msg) {
        if (td == null) {
            td = new ToastUtil(WKApp.getContext());
        }
        td.setText(msg);
        td.createShort().show();
    }

    public static void shortShow(int msg) {
        if (td == null) {
            td = new ToastUtil(WKApp.getContext());
        }
        td.setText(WKApp.getContext().getString(msg));
        td.createShort().show();
    }

    private Context context;
    private Toast toast;
    private String msg;

    private ToastUtil(Context context) {
        this.context = context;
    }

    private Toast create() {
        View contentView = View.inflate(context, R.layout.dlg_toast, null);
        TextView tvMsg = (TextView) contentView.findViewById(R.id.tv_toast_msg);
        toast = new Toast(context);
        toast.setView(contentView);
        toast.setGravity(Gravity.CENTER, 0, UnitUtil.dp2px(32));
        toast.setDuration(Toast.LENGTH_LONG);
        tvMsg.setText(msg);
        return toast;
    }

    private Toast createShort() {
        View contentView = View.inflate(context, R.layout.dlg_toast, null);
        TextView tvMsg = (TextView) contentView.findViewById(R.id.tv_toast_msg);
        toast = new Toast(context);
        toast.setView(contentView);
        toast.setGravity(Gravity.CENTER, 0, UnitUtil.dp2px(32));
        toast.setDuration(Toast.LENGTH_SHORT);
        tvMsg.setText(msg);
        return toast;
    }

    public void setText(String text) {
        msg = text;
    }

    public static void shortShow(Editable editable) {
        if (td == null) {
            td = new ToastUtil(WKApp.getContext());
        }
        td.setText(String.valueOf(editable));
        td.createShort().show();
    }
}