package k.android.TD1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Spinner levelSelectSpinner = (Spinner) findViewById(R.id.levelSelectSpinner);
        
        // Enable the spinner drop down
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.levels, R.layout.main);
        adapter.setDropDownViewResource(R.id.levelSelectSpinner);
        levelSelectSpinner.setAdapter(adapter);
//        levelSelectSpinner.setOnItemSelectedListener(new LevelSelectSpinnerListener());
        
        // Implement the spinner listener
        levelSelectSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				Toast.makeText(parent.getContext(), "Level: " + 
						parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// DO NOTHING
			}
        });
    }
/*    
    public class LevelSelectSpinnerListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			Toast.makeText(parent.getContext(), "Level: " + 
					parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// DO NOTHING
		}
    	
    }
*/
}


