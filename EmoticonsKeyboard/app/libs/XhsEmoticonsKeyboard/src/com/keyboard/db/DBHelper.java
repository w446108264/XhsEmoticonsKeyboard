package com.keyboard.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.keyboard.bean.EmoticonBean;
import com.keyboard.bean.EmoticonSetBean;

import java.util.ArrayList;

public class DBHelper {

    /**
     * 0-->1 add emoticons,emoticonset
     */
    private static final int VERSION = 1;

    private static final String DATABASE_NAME = "xhsemoticons.db";
    private static final String TABLE_NAME_EMOTICONS = "emoticons";
    private static final String TABLE_NAME_EMOTICONSET = "emoticonset";

    private SQLiteDatabase db;
    private DBOpenHelper dbOpenHelper;

    public DBHelper(Context context) {
        this.dbOpenHelper = new DBOpenHelper(context);
        establishDb();
    }

    private void establishDb() {
        if (this.db == null) {
            this.db = this.dbOpenHelper.getWritableDatabase();
        }
    }

    public ContentValues createEmoticonSetContentValues(EmoticonBean bean, String beanSetName) {
        if (bean == null) {
            return null;
        }
        ContentValues values = new ContentValues();
        values.put(TableColumns.EmoticonColumns.EVENTTYPE, bean.getEventType());
        values.put(TableColumns.EmoticonColumns.CONTENT, bean.getContent());
        values.put(TableColumns.EmoticonColumns.ICONURI, bean.getIconUri());
        values.put(TableColumns.EmoticonColumns.EMOTICONSET_NAME, beanSetName);
        return values;
    }

    public long insertEmoticonBean(EmoticonBean bean, String beanSetName) {

        long result = -1;
        if (bean == null || db == null) {
            return result;
        }
        ContentValues values = new ContentValues();
        values.put(TableColumns.EmoticonColumns.EVENTTYPE, bean.getEventType());
        values.put(TableColumns.EmoticonColumns.CONTENT, bean.getContent());
        values.put(TableColumns.EmoticonColumns.ICONURI, bean.getIconUri());
        values.put(TableColumns.EmoticonColumns.EMOTICONSET_NAME, beanSetName);
        try {
            result = db.insert(TABLE_NAME_EMOTICONS, null, values);
        } catch (SQLiteConstraintException e) {
        }
        return result;
    }

    public long insertEmoticonBeans(ContentValues[] values) {
        db.beginTransaction();
        int insertSuccessCount = values.length;
        try {
            for (ContentValues cv : values) {
                if (db.insert(TABLE_NAME_EMOTICONS, null, cv) < 0) {
                    insertSuccessCount--;
                }
            }
            db.setTransactionSuccessful();
        } catch (SQLiteConstraintException e) {
        } catch (Exception e) {
        } finally {
            db.endTransaction();
        }
        return insertSuccessCount;
    }

    public long insertEmoticonSet(EmoticonSetBean bean) {
        long result = -1;
        if (bean == null || db == null || TextUtils.isEmpty(bean.getName())) {
            return result;
        }

        ContentValues values = new ContentValues();
        values.put(TableColumns.EmoticonSetColumns.NAME, bean.getName());
        values.put(TableColumns.EmoticonSetColumns.LINE, bean.getLine());
        values.put(TableColumns.EmoticonSetColumns.ROW, bean.getRow());
        values.put(TableColumns.EmoticonSetColumns.ICONURI, bean.getIconUri());
        values.put(TableColumns.EmoticonSetColumns.ICONNAME, bean.getIconName());
        values.put(TableColumns.EmoticonSetColumns.ISSHOWDELBTN, bean.isShowDelBtn() ? 1 : 0);
        values.put(TableColumns.EmoticonSetColumns.ITEMPADDING, bean.getItemPadding());
        values.put(TableColumns.EmoticonSetColumns.HORIZONTALSPACING, bean.getHorizontalSpacing());
        values.put(TableColumns.EmoticonSetColumns.VERTICALSPACING, bean.getVerticalSpacing());
        result = db.insert(TABLE_NAME_EMOTICONSET, null, values);

        ArrayList<EmoticonBean> emoticonList = bean.getEmoticonList();
        if (emoticonList != null) {
            String emoticonSetname = bean.getName();
            ContentValues[] contentValues = new ContentValues[emoticonList.size()];
            for (int i = 0; i < emoticonList.size(); i++) {
                contentValues[i] = createEmoticonSetContentValues(emoticonList.get(i), emoticonSetname);
            }
            insertEmoticonBeans(contentValues);
        }
        return result;
    }

