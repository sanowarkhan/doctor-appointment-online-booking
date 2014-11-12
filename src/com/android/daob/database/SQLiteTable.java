
package com.android.daob.database;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

import com.android.daob.model.DoctorModel;
import com.android.daob.model.SpecialtyModel;
import com.android.daob.model.WorkingPlaceModel;
import com.android.daob.utils.Constants;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SQLiteTable {
    protected static final String TAG = "DataAdapter";

    Context mContext;

    SQLiteDatabase mDb;

    DBHelper mDbHelper;

    public SQLiteTable(Context context) {
        this.mContext = context;
        mDbHelper = new DBHelper(mContext);
    }

    public SQLiteTable open() throws SQLException {
        try {
            mDb = mDbHelper.getReadableDatabase();
        } catch (SQLException mSQLException) {
            Log.e(TAG, "open >>" + mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close() {
        mDbHelper.close();
    }
    
    DoctorModel initDoctorModelFromCursor(Cursor c){
    	DoctorModel doc = new DoctorModel();
    	doc.setDoctorId(c.getInt(c.getColumnIndex(Constants.ID)));
    	doc.setDoctorName(c.getString(c.getColumnIndex(Constants.NAME)));
    	doc.setDescription(c.getString(c.getColumnIndex(Constants.DESCRIPTION)));
    	doc.setEducation(c.getString(c.getColumnIndex(Constants.EDUCATION)));
    	doc.setDoctorWorkingPlace(c.getString(c.getColumnIndex(Constants.WORKING_PLACE)));
		return doc;
    }

    WorkingPlaceModel initWorkingPlaceFromCursor(Cursor c) {
        WorkingPlaceModel wp = new WorkingPlaceModel();
        wp.setWorkingPlaceId(c.getInt(c.getColumnIndex(Constants.ID)));
        wp.setWorkingPlaceName(c.getString(c.getColumnIndex(Constants.NAME)));
        wp.setAddress(c.getString(c.getColumnIndex(Constants.ADDRESS)));
        return wp;
    }

    SpecialtyModel initSpecialtyFromCursor(Cursor c) {
        SpecialtyModel spec = new SpecialtyModel();
        spec.setSpecialtyId(c.getInt(c.getColumnIndex(Constants.ID)));
        spec.setSpecialtyName(c.getString(c.getColumnIndex(Constants.NAME)));
        return spec;
    }
    
    //search doctor
    public ArrayList<DoctorModel> searchDoctor(int specialtyId, int workingPlaceId, String name){
    	ArrayList<DoctorModel> listDoctorModels = new ArrayList<DoctorModel>();
    	try{
    		String sql = "";
    		if(specialtyId == 0 && workingPlaceId == 0 && name != ""){
    			sql = "Select * from " + Constants.DOCTOR_TABLE + " where Name like '%" + name + "%'";
    		} else if(specialtyId == 0 && workingPlaceId != 0){
    			sql = "Select * from " + Constants.DOCTOR_TABLE + " where WorkingPlace like '%" + workingPlaceId +";%'"
        				+ " and Name like '%" + name + "%'";
    		} else if(workingPlaceId == 0 && specialtyId != 0){
    			sql = "Select * from " + Constants.DOCTOR_TABLE + " where Specialty='" + specialtyId +"'"
        				+ " and Name like '%" + name + "%'";
    		} else if(specialtyId != 0 && workingPlaceId != 0){
    			sql = "Select * from " + Constants.DOCTOR_TABLE + " where Specialty='"
        				+ specialtyId + "' and WorkingPlace like '%" + workingPlaceId +";%'"
        				+ " and Name like '%" + name + "%'";
    		} else{
    			sql = "Select * from " + Constants.DOCTOR_TABLE;
    		}
    		
    		Cursor mCur = mDb.rawQuery(sql, null);
    		if(mCur != null){
    			DoctorModel doc = new DoctorModel();
    			
    			while (mCur.moveToNext()) {
   				 doc = initDoctorModelFromCursor(mCur);
   				 listDoctorModels.add(doc);
    			}
    		}
    	} catch (SQLException mSQLException){
    		Log.e(TAG, "getTestData >>" + mSQLException.toString());
    		throw mSQLException;
    	}
    	return (ArrayList<DoctorModel>) listDoctorModels;
    }
    
    //get all doctor
    public List<DoctorModel> getAllDoctor(){
    	List<DoctorModel> listDoctorModels = new ArrayList<DoctorModel>();
    	try {
    		String sql = "Select * from " + Constants.DOCTOR_TABLE;
    		Cursor mCur = mDb.rawQuery(sql, null);
    		if(mCur != null){
    			DoctorModel doc = new DoctorModel();
    			
    			while (mCur.moveToNext()) {
    				 doc = initDoctorModelFromCursor(mCur);
    				 listDoctorModels.add(doc);
    			}
    		}
    	} catch (SQLException mSQLException) {
    		Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
    	}
    	return listDoctorModels;
    }
    
 // insert specialty to db
    public void insertDoctor(DoctorModel doc) {
        ContentValues values = new ContentValues();
        values.put(Constants.ID, doc.getDoctorId());
        values.put(Constants.NAME, doc.getDoctorName());
        values.put(Constants.DESCRIPTION, doc.getDescription());
        values.put(Constants.EDUCATION, doc.getEducation());
        values.put(Constants.WORKING_PLACE, doc.getDoctorWorkingPlace());
        mDb.insert(Constants.DOCTOR_TABLE, null, values);
    }

    // delete all data
    public void deleteDoctor() {
        mDb.delete(Constants.DOCTOR_TABLE, null, null);
    }

    // get all specialty
    public List<WorkingPlaceModel> getAllWorkingPlace() {
        List<WorkingPlaceModel> listwWorkingPlaceModels = new ArrayList<WorkingPlaceModel>();
        try {
            String sql = "Select * from " + Constants.HOSPITAL_TABLE;

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                WorkingPlaceModel wp = new WorkingPlaceModel();

                while (mCur.moveToNext()) {
                    wp = initWorkingPlaceFromCursor(mCur);
                    listwWorkingPlaceModels.add(wp);
                }
            }
            return listwWorkingPlaceModels;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    // insert specialty to db
    public void insertWorkingPlace(WorkingPlaceModel wp) {
        ContentValues values = new ContentValues();
        values.put(Constants.ID, wp.getWorkingPlaceId());
        values.put(Constants.NAME, wp.getWorkingPlaceName());
        values.put(Constants.ADDRESS, wp.getAddress());
        mDb.insert(Constants.HOSPITAL_TABLE, null, values);
    }

    // delete all data
    public void deleteWorkingPlace() {
        mDb.delete(Constants.HOSPITAL_TABLE, null, null);
    }

    // get all specialty
    public List<SpecialtyModel> getAllSpecialty() {
        List<SpecialtyModel> listSpecialtyModels = new ArrayList<SpecialtyModel>();
        try {
            String sql = "Select * from " + Constants.SPECIALTY_TABLE;

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                SpecialtyModel spec = new SpecialtyModel();

                while (mCur.moveToNext()) {
                    spec = initSpecialtyFromCursor(mCur);
                    listSpecialtyModels.add(spec);
                }
            }
            return listSpecialtyModels;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    // insert specialty to db
    public void insertSpecialty(SpecialtyModel spec) {
        ContentValues values = new ContentValues();
        values.put(Constants.ID, spec.getSpecialtyId());
        values.put(Constants.NAME, spec.getSpecialtyName());
        mDb.insert(Constants.SPECIALTY_TABLE, null, values);
    }

    // delete all data
    public void deleteSpecialty() {
        mDb.delete(Constants.SPECIALTY_TABLE, null, null);
    }
    
    public long getCountDoctor() {
        long count = 0;

        try {
            String sql = "Select count (*) from " + Constants.DOCTOR_TABLE;

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                while (mCur.moveToNext()) {
                    count = mCur.getLong(0);
                }
            }
            return count;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public long getCountWorkingPlace() {
        long count = 0;

        try {
            String sql = "Select count (*) from " + Constants.HOSPITAL_TABLE;

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                while (mCur.moveToNext()) {
                    count = mCur.getLong(0);
                }
            }
            return count;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public long getCountSpecialty() {
        long count = 0;

        try {
            String sql = "Select count (*) from " + Constants.SPECIALTY_TABLE;

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                while (mCur.moveToNext()) {
                    count = mCur.getLong(0);
                }
            }
            return count;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }
}
