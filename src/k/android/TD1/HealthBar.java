package k.android.TD1;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * == STRUCT == <br><br>
 * @author Edward
 *
 */
public class HealthBar {
	public Paint borderPaint = new Paint();
	public Paint harmPaint = new Paint();
	public Paint healthPaint = new Paint();
	
	public Rect borderRect = new Rect();
	public Rect harmRect = new Rect();
	public Rect healthRect = new Rect();
	
	public int x, y, w, h, border, health, maxHealth;
	
	public HealthBar(int width, int height, int borderWidth, int currentHealth, int maximumHealth){
		health = currentHealth;
		maxHealth = maximumHealth;
		w = width;
		h = height;
		borderPaint.setColor(Color.BLACK);
		harmPaint.setColor(Color.RED);
		healthPaint.setColor(Color.GREEN);
	}
	
	public boolean draw(Canvas canvas){
		borderRect.left = x - border;
		borderRect.right = x + w + 2 * border;
		borderRect.top = y - border;
		borderRect.bottom = y + h + border;
		canvas.drawRect(borderRect, borderPaint);
		
		harmRect.left = x;
		harmRect.right = x + w;
		harmRect.top = y;
		harmRect.bottom = y + h;
		canvas.drawRect(harmRect, harmPaint);
		
		healthRect.left = x;
		if(maxHealth != 0)
			healthRect.right = (x + w) * (health/maxHealth);
		else
			healthRect.right = x;
		healthRect.top = y;
		healthRect.bottom = y + h;
		canvas.drawRect(healthRect, healthPaint);
		
		return true;
	}
	
	public void setPos(int xCoord, int yCoord){
		x = xCoord;
		y = yCoord;
	}
	
	public void move(int dx, int dy){
		x += dx;
		y += dy;
	}
	
	public void updateCurrentHealth(int newCurrentHealth){
		health = newCurrentHealth;
	}
	
}
