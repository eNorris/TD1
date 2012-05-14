package k.android.TD1;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

// FIXME CoreActivity.m_gameView is not the same gameview rendering....

public class CoreActivity extends Activity{

	// Member Variables
	public static final String TAG = "CoreActivity";
//	public static GameView m_gameView;
	static protected Tower m_inputTower = null;
	protected static boolean m_floatingTower = false;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		loadTowerBitmapSources();
		loadCreepBitmapSources();
		m_inputTower = new Tower(Tower.TYPE_1);
		m_inputTower.drawable = false;
		m_inputTower.visible = false;
		
		// Launch the XML content before realization
		setContentView(R.layout.core);
		
		// Realize elements
		final GameView gameView = (GameView) findViewById(R.id.xml_gameView_id);
		final Button newTowerButton = (Button) findViewById(R.id.newTowerButton_id);
		
		// set onClick methods for buttons
		newTowerButton.setOnTouchListener(new View.OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:
					m_inputTower = new Tower(Tower.TYPE_1);
					m_inputTower.setCenter((int) event.getX(), (int) event.getY());
					m_inputTower.drawable = true;
					m_inputTower.visible = true;
					m_floatingTower = true;
					break;
				case MotionEvent.ACTION_UP:
					if(m_floatingTower){
						Log.i(TAG, "releasing, Placing tower");
						gameView.m_towers.add(m_inputTower);
						Log.i(TAG, "new size = " + gameView.m_towers.size());
						m_floatingTower = false;
					}
					break;
				default:
					m_inputTower.setCenter((int) event.getX(), (int) event.getY());
				};
				return true;
			}
		});
	}
	
	public void loadTowerBitmapSources(){
		Tower.towerBitmapSources = new ArrayList<Bitmap>();
		if(Tower.towerBitmapIds != null)
			for(int i = 0; i < Tower.towerBitmapIds.length; i++)
				Tower.towerBitmapSources.add(BitmapFactory.decodeResource(getResources(), Tower.towerBitmapIds[i]));
	}
	
	public void loadCreepBitmapSources(){
		Creep.creepBitmapSources = new ArrayList<Bitmap>();
		if(Creep.creepBitmapIds != null)
			for(int i = 0; i < Creep.creepBitmapIds.length; i++)
				Creep.creepBitmapSources.add(BitmapFactory.decodeResource(getResources(), Creep.creepBitmapIds[i]));
	}
}



class GameView extends SurfaceView implements SurfaceHolder.Callback{

	// Member Variables
	private int m_level = LevelCode.UNKNOWN_LEVEL;
	private GameThread m_gameThread;
	private GraphicObject m_background;
	// TODO either structure or privatize everything
	protected ArrayList<Tower> m_towers = new ArrayList<Tower>();
	private ArrayList<Creep> m_creeps = new ArrayList<Creep>();
	private ArrayList<CreepPath> m_paths = new ArrayList<CreepPath>();
	private Random m_random = new Random();
	
	private static final String TAG = "GameView";
	
	private int m_bgId = R.drawable.choose;

	// Constructors
	public GameView(Context context) {
		super(context);
		getHolder().addCallback(this);
		m_gameThread = new GameThread(getHolder(), this);
		setFocusable(true);
		m_level = MainActivity.m_level;
		m_bgId = getBgResId();
	}
	
	public GameView(Context context,  AttributeSet attrs) {
		super(context, attrs);
		getHolder().addCallback(this);
		m_gameThread = new GameThread(getHolder(), this);
		setFocusable(true);
		m_level = MainActivity.m_level;
		m_bgId = getBgResId();
	}
	
	public GameView(Context context,  AttributeSet attrs, int defStyle) {
		super(context, attrs);
		getHolder().addCallback(this);
		m_gameThread = new GameThread(getHolder(), this);
		setFocusable(true);
		m_level = MainActivity.m_level;
		m_bgId = getBgResId();
	}

	// Surface Functions
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		loadBackgroundSourceBitmap(m_bgId);
		
		if(m_gameThread.getState() == Thread.State.TERMINATED){
			m_gameThread = new GameThread(getHolder(), this);
			m_gameThread.setRunning(true);
			m_gameThread.start();
		}else{
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
	
	// Member Functions
	public void onDraw(Canvas canvas){
		
		// May add more creeps
		if(m_random.nextInt() % 10 == 0){
			int tmpRand = m_random.nextInt(3);
			Creep tmp = new Creep(tmpRand);
			tmp.setOnPath(m_paths.get(m_random.nextInt(10)));
			m_creeps.add(tmp);
		}
		
		if(canvas != null){
			canvas.drawColor(Color.BLACK);
			if(m_background != null && m_background.bitmap != null)
				canvas.drawBitmap(m_background.bitmap, null, canvas.getClipBounds(), null);
		}
		
		for(int i = 0; i < m_towers.size(); i++){
			m_towers.get(i).draw(canvas);
		}
		
		for(int i = 0; i < m_creeps.size(); i++){
			if(m_creeps.get(i).advanceAlongPath()){
				m_creeps.get(i).onDeath();
				m_creeps.remove(i);
				if(i > 0) i--;
			}else{
				m_creeps.get(i).draw(canvas);
			}
		}
		
		CoreActivity.m_inputTower.draw(canvas);
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
	
	public void initPaths(ArrayList<CreepPath> paths){
		for(int i = 0; i < 10; i++){
			CreepPath tmp = new CreepPath();
			if(this.getHeight() != 0){
				tmp.addPoint(new Point(-20, Math.abs(m_random.nextInt()) % this.getHeight()));
				tmp.addPoint(new Point(this.getWidth() + 20, Math.abs(m_random.nextInt()) % this.getHeight()));
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




















