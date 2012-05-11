package k.android.TD1;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class Tower extends DestructableGraphicsObject{
	
	// Member Variables
	ArrayList<AttackMethod> m_attachMethods = new ArrayList<AttackMethod>();
	
	Tower(Bitmap srcBit) {
		super(srcBit);
	}

	public void doNothing(){
		
	}
}

class AttackMethod{
	
}