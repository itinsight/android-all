package mobi.esys.upnews_lite;

import java.io.File;

import mobi.esys.constants.K2Constants;
import android.app.Activity;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;

import com.android.vending.billing.IInAppBillingService;

public class StartActivity extends Activity {
	protected static final String RESPONSE_CODE = "0";
	private transient IInAppBillingService billingService;
	private transient ServiceConnection billingServiceConn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		File videoDir = new File(Environment.getExternalStorageDirectory()
				+ K2Constants.VIDEO_DIR_NAME);
		if (!videoDir.exists()) {
			videoDir.mkdir();
		}

		Thread timer = new Thread() {
			public void run() {
				try {
					sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					Intent i = new Intent(StartActivity.this,
							DriveAuthActivity.class);
					finish();
					startActivity(i);
				}
			}
		};
		timer.start();

		// ServiceConnection mServiceConn = new ServiceConnection() {
		//
		// @Override
		// public void onServiceDisconnected(ComponentName name) {
		// billingService = null;
		// }
		//
		// @Override
		// public void onServiceConnected(ComponentName name, IBinder service) {
		// billingService = IInAppBillingService.Stub.asInterface(service);
		// GetProductInfoTask getProductInfoTask = new GetProductInfoTask(
		// StartActivity.this);
		// getProductInfoTask.execute(billingService);
		// try {
		// String purchaseData = "";
		// Bundle ownedItems = billingService.getPurchases(3,
		// getPackageName(), "subs", null);
		// int response = ownedItems.getInt("RESPONSE_CODE");
		// if (response == 0) {
		// ArrayList<String> purchaseDataList = ownedItems
		// .getStringArrayList("INAPP_PURCHASE_DATA_LIST");
		//
		// for (int i = 0; i < purchaseDataList.size(); ++i) {
		// purchaseData = purchaseDataList.get(i);
		// Log.d("info", purchaseData);
		// }
		//
		// }
		// if (purchaseData == "") {
		// Bundle buyIntentBundle = billingService.getBuyIntent(3,
		// getPackageName(), "one_month", "subs", "");
		// PendingIntent pendingIntent = buyIntentBundle
		// .getParcelable("BUY_INTENT");
		//
		// startIntentSenderForResult(
		// pendingIntent.getIntentSender(), 1001,
		// new Intent(), Integer.valueOf(0),
		// Integer.valueOf(0), Integer.valueOf(0));
		// } else {
		// startActivity(new Intent(StartActivity.this,
		// DriveAuthActivity.class));
		// finish();
		//
		// }
		//
		// } catch (RemoteException e) {
		// } catch (SendIntentException e) {
		// }
		// }
		//
		// };
		//
		// bindService(new Intent(
		// "com.android.vending.billing.InAppBillingService.BIND"),
		// mServiceConn, Context.BIND_AUTO_CREATE);

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (billingService != null && billingServiceConn != null) {
			unbindService(billingServiceConn);
		}

	}

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// if (requestCode == 1001) {
	// String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
	//
	// if (resultCode == RESULT_OK) {
	// try {
	// JSONObject jo = new JSONObject(purchaseData);
	// String sku = jo.getString("productId");
	// Log.d("buy", "You have bought the " + sku
	// + ". Excellent choice," + "adventurer!");
	// startActivity(new Intent(StartActivity.this,
	// DriveAuthActivity.class));
	//
	// } catch (JSONException e) {
	// Log.d("buy", "Failed to parse purchase data.");
	// e.printStackTrace();
	// }
	// } else {
	// finish();
	// }
	// }
	// }
}
