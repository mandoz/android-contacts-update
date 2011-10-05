package com.mandoz.android.contactsupdate;

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.content.ContentResolver;

/**
 * ContactAPI for Android 2.0+
 * @author Mohannad Ali
 *
 */
public class ContactAPISdk5 extends ContactAPI {

	private ContentResolver cr;
	ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

	@Override
	public void setCr(ContentResolver cr) {
		this.cr = cr;
	}


	@Override
	public void getPhoneNumbersChanges() {

		Cursor pCur = this.cr.query(Phone.CONTENT_URI, null, null, null, null);
		while (pCur.moveToNext()) {
			String phoneID = pCur.getString(pCur.getColumnIndex(Phone._ID));
			String prefix;
			String phoneNumber = getOnlyNumerics(pCur.getString(pCur
					.getColumnIndex(Phone.NUMBER)));
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
				String where = Phone._ID + " = ? ";

				String[] params = new String[] { phoneID };

				ops.add(ContentProviderOperation.newUpdate(Data.CONTENT_URI)
						.withSelection(where, params)
						.withValue(Phone.NUMBER, newNumber).build());
			}
		}
		pCur.close();
		
	}
	
	public int execute(){
		
		try {
			this.getPhoneNumbersChanges();
			context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OperationApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 100;
	}
}