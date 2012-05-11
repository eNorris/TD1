package k.android.TD1;

import java.util.ArrayList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;

public class Creep extends DestructableGraphicsObject{

	// Member Variables
	private static final String TAG = "Creep";
	float movement = 10;
	float movementLeft = 1;
	float movementToGo = 1;
	int defense = 1;
	int mass = 1;
	int targetPoint = 0;
	boolean lockedToPath = false;
	Color color = new Color();
	CreepPath path = new CreepPath();
	Creep(Bitmap srcBitmap) {
		super(srcBitmap);
	}
	
	// returns true when path is completed
	public boolean calcNextPoint(){
		targetPoint++;
		if(targetPoint >= path.points.size())
			return true;
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
	
	// returns true on path completion
	// TODO
	public void pathAdvance(){
		if(lockedToPath){
			movementLeft = movement;
			advance();
			movementToGo -= movementLeft;
			if(movementToGo <= 0){
				movementLeft = movementToGo * -1;
				calcNextPoint();
				pathAdvance();
			}
		}else{
			Log.e(TAG, "Advancing on no path");
		}
	}
	
	// returns true if the creep was successfully put on the path
	public boolean setOnPath(CreepPath spath){
		if(spath != null && spath.isValid()){
			path = spath;
			lockedToPath = true;
			return true;
		}else{
			Log.e(TAG, "Setting on invalid path");
		}
		return false;
	}
}


// Validate that the path contains at least two elements
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
	
	public boolean isValid(){
		return points.size() < 2;
	}
}