    public EmoticonBean queryEmoticonBean(String contentStr) {
        String sql = "select * from " + TABLE_NAME_EMOTICONS + " where " + TableColumns.EmoticonColumns.CONTENT + " = '" + contentStr + "'";
        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                long eventType = cursor.getLong(cursor.getColumnIndex(TableColumns.EmoticonColumns.EVENTTYPE));
                String content = cursor.getString(cursor.getColumnIndex(TableColumns.EmoticonColumns.CONTENT));
                String iconUri = cursor.getString(cursor.getColumnIndex(TableColumns.EmoticonColumns.ICONURI));
                return new EmoticonBean(eventType, iconUri, content);
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    public ArrayList<EmoticonBean> queryEmoticonBeanList(String sql) {
        Cursor cursor = db.rawQuery(sql, null);
        try {
            int count = cursor.getCount();
            ArrayList<EmoticonBean> beanList = new ArrayList<EmoticonBean>();
            if (count > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < count; i++) {
                    long eventType = cursor.getLong(cursor.getColumnIndex(TableColumns.EmoticonColumns.EVENTTYPE));
                    String content = cursor.getString(cursor.getColumnIndex(TableColumns.EmoticonColumns.CONTENT));
                    String iconUri = cursor.getString(cursor.getColumnIndex(TableColumns.EmoticonColumns.ICONURI));
                    EmoticonBean bean = new EmoticonBean(eventType, iconUri, content);
                    beanList.add(bean);
                    cursor.moveToNext();
                }
                return beanList;
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    public ArrayList<EmoticonBean> queryAllEmoticonBeans() {
        String sql = "select * from " + TABLE_NAME_EMOTICONS;
        return queryEmoticonBeanList(sql);
    }

    public ArrayList<EmoticonSetBean> queryEmoticonSet(String ... setNames) {
        if(setNames == null || setNames.length == 0){
            return null;
        }
        String sql = "select * from " + TABLE_NAME_EMOTICONSET + " where " ;
        for(int i = 0 ;i < setNames.length ; i++){
            if(i != 0){
                sql = sql + " or ";
            }
            sql = sql + TableColumns.EmoticonSetColumns.NAME + " = '" + setNames[i] + "' ";
        }
        return queryEmoticonSet(sql);
    }

    public ArrayList<EmoticonSetBean> queryEmoticonSet(ArrayList<String> setNameList) {
        if(setNameList == null || setNameList.size() == 0){
            return null;
        }
        String sql = "select * from " + TABLE_NAME_EMOTICONSET + " where " ;
        int i = 0 ;
        for(String name : setNameList){
            if(i != 0){
                sql = sql + " or ";
            }
            sql = sql + TableColumns.EmoticonSetColumns.NAME + " = '" + name + "' ";
            i++;
        }
        return queryEmoticonSet(sql);
    }

    public ArrayList<EmoticonSetBean> queryAllEmoticonSet() {
        String sql = "select * from " + TABLE_NAME_EMOTICONSET;
        return queryEmoticonSet(sql);
    }

    public ArrayList<EmoticonSetBean> queryEmoticonSet(String sql) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, null);
            int count = cursor.getCount();
            ArrayList<EmoticonSetBean> beanList = new ArrayList<EmoticonSetBean>();
            if (count > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < count; i++) {
                    String name = cursor.getString(cursor.getColumnIndex(TableColumns.EmoticonSetColumns.NAME));
                    int line = cursor.getInt(cursor.getColumnIndex(TableColumns.EmoticonSetColumns.LINE));
                    int row = cursor.getInt(cursor.getColumnIndex(TableColumns.EmoticonSetColumns.ROW));
                    String iconUri = cursor.getString(cursor.getColumnIndex(TableColumns.EmoticonSetColumns.ICONURI));
                    String iconName = cursor.getString(cursor.getColumnIndex(TableColumns.EmoticonSetColumns.ICONNAME));
                    boolean isshowdelbtn = cursor.getInt(cursor.getColumnIndex(TableColumns.EmoticonSetColumns.ISSHOWDELBTN)) == 1 ? true : false;
                    int itempadding = cursor.getInt(cursor.getColumnIndex(TableColumns.EmoticonSetColumns.ITEMPADDING));
                    int horizontalspacing = cursor.getInt(cursor.getColumnIndex(TableColumns.EmoticonSetColumns.HORIZONTALSPACING));
                    int verticalSpacing = cursor.getInt(cursor.getColumnIndex(TableColumns.EmoticonSetColumns.VERTICALSPACING));

                    ArrayList<EmoticonBean> emoticonList = null;
                    if (!TextUtils.isEmpty(name)) {
                        String sqlGetEmoticonBean = "select * from " + TABLE_NAME_EMOTICONS + " where " + TableColumns.EmoticonColumns.EMOTICONSET_NAME + " = '" + name + "'";
                        emoticonList = queryEmoticonBeanList(sqlGetEmoticonBean);
                    }

                    int pageCount = 0;
                    if (emoticonList != null) {
                        int del = isshowdelbtn ? 1 : 0;
                        int everyPageMaxSum = row * line - del;
                        pageCount = (int) Math.ceil((double) emoticonList.size() / everyPageMaxSum);
                    }

                    EmoticonSetBean bean = new EmoticonSetBean(name, line, row, iconUri, iconName, isshowdelbtn, itempadding, horizontalspacing, verticalSpacing, emoticonList);
                    beanList.add(bean);
                    cursor.moveToNext();
                }
                return beanList;
            }
        }
        catch (SQLiteException e){

        }
        finally {
            cursor.close();
        }
        return null;
    }


    public void cleanup() {
        if (this.db != null) {
            this.db.close();
            this.db = null;
        }
    }

    private static class DBOpenHelper extends SQLiteOpenHelper {

        public DBOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, VERSION);
        }

        private static void createEmoticonsTable(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME_EMOTICONS + " ( " +
                    TableColumns.EmoticonColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TableColumns.EmoticonColumns.EVENTTYPE + " INTEGER, " +
                    TableColumns.EmoticonColumns.CONTENT + " TEXT NOT NULL, " +
                    TableColumns.EmoticonColumns.ICONURI + " TEXT NOT NULL, " +
                    TableColumns.EmoticonColumns.EMOTICONSET_NAME + " TEXT NOT NULL);");


            db.execSQL("CREATE TABLE " + TABLE_NAME_EMOTICONSET + " ( " +
                    TableColumns.EmoticonSetColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TableColumns.EmoticonSetColumns.NAME + " TEXT NOT NULL UNIQUE, " +
                    TableColumns.EmoticonSetColumns.LINE + " INTEGER, " +
                    TableColumns.EmoticonSetColumns.ROW + " INTEGER, " +
                    TableColumns.EmoticonSetColumns.ICONURI + " TEXT, " +
                    TableColumns.EmoticonSetColumns.ICONNAME + " TEXT, " +
                    TableColumns.EmoticonSetColumns.ISSHOWDELBTN + " BOOLEAN, " +
                    TableColumns.EmoticonSetColumns.ITEMPADDING + " INTEGER, " +
                    TableColumns.EmoticonSetColumns.HORIZONTALSPACING + " INTEGER, " +
                    TableColumns.EmoticonSetColumns.VERTICALSPACING + " TEXT);");
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            onUpgrade(sqLiteDatabase, 0, VERSION);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int currentVersion) {
            for (int version = oldVersion + 1; version <= currentVersion; version++) {
                upgradeTo(sqLiteDatabase, version);
            }
        }

        private void upgradeTo(SQLiteDatabase db, int version) {
            switch (version) {
                case 1:
                    createEmoticonsTable(db);
                    break;
                default:
                    throw new IllegalStateException("Don't know how to upgrade to " + version);
            }
        }
    }
}