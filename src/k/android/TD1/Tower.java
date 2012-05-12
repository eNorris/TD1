package k.android.TD1;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class Tower extends DestructableGraphicObject{
	
	// Member Variables
	ArrayList<AttackMethod> m_attackMethods = new ArrayList<AttackMethod>();
	
	Tower(Bitmap srcBit) {
		super(srcBit);
	}

	public void doNothing(){
		
	}
}

/**
 * == STRUCT == <br>
 * Allows towers to attack creeps either directly or indrectly
 * @author new user
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
}

interface AttackMethodInterface{
	public void setCreepPool(ArrayList<Creep> creeps);
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

	@Override
	public void setCreepPool(ArrayList<Creep> creeps) {
		creepPool = creeps;
	}
	
}






