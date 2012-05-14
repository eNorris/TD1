package k.android.TD1;

import java.util.ArrayList;

import android.graphics.Bitmap;
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
	public boolean visible = true;
	
	
	Tower(Bitmap srcBit) {
		super(srcBit);
	}
	
	/**
	 * Copy Constructor
	 * @param src
	 */
	Tower(Tower src){
		super((DestructableGraphicObject) src);
		visible = src.visible;
		attackMethods = new ArrayList<AttackMethod>();
		for(int i = 0; i < src.attackMethods.size(); i++)
			attackMethods.add(src.attackMethods.get(i).deepCopy());
	}
	
	Tower(int towerTypeId){
		super(towerBitmapSources.get(towerTypeId)); // TODO calculate correct index
		setType(towerTypeId);
	}
	
//	public static void loadBitmaps(int[] towerIds){
//		if(towerIds != null)
//			for(int i = 0; i < towerIds.length; i++)
//				towerBitmapSources.add(BitmapFactory.decodeResource(getResources(), towerIds[i]));
//	
//	}

//	public void setVisible(boolean v){
//		visible = v;
//	}
	
//	public boolean isVisible(){
//		return m_visible;
//	}
	
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
}






