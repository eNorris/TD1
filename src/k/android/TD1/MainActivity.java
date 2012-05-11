package k.android.TD1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static final String TAG = "MainActivity:";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Realize Layouts
        final Spinner levelSelectSpinner = (Spinner) findViewById(R.id.levelSelectSpinner_id);
        final ImageView levelImg = (ImageView) findViewById(R.id.levelSelectImg_id);
        final Button startButton = (Button) findViewById(R.id.startButton_id);
        
        // Enable the spinner drop down
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.levels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        levelSelectSpinner.setAdapter(adapter);
        
        // Implement the spinner listener
        levelSelectSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				Toast.makeText(parent.getContext(), "Level: " + 
						parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();
				Log.i(TAG + "Selected Item", "pos = " + pos);
				switch(pos){
				case 0: // Jungle
					levelImg.setImageResource(R.drawable.jungle);
					break;
				case 1: // Swamp
					levelImg.setImageResource(R.drawable.swamp);
					break;
				case 2: // Desert
					levelImg.setImageResource(R.drawable.desert);
					break;
				case 3: // Lake
					levelImg.setImageResource(R.drawable.lake);
					break;
				case 4: // Ocean
					levelImg.setImageResource(R.drawable.ocean);
					break;
				case 5: // Forest
					levelImg.setImageResource(R.drawable.forest);
					break;
				case 6: // Tundra
					levelImg.setImageResource(R.drawable.tundra);
					break;
				case 7: // Canyon
					levelImg.setImageResource(R.drawable.canyon);
					break;
				case 8: // Valley
					levelImg.setImageResource(R.drawable.valley);
					break;
				case 9: // Mountain
					levelImg.setImageResource(R.drawable.mountain);
					break;
				case 10: // Plateau
					levelImg.setImageResource(R.drawable.plateau);
					break;
				case 11: // Moon
					levelImg.setImageResource(R.drawable.moon);
					break;
				default:
					levelImg.setImageResource(R.drawable.choose);
				};
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// DO NOTHING
			}
        });
        
        startButton.setOnClickListener(new OnClickActivitySwapper(this, CoreActivity.class));
    }
}


class OnClickActivitySwapper implements OnClickListener{
	
	private Activity m_caller;
	private Class<?> m_next;
	
	OnClickActivitySwapper(Activity caller, Class<?> nextActivityClass){
		m_caller = caller;
		m_next = nextActivityClass;
	}

	@Override
	public void onClick(View v) {
		Intent myIntent1 = new Intent(m_caller, m_next);
	    m_caller.startActivity(myIntent1);
	}
	
}












