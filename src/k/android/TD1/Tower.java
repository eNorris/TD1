package k.android.TD1;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

/**
 * == STRUCT == <br>
 * @author Edward
 *
 */
public class Tower extends DestructableGraphicObject{
	
	// Static Member
	public static ArrayList<Bitmap> towerBitmapSources;
	public static int[] towerBitmapIds = {
			R.drawable.towerdefault,
			R.drawable.tower1,
			R.drawable.tower2,
			R.drawable.tower3
		};
	
	public static final String TAG = "Tower";
	public static final int TYPE_UNDEF = -1;
	public static final int TYPE_1 = 1;
	public static final int TYPE_2 = 2;
	public static final int TYPE_3 = 3;
	
	// Member Variables
	public ArrayList<AttackMethod> attackMethods = new ArrayList<AttackMethod>();
	public boolean active = false;
	
	public Tower(){
		// Default
	}
	
	Tower(Bitmap srcBit) {
		super(srcBit);
	}
	
	Tower(int towerTypeId){
		super(towerBitmapSources.get(towerTypeId)); // TODO calculate correct index
		setType(towerTypeId);
	}
	
//	public Tower deepCopy(){
//		Tower tmp = new Tower();
//		tmp = (Tower) super.deepCopy();
//		attackMethods = new ArrayList<AttackMethod>();
//		for(int i = 0; i < attackMethods.size(); i++)
//			tmp.attackMethods.add(attackMethods.get(i).deepCopy());
//		return tmp;
//	}
	
	public Tower shadowCopy(){
		Tower tmp = new Tower();
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
		tmp.active = active;
		tmp.attackMethods = new ArrayList<AttackMethod>();
		for(int i = 0; i < attackMethods.size(); i++){
			tmp.attackMethods.add(attackMethods.get(i).deepCopy());
		}
		return tmp;
	}
	
	public void setType(int towerTypeId){
		drawable = true;
		attackMethods.clear();
		
		switch (towerTypeId){
		case TYPE_1:
			bitmap = towerBitmapSources.get(Tower.TYPE_1);
			attackMethods.add(new LineAttackMethod(this));
			break;
		case TYPE_2:
			bitmap = towerBitmapSources.get(Tower.TYPE_2);
			attackMethods.add(new LineAttackMethod(this));
			break;
		case TYPE_3:
			bitmap = towerBitmapSources.get(Tower.TYPE_3);
			attackMethods.add(new LineAttackMethod(this));
			break;
		default:
			Log.e(TAG, "No towerTypeId to match " + towerTypeId);
			break;
		};
		updateSize();
	}
	
	public boolean draw(Canvas canvas){
//		Log.d(TAG, "active = " + active);
///		boolean toReturn = super.draw(canvas);
//		for(int i = 0; i < attackMethods.size(); i++){
//			Log.d(TAG, "launching attacks!");
//			attackMethods.get(i).findTargets();
//			attackMethods.get(i).attack();
//			if(!attackMethods.get(i).draw(canvas)){
//				toReturn = false;
//				Log.e(TAG, "AttackMethod failed to draw");
//			}
///		}
		return super.draw(canvas);
	}
}






