package com.swipee.in.billing;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;


public class BillingService extends Service implements ServiceConnection{
	
	private static final String TAG = "BillingService";
	
	/** The service connection to the remote MarketBillingService. */
	private IMarketBillingService mService;
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "Service starting with onCreate");
		
		try {
			Intent intent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
// This is the key line that fixed everything for me
			intent.setPackage("com.android.vending");

			boolean bindResult = bindService(intent, this, Context.BIND_AUTO_CREATE);
			if(bindResult){
				Log.i(TAG,"Market Billing Service Successfully Bound");
			} else {
				Log.e(TAG,"Market Billing Service could not be bound.");
				//TODO stop user continuing
			}
		} catch (SecurityException e){
			Log.e(TAG,"Market Billing Service could not be bound. SecurityException: "+e);
			//TODO stop user continuing
		} catch (Exception exception){
			Log.e(TAG,"Exception: "+exception);
		}
	}
	
	public void setContext(Context context) {
        attachBaseContext(context);
    }
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		Log.i(TAG, "Market Billing Service Connected.");
		mService = IMarketBillingService.Stub.asInterface(service);
		BillingHelper.instantiateHelper(getBaseContext(), mService);
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		
	}

}
