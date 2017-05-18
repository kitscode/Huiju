package kevinz.huiju.utils.rearrange_tabs;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kevinz.huiju.R;
import kevinz.huiju.db.DBHelper;
import kevinz.huiju.utils.Tabs;

public class DraggableGridViewSampleActivity extends Activity {
	static Random random = new Random();
	DraggableGridView dgv;
	DraggableGridView gridView;

	private Context context = DraggableGridViewSampleActivity.this;

	public static List<String> tabs=new ArrayList<>();
	SQLiteDatabase db;

	private void initTabs(){
		tabs.clear();
		db=new DBHelper(this,"huiju",null,1).getWritableDatabase();
		Cursor c=db.rawQuery("select * from tabs",null);
		while(c.moveToNext()){
			tabs.add(c.getString(1));
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.draggable);
		initTabs();
		dgv = ((DraggableGridView) findViewById(R.id.gridview));

		gridView = (DraggableGridView) findViewById(R.id.gridview1);



		for (String tab:tabs) {
			ImageView view = new ImageView(DraggableGridViewSampleActivity.this);
			view.setImageBitmap(getThumb(Tabs.tabMap.get(tab)));
			dgv.addView(view);

		}

//		for (int i = 0; i < 8; i++) {
//			ImageView view = new ImageView(DraggableGridViewSampleActivity.this);
//			String a = i + "1";
//			view.setImageBitmap(getThumb(a));
//			gridView.addView(view);
//		}

		setListeners();
		setResult(1);
	}

	private void setListeners() {
		dgv.setOnRearrangeListener(new OnRearrangeListener() {
			public void onRearrange(int oldIndex, int newIndex) {
				 String word = tabs.remove(oldIndex);
				 tabs.add(newIndex, word);
			}
		});
		dgv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				dgv.removeViewAt(arg2);
				// poem.remove(arg2);
				gridView.addView(arg1);
				Animation animation=AnimationUtils.loadAnimation(context, R.anim.scale);
				arg1.setAnimation(animation);

			}
		});
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				gridView.removeViewAt(position);

				dgv.addView(view);
				Animation animation=AnimationUtils.loadAnimation(context, R.anim.scale);
				view.setAnimation(animation);
			}

		});

	}

	private Bitmap getThumb(String s) {
		Bitmap bmp = Bitmap.createBitmap(100, 50, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bmp);
		Paint paint = new Paint();

		paint.setColor(Color.rgb(random.nextInt(128), random.nextInt(128),
				random.nextInt(128)));
		paint.setTextSize(20);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		canvas.drawRect(new Rect(0, 0, 100, 50), paint);
		paint.setColor(Color.WHITE);
		paint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(s, 50, 30, paint);

		return bmp;
	}


	@Override
	protected void onPause() {
		super.onPause();
		db.execSQL("delete from tabs");
		for(int i=0;i<tabs.size();i++){
			db.execSQL("insert into tabs values("+i+",'"+tabs.get(i)+"','"+Tabs.tabMap.get(tabs.get(i))+"')");
		}
	}
}