package com.ajtum.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class DatabaseManager {

	
	public static Cursor getAllInTable(SQLiteDatabase db,String table,String columns[]){
		return db.query(table, columns, null,null,null,null,null);
	}

	// select Place
	public static Cursor getPlacebyID(SQLiteDatabase db,String table,String columns[],String id){
		return db.query(table, columns, "_place_id="+"'"+id+"'",null,null,null,null);
	}
	
	// select org by place
	public static Cursor getOrgbyPlaceID(SQLiteDatabase db,String table,String columns[],String place_id){
		return db.query(table, columns, "_place_id="+"'"+place_id+"'",null,null,null,null);
	}
	
	// select org sort org_id
	public static Cursor getOrgbyOrgID(SQLiteDatabase db,String org_id){
		
		String sql="SELECT _org_id,place_name,org_name,address,website,tel,work_date,work_time,place_lat,place_long,place._place_id  " +
				"FROM place,org   " +
				"WHERE place._place_id=org._place_id  " +
				"AND _org_id='"+org_id+"' ";
				
		return db.rawQuery(sql, null);
		//return db.query(table, columns, "_org_id="+"'"+org_id+"'",null,null,null,null);
	}
	
	// select org by Faculty
	public static Cursor getOrgbyFaculty(SQLiteDatabase db,String table,String columns[]){
		return db.query(table, columns, "_type_id="+"'2'",null,null,null,"org_name");
	}
	
	// select org by Institute
	public static Cursor getOrgbyinstitute(SQLiteDatabase db,String table,String columns[]){
		return db.query(table, columns, "_type_id="+"'1'",null,null,null,"org_name");
	}
	
	
	// search by Institute
	public static Cursor searchByActionBar(SQLiteDatabase db,String query){
		
		
		String sql="SELECT _place_id,place_name,place_lat,place_long  " +
				"FROM place   " +
				"WHERE place_name like '%"+query+"%'  ";
				
		Log.d("ajtum", ""+sql);
		
		return db.rawQuery(sql, null);
	}
	
	
	
}
