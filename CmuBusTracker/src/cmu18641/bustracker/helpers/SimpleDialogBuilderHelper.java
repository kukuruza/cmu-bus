package cmu18641.bustracker.helpers;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;

public class SimpleDialogBuilderHelper {

	private Context context; 
	private String message; 
	private String buttonText; 
	
	public SimpleDialogBuilderHelper(Context context, String message, String buttonText) { 
		this.context = context; 
		this.message = message; 
		this.buttonText = buttonText; 
		showMessage(); 
	}
	
	// need to set cancelable
	// button string 
	// button press event
	public void showMessage() { 
		Builder builder = new AlertDialog.Builder(context);
	    builder.setMessage(message);
	    builder.setCancelable(true); 
	    builder.setPositiveButton(buttonText,
	            new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int id) {
	        	dialog.cancel();

	        }
	    });
	    
	    AlertDialog dialog = builder.create();
	    dialog.show();
	}
	
}
