package com.stalker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.stalker.DBHelper.Todo;

public class NotificationActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_notification);

		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Intent intent = getIntent();
		if (intent != null) {
			final ArrayList<Todo> todos = (ArrayList<Todo>) intent.getExtras().getSerializable("todos");

			for(int i=0; i< todos.size(); i++){
				Map<String, String> datum = new HashMap<String, String>(2);
			    datum.put("First Line", todos.get(i).getNote());
//			    datum.put("Second Line","@ " + todos.get(i).getPrefLoc());
			    data.add(datum);
			}


			SimpleAdapter adapter = new SimpleAdapter(this, data,
	                android.R.layout.simple_list_item_2, 
//	                new String[] {"First Line", "Second Line" },
	                new String[] {"First Line"},
	                new int[] {android.R.id.text1});

			ListView notifications = (ListView) findViewById(R.id.notificationsList);
			notifications.setAdapter(adapter);
			
			notifications.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub
					Intent itemMapTransition = new Intent(getApplicationContext(), MapAllTODOs.class);
					itemMapTransition.putExtra("identifier", todos.get(position).getId());
					startActivity(itemMapTransition);
				}
				
			});
		}

	}
}