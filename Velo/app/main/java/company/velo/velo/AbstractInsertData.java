package company.velo.velo;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jangsujin on 2017. 7. 8..
 */

abstract class AbstractInsertData extends AsyncTask<MyTaskParams, Void, String> {
	//ProgressDialog progressDialog;
	String serverURL = null;

	private Context mContext = null;
	String TAG;

	abstract String doSomething(MyTaskParams... params);

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

		//progressDialog.dismiss();
		Log.d(TAG, "POST response  - " + result);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		//progressDialog = ProgressDialog.show(mContext, "Please Wait", null, true, true);
	}

	public void setContext(Context context) {
		mContext = context;
	}

	public void setString(String url, String input_tag) {
		serverURL = "http://10.0.2.2/~jangsujin/backend/" + url;
		TAG = input_tag;
	}

	@Override
	public String doInBackground(MyTaskParams... params) {

		String postParameters = doSomething(params);

		try {

			URL url = new URL(serverURL);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

			httpURLConnection.setReadTimeout(5000);
			httpURLConnection.setConnectTimeout(5000);
			httpURLConnection.setRequestMethod("POST");
			// httpURLConnection.setRequestProperty("content-type",
			// "application/json");
			httpURLConnection.setDoInput(true);
			httpURLConnection.connect();

			OutputStream outputStream = httpURLConnection.getOutputStream();
			outputStream.write(postParameters.getBytes("UTF-8"));
			outputStream.flush();
			outputStream.close();

			int responseStatusCode = httpURLConnection.getResponseCode();
			Log.d(TAG, "POST response code - " + responseStatusCode);

			InputStream inputStream;
			if (responseStatusCode == HttpURLConnection.HTTP_OK) {
				inputStream = httpURLConnection.getInputStream();
			} else {
				inputStream = httpURLConnection.getErrorStream();
			}

			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			StringBuilder sb = new StringBuilder();
			String line = null;

			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}

			bufferedReader.close();

			return sb.toString();

		} catch (Exception e) {

			Log.d(TAG, "InsertData: Error ", e);

			return new String("Error: " + e.getMessage());
		}

	}
}
