package k.android.TD1;

public class Util {

	public static float distance(int x1, int y1, int x2, int y2){
		float dx = (float)(x1-x2);
		float dy = (float)(y1-y2);
		return (float) Math.sqrt(dx*dx+dy*dy);
	}
	
}
