package mohammed.movieappnd.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by siko on 6/24/2017.
 */

public class MoviePreferences {
    static final String PREF_LIST_KEY= "weather";


    public  static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }


    public static void setJson(Context context, String json) {

        set(context,PREF_LIST_KEY, json);
    }

    public static void set(Context context,String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key, value);
        editor.commit();
    }
    public static String getJson(Context ctx)
    {

        return getSharedPreferences(ctx).getString(PREF_LIST_KEY, "");

    }


}
