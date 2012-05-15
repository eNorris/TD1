package k.android.TD1;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

// TODO inherit to add Movable and DMGraphicObject as well

/**
 * == STRUCT == <br> <br>
 * -Members- <br>
 * x - x coordinate of the top left corner of the bitmap <br>
 * y - y coordinate of the top left corner of the bitmap <br>
 * cx - x coordinate of the center of the bitmap <br>
 * cy - y coordinate of the center of the bitmap <br>
 * dx - x component of the velocity of the bitmap, used in advance() <br>
 * dy - y component of the velocity of the bitmap, used in advance() <br>
 * w - width (px) of the bitmap <br>
 * h - height (px) of the bitmap <br>
 * bitmap - the Bitmap object that will be drawn <br>
 * drawable - true if the object is in a state in which drawing is legal <br>
 * visible - if true, the bitmap should be drawn as long as drawable is also true <br>
 * TAG - String containing "GraphicObject", used for debugging <br>
 * @author Edward Noris
 *
 */
class GraphicObject{
	
	// Struct members
	public int x = 0, y = 0, cx = 0, cy = 0, dx = 0, dy = 0,
			h = 0, w = 0;
	public Bitmap bitmap = null;
	public boolean drawable = false;
	public boolean visible = true;
	public static final String TAG = "GraphicObject";
	
	/**
	 * Default Constructor <br> <br>
	 * Leaves all values as their default values; not in a drawable state
	 */
	GraphicObject(){
		// Default
	}
	
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
	
	/**
	 * Creates a deep copy of a GraphicObject. Deep copying of the bitmap field is 
	 * not guaranteed
	 * @return
	 * 	A deep copy of the calling object
	 */
	public GraphicObject deepCopy(){
		// TODO test
		GraphicObject tmp = new GraphicObject();
		tmp.x = x;
		tmp.y = y;
		tmp.cx = cx;
		tmp.cy = cy;
		tmp.dx = dx;
		tmp.dy = dy;
		tmp.w = w;
		tmp.h = h;
		tmp.visible = visible;
		tmp.drawable = drawable;
		tmp.bitmap = Bitmap.createBitmap(bitmap);
		return tmp;
	}
	
	/**
	 * <b>Shadow on Bitmap:bitmap </b> <br><br>
	 * Creates a shadow copy of the calling object
	 * @return
	 * 	a shadow (in bitmap) of the calling object
	 */
	public GraphicObject shadowCopy(){
		GraphicObject tmp = new GraphicObject();
		tmp.x = x;
		tmp.y = y;
		tmp.cx = cx;
		tmp.cy = cy;
		tmp.dx = dx;
		tmp.dy = dy;
		tmp.w = w;
		tmp.h = h;
		tmp.visible = visible;
		tmp.drawable = drawable;
		tmp.bitmap = bitmap;
		return tmp;
	}
	
	public void setPos(int sx, int sy){
		x = sx; y = sy;
		updateCenter();
	}
	
	public void setCenter(int scx, int scy){
		cx = scx; cy = scy;
		updateTopLeft();
	}
	
	public void move(int sx, int sy){
		x += sx; y += sy;
		cx += sx; cy += sy;
	}
	
	public void advance(){
		move(dx, dy);
	}
	
	public void updateCenter(){
		cx = x + w/2;
		cy = y + h/2;
	}
	
	public void updateTopLeft(){
		x = cx - w/2;
		y = cy - h/2;
	}
	
	public void updateSize(){
		if(bitmap != null){
			w = bitmap.getWidth();
			h = bitmap.getHeight();
		}else{
			w = h = 0;
		}
	}
	
	/**
	 * Ensures the object can be drawn and draws it, checks the canvas state, the bitmap state,
	 * drawable and visible
	 * @param canvas - The canvas to which the GraphicObject will be drawn
	 * @return
	 * 	True if the object was drawn, false otherwise
	 */
	public boolean draw(Canvas canvas){
		if(canvas != null && drawable && bitmap!= null && visible){
			canvas.drawBitmap(bitmap, x, y, null);
			return true;
		}else{
			if(drawable && bitmap == null)
				Log.e(TAG, "drawing null drawable GraphicObject");
			return false;
		}
	}
}



/**
 * Extends the GraphicObject class (Privatized). Adds health functionality <br><br>
 * @author Edward Norris
 *
 */
class DestructableGraphicObject extends GraphicObject{

	// Member Variables
	int m_maxHealth = 1;
	int m_health = 1;
	boolean m_alive = true;
	
	DestructableGraphicObject(){
		// Defaults
	}
	
	DestructableGraphicObject(Bitmap srcBitmap) {
		super(srcBitmap);
	}
	
