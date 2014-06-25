package com.ajtum.database;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private Context context;
	private SQLiteDatabase db;
	private String dbPath = DatabaseConfig.DB_PATH+DatabaseConfig.DB_NAME;
	private int flagOpenDB = SQLiteDatabase.OPEN_READONLY;//R/W=0 R=1
	
	public DatabaseHelper(Context context,int flags) {
		super(context, DatabaseConfig.DB_NAME, null, DatabaseConfig.DB_VERSION);
		// TODO Auto-generated constructor stub
		this.context = context;
		flagOpenDB = flags;
	}
	
	////////////////////////////////////// override /////////////////////////////////////////

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public synchronized void close() {
		// TODO Auto-generated method stub
		if(db!=null) db.close();
		super.close();
	}
	
	//////////////////////////////////////// private function ////////////////////////////////
	private boolean isDBExists(){
		SQLiteDatabase checkDB = null;
		try{
			checkDB = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
		}catch(SQLException e){
			
		}
		
		if(checkDB!=null) checkDB.close();
		return checkDB!=null;
	}
	
	private void copyDatabase(){
		try {
			InputStream inputFile = context.getAssets().open(DatabaseConfig.DB_NAME);
			OutputStream outputFile = new FileOutputStream(dbPath);
			
			byte buffer[] = new byte[1024];
			int length;
			while((length = inputFile.read(buffer))>0){
				outputFile.write(buffer, 0, length);
			}
			
			outputFile.flush();
			outputFile.close();
			inputFile.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/////////////////////////////////////// public function //////////////////////////////////
	public void createDatabase(){
		if(!isDBExists()){
			if(flagOpenDB==0)		getWritableDatabase();
			else if(flagOpenDB==1)	getReadableDatabase();
			//copy file:: asset to device
			copyDatabase();
		}
	}
	
	public SQLiteDatabase openDatabase(){
		db = SQLiteDatabase.openDatabase(dbPath,null,flagOpenDB);
		return db;
	}
	
}
