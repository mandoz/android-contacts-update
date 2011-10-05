package com.mandoz.android.contactsupdate;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;

/**
 * ContactAPI for Android 1.5
 * 
 * @author Mohannad Ali
 * 
 */
@SuppressWarnings("deprecation")
public class ContactAPISdk3 extends ContactAPI {

	private ContentResolver cr;

	@Override
	public void setCr(ContentResolver cr) {
		this.cr = cr;
	}

	@Override
	public void getPhoneNumbersChanges() {

		Cursor pCur = this.cr.query(Contacts.Phones.CONTENT_URI, null, null,
				null, null);
		while (pCur.moveToNext()) {
			long phoneID = pCur.getLong(pCur
					.getColumnIndex(Contacts.Phones._ID));
			String prefix;
			String phoneNumber = getOnlyNumerics(pCur.getString(pCur
					.getColumnIndex(Contacts.Phones.NUMBER)));

			String newPrefix = null;
			String newNumber = null;

			if (phoneNumber.charAt(0) == '+') {
				if (phoneNumber.substring(0, 3).equals("+20")) {
					if (phoneNumber.length() == 12) {
						prefix = phoneNumber.substring(0, 5);
						if (prefix.equals("+2010") || prefix.equals("+2016") || prefix.equals("+2019") ) {
							newPrefix = prefix.substring(0, 4) + "0" + prefix.substring(4, 5);
							newNumber = newPrefix + phoneNumber.substring(5, phoneNumber.length());
						} else if (prefix.equals("+2011") || prefix.equals("+2014")) {
							newPrefix = prefix.substring(0, 4) + "1" + prefix.substring(4, 5);
							newNumber = newPrefix + phoneNumber.substring(5, phoneNumber.length());
						} else if (prefix.equals("+2012") || prefix.equals("+2017") || prefix.equals("+2018")) {
							newPrefix = prefix.substring(0, 4) + "2" + prefix.substring(4, 5);
							newNumber = newPrefix + phoneNumber.substring(5, phoneNumber.length());
						} 
					}else if(phoneNumber.length() == 13) {
						prefix = phoneNumber.substring(0, 6);
						if(prefix.equals("+20151")){
							newPrefix = "+20101";
							newNumber = newPrefix + phoneNumber.substring(6, phoneNumber.length());
						}else if(prefix.equals("+20152")){
							newPrefix = "+20112";
							newNumber = newPrefix + phoneNumber.substring(6, phoneNumber.length());
						}else if(prefix.equals("+20150")){
							newPrefix = "+20120";
							newNumber = newPrefix + phoneNumber.substring(6, phoneNumber.length());
						}else if(prefix.equals("+20155")){
							newPrefix = "+20115";
							newNumber = newPrefix + phoneNumber.substring(6, phoneNumber.length());
						}
					}
				}
			}else{
				if (phoneNumber.length() == 10) {
					prefix = phoneNumber.substring(0, 3);
					if (prefix.equals("010") || prefix.equals("016") || prefix.equals("019") ) {
						newPrefix = prefix.substring(0, 2) + "0" + prefix.substring(2, 3);
						newNumber = newPrefix + phoneNumber.substring(3, phoneNumber.length());
					} else if (prefix.equals("011") || prefix.equals("014")) {
						newPrefix = prefix.substring(0, 2) + "1" + prefix.substring(2, 3);
						newNumber = newPrefix + phoneNumber.substring(3, phoneNumber.length());
					} else if (prefix.equals("012") || prefix.equals("017") || prefix.equals("018")) {
						newPrefix = prefix.substring(0, 2) + "2" + prefix.substring(2, 3);
						newNumber = newPrefix + phoneNumber.substring(3, phoneNumber.length());
					} 
				}else if(phoneNumber.length() == 11) {
					prefix = phoneNumber.substring(0, 4);
					if(prefix.equals("0151")){
						newPrefix = "0101";
						newNumber = newPrefix + phoneNumber.substring(4, phoneNumber.length());
					}else if(prefix.equals("0152")){
						newPrefix = "0112";
						newNumber = newPrefix + phoneNumber.substring(4, phoneNumber.length());
					}else if(prefix.equals("0150")){
						newPrefix = "0120";
						newNumber = newPrefix + phoneNumber.substring(4, phoneNumber.length());
					}else if(prefix.equals("0155")){
						newPrefix = "0115";
						newNumber = newPrefix + phoneNumber.substring(4, phoneNumber.length());
					}
				}else if (phoneNumber.length() == 13) {
					prefix = phoneNumber.substring(0, 6);
					if (prefix.equals("002010") || prefix.equals("002016") || prefix.equals("002019") ) {
						newPrefix = prefix.substring(0, 5) + "0" + prefix.substring(5, 6);
						newNumber = newPrefix + phoneNumber.substring(3, phoneNumber.length());
					} else if (prefix.equals("002011") || prefix.equals("002014")) {
						newPrefix = prefix.substring(0, 5) + "1" + prefix.substring(5, 6);
						newNumber = newPrefix + phoneNumber.substring(3, phoneNumber.length());
					} else if (prefix.equals("002012") || prefix.equals("002017") || prefix.equals("002018")) {
						newPrefix = prefix.substring(0, 5) + "2" + prefix.substring(5, 6);
						newNumber = newPrefix + phoneNumber.substring(3, phoneNumber.length());
					} 
				}else if(phoneNumber.length() == 14){
					prefix = phoneNumber.substring(0, 7);
					if(prefix.equals("0020151")){
						newPrefix = "0020101";
						newNumber = newPrefix + phoneNumber.substring(7, phoneNumber.length());
					}else if(prefix.equals("0020152")){
						newPrefix = "0020112";
						newNumber = newPrefix + phoneNumber.substring(7, phoneNumber.length());
					}else if(prefix.equals("0020150")){
						newPrefix = "0020120";
						newNumber = newPrefix + phoneNumber.substring(7, phoneNumber.length());
					}else if(prefix.equals("0020155")){
						newPrefix = "0020115";
						newNumber = newPrefix + phoneNumber.substring(7, phoneNumber.length());
					}
				}
			}

			if (newNumber != null) {
				ContentValues values = new ContentValues();
				values.put(Contacts.Phones.NUMBER, newNumber);
				System.out.println(phoneID + " - " + phoneNumber);
				Uri uri = ContentUris.withAppendedId(
						Contacts.Phones.CONTENT_URI, phoneID);
				this.cr.update(uri, values, null, null);
			}
		}
		pCur.close();
	}

	@Override
	public int execute() {

		this.getPhoneNumbersChanges();

		return 100;
	}
}