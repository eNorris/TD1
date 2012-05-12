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

class AttackMethod{
	
}