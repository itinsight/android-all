package mobi.esys.upnews_tube.instagram;

import android.os.AsyncTask;
import android.util.Log;

import net.londatiga.android.instagram.InstagramRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import mobi.esys.upnews_tube.UpnewsTubeApp;
import mobi.esys.upnews_tube.net.NetMonitor;


public class CheckInstaTagTask extends AsyncTask<String, Void, Boolean> {
    private transient String mHashTag;
    private transient UpnewsTubeApp mApp;

    public CheckInstaTagTask(String hashTag, UpnewsTubeApp app) {
        mHashTag = hashTag;
        mApp = app;
    }


    @Override
    protected Boolean doInBackground(String... params) {
        boolean status = false;
        String response = "";
        if (mHashTag.length() >= 2) {
            final InstagramRequest request = new InstagramRequest(params[0]);

            String edTag = mHashTag.substring(1).toLowerCase(Locale.ENGLISH);

            if (NetMonitor.isNetworkAvailable(mApp)) {
                try {
                    final List<NameValuePair> reqParams = new ArrayList<NameValuePair>(
                            1);
                    reqParams.add(new BasicNameValuePair("count", String
                            .valueOf(100)));
                    response = request.requestGet("/tags/" + edTag
                            + "/media/recent", reqParams);
                    if (isJSONValid(response)) {
                        JSONObject resObject = new JSONObject(response);
                        if (resObject.has("meta")
                                && resObject.getJSONObject("meta").has(
                                "error_type")) {
                            status = false;
                            Log.d("unTag_CheckInstaTag","Error JSON response");
                        } else {
                            status = true;
                            Log.d("unTag_CheckInstaTag","All ok. Instagram tag is valid.");
                        }
                    }
                } catch (Exception e) {
                    status = false;
                    Log.d("unTag_CheckInstaTag","Error checking");
                }
            } else {
                Log.d("unTag_CheckInstaTag","No inet, can't check instagram tag");
            }
        }

        return status;
    }

    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
}