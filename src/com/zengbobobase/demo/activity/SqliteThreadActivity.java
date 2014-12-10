package com.zengbobobase.demo.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import com.zengbobobase.demo.R;

public class SqliteThreadActivity extends Activity implements OnClickListener {
	private static final String TAG = "SqliteThreadActivity";
	private DBManager mgr;
	private ListView listView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 初始化DBManager
		// mgr = new DBManager(this);
		// DBManager mgr1 = new DBManager(this);
		// DBManager mgr2 = new DBManager(this);
		setContentView(R.layout.activity_sqlite_thread_layout);
		findViewById(R.id.btn_add).setOnClickListener(this);
		findViewById(R.id.btn_update).setOnClickListener(this);
		findViewById(R.id.btn_delete).setOnClickListener(this);
		findViewById(R.id.btn_query).setOnClickListener(this);
		findViewById(R.id.btn_queryTheCursor).setOnClickListener(this);
		listView = (ListView) findViewById(R.id.listView);
		new MyThread(1, 1000).start();
		new MyThread(2, 1000).start();
		new MyThread(3, 1000).start();
		new MyThread(4, 1000).start();
	}

	private static void log(String str) {
		Log.i(TAG, TAG + ":" + str);
	}

	public class MyThread extends Thread {
		private int threadId = -1;
		private int sleepTime = 1000;

		public MyThread(int threadId, int sleepTime) {
			super();
			this.threadId = threadId;
			this.sleepTime = sleepTime;
		}

		@Override
		public void run() {
			super.run();
			try {
				log("MyThread run threadId:" + threadId + ",sleepTime:"
						+ sleepTime);
				DBManager mgr = DBManager.getInstance(
						SqliteThreadActivity.this, threadId);
				Thread.sleep(sleepTime);
				// query(threadId,null,mgr);
				if(threadId==1||threadId==2){
					add(threadId, null, mgr);
				}else{
//					query(threadId, null, mgr);
					update(threadId, null, mgr);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.btn_add:
			add(v);
			break;
		case R.id.btn_update:
			update(v);
			break;
		case R.id.btn_delete:
			delete(v);
			break;
		case R.id.btn_query:
			query(v);
			break;
		case R.id.btn_queryTheCursor:
			queryTheCursor(v);
			break;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 应用的最后一个Activity关闭时应释放DB
		mgr.closeDB();
	}

	public void add(View view) {
		add(-1, view, mgr);
	}

	public void add(int threadId, View view, DBManager mgr) {
		ArrayList<Person> persons = new ArrayList<Person>();
		if(threadId==1){
			threadId =11;
		}else{
			threadId =22;
		}
		Person person1 = new Person(threadId+"Ella", 22, "lively girl");
		// Person person2 = new Person("Jenny", 22, "beautiful girl");
		// Person person3 = new Person("Jessica", 23, "sexy girl");
		// Person person4 = new Person("Kelly", 23, "hot baby");
		// Person person5 = new Person("Jane", 25, "a pretty woman");

		persons.add(person1);
		// persons.add(person2);
		// persons.add(person3);
		// persons.add(person4);
		// persons.add(person5);

		log("add threadId:" + threadId);
		mgr.add(persons);
		log("add threadId:" + threadId);
		mgr.add(persons);
		log("add threadId:" + threadId);
		mgr.add(persons);
	}

	public void update(View view) {
		update(-1, view, mgr);
	}

	public void update(int threadId, View view, DBManager mgr) {
		Person person = new Person();
		person.name = "Jane";
		person.age = 30;
		log("update threadId:" + threadId);
		mgr.updateAge(person);
		
		Person person_1 = new Person();
		person_1.name = "Jessica";
		person_1.age = 300;
		log("update threadId:" + threadId);
		mgr.updateAge(person_1);
	}

	public void delete(View view) {
		delete(-1, view, mgr);
	}

	public void delete(int threadId, View view, DBManager mgr) {
		Person person = new Person();
		person.age = 30;
		mgr.deleteOldPerson(person);
	}

	public void query(View view) {
		query(-1, view, mgr);
	}

	public void query(int threadId, View view, DBManager mgr) {
		List<Person> persons = mgr.query();
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (Person person : persons) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("name", person.name);
			map.put("info", person._id + " age, " + person.age + " years old, "
					+ person.info);
			// list.add(map);
			log("query threadId:" + threadId + ",person._id:" + person._id);
		}
		// SimpleAdapter adapter = new SimpleAdapter(this, list,
		// android.R.layout.simple_list_item_2, new String[] { "name",
		// "info" }, new int[] { android.R.id.text1,
		// android.R.id.text2 });
		// listView.setAdapter(adapter);
	}

	public void queryTheCursor(View view) {
		queryTheCursor(-1, view, mgr);
	}

	public void queryTheCursor(int threadId, View view, DBManager mgr) {
		Cursor c = mgr.queryTheCursor();
		startManagingCursor(c); // 托付给activity根据自己的生命周期去管理Cursor的生命周期
		CursorWrapper cursorWrapper = new CursorWrapper(c) {
			@Override
			public String getString(int columnIndex) {
				// 将简介前加上年龄
				if (getColumnName(columnIndex).equals("info")) {
					int age = getInt(getColumnIndex("age"));
					return age + " years old, " + super.getString(columnIndex);
				}
				return super.getString(columnIndex);
			}
		};
		// 确保查询结果中有"_id"列
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_2, cursorWrapper,
				new String[] { "name", "info" }, new int[] {
						android.R.id.text1, android.R.id.text2 });
		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(adapter);
	}

	/**************** DBHelper **********************/
	// TODO DBHelper ----1
	public static class DBHelper extends SQLiteOpenHelper {

		private static final String DATABASE_NAME = "test.db";
		private static final int DATABASE_VERSION = 1;
		private static DBHelper instance;

		public static synchronized DBHelper getInstance(Context context) {
			if (instance == null) {
				synchronized (DBHelper.class) {
					if (instance == null) {
						instance = new DBHelper(context);
					}
				}
			}

			return instance;
		}

		private DBHelper(Context context) {
			// CursorFactory设置为null,使用默认值
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		// 数据库第一次被创建时onCreate会被调用
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE IF NOT EXISTS person"
					+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, age INTEGER, info TEXT)");
		}

		// 如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("ALTER TABLE person ADD COLUMN other STRING");
		}

	}

	/**************** DBManager **********************/
	// TODO DBManager ----2
	public static class DBManager {
		private DBHelper helper;
		private SQLiteDatabase db;
		private int threadId;
		private static DBManager instance;

		public static synchronized DBManager getInstance(Context context,
				int threadId) {
			if (instance == null) {
				synchronized (DBManager.class) {
					if (instance == null) {
						instance = new DBManager(context, threadId);
					}
				}

			}

			return instance;
		}

		private DBManager(Context context) {
			this(context, -1);
		}

		private DBManager(Context context, int threadId) {
			helper = DBHelper.getInstance(context);
			// 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
			// mFactory);
			// 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
			db = helper.getWritableDatabase();
			this.threadId = threadId;
			log("DBManager threadId:" + threadId);
		}

		/**
		 * add persons
		 * 
		 * @param persons
		 */
		public void add(List<Person> persons) {
			db= helper.getWritableDatabase();
//			db.beginTransaction(); // 开始事务
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				for (Person person : persons) {
					log("DBManager add threadId:" + threadId + ",person._id:"
							+ person._id+",name:"+person.name);
					db.execSQL(
							"INSERT INTO person VALUES(null, ?, ?, ?)",
							new Object[] { person.name, person.age, person.info });
				}
//				db.setTransactionSuccessful(); // 设置事务成功完成
			} finally {
//				db.endTransaction(); // 结束事务
//				db.close();
//				db=null;
			}
		}

		/**
		 * update person's age
		 * 
		 * @param person
		 */
		public void updateAge(Person person) {

			log("MyThread updateAge threadId:" + threadId + ",person.age:"
					+ person.age);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			db= helper.getWritableDatabase();
//			db.beginTransaction();
			ContentValues cv = new ContentValues();
			cv.put("age", person.age);
			db.update("person", cv, "name = ?", new String[] { person.name });
//			db.endTransaction();
		}

		/**
		 * delete old person
		 * 
		 * @param person
		 */
		public void deleteOldPerson(Person person) {
			db.delete("person", "age >= ?",
					new String[] { String.valueOf(person.age) });
		}

		/**
		 * query all persons, return list
		 * 
		 * @return List<Person>
		 */
		public List<Person> query() {
			ArrayList<Person> persons = new ArrayList<Person>();
			Cursor c = queryTheCursor();
			while (c.moveToNext()) {
				log("DBManager query threadId:" + threadId);
				Person person = new Person();
				person._id = c.getInt(c.getColumnIndex("_id"));
				person.name = c.getString(c.getColumnIndex("name"));
				person.age = c.getInt(c.getColumnIndex("age"));
				person.info = c.getString(c.getColumnIndex("info"));
				persons.add(person);
			}
			c.close();
			return persons;
		}

		/**
		 * query all persons, return cursor
		 * 
		 * @return Cursor
		 */
		public Cursor queryTheCursor() {
			db= helper.getWritableDatabase();
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Cursor c = db.rawQuery("SELECT * FROM person", null);
			return c;
		}

		/**
		 * close database
		 */
		public void closeDB() {
			db.close();
		}
	}

	/**************** Person **********************/
	// TODO Person ----2
	public static class Person {
		public int _id;
		public String name;
		public int age;
		public String info;

		public Person() {
		}

		public Person(String name, int age, String info) {
			this.name = name;
			this.age = age;
			this.info = info;
		}
	}

}
