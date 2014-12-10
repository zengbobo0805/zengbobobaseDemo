package com.zengbobobase.demo.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.zengbobobase.demo.R;

public class SQliteDBActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

	
	private View nni , nsi , simr , mr ,sinmr;
	
	void init(){
		//多线程写， 每个线程使用各自的SQLiteOpenHelper。可能会抛出异常，导致插入错误
		nni = findViewById(R.id.nni);
		nni.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				List<InsertThread> list = new ArrayList<InsertThread>();
				for(int i = 0 ; i < 10 ; ++i){
					list.add(new InsertThread(SQliteDBActivity.this  , new DbHelper(SQliteDBActivity.this) , 50));
				}
				startThreads(list);
				waitAndClose(list);
			}
		});
		
		//多线程写， 使用同一个SQLiteOpenHelper。不会出问题
		nsi = findViewById(R.id.nsi);
		nsi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DbHelper db = new DbHelper(SQliteDBActivity.this);
				List<InsertThread> list = new ArrayList<InsertThread>();
				for(int i = 0 ; i < 4 ; ++i){
					list.add(new InsertThread(SQliteDBActivity.this  , db , 50));
				}
				startThreads(list);
				waitAndClose(list);
			}
		});
		
		//1个线程写，多个线程读，每个读线程使用各自的SQLiteOpenHelper
		simr = findViewById(R.id.simr);
		simr.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				List<InsertThread> list = new ArrayList<InsertThread>();
				list.add(new InsertThread(SQliteDBActivity.this  , new DbHelper(SQliteDBActivity.this) , 1000));
				
				List<ReadThread> rlist = new ArrayList<ReadThread>();
				for(int i = 0 ; i < 10 ; ++i){
					rlist.add(new ReadThread(SQliteDBActivity.this  , new DbHelper(SQliteDBActivity.this) , 50));	
				}

				startThreads(list);
				startThreads(rlist);
				waitAndClose(rlist);
				waitAndClose(list);
			}
		});
		
		
		//多个线程读，每个读线程使用各自的SQLiteOpenHelper
		mr = findViewById(R.id.mr);
		mr.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				List<ReadThread> rlist = new ArrayList<ReadThread>();
				for(int i = 0 ; i < 10 ; ++i){
					rlist.add(new ReadThread(SQliteDBActivity.this  , new DbHelper(SQliteDBActivity.this) , 200));	
				}
				startThreads(rlist);
				waitAndClose(rlist);
			}
		});
		
		//单线程写，多线程读，使用一个SQLiteOpenHelper，读线程使用只读sqlitedatebase
		sinmr = findViewById(R.id.sinmr);
		sinmr.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DbHelper db = new DbHelper(SQliteDBActivity.this);
				List<InsertThread> list = new ArrayList<InsertThread>();
				list.add(new InsertThread(SQliteDBActivity.this  , db , 1000));
				
				List<ReadThread> rlist = new ArrayList<ReadThread>();
				for(int i = 0 ; i < 10 ; ++i){
					rlist.add(new ReadThread(SQliteDBActivity.this  ,db, 50));	
				}

				startThreads(list);
				startThreads(rlist);
				for(Thread t : rlist){
					try {
						t.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				waitAndClose(list);
			}
		});
		
	}
	
	
	
	static class InsertThread extends Thread{
		static AtomicInteger count = new AtomicInteger(0);
		public DbHelper db;
		private int insertCount;
		private static String toinsert=new String("考了多少分就哭了是否四季咖啡的发生的发生的飞水jkjhkjljkljkl;kl;kl;cxkl;v电费水电费水电费收到发生的飞水电费收到发生的飞水电费水电费的飞水电费收到发生的飞收到发生的飞kl;kl;sdffklsdfklsdkl;fsdkl;fkl;sdfkl;sdkl;fkl;sdfkl;sdfsdakl;fkl;sdkl;fasdkl;fkl;sdfkl;sdkl;第三方圣达菲圣达菲收到飞收到飞收到发生的飞阿斯蒂芬收到发生的发生的发送飞");
		InsertThread(Context context , DbHelper db , int insertCount){
			setName("InsertThread#"+count.getAndIncrement());
			this.insertCount = insertCount;
			this.db = db;
		}
		
		
		@Override
		public void run() {
			super.run();
			try{
				for(int i = 0 ; i < insertCount ; ++i){
					db.insert(toinsert);	
				}
			}finally{
			}
		}
	}
	
	
	static class ReadThread extends Thread{
		static AtomicInteger count = new AtomicInteger(0);
		public DbHelper db;
		private int readCount;
		
		ReadThread(Context context , DbHelper db , int readCount){
			setName("ReadThread#"+count.getAndIncrement());
			this.readCount = readCount;
			this.db = db;
		}
		
		@Override
		public void run() {
			super.run();
			for(int i = 0 ; i < readCount ; ++i){
				db.getCount();	
			}
			
		}
	}
	
	static class OnlyReadThread extends Thread{
		static AtomicInteger count = new AtomicInteger(0);
		public DbHelper db;
		private int readCount;
		private SQLiteDatabase sqldb;
		
		OnlyReadThread(Context context , DbHelper db , int readCount){
			setName("onlyReadThread#"+count.getAndIncrement());
			this.readCount = readCount;
			this.db = db;
			sqldb = db.getOnlyReadDatabase();
		}
		
		@Override
		public void run() {
			super.run();
			for(int i = 0 ; i < readCount ; ++i){
				db.getCount(sqldb);	
			}
			
		}
	}
	
	void startThreads(Collection<? extends Thread> ts ){
		for(Thread t : ts){
			t.start();
		}
	}
	
	public static final String TAG = "DbHelper";
	static void log(String dec){
		Log.i(TAG, dec + ". current thread="+Thread.currentThread().getName());
	}
	
	void waitAndClose(Collection<? extends Thread> ts ){
		for(Thread t : ts){
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for(Thread t : ts){
			if(t instanceof InsertThread){
				((InsertThread)t).db.close();
			}else if(t instanceof ReadThread){
				((ReadThread)t).db.close();
			}
		}
		log("finish!");
	}

public static class DbHelper extends SQLiteOpenHelper{

	public static final String TAG = "DbHelper";
	
	private static String DEFAULT_DB_NAME = "test.db";
	private static int DEFAULT_VERSION = 1;
	
	 private final Context mContext;
	 private final String mName;
	 private final CursorFactory mFactory;
	 private final int mNewVersion;
	
	public DbHelper(Context context) {
		this(context, DEFAULT_DB_NAME, null, DEFAULT_VERSION);
	}

	public DbHelper(Context context, String name, CursorFactory factory, int version) {
		super(context,name,factory,version);
		if (version < 1) throw new IllegalArgumentException("Version must be >= 1, was " + version);
		mContext = context;
		mName = name;
		mFactory = factory;
		mNewVersion = version;
	}
	 
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	
	public void insert(String dec){
		log("insert");
		ContentValues cv = new ContentValues();
		cv.put(DEC, dec);
		getWritableDatabase().insert(TABLE_NAME, null, cv);
	}
	
	public int getCount() {
		return getCount(getReadableDatabase());
	}
	
	
	public int getCount(SQLiteDatabase db) {
		log("DbHelper getCount db:"+db);
		int count = -1;
		if(db == null)	db = getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
		try{
			if (c.moveToFirst()) {
				count = c.getCount();
			}
		}finally{
			c.close();
		}
		log( "getCount="+count);
		return count;
	}
	
	public static final String TABLE_NAME = "test_t";
	public static final String ID = "_id";
	public static final String DEC = "descreption";
	
	private static final String CREATE_TABLE = "create table if not exists "+TABLE_NAME +" ( " 
			+ID + " INTEGER primary key autoincrement, " +DEC + " text " +" );"; 

	
	void log(String dec){
		Log.i(TAG, dec + ". current thread="+Thread.currentThread().getName());
	}
	
	
    public SQLiteDatabase getOnlyReadDatabase() {
    	try{
    		getWritableDatabase(); //保证数据库版本最新
    	}catch(SQLiteException e){
    		Log.e(TAG, "Couldn't open " + mName + " for writing (will try read-only):",e);
    	}
    	
        SQLiteDatabase db = null;
        try {
            String path = mContext.getDatabasePath(mName).getPath();
            db = SQLiteDatabase.openDatabase(path, mFactory, SQLiteDatabase.OPEN_READONLY);
            if (db.getVersion() != mNewVersion) {
                throw new SQLiteException("Can't upgrade read-only database from version " +
                        db.getVersion() + " to " + mNewVersion + ": " + path);
            }

            onOpen(db);
            readOnlyDbs.add(db);
            return db;
        } finally {
        }
    }
    
    
    private List<SQLiteDatabase> readOnlyDbs = new LinkedList<SQLiteDatabase>();

	@Override
	public synchronized void close() {
		super.close();
		for(SQLiteDatabase db : readOnlyDbs){
			if (db != null && db.isOpen()) {
	            db.close();
			}
		}
		readOnlyDbs.clear();
	}
    
}
	
}
