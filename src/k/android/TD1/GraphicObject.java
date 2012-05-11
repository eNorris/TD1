package k.android.TD1;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 
 * @author Edward Noris
 *
 */
class GraphicObject{
	public int x = 0, y = 0, cx = 0, cy = 0, dx = 0, dy = 0,
			h = 0, w = 0;
	Bitmap bitmap = null;
	
	/**
	 * Loads a new resource into the Graphics Object, automatically
	 * allocates the width and height
	 * @param res
	 * 	The resources used - generally call getResource()
	 * @param resId
	 * 	The resource id - generally R.drawable.X
	 */
	GraphicObject(Resources res, int resId){
		bitmap = BitmapFactory.decodeResource(res, resId);
		w = (int) bitmap.getWidth();
		h = (int) bitmap.getHeight();
		cx = w/2;
		cy = h/2;
	}
	
	/**
	 * Clones an already existing Bitmap into a GraphicObject. This allows an array
	 * of Bitmaps to be created and each GraphicObject can reference that array
	 * and still remain independent of other GraphicObjects in terms of location
	 * @param srcBitmap
	 * 	The Bitmap that will be cloned in
	 */
	GraphicObject(Bitmap srcBitmap){
		bitmap = srcBitmap;
		w = (int) bitmap.getWidth();
		h = (int) bitmap.getHeight();
		cx = w/2;
		cy = h/2;
	}
	
	public void setPos(int sx, int sy){
		x = sx; y = sy;
		updateCenter();
	}
	
	public void setCenter(int scx, int scy){
		cx = scx; cy = scy;
		updateTopLeft();
	}
	
	public void updateCenter(){
		cx = x + w/2;
		cy = y + h/2;
	}
	
	public void updateTopLeft(){
		x = cx - w/2;
		y = cy - h/2;
	}
}
