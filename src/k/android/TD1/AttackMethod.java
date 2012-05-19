package k.android.TD1;

import java.util.ArrayList;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

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
	public Paint paint = new Paint();
	
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
	
	abstract public boolean draw(Canvas canvas);
	abstract public AttackMethod deepCopy();
	abstract public void attack();
	abstract public void findTargets();
}



class LineAttackMethod extends AttackMethod{
	
	public static final String TAG = "AttackMethod";
	
	LineAttackMethod(Tower ownerTower){
		super(ownerTower);
		power = 3;
		maxTargets = 2;
	}
	
	@Override
	public void attack() {
		if(owner.active){
			for(int i = 0; i < targets.size(); i++){
				if(targets.get(i).doDamageStrict(power))
				{
					targets.get(i).onDeath();
					if(i != 0) i--;
				}
			}
		}
	}

	@Override
	public void findTargets() {
		if(owner.active){
			for(int i = 0; i < (maxTargets - targets.size()); i++){
				float bestDist = 1000000000.0f;
				int bestIndex = -1;
				for(int j = 0; j < creepPool.size(); j++){
					float dist = Util.distance(owner.x, owner.y, creepPool.get(j).x, creepPool.get(j).y);
					if(dist < bestDist && !targets.contains(creepPool.get(j))){
						bestIndex = j;
						bestDist = dist;
					}
				}
				if(bestIndex != -1)
					targets.add(creepPool.get(bestIndex));
			}
		}
	}

	@Override
	public boolean draw(Canvas canvas) {
		paint.setColor(Color.RED);
		for(int i = 0; i < targets.size(); i++)
			if(canvas != null)
				canvas.drawLine(owner.cx, owner.cy, targets.get(i).cx, targets.get(i).cy, paint);
		return true;
	}

	// TODO this isn't copying properly
	@Override
	public LineAttackMethod deepCopy() {
		LineAttackMethod tmp = new LineAttackMethod(owner);
		tmp.power = power;
		tmp.maxTargets = maxTargets;
		tmp.targets = new ArrayList<Creep>();
		for(int i = 0; i < targets.size(); i++)
			tmp.targets.add(targets.get(i));
		tmp.paint = new Paint();
		return tmp;
	}
}



class CollisionAttackMethod extends AttackMethod{

	public CollisionAttackMethod(Tower ownerTower) {
		super(ownerTower);
	}

	@Override
	public boolean draw(Canvas canvas) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public AttackMethod deepCopy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void attack() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void findTargets() {
		// TODO Auto-generated method stub
		
	}
	
	class Collidable extends MovableGraphicObject{
		
	}
	
}

















