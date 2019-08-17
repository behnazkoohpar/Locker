package locker.koohpar.com.locker;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import android.provider.Settings.Secure;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new GridAdapter(this));
        String android_id = Secure.getString(getContentResolver(),Secure.ANDROID_ID);
        if(!android_id.equalsIgnoreCase("72ce90dd8373b562")){
            CommonUtils.showSingleButtonAlert(MainActivity.this, "توجه", "شما مجاز به استفاده از این برنامه نمی باشید", "تائید", new CommonUtils.IL() {
                @Override
                public void onSuccess() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        finishAffinity();
                    }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        finishAndRemoveTask();
                    }
                }

                @Override
                public void onCancel() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        finishAffinity();
                    }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        finishAndRemoveTask();
                    }
                }
            });
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                finishAffinity();
//            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                finishAndRemoveTask();
//            }
        }
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                callLocker(position, MainActivity.this);
            }
        });
    }

    public static void callLocker(int position, final Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url =
                "http://" +
                        getSavedObjectFromPreference(context, "Locker", "ip", String.class)
                        + ":"
                        + getSavedObjectFromPreference(context, "Locker", "port", String.class) +
                        "/datasnap/service/tdevice/openLockerWithAndroid/" + (position+1);
        Log.d("hhhhhhhhhhhhhhhh: ",url);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String s = String.valueOf(response);
                        if (s.equalsIgnoreCase("{\"result\":[true]}"))
                            CommonUtils.showSingleButtonAlert(context, context.getString(R.string.txt_attention), "درب کمد باز شد", context.getString(R.string.pop_up_ok), null);
                        else
                            CommonUtils.showSingleButtonAlert(context, context.getString(R.string.txt_attention), "خطا در باز شدن درب کمد", context.getString(R.string.pop_up_ok), null);
                        // display response
                        Log.d("Response", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        );
        queue.add(getRequest);

    }


    public static void showTwoButtonAlertForEdit(final Context context, String msg, String buttonOk, String btnCancel, final int position) {
        try {
            TextView tvAlertMessage;
            ImageView btnAlertCancel;
            Button btnOk, btCancel;
            View dialogAlert = LayoutInflater.from(context).inflate(R.layout.dialog_edit_alert, null);
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(dialogAlert);
            WindowManager.LayoutParams windowLayout = dialog.getWindow().getAttributes();
            windowLayout.gravity = Gravity.CENTER;
            tvAlertMessage = (TextView) dialogAlert.findViewById(R.id.tv_alert_message);
            btnOk = (Button) dialogAlert.findViewById(R.id.btnOk);
            btnOk.setText(buttonOk);
            btCancel = (Button) dialogAlert.findViewById(R.id.btnCancel);
            final EditText ip = (EditText) dialogAlert.findViewById(R.id.ip);
            final EditText port = (EditText) dialogAlert.findViewById(R.id.port);
            btCancel.setText(btnCancel);
            btnAlertCancel = (ImageView) dialogAlert.findViewById(R.id.btn_alert_cancel);

            if (!TextUtils.isEmpty(msg))
                tvAlertMessage.setText(msg);
            btCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!ip.getText().toString().isEmpty() && !port.getText().toString().isEmpty()) {
                        MainActivity.saveObjectToSharedPreference(context, "Locker", "ip", ip.getText().toString());
                        MainActivity.saveObjectToSharedPreference(context, "Locker", "port", port.getText().toString());
                        MainActivity.callLocker(position, context);
                    } else
                        CommonUtils.showSingleButtonAlert(context, context.getString(R.string.txt_attention), "لطفا پورت و آی پی را وارد نمائید", context.getString(R.string.pop_up_ok), null);
                    dialog.dismiss();
                }
            });
            btnAlertCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void saveObjectToSharedPreference(Context context, String preferenceFileName, String serializedObjectKey, Object object) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(object);
        sharedPreferencesEditor.putString(serializedObjectKey, serializedObject);
        sharedPreferencesEditor.apply();
    }

    public static <GenericClass> GenericClass getSavedObjectFromPreference(Context context, String preferenceFileName, String preferenceKey, Class<GenericClass> classType) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        if (sharedPreferences.contains(preferenceKey)) {
            final Gson gson = new Gson();
            return gson.fromJson(sharedPreferences.getString(preferenceKey, ""), classType);
        }
        return null;
    }

}
