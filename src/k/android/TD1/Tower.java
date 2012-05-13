package k.android.TD1;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
	
	public ArrayList<AttackMethod> attackMethods = new ArrayList<AttackMethod>();
	public boolean visible = true;
	
	public static final String TAG = "Tower";
	
	
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
			attackMethods.add(new AttackMethod(src.attackMethods.get(i)));
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
		case 0:
			break;
		case 1:
			break;
		case 2:
			break;
		default:
			Log.e(TAG, "No towerTypeId to match " + towerTypeId);
			break;
		};
	}
}

/**
 * == STRUCT == <br>
 * Allows towers to attack creeps either directly or indrectly
 * @author Edward
 *
 */
class AttackMethod{
	// Member Variables
	public int power = 1;
	public int maxTargets = 1;
	public Tower owner;
	public ArrayList<Creep> targets = new ArrayList<Creep>();
	public static ArrayList<Creep> creepPool;
	
	public AttackMethod(Tower ownerTower){
		owner = ownerTower;
	}
	
	AttackMethod(AttackMethod src){
		power = src.power;
		maxTargets = src.maxTargets;
		owner = src.owner;
		targets = new ArrayList<Creep>();
		for(int i = 0; i < src.targets.size(); i++){
			targets.add(src.targets.get(i));
		}
	}
	
	public static void setCreepPool(ArrayList<Creep> creeps){
		creepPool = creeps;
	}
	
	
}

interface AttackMethodInterface{
//	public void setCreepPool(ArrayList<Creep> creeps);
	public void attack();
	public void findTargets();
}


class LineAttackMethod extends AttackMethod implements AttackMethodInterface{
	
	LineAttackMethod(Tower ownerTower){
		super(ownerTower);
		power = 10;
		maxTargets = 5;
	}
	
	@Override
	public void attack() {
		for(int i = 0; i < targets.size(); i++){
			if(targets.get(i).doDamageStrict(power))
			{
				targets.remove(i);
				if(i != 0) i--;
			}
		}
	}

	@Override
	public void findTargets() {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public void setCreepPool(ArrayList<Creep> creeps) {
//		creepPool = creeps;
//	}
	
}






