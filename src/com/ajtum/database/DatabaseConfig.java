package com.ajtum.database;

public class DatabaseConfig {
	//general
	public static final String DB_PATH = "data/data/com.ajtum.itrmu.aroundrmu/databases/";
	public static final String DB_NAME = "db_aroundrmu.sqlite";
	public static final int DB_VERSION = 6;
	
	//table
	public static final String TABLE_Place = "place";
	public static final String COLUMNS_Place[] = {"_place_id","place_name","place_lat","place_long"};

	public static final String TABLE_Org = "org";
	public static final String COLUMNS_Org[] = {"_org_id","_place_id","_type_id","org_name","description","address","website","tel","work_date","work_time"};
	
	public static final String TABLE_Type = "type";
	public static final String COLUMNS_Type[] = {"_type_id","status"};

	public static final String PackageName="com.ajtum.itrmu.aroundrmu";
	
	
}
