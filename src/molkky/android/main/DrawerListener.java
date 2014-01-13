package molkky.android.main;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class DrawerListener implements ListView.OnItemClickListener{

	private Context context;
	
	public DrawerListener(Context c){
		context = c;
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Log.d("DEBUG", "Message de test");
	}

}
