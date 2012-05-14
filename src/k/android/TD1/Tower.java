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
	public static final int TYPE_1 = 0;
	public static final int TYPE_2 = 1;
	public static final int TYPE_3 = 2;
	
	// Member Variables
	public ArrayList<AttackMethod> attackMethods = new ArrayList<AttackMethod>();
	
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
	
	public Tower deepCopy(){
		Tower tmp = new Tower();
		tmp = (Tower) super.deepCopy();
		attackMethods = new ArrayList<AttackMethod>();
		for(int i = 0; i < attackMethods.size(); i++)
			tmp.attackMethods.add(attackMethods.get(i).deepCopy());
		return tmp;
	}
	
	public Tower shadowCopy(){
		Tower tmp = new Tower();
		tmp = (Tower) super.shadowCopy();
		attackMethods = new ArrayList<AttackMethod>();
		for(int i = 0; i < attackMethods.size(); i++)
			tmp.attackMethods.add(attackMethods.get(i).deepCopy());
		return tmp;
	}
	
	public void setType(int towerTypeId){
		switch (towerTypeId){
		case TYPE_1:
			bitmap = towerBitmapSources.get(0);
			attackMethods.add(new LineAttackMethod(this));
			break;
		case TYPE_2:
			bitmap = towerBitmapSources.get(1);
			attackMethods.add(new LineAttackMethod(this));
			break;
		case TYPE_3:
			bitmap = towerBitmapSources.get(2);
			attackMethods.add(new LineAttackMethod(this));
			break;
		default:
			Log.e(TAG, "No towerTypeId to match " + towerTypeId);
			break;
		};
	}
	
	public void draw(Canvas canvas){
		if(canvas != null && drawable && bitmap != null && visible){
			canvas.drawBitmap(bitmap, x, y, null);
			for(int i = 0; i < attackMethods.size(); i++)
				attackMethods.get(i).draw(canvas);
		}
	}
}






