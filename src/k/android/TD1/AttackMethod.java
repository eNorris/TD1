package k.android.TD1;

import java.util.ArrayList;

import android.graphics.Canvas;

/**
 * == STRUCT == <br>
 * Allows towers to attack creeps either directly or indrectly
 * @author Edward
 *
 */
abstract class AttackMethod{
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
	
	abstract public void drawSelf(Canvas canvas);
	
	abstract public AttackMethod deepCopy();
}

// TODO Just put all these in the abstract type and get rid of the interface altogether?
interface AttackMethodInterface{
//	public void setCreepPool(ArrayList<Creep> creeps);
	public void attack();
	public void findTargets();
	public void drawSelf(Canvas canvas);
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
	public void drawSelf(Canvas canvas) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LineAttackMethod deepCopy() {
		LineAttackMethod tmp = new LineAttackMethod(owner);
		tmp.power = power;
		tmp.maxTargets = maxTargets;
		for(int i = 0; i < targets.size(); i++)
			tmp.targets.add(targets.get(i));
		return tmp;
	}

//	@Override
//	public void setCreepPool(ArrayList<Creep> creeps) {
//		creepPool = creeps;
//	}
	
}
