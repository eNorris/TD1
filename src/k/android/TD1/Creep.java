package k.android.TD1;

import java.util.ArrayList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;

public class Creep extends DestructableGraphicObject{

	// Member Variables
	private static final String TAG = "Creep";
	float movement = 10;
	float movementLeft = 5;
	float movementToGo = 1;
//	int defense = 1;
//	int mass = 1;
	int targetPoint = 0;
	boolean lockedToPath = false;
//	Color color = new Color();
	CreepPath path = new CreepPath();
	
	Creep(Bitmap srcBitmap) {
		super(srcBitmap);
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
		float modifier = totaltogo == 0.0f ? 0.0f : movement / totaltogo;
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
	public boolean pathAdvance(){
		if(lockedToPath){
			movementLeft = movement;
			advance();
			movementToGo -= movementLeft;
			if(movementToGo <= 0){
				movementLeft = movementToGo * -1;
				if(calcNextPoint())
					return true;
				pathAdvance();
			}
		}else{
			Log.v(TAG, "Advancing on no path");
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
			x = path.points.get(0).x;
			y = path.points.get(0).y;
			lockedToPath = true;
			Log.v(TAG, "Set to valid path");
			return true;
		}else{
			Log.v(TAG, "Setting on invalid path");
		}
		return false;
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













