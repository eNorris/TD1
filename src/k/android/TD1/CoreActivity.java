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
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

// FIXME dissapear bfore reachin end

public class CoreActivity extends Activity{

	// Member Variables
	public static final String TAG = "CoreActivity";
	static protected Tower m_inputTower = null;
	protected static boolean m_floatingTower = false;
	
	public static TextView playerNameTextView;
	public static TextView playerHealthTextView;
	public static TextView playerMoneyTextView;
	
	public static Handler handler = new Handler();
		
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
		final Button newTowerButton = (Button) findViewById(R.id.newTowerButton_id);
		playerNameTextView = (TextView) findViewById(R.id.corePlayerName_id);
		playerMoneyTextView = (TextView) findViewById(R.id.corePlayerMoney_id);
		playerHealthTextView = (TextView) findViewById(R.id.corePlayerHealth_id);
		
		playerNameTextView.setText(GameView.playerName);
		playerMoneyTextView.setText(new Integer(GameView.playerMoney).toString());
		playerHealthTextView.setText(new Integer(GameView.playerHealth).toString());
		
		
		// set onClick methods for buttons
		newTowerButton.setOnTouchListener(new View.OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:
					
					// FIXME Attackmethods are being created with the wrong tower as their parent
					
					m_inputTower.setType(Tower.TYPE_1);
					
					Log.d(TAG, "@actiondown: inputtowerSize = " + m_inputTower.attackMethods.size());
					
					m_inputTower.setCenter((int) event.getX(), (int) event.getY());
					m_inputTower.drawable = true;
					m_inputTower.visible = true;
//					m_inputTower.active = false;
					m_floatingTower = true;
					
					
					
					break;
				case MotionEvent.ACTION_UP:
					if(m_floatingTower){
						Log.i(TAG, "releasing, Placing tower");
						m_inputTower.active = true;
						Log.d(TAG, "@MotionEvent.up: atksize = " + m_inputTower.attackMethods.size());
						GameView.playerMoney -= m_inputTower.cost;
						
						GameView.worldTowerList.add(m_inputTower.shadowCopy());
						
						m_inputTower.visible = false;
						Log.i(TAG, "new size = " + GameView.worldTowerList.size());
						m_floatingTower = false;
					}
					break;
				default:
					Log.d(TAG, "calling motion event default");
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
	
//	public static void setPlayerHealth(int health){
//		playerHealthTextView.setText(new Integer(health).toString());
//	}
	
	public static void setPlayerHealth(final int health){
		handler.post(new Runnable(){
			@Override
			public void run() {
				playerHealthTextView.setText(new Integer(health).toString());
			}
		});
	}
	
	public static void setPlayerMoney(final int money){
		handler.post(new Runnable(){
			@Override
			public void run() {
				playerMoneyTextView.setText(new Integer(money).toString());
			}
		});
	}
}


class GameView extends SurfaceView implements SurfaceHolder.Callback{

	// Member Variables
	private int m_level = LevelCode.UNKNOWN_LEVEL;
	private GameThread m_gameThread;
	private GraphicObject m_background;
	public static ArrayList<Tower> worldTowerList = new ArrayList<Tower>();
	public static ArrayList<Creep> worldCreepList = new ArrayList<Creep>();
	public static ArrayList<CreepPath> worldPathList;
	private Random m_random = new Random();
	
	private static final String TAG = "GameView";
	
	private int m_bgId = R.drawable.choose;
	
	public static int playerMoney = 0;
	public static int playerHealth = 1;
	public static String playerName = "Unknown Player";

	// Constructors
	public GameView(Context context) {
		super(context);
		initGameView();
	}
	
	public GameView(Context context,  AttributeSet attrs) {
		super(context, attrs);
		initGameView();
	}
	
	public GameView(Context context,  AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initGameView();
	}
	
