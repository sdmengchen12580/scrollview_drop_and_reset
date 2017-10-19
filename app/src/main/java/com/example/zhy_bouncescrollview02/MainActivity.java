package com.example.zhy_bouncescrollview02;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.zhy_bouncescrollview02.BounceScrollView.Callback;

public class MainActivity extends Activity
{

	private ListView mListView;
	private BounceScrollView mScrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mScrollView = (BounceScrollView) findViewById(R.id.id_scrollView);
		mScrollView.setCallBack(new Callback()
		{

			@Override
			public void callback()
			{
				Toast.makeText(MainActivity.this, "you can do something!", 0)
						.show();
				Intent intent = new Intent(MainActivity.this,
						SecondActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			}
		});

		mListView = (ListView) findViewById(R.id.id_listView);
		mListView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, new ArrayList<String>(
						Arrays.asList("Hello", "World", "Welcome", "Java",
								"Android", "Lucene", "C++", "C#", "HTML",
								"Welcome", "Java", "Android", "Lucene", "C++",
								"C#", "HTML"))));
	}

}