	public DestructableGraphicObject deepCopy(){
		DestructableGraphicObject tmp = (DestructableGraphicObject) super.deepCopy();
		tmp.m_maxHealth = m_maxHealth;
		tmp.m_health = m_health;
		tmp.m_alive = m_alive;
		return tmp;
	}
	
	public DestructableGraphicObject shadowCopy(){
		DestructableGraphicObject tmp = new DestructableGraphicObject();
//		tmp = (DestructableGraphicObject) super.shadowCopy();
		
		// TODO figure out how you inherit from super.shadowCopy()
		tmp.x = x;
		tmp.y = y;
		tmp.cx = cx;
		tmp.cy = cy;
		tmp.dx = dx;
		tmp.dy = dy;
		tmp.w = w;
		tmp.h = h;
		tmp.visible = visible;
		tmp.drawable = drawable;
		tmp.bitmap = bitmap;
		
		tmp.m_maxHealth = m_maxHealth;
		tmp.m_health = m_health;
		tmp.m_alive = m_alive;
		return tmp;
	}
	
	/**
	 * Initializes an objects health, both maxHealth and health are set to the 
	 * provided parameter
	 * @param health
	 * 	The value to which health and maxHealth will be set. If this value is 0 or 
	 * 	less, both will be set to 1
	 */
	public void initHealth(int health){
		setMaxHealth(health);
		m_health = m_maxHealth;
	}
	
	/**
	 * Sets an objects maxHealth. Must be greater than zero
	 * @param maxHealth
	 * 	The value maxHealth will be set to - if zero or less, maxHealth will be 
	 * 	set to 1 instead and false will be returned.
	 * @return
	 * 	True if maxHealth was set the the variable sent, false if it was defaulted to 1
	 */
	public boolean setMaxHealth(int maxHealth){
		if(maxHealth > 0){
			m_maxHealth = maxHealth;
			return true;
		}else{
			m_maxHealth = 1;
			return false;
		}
	}
	
	/**
	 * Damages the object and returns whether the object is still alive after
	 * damage is dealt 
	 * @param damageDone
	 * 	The amount of health subtracted from m_health (can be negative or zero, 
	 *  does <b>Not</b> cap amount of damage that can be healed)
	 * @return
	 * 	True if the object is still alive, false otherwise
	 */
	public boolean doDamage(int damageDone){
		m_health -= damageDone;
		return checkAlive();
	}
	
	/**
	 * Damages the object and makes certain checks to verify that the object was
	 * damaged and that the objects health never becomes negative
	 * @param damageDone
	 * 	The amount of damage done, negative values will be treated as 0
	 * @return
	 * 	True if the object is still alive, false otherwise
	 */
	public boolean doDamageStrict(int damageDone){
		int correctedDamage = damageDone < 0 ? 0 : damageDone;
		m_health -= correctedDamage;
		m_health = m_health < 0 ? 0 : m_health;
		return checkAlive();
	}
	
	/**
	 * Strictly heals the object (will not damage) and will not allow health
	 * to exceed maxHealth
	 * @param healDone
	 * 	The amount of health that will be restored, health will be capped at maxHealth
	 * @return
	 * 	True if the object is still alive, false otherwise
	 */
	public boolean doHealStrict(int healDone){
		int correctedHeal = healDone < 0 ? 0 : healDone;
		m_health -= correctedHeal;
		m_health = m_health > m_maxHealth ? m_maxHealth : m_health;
		return checkAlive();
	}
	
	/**
	 * Adds to an object's current health, will not allow health to exceed maxHealth
	 * @param healDone
	 * 	The amount that will be added to the object's health (will accept zero
	 *  negative numbers)
	 * @return
	 * 	True if the object is still alive, false otherwise
	 */
	public boolean doHeal(int healDone){
		m_health += healDone;
		if(m_health > m_maxHealth)
			m_health = m_maxHealth;
		return checkAlive();
	}
	
	
	/**
	 * updates m_alive and returns true if still alive
	 * @return
	 * 	True if health > 0, false otherwise false
	 */
	public boolean checkAlive(){
		if(m_health > 0)
			m_alive = true;
		else
			m_alive = false;
		return m_alive;
	}
	
	/**
	 * Instantly brings a creep's health to zero and sets it as no longer alive
	 */
	public void kill(){
		m_health = 0;
		m_alive = false;
	}
	
	public boolean draw(Canvas canvas){
		if(canvas != null && drawable && bitmap!= null && visible){
			canvas.drawBitmap(bitmap, x, y, null);
			return true;
		}else{
			if(drawable && bitmap == null)
				Log.e(TAG, "drawing null drawable DestructableGraphicObject");
			return false;
		}
	}
	
}












