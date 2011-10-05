package com.mandoz.android.contactsupdate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Main App activity
 * @author Mohannad Ali
 *
 */
public final class ContactsUpdateActivity extends Activity {
	ContactAPI contactsAPI;
	static final int PROGRESS_DIALOG = 0;
	ProgressThread progressThread;
    ProgressDialog progressDialog;
    Context context = this;
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            int total = msg.arg1;

            if (total >= 100){
                dismissDialog(PROGRESS_DIALOG);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Contacts Successfully Updated. Click OK to exit.")
                       .setCancelable(false)
                       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {
                        	   ContactsUpdateActivity.this.finish();
                           }
                       });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    };
	/**
	 * Called when the activity is first created. Responsible for initializing
	 * the UI.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		 TextView t3 = (TextView) findViewById(R.id.textView1);
	        t3.setText(
	            Html.fromHtml(
	                "<a href=\"http://www.mandoz.com\">www.mandoz.com</a> "));
	        t3.setMovementMethod(LinkMovementMethod.getInstance());
	    
         
		contactsAPI = ContactAPI.getAPI();
		ContentResolver cr = getContentResolver();
		//Context context = getApplicationContext();
		contactsAPI.setCr(cr);
		contactsAPI.setContext(this);
		
		final Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
       	 
            public void onClick(View v) {
            	showDialog(PROGRESS_DIALOG);
            }
        });
	}
	
	protected Dialog onCreateDialog(int id) {
        switch(id) {
        case PROGRESS_DIALOG:
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Updating contacts, please wait...");
            return progressDialog;
        default:
            return null;
        }
    }
	
	@Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch(id) {
        case PROGRESS_DIALOG:
            progressDialog.setProgress(0);
            progressThread = new ProgressThread(handler);
            progressThread.start();
    }
        
     // Define the Handler that receives messages from the thread and update the progress
        
        
	}
	
	 /** Nested class that performs progress calculations (counting) */
    private class ProgressThread extends Thread {
        Handler mHandler;
        final static int STATE_DONE = 0;
        int total;
       
        ProgressThread(Handler h) {
            mHandler = h;
        }
       
        public void run() {
                total = contactsAPI.execute();
                Message msg = mHandler.obtainMessage();
                msg.arg1 = total;
                mHandler.sendMessage(msg);
        }
    }
}