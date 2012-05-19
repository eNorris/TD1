package k.android.TD1;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;

/**
 * == STRUCT == <br>
 * Creep class - The things towers shoot at
 * @author Edward
 *
 */
public class Creep extends DestructableGraphicObject{

	// Static Variables
	public static ArrayList<Bitmap> creepBitmapSources;
	public static int[] creepBitmapIds = {
			R.drawable.creep1,
			R.drawable.creep2,
			R.drawable.creep3
		};
	public static final String TAG = "Creep";
	public static final int TYPE_UNDEF = -1;
	public static final int CREEP_TYPE_1 = 0;
	public static final int CREEP_TYPE_2 = 1;
	public static final int CREEP_TYPE_3 = 2;
	
	// Member Variables
	float movement = 10;
	float movementLeft = 5;
	float movementToGo = 1;
	int targetPoint = 0;
	boolean lockedToPath = false;
	CreepPath path = new CreepPath();
	public HealthBar healthBar;
	

	public Creep(){
		// Default
	}
	
	public Creep(Bitmap srcBitmap, int creepTypeId) {
		super(srcBitmap);
		setCreepType(creepTypeId);
		initHealthBar();
	}
	
	public Creep(int creepTypeId){
		super(creepBitmapSources.get(creepTypeId));
		setCreepType(creepTypeId);
		initHealthBar();
	}
	
	public void initHealthBar(){
		healthBar = new HealthBar(30, 5, 1, this.m_health, this.m_maxHealth);
	}
	
	/**
	 * 
	 * @return
	 * 	True when the path is completed, false otherwise
	 */
	public boolean calcNextPoint(){
		targetPoint++;
		if(targetPoint >= path.points.size()){
			lockedToPath = false; // release the path
			return true;
		}
		float xtogo = (float) path.at(targetPoint).x - (float) x;
		float ytogo = (float) path.at(targetPoint).y - (float) y;
		float totaltogo = (float) Math.sqrt(xtogo*xtogo + ytogo*ytogo);
		movementToGo = totaltogo;
		// Check for divide by zero error in the case of multiple points on top of one another
		float modifier = (totaltogo == 0.0f ? 0.0f : movement / totaltogo);
		dx = (int) (xtogo * modifier);
		dy = (int) (ytogo * modifier);
		return false;
	}
	
	public void resetToSamePoint(){
		float xtogo = (float) path.at(targetPoint).x - (float) x;
		float ytogo = (float) path.at(targetPoint).y - (float) y;
		float totaltogo = (float) Math.sqrt(xtogo*xtogo + ytogo*ytogo);
		float modifier = movement / totaltogo;
		dx = (int) (xtogo * modifier);
		dy = (int) (ytogo * modifier);
	}
	
	/**
	 * 
	 * @return
	 * 	True if the creep has reached the end of the line
	 */
	public boolean advanceAlongPath(){
		if(lockedToPath){
			movementLeft = movement;
			advance();
			movementToGo -= movementLeft;
			if(movementToGo <= 0){
				movementLeft = movementToGo * -1;
				if(calcNextPoint())
					return true;
				advanceAlongPath();
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param spath
	 * @return
	 * 	True if the creep was successfully set to the path
	 */
	public boolean setOnPath(CreepPath spath){
		if(spath != null && spath.isValid()){
			path = spath;
			setCenter(path.points.get(0).x, path.points.get(0).y);
//			x = path.points.get(0).x;
//			y = path.points.get(0).y;
			lockedToPath = true;
//			Log.v(TAG, "Set to valid path");
			return true;
		}
		return false;
	}
	
	public void setCreepType(int creepTypeId){
		drawable = true;
		visible = true;
		switch(creepTypeId){
		case Creep.CREEP_TYPE_1:
			initHealth(300);
			movement = 5;
			break;
		case Creep.CREEP_TYPE_2:
			initHealth(500);
			movement = 7;
			break;
		case Creep.CREEP_TYPE_3:
			initHealth(1000);
			movement = 3;
			break;
		default:
			initHealth(1);
		}
	}
	
	public void onDeath(){
		GameView.playerHealth -= this.m_health;
//		Log.v(TAG, "@onDeath: calling");
		// The creep can no longer be trargeted
		for(int i = 0; i < GameView.worldTowerList.size(); i++){
			for(int j = 0; j < GameView.worldTowerList.get(i).attackMethods.size(); j++){
				GameView.worldTowerList.get(i).attackMethods.get(j).targets.remove(this);
			}
		}
		// The creep no longer exists in the world at all
		GameView.worldCreepList.remove(this);
	}

	@Override
	public boolean draw(Canvas canvas) {
		healthBar.setPos(x + 5, y - 5);
		return (super.draw(canvas) && healthBar.draw(canvas));
	}
}


class CreepPath{
	public ArrayList<Point> points;
	
	public CreepPath(){
		points = new ArrayList<Point>();
	}
	
	public void addPoint(Point p){
		points.add(p);
	}
	
	public Point at(int index){
		return points.get(index);
	}
	
	/**
	 * Validates the path actually contains at least two points
	 * @return
	 * 	True if the path contains at least 2 points
	 */
	public boolean isValid(){
		return points.size() >= 2;
	}
}













