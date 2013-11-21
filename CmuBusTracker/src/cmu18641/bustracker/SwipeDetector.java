package cmu18641.bustracker;

import android.util.Log;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;

public class SwipeDetector extends SimpleOnGestureListener {
	 private static final int SWIPE_MIN_DISTANCE = 120;
     private static final int SWIPE_MAX_OFF_PATH = 250;
     private static final int SWIPE_THRESHOLD_VELOCITY = 200;

     
     //SwipeDetector(Context current, Context desired) { 
    //	 
     //}
     
     @Override
     public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
         if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
            return false;
         }
         // right to left swipe
         if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
             Log.v("gesture", "Fling left");
             return true;
         } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
             Log.v("gesture", "Fling right");
             return true;
         }

         return false;
     }
     
     @Override
     public boolean onDown(MotionEvent event) { 
         Log.v("gesture","onDown event"); 
         return true;
     }
}
