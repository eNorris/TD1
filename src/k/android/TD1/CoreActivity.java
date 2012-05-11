package k.android.TD1;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CoreActivity extends Activity{

	// Member Variables
	GameView m_gameView;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_gameView = new GameView(this);
		setContentView(m_gameView);
	}
}



class GameView extends SurfaceView implements SurfaceHolder.Callback{

	// Member Variables
	private int m_level = LevelCode.UNKNOWN_LEVEL;
	private GameThread m_gameThread;
	private GraphicObject m_background;
	private ArrayList<Bitmap> m_sourceBitmaps = new ArrayList<Bitmap>();
	private ArrayList<Tower> m_towers = new ArrayList<Tower>();
	
	private int m_bgId = R.drawable.choose;
	
	private int[] m_towerIds = {
		R.drawable.tower1,
		R.drawable.tower2,
		R.drawable.tower3
	};
	
	private int[] m_creepIds = null;
	
	private int[] m_otherIds = null;
	
	Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.forest);
	Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.creep1);
	Tower myTower = new Tower(bitmap);
	Creep myCreep = new Creep(bitmap2);
	CreepPath myPath = new CreepPath();

	
	public GameView(Context context) {
		super(context);
		getHolder().addCallback(this);
		m_gameThread = new GameThread(getHolder(), this);
		setFocusable(true);
		m_level = MainActivity.m_level;
		m_bgId = getBgResId();
		
		myPath.addPoint(new Point(2,3));
		myPath.addPoint(new Point(50,50));
		myPath.addPoint(new Point(200,400));
		myCreep.setOnPath(myPath);
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		loadSourceBitmaps(m_bgId, m_towerIds, m_creepIds, m_otherIds);
		if(!m_gameThread.isRunning){
			m_gameThread.setRunning(true);
			m_gameThread.start();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		boolean retry = true;
		m_gameThread.setRunning(false);
		
		while(retry){
			try{
				m_gameThread.join();
				retry = false;
			}catch(InterruptedException e){
				// Try again...
			}
		}
	}
	
	public void onDraw(Canvas canvas){
		
		if(canvas != null){
			canvas.drawColor(Color.BLACK);
			if(m_background != null && m_background.bitmap != null)
				canvas.drawBitmap(m_background.bitmap, null, canvas.getClipBounds(), null);
		}
		for(int i = 0; i < m_towers.size(); i++)
			drawTower(canvas, m_towers.get(i));
		// TODO replace with the array later
		myCreep.pathAdvance();
		drawCreep(canvas, myCreep);
	}
	
	public boolean onTouchEvent(MotionEvent event){
		if(event.getAction() == MotionEvent.ACTION_DOWN)
			if(m_sourceBitmaps != null){
				Tower tmp = new Tower(m_sourceBitmaps.get(0));
				tmp.setCenter((int)event.getX(), (int)event.getY());
				m_towers.add(tmp);
			}
		return true;
	}
	
	public void drawTower(Canvas canvas, Tower tower){
		if(canvas != null && tower != null && tower.bitmap != null)
			canvas.drawBitmap(tower.bitmap, tower.x, tower.y, null);
	}
	
	public void drawCreep(Canvas canvas, Creep creep){
		if(canvas != null && creep != null && creep.bitmap != null)
			canvas.drawBitmap(creep.bitmap, creep.x, creep.y, null);
	}
	
	public void loadSourceBitmaps(int bg, int[] towers, int[] creeps, int[] others){
		if(bg != 0)
			m_background = new GraphicObject(BitmapFactory.decodeResource(getResources(), bg));
		if(towers != null)
			for(int i = 0; i < towers.length; i++)
				m_sourceBitmaps.add(BitmapFactory.decodeResource(getResources(), towers[i]));
		if(creeps != null)
			for(int i = 0; i < creeps.length; i++)
				m_sourceBitmaps.add(BitmapFactory.decodeResource(getResources(), creeps[i]));
		if(others != null)
			for(int i = 0; i < others.length; i++)
				m_sourceBitmaps.add(BitmapFactory.decodeResource(getResources(), others[i]));
	}
	
	public int getBgResId(){
		switch(m_level){
		case LevelCode.JUNGLE_LEVEL:
			return R.drawable.jungle;
		case LevelCode.SWAMP_LEVEL:
			return R.drawable.swamp;
		case LevelCode.DESERT_LEVEL:
			return R.drawable.desert;
		case LevelCode.LAKE_LEVEL:
			return R.drawable.lake;
		case LevelCode.OCEAN_LEVEL:
			return R.drawable.ocean;
		case LevelCode.FOREST_LEVEL:
			return R.drawable.forest;
		case LevelCode.TUNDRA_LEVEL:
			return R.drawable.tundra;
		case LevelCode.CANYON_LEVEL:
			return R.drawable.canyon;
		case LevelCode.VALLEY_LEVEL:
			return R.drawable.valley;
		case LevelCode.MOUNTAIN_LEVEL:
			return R.drawable.mountain;
		case LevelCode.PLATEAU_LEVEL:
			return R.drawable.plateau;
		case LevelCode.MOON_LEVEL:
			return R.drawable.moon;
		default:
			return R.drawable.choose;
		}
	}
}




class GameThread extends Thread{
	SurfaceHolder m_surfaceHolder;
	GameView m_gameView;
	boolean isRunning = false;
	
	public GameThread(SurfaceHolder holder, GameView game){
		m_surfaceHolder = holder;
		m_gameView = game;
	}
	
	public void setRunning(boolean r){
		isRunning = r;
	}
	
	public SurfaceHolder getSurfaceHolder(){
		return m_surfaceHolder;
	}
	
	public void run(){
		Canvas c;
		while(isRunning){
			c = null;
			try{
				c = m_surfaceHolder.lockCanvas();
				synchronized (m_surfaceHolder){
					m_gameView.onDraw(c);
				}
			}finally{
				if(c != null)
					m_surfaceHolder.unlockCanvasAndPost(c);
			}
		}
	}
}




















