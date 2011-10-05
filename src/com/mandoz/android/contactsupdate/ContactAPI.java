package com.mandoz.android.contactsupdate;

import android.os.Build;
import android.content.ContentResolver;
import android.content.Context;

/**
 * Abstract ContactAPI that selects the class for different Android versions.
 * @author Mohannad Ali
 */
public abstract class ContactAPI {

	private static ContactAPI api;
	protected Context context;
	/**
	 * Returns ContactAPI class based on Android version.
	 * @return ContactAPI class
	 */
	public static ContactAPI getAPI() {
		if (api == null) {
			String apiClass;
			if (Integer.parseInt(Build.VERSION.SDK) >= 5) {
				apiClass = "com.mandoz.android.contactsupdate.ContactAPISdk5";
			} else {
				apiClass = "com.mandoz.android.contactsupdate.ContactAPISdk3";
			}
			
			try {
				Class<? extends ContactAPI> realClass = Class.forName(apiClass).asSubclass(ContactAPI.class);
				api = realClass.newInstance();
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
			
		}
		return api;
	}

	/**
	 * Remove non-numeric characters from phone number.
	 * @param str
	 * @return Phone after removal of non-numeric characters.
	 */
	public static String getOnlyNumerics(String str) {

		if (str == null) {
			return null;
		}

		StringBuffer strBuff = new StringBuffer();
		char c;

		for (int i = 0; i < str.length(); i++) {
			c = str.charAt(i);

			if (Character.isDigit(c) || (c == '+' && i == 0)) {
				strBuff.append(c);
			}
		}
		return strBuff.toString();
	}
	
	/**
	 * Calculates all necessary phone number changes
	 */
	public abstract void getPhoneNumbersChanges();
	
	/**
	 * Set ContentResolver
	 * @param cr ContentResolver
	 */
	public abstract void setCr(ContentResolver cr);
	
	public abstract int execute();
	
	public void setContext(Context context){
		this.context = context;
	}
	
}