	private void initGameView(){
		getHolder().addCallback(this);
		m_gameThread = new GameThread(getHolder(), this);
		setFocusable(true);
		m_level = MainActivity.m_level;
		m_bgId = getBgResId();
		AttackMethod.creepPool = worldCreepList;
		
		playerMoney = 2500;
		playerHealth = 2500;
		playerName = "Edward";
	}

	// Surface Functions
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		loadBackgroundSourceBitmap(m_bgId);
		
		if(m_gameThread.getState() == Thread.State.TERMINATED){
			m_gameThread = new GameThread(getHolder(), this);
			m_gameThread.setRunning(true);
			m_gameThread.start();
		}else{
			m_gameThread.setRunning(true);
			m_gameThread.start();
		}
		
		initPaths();
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
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
		
		if(!updateGameState()){
			Log.e(TAG, "Failed to update game state!");
		}
		
		// Redraw the world
		if(canvas != null){
			canvas.drawColor(Color.BLACK);
			if(m_background != null && m_background.bitmap != null)
				canvas.drawBitmap(m_background.bitmap, null, canvas.getClipBounds(), null);
		}
		
		// draw all towers
		for(int i = 0; i < worldTowerList.size(); i++)
			worldTowerList.get(i).draw(canvas);
		
		// draw all creeps
		for(int i = 0; i < worldCreepList.size(); i++)
			worldCreepList.get(i).draw(canvas);
		
		// Draw attack methods
		for(int i = 0; i < worldTowerList.size(); i++)
			for(int j = 0; j < worldTowerList.get(i).attackMethods.size(); j++)
				worldTowerList.get(i).attackMethods.get(j).draw(canvas);
		
		
		// Draw the tower being placed
		if(CoreActivity.m_inputTower != null)
			CoreActivity.m_inputTower.draw(canvas);
	}
	
	public boolean updateGameState(){
		
		if(worldPathList == null){
			Log.e(TAG, "world path is null");
			return false;
		}
		
		if(worldPathList.size() == 0){
			Log.wtf(TAG,  "Just blow up!");
			throw new RuntimeException();
		}
		
		boolean toReturn = true;
		
		// Add some more creeps
		if(m_random.nextInt() % 10 == 0){
			int tmpRand = m_random.nextInt(3);
			Creep tmp = new Creep(tmpRand);
			tmp.setOnPath(worldPathList.get(m_random.nextInt(10)));
			worldCreepList.add(tmp);
		}
		
		// Update creep positions and kill some off
		for(int i = 0; i < worldCreepList.size(); i++){
			if(worldCreepList.get(i).advanceAlongPath()){
				worldCreepList.get(i).onDeath();
				if(i > 0) i--;
			}else{
				worldCreepList.get(i).healthBar.updateCurrentHealth(worldCreepList.get(i).m_health);
			}
		}
		
		// Update Tower Attack Methods
		for(int i = 0; i < worldTowerList.size(); i++){
			for(int j = 0; j < worldTowerList.get(i).attackMethods.size(); j++){
				worldTowerList.get(i).attackMethods.get(j).findTargets();
				worldTowerList.get(i).attackMethods.get(j).attack();
			}
		}
		
		// Update player info
		CoreActivity.setPlayerHealth(GameView.playerHealth);
		CoreActivity.setPlayerMoney(GameView.playerMoney);
		
		// Determine if the game is over
		if(playerHealth <= 0){
			loseGame();
		}
		
		return toReturn;
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
	
	public void initPaths(){
		Log.d(TAG, "running path gen");
		worldPathList = new ArrayList<CreepPath>();
		for(int i = 0; i < 10; i++){
			CreepPath tmp = new CreepPath();
			if(this.getHeight() != 0){
				tmp.addPoint(new Point(-20, Math.abs(m_random.nextInt()) % this.getHeight()));
				tmp.addPoint(new Point(this.getWidth() + 20, Math.abs(m_random.nextInt()) % this.getHeight()));
				worldPathList.add(tmp);
			}else{
				Log.v(TAG, "Height/Width is zero!!!");
			}
		}
	}
	
	public void loseGame(){
		// TODO
	}
	
	public void winGame(){
		// TODO
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




















