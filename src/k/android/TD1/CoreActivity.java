package k.android.TD1;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
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
	private ArrayList<Bitmap> m_towerSourceBitmaps = new ArrayList<Bitmap>();
	private ArrayList<Bitmap> m_creepSourceBitmaps = new ArrayList<Bitmap>();
	private ArrayList<Tower> m_towers = new ArrayList<Tower>();
	private ArrayList<Creep> m_creeps = new ArrayList<Creep>();
	private ArrayList<CreepPath> m_paths = new ArrayList<CreepPath>();
	private Random m_random = new Random();
	
	private static final String TAG = "GameView";
	
	private int m_bgId = R.drawable.choose;
	
	private int[] m_towerIds = {
		R.drawable.tower1,
		R.drawable.tower2,
		R.drawable.tower3
	};
	
	private int[] m_creepIds = {
		R.drawable.creep1,	
		R.drawable.creep2,
		R.drawable.creep3
	};
	
//	private int[] m_otherIds = null;
	
	Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.forest);
	Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.creep1);
	Tower myTower = new Tower(bitmap);
	Creep myCreep = new Creep(bitmap2);

	
	public GameView(Context context) {
		super(context);
		getHolder().addCallback(this);
		m_gameThread = new GameThread(getHolder(), this);
		setFocusable(true);
		m_level = MainActivity.m_level;
		m_bgId = getBgResId();
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		loadTowerSourceBitmaps(m_towerIds);
		loadCreepSourceBitmaps(m_creepIds);
		loadBackgroundSourceBitmap(m_bgId);
		if(!m_gameThread.isRunning){
			m_gameThread.setRunning(true);
			m_gameThread.start();
		}
		initPaths(m_paths);
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {}

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
		
		// May add more creeps
		if(m_random.nextInt() % 3 == 0){
			if(m_creepSourceBitmaps.size() == 0)
				Log.d(TAG, "No creep source");
			Creep tmp = new Creep(m_creepSourceBitmaps.get(m_random.nextInt(3)));
			tmp.setOnPath(m_paths.get(m_random.nextInt(10)));  // TODO must init paths first
			m_creeps.add(tmp);
		}
		
		if(canvas != null){
			canvas.drawColor(Color.BLACK);
			if(m_background != null && m_background.bitmap != null)
				canvas.drawBitmap(m_background.bitmap, null, canvas.getClipBounds(), null);
		}
		
		for(int i = 0; i < m_towers.size(); i++)
			drawTower(canvas, m_towers.get(i));
		
		for(int i = 0; i < m_creeps.size(); i++){
			if(m_creeps.get(i).pathAdvance()){
				m_creeps.remove(i); // Handle the death of a creep here <- TODO
				if(i > 0) i--;
			}
			drawCreep(canvas, m_creeps.get(i));
		}
	}
	
	public boolean onTouchEvent(MotionEvent event){
		if(event.getAction() == MotionEvent.ACTION_DOWN)
			if(m_towerSourceBitmaps != null){
				Tower tmp = new Tower(m_towerSourceBitmaps.get(0));
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
	
	public void loadTowerSourceBitmaps(int[] towerIds){
//		if(bg != 0)
//			m_background = new GraphicObject(BitmapFactory.decodeResource(getResources(), bg));
		if(towerIds != null)
			for(int i = 0; i < towerIds.length; i++)
				m_towerSourceBitmaps.add(BitmapFactory.decodeResource(getResources(), towerIds[i]));
//		if(creeps != null)
//			for(int i = 0; i < creeps.length; i++)
//				m_towerSourceBitmaps.add(BitmapFactory.decodeResource(getResources(), creeps[i]));
//		if(others != null)
//			for(int i = 0; i < others.length; i++)
//				m_towerSourceBitmaps.add(BitmapFactory.decodeResource(getResources(), others[i]));
	}
	
	public void loadCreepSourceBitmaps(int[] creepIds){
		if(creepIds != null)
			for(int i = 0; i < creepIds.length; i++)
				m_creepSourceBitmaps.add(BitmapFactory.decodeResource(getResources(), creepIds[i]));
	}
	
	public void loadBackgroundSourceBitmap(int backgroundId){
		if(backgroundId != 0)
			m_background = new GraphicObject(BitmapFactory.decodeResource(getResources(), backgroundId));
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
	
	// TODO account for width of creeps when spawning paths
	public void initPaths(ArrayList<CreepPath> paths){
		for(int i = 0; i < 10; i++){
			CreepPath tmp = new CreepPath();
			if(this.getHeight() != 0){
				tmp.addPoint(new Point(-10, Math.abs(m_random.nextInt()) % this.getHeight()));
				tmp.addPoint(new Point(this.getWidth() + 10, Math.abs(m_random.nextInt()) % this.getHeight()));
				paths.add(tmp);
			}else{
				Log.v(TAG, "Height/Width is zero!!!");
			}
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




















