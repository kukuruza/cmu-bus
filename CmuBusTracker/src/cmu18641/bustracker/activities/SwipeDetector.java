package cmu18641.bustracker.activities;

import cmu18641.bustracker.R;
import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;

public class SwipeDetector extends SimpleOnGestureListener {
	 private static final int SWIPE_MIN_DISTANCE = 120;
     private static final int SWIPE_MAX_OFF_PATH = 200;
     private static final int SWIPE_THRESHOLD_VELOCITY = 200;

     private Context currentContext;
     
     SwipeDetector(Context current) { 
    	 this.currentContext = current; 
     }
     
     @Override
     public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
    	 boolean flingStatus = false; 
    	 
         // swipe is not sufficiently horizontal 
    	 if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
    		 flingStatus = false;
         }
         // right to left swipe detected 
    	 else if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
             Log.v("gesture", "Fling left event"); 
             flingStatus = true;
         } 
         // left to right swipe detected
         else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
             Log.v("gesture", "Fling right event");
             MediaPlayer mPlayer = MediaPlayer.create(currentContext, R.raw.swipe);
             mPlayer.start();
             
             mPlayer.setOnCompletionListener(new OnCompletionListener() {

                 public void onCompletion(MediaPlayer mPlayer) {
                     mPlayer.release();
                     mPlayer = null; 
                 }

             });

 			 ((Activity)currentContext).finish(); // end activity
             flingStatus = true;
         }

         return flingStatus;
     }
     
     @Override
     public boolean onDown(MotionEvent event) { 
         Log.v("gesture","onDown event"); 
         return true;
     }
}
