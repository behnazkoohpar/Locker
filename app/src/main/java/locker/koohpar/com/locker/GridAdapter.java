package locker.koohpar.com.locker;

/**
 * Created by cmos on 20/07/2018.
 */

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {
    private Context mContext;

    // Constructor
    public GridAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position,
                        View convertView, ViewGroup parent) {

        Button btn;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            btn = new Button(mContext);
            btn.setLayoutParams(new GridView.LayoutParams(150, 60));
            btn.setPadding(8, 8, 8, 8);

        } else {
            btn = (Button) convertView;
        }
        btn.setText(String.valueOf(mThumbIds[position]));
        // filenames is an array of strings
        btn.setTextColor(Color.WHITE);
        btn.setTextSize(35);
        btn.setBackgroundResource(R.drawable.button);
        btn.setId(position);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.getSavedObjectFromPreference(mContext, "Locker", "ip", String.class) == null ||
                        MainActivity.getSavedObjectFromPreference(mContext, "Locker", "ip", String.class).equalsIgnoreCase("") ||
                        MainActivity.getSavedObjectFromPreference(mContext, "Locker", "port", String.class) == null ||
                        MainActivity.getSavedObjectFromPreference(mContext, "Locker", "port", String.class).equalsIgnoreCase("")) {

                    MainActivity.showTwoButtonAlertForEdit(mContext, "لطفا آی پی و پورت مورد نظر را وارد نمائید", mContext.getString(R.string.ok), mContext.getString(R.string.btn_cancel), position);
                }
                else
                    MainActivity.callLocker(position,mContext);
            }


        });
        return btn;
    }

    // Keep all Images in array
    public int[] mThumbIds = {
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26,
            27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50,
            51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68,69,70,71,72,73,74,75,76,77,
            78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104
    };


}