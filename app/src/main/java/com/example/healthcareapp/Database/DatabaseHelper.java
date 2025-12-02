package com.example.healthcareapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;

import com.example.healthcareapp.Admin.Models.NewUserModel;
import com.example.healthcareapp.ImageHelper;
import com.example.healthcareapp.Model.DoctorListModel;
import com.example.healthcareapp.Model.PopularProductListModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "HealthCareApp.db";
    public static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_USER = "User";
    public static final String TABLE_DOCTOR = "Doctor";
    public static final String TABLE_MEDICINE = "Medicine";
    public static final String TABLE_CART = "Cart";
    public static final String TABLE_MEDICINE_SEARCH_HISTORY = "MedicineSearchHistory";
    public static final String TABLE_DOCTOR_SEARCH_HISTORY = "DoctorSearchHistory";
    public static final String TABLE_MEDICINE_ORDER = "MedicineOrder";
    public static final String TABLE_NOTIFICATION = "Notification";

    Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    ImageHelper imageHelper = new ImageHelper();

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");

        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Name TEXT NOT NULL, " +
                "Image BLOB DEFAULT NULL, " +
                "Email TEXT NOT NULL UNIQUE, " +
                "Password TEXT NOT NULL, " +
                "Gender TEXT NOT NULL, " +
                "Address TEXT NOT NULL, " +
                "Age INTEGER NOT NULL, " +
                "Height REAL NOT NULL, " +
                "Weight REAL NOT NULL, " +
                "BloodGroup TEXT NOT NULL, " +
                "ActivityLevel TEXT NOT NULL)";
        db.execSQL(CREATE_USER_TABLE);

        String CREATE_DOCTOR_TABLE = "CREATE TABLE " + TABLE_DOCTOR + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Name TEXT NOT NULL, " +
                "Image BLOB NOT NULL, " +
                "JobTitle TEXT NOT NULL, " +
                "WhatsAppNumber TEXT NOT NULL, " +
                "PhoneNumber TEXT NOT NULL, " +
                "InstagramUsername TEXT NOT NULL, " +
                "Location TEXT NOT NULL, " +
                "Rating REAL NOT NULL, " +
                "Description TEXT NOT NULL)";
        db.execSQL(CREATE_DOCTOR_TABLE);

        String CREATE_MEDICINE_TABLE = "CREATE TABLE " + TABLE_MEDICINE + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Image BLOB NOT NULL, " +
                "Name TEXT NOT NULL, " +
                "Price REAL NOT NULL, " +
                "Category TEXT NOT NULL, " +
                "ExpiryDate TEXT NOT NULL, " +
                "Rating REAL NOT NULL, " +
                "Description TEXT NOT NULL)";
        db.execSQL(CREATE_MEDICINE_TABLE);

        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Name TEXT NOT NULL, " +
                "Image BLOB NOT NULL, " +
                "MedicineID INTEGER NOT NULL, " +
                "UserID INTEGER NOT NULL, " +
                "Quantity INTEGER NOT NULL DEFAULT 1, " +
                "Price REAL NOT NULL, " +
                "FOREIGN KEY (MedicineID) REFERENCES " + TABLE_MEDICINE + "(ID) ON DELETE CASCADE, " +
                "FOREIGN KEY (UserID) REFERENCES " + TABLE_USER + "(ID) ON DELETE CASCADE)";
        db.execSQL(CREATE_CART_TABLE);

        String CREATE_MEDICINE_SEARCH_HISTORY = "CREATE TABLE " + TABLE_MEDICINE_SEARCH_HISTORY + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UserID INTEGER NOT NULL, " +
                "MedicineName TEXT NOT NULL, " +
                "FOREIGN KEY (UserID) REFERENCES " + TABLE_USER + "(ID) ON DELETE CASCADE)";
        db.execSQL(CREATE_MEDICINE_SEARCH_HISTORY);

        String CREATE_DOCTOR_SEARCH_HISTORY = "CREATE TABLE " + TABLE_DOCTOR_SEARCH_HISTORY + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UserID INTEGER NOT NULL, " +
                "DoctorNameSpecification TEXT NOT NULL, " +
                "FOREIGN KEY (UserID) REFERENCES " + TABLE_USER + "(ID) ON DELETE CASCADE)";
        db.execSQL(CREATE_DOCTOR_SEARCH_HISTORY);

        String CREATE_MEDICINE_ORDER = "CREATE TABLE " + TABLE_MEDICINE_ORDER + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UserID INTEGER NOT NULL, " +
                "MedicineID INTEGER NOT NULL, " +
                "Price REAL NOT NULL, " +
                "Quantity INTEGER NOT NULL, " +
                "OrderDate TEXT NOT NULL, " +
                "FOREIGN KEY (UserID) REFERENCES " + TABLE_USER + "(ID) ON DELETE CASCADE, " +
                "FOREIGN KEY (MedicineID) REFERENCES " + TABLE_MEDICINE + "(ID) ON DELETE CASCADE)";
        db.execSQL(CREATE_MEDICINE_ORDER);

        String CREATE_TABLE_NOTIFICATION = "CREATE TABLE " + TABLE_NOTIFICATION + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UserID INTEGER NOT NULL, " +
                "MedicineID INTEGER NOT NULL, " +
                "Title TEXT NOT NULL, " +
                "Message TEXT NOT NULL, " +
                "FOREIGN KEY (UserID) REFERENCES " + TABLE_USER + "(ID) ON DELETE CASCADE, " +
                "FOREIGN KEY (MedicineID) REFERENCES " + TABLE_MEDICINE + "(ID) ON DELETE CASCADE)";
        db.execSQL(CREATE_TABLE_NOTIFICATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCTOR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICINE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICINE_SEARCH_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCTOR_SEARCH_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICINE_ORDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Insert User
    public boolean insertUser(String name, byte[] image, String email, String password,
                              String gender, String address, int age, double height, double weight,
                              String bloodGroup, String activityLevel) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Name", name);
        values.put("Image", image);
        values.put("Email", email);
        values.put("Password", password);
        values.put("Gender", gender);
        values.put("Address", address);
        values.put("Age", age);
        values.put("Height", height);
        values.put("Weight", weight);
        values.put("BloodGroup", bloodGroup);
        values.put("ActivityLevel", activityLevel);

        long result = db.insert(TABLE_USER, null, values);
        return result != -1;
    }

    // Upadate data
    public boolean updateUserProfile(int userId, String name, byte[] image, String email,
                                     String gender, String address, int age, double height, double weight,
                                     String bloodGroup, String activityLevel) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Name", name);
        values.put("Image", image);
        values.put("Email", email);
        values.put("Gender", gender);
        values.put("Address", address);
        values.put("Age", age);
        values.put("Height", height);
        values.put("Weight", weight);
        values.put("BloodGroup", bloodGroup);
        values.put("ActivityLevel", activityLevel);

        int result = db.update(TABLE_USER, values, "ID=?", new String[]{String.valueOf(userId)});
        return result > 0;
    }

    public boolean updateUser(int userId, String name, byte[] image, String email, String password,
                                     String gender, String address, int age, double height, double weight,
                                     String bloodGroup, String activityLevel) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Name", name);
        values.put("Image", image);
        values.put("Email", email);
        values.put("Password", password);
        values.put("Gender", gender);
        values.put("Address", address);
        values.put("Age", age);
        values.put("Height", height);
        values.put("Weight", weight);
        values.put("BloodGroup", bloodGroup);
        values.put("ActivityLevel", activityLevel);

        int result = db.update(TABLE_USER, values, "ID=?", new String[]{String.valueOf(userId)});
        return result > 0;
    }

    // Update Password
    public boolean updatePassword(int userId, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Password", password);

        int result = db.update(TABLE_USER, values, "ID=?", new String[]{String.valueOf(userId)});
        return result > 0;
    }

    // Delete Function
    public boolean deleteUser(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_USER, "ID=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    // get list of user names
    public List<String> getAllUserNames() {
        List<String> userNames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT Name FROM " + TABLE_USER;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                userNames.add(name);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return userNames;
    }

    public Cursor getSearchedUser(String userName){
        String query = "SELECT * FROM " + TABLE_USER + " WHERE Name LIKE ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, new String[]{userName + "%"});
        }
        return cursor;
    }

    // Select Data From User Table
    public Cursor getAllUsers() {
        String query = "SELECT * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    // Get User ID
    public int getUserIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        int userId = -1; // Default -1 if not found
        Cursor cursor = db.rawQuery("SELECT ID FROM "+ TABLE_USER +" WHERE Email = ?", new String[]{email});
        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getInt(0);
            cursor.close();
        }
        db.close();
        return userId;
    }

    // get new users
    public List<NewUserModel> getNewUsers(){
        List<NewUserModel> newUserList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_USER + " ORDER BY ID DESC LIMIT 5";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()){
            do{
                int ID = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
                String Name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
                String ActivityLevel = cursor.getString(cursor.getColumnIndexOrThrow("ActivityLevel"));
                byte[] userImage = cursor.getBlob(cursor.getColumnIndexOrThrow("Image"));
                Bitmap Image = null;
                if(userImage != null){
                    Image = imageHelper.bytesToBitmap(userImage);
                }

                newUserList.add(new NewUserModel(
                        ID,
                        Image,
                        Name,
                        ActivityLevel
                ));

            } while (cursor.moveToNext());
        }

        return newUserList;
    }

    // get user Count
    public int getUserCount() {
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT COUNT(*) FROM " + TABLE_USER;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return count;
    }

    // Get Name by User ID
    public String getNameByUserId(int userId) {
        return getColumnValueById(TABLE_USER, "Name", userId);
    }

    // Get Email by User ID
    public String getEmailByUserId(int userId) {
        return getColumnValueById(TABLE_USER, "Email", userId);
    }

    // Get Gender by User ID
    public String getGenderByUserId(int userId) {
        return getColumnValueById(TABLE_USER, "Gender", userId);
    }

    // get address by user id
    public String getAddressByUserId(int userId) {
        return getColumnValueById(TABLE_USER, "Address", userId);
    }

    // Get Age by User ID
    public int getAgeByUserId(int userId) {
        String value = getColumnValueById(TABLE_USER, "Age", userId);
        return value != null ? Integer.parseInt(value) : -1;
    }

    // Get Height by User ID
    public float getHeightByUserId(int userId) {
        String value = getColumnValueById(TABLE_USER, "Height", userId);
        return value != null ? Float.parseFloat(value) : -1f;
    }

    // Get Weight by User ID
    public float getWeightByUserId(int userId) {
        String value = getColumnValueById(TABLE_USER, "Weight", userId);
        return value != null ? Float.parseFloat(value) : -1f;
    }

    // Get Blood Group by User ID
    public String getBloodGroupByUserId(int userId) {
        return getColumnValueById(TABLE_USER, "BloodGroup", userId);
    }

    // Get Activity Level by User ID
    public String getActivityLevelByUserId(int userId) {
        return getColumnValueById(TABLE_USER, "ActivityLevel", userId);
    }

    // Get Password by User ID
    public String getUserPassword(int userId) {
        return getColumnValueById(TABLE_USER, "Password", userId);
    }

    // Get Image (BLOB) by User ID
    public byte[] getImageByUserId(int userId) {
        return getImageByID(TABLE_USER, userId);
    }

    // Insert Doctor
    public boolean insertDoctor(String name, byte[] image, String jobTitle, String whatsapp, String phone,
                                String insta, String location, double rating, String description) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Name", name);
        values.put("Image", image);
        values.put("JobTitle", jobTitle);
        values.put("WhatsAppNumber", whatsapp);
        values.put("PhoneNumber", phone);
        values.put("InstagramUsername", insta);
        values.put("Location", location);
        values.put("Rating", rating);
        values.put("Description", description);

        long result = db.insert(TABLE_DOCTOR, null, values);
        return result != -1;
    }

    // Update Doctor
    public boolean updateDoctors(int doctorID, String name, byte[] image, String jobTitle, String whatsapp, String phone,
                                 String insta, String location, double rating, String description){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Name", name);
        values.put("Image", image);
        values.put("JobTitle", jobTitle);
        values.put("WhatsAppNumber", whatsapp);
        values.put("PhoneNumber", phone);
        values.put("InstagramUsername", insta);
        values.put("Location", location);
        values.put("Rating", rating);
        values.put("Description", description);

        int result = db.update(TABLE_DOCTOR, values, "ID=?", new String[]{String.valueOf(doctorID)});
        return result > 0;
    }

    // Delete Doctor
    public boolean deleteDoctor(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_DOCTOR, "ID=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    // Select All Doctors
    public Cursor getAllDoctors() {
        String query = "SELECT * FROM " + TABLE_DOCTOR;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor getSearchedDoctor(String doctorNameSpecification){
        String query = "SELECT * FROM " + TABLE_DOCTOR + " WHERE Name LIKE ? OR JobTitle Like ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, new String[]{doctorNameSpecification + "%", doctorNameSpecification + "%"});
        }
        return cursor;
    }

    public int getDoctorCount() {
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT COUNT(*) FROM " + TABLE_DOCTOR;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return count;
    }


    // Get Top 5 Doctors
    public List<DoctorListModel> getTopFiveDoctors() {
        List<DoctorListModel> doctorList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query for top 5 doctors with highest rating
        String query = "SELECT * FROM " + TABLE_DOCTOR + " ORDER BY Rating DESC LIMIT 5";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int doctorId = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
                String speciality = cursor.getString(cursor.getColumnIndexOrThrow("JobTitle"));
                double rating = cursor.getDouble(cursor.getColumnIndexOrThrow("Rating"));

                // Convert BLOB to Bitmap
                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow("Image"));
                Bitmap imgDoctor = imageHelper.bytesToBitmap(imageBytes);

                // Create model object
                DoctorListModel doctor = new DoctorListModel(
                        doctorId,
                        imgDoctor,
                        name,
                        speciality,
                        rating
                );

                doctorList.add(doctor);

            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return doctorList;
    }

    // Get Top 5 Doctors
    public List<DoctorListModel> getNewDoctors() {
        List<DoctorListModel> doctorList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query for top 5 doctors with highest rating
        String query = "SELECT * FROM " + TABLE_DOCTOR + " ORDER BY ID DESC LIMIT 5";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int doctorId = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
                String speciality = cursor.getString(cursor.getColumnIndexOrThrow("JobTitle"));
                double rating = cursor.getDouble(cursor.getColumnIndexOrThrow("Rating"));

                // Convert BLOB to Bitmap
                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow("Image"));
                Bitmap imgDoctor = imageHelper.bytesToBitmap(imageBytes);

                // Create model object
                DoctorListModel doctor = new DoctorListModel(
                        doctorId,
                        imgDoctor,
                        name,
                        speciality,
                        rating
                );

                doctorList.add(doctor);

            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return doctorList;
    }

    // get list of doctor names
    public List<String> getAllDoctorNames() {
        List<String> doctorNames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT Name FROM " + TABLE_DOCTOR;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                doctorNames.add(name);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return doctorNames;
    }

    // get list of doctor specification
    public List<String> getAllDoctorSpecification() {
        List<String> doctorSpecification = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT JobTitle FROM " + TABLE_DOCTOR;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                doctorSpecification.add(name);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return doctorSpecification;
    }




    // Get Doctor Name
    public String getDoctorName(int doctorID) {
        return getColumnValueById(TABLE_DOCTOR, "Name", doctorID);
    }

    // Get Doctor Image
    public byte[] getDoctorImage(int doctorID) {
        return getImageByID(TABLE_DOCTOR, doctorID);
    }

    // Get Doctor Job Title
    public String getDoctorJobTitle(int doctorID) {
        return getColumnValueById(TABLE_DOCTOR, "JobTitle", doctorID);
    }

    // Get Doctor WhatsApp Number
    public String getDoctorWhatsAppNumber(int doctorID) {
        return getColumnValueById(TABLE_DOCTOR, "WhatsAppNumber", doctorID);
    }

    // Get Doctor Phone Number
    public String getDoctorPhoneNumber(int doctorID) {
        return getColumnValueById(TABLE_DOCTOR, "PhoneNumber", doctorID);
    }

    // Get Doctor Instagram Username
    public String getDoctorInstagramUsername(int doctorID) {
        return getColumnValueById(TABLE_DOCTOR, "InstagramUsername", doctorID);
    }

    // Get Doctor Location
    public String getDoctorLocation(int doctorID) {
        return getColumnValueById(TABLE_DOCTOR, "Location", doctorID);
    }

    // Get Doctor Rating
    public double getDoctorRating(int doctorID) {
        String value =  getColumnValueById(TABLE_DOCTOR, "Rating", doctorID);
        return Double.parseDouble(value);
    }

    // Get Doctor Description
    public String getDoctorDescription(int doctorID) {
        return getColumnValueById(TABLE_DOCTOR, "Description", doctorID);
    }

    // Insert Medicine
    public boolean insertMedicine(byte[] image, String name, double price, String category,
                                  String expiryDate, double rating, String description) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Image", image);
        values.put("Name", name);
        values.put("Price", price);
        values.put("Category", category);
        values.put("ExpiryDate", expiryDate);
        values.put("Rating", rating);
        values.put("Description", description);

        long result = db.insert(TABLE_MEDICINE, null, values);
        return result != -1;
    }

    // Update Medicine
    public boolean updateMedicine(int medicineID, byte[] image, String name, double price, String category,
                                  String expiryDate, double rating, String description){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Image", image);
        values.put("Name", name);
        values.put("Price", price);
        values.put("Category", category);
        values.put("ExpiryDate", expiryDate);
        values.put("Rating", rating);
        values.put("Description", description);

        int result = db.update(TABLE_MEDICINE, values, "ID=?", new String[]{String.valueOf(medicineID)});
        return result > 0;
    }

    // Delete Medicine
    public boolean deleteMedicine(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_MEDICINE, "ID=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public double getMedicinePriceById(int medicineID) {
        String price = getColumnValueById(TABLE_MEDICINE, "Price", medicineID);
        return Double.parseDouble(price);
    }

    public List<String> getAllMedicineNames() {
        List<String> medicineNames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT Name FROM " + TABLE_MEDICINE;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                medicineNames.add(name);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return medicineNames;
    }

    public Cursor getSearchedMedicine(String medicineName){
        String query = "SELECT * FROM " + TABLE_MEDICINE + " WHERE Name LIKE ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, new String[]{medicineName + "%"});
        }
        return cursor;
    }

    // Select All Medicines
    public Cursor getAllMedicines() {
        String query = "SELECT * FROM " + TABLE_MEDICINE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public int getMedicineCount() {
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT COUNT(*) FROM " + TABLE_MEDICINE;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return count;
    }

    // get top 5 medicines
    public List<PopularProductListModel> getTopFiveMedicines() {
        List<PopularProductListModel> medicineList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query for top 5 doctors with highest rating
        String query = "SELECT * FROM " + TABLE_MEDICINE + " ORDER BY Rating DESC LIMIT 5";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int medicineId = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
                Double price = cursor.getDouble(cursor.getColumnIndexOrThrow("Price"));
                double rating = cursor.getDouble(cursor.getColumnIndexOrThrow("Rating"));

                // Convert BLOB to Bitmap
                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow("Image"));
                Bitmap imgDoctor = imageHelper.bytesToBitmap(imageBytes);

                // Create model object
                PopularProductListModel medicine = new PopularProductListModel(
                        medicineId,
                        imgDoctor,
                        name,
                        rating,
                        price
                );

                medicineList.add(medicine);

            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return medicineList;
    }

    // Get Medicine Image
    public byte[] getMedicineImage(int medicineID){
        return getImageByID(TABLE_MEDICINE, medicineID);
    }

    // Get Medicine Name
    public String getMedicineName(int medicineID){
        return getColumnValueById(TABLE_MEDICINE, "Name", medicineID);
    }

    // Get Medicine Price
    public Double getMedicinePrice(int medicineID){
        String price = getColumnValueById(TABLE_MEDICINE, "Price", medicineID);
        return Double.parseDouble(price);
    }

    // Get Medicine Category
    public String getMedicineCategory(int medicineID){
        return getColumnValueById(TABLE_MEDICINE, "Category", medicineID);
    }

    // Get Medicine Expiry Date
    public String getMedicineExpiryDate(int medicineID){
        return getColumnValueById(TABLE_MEDICINE, "ExpiryDate", medicineID);
    }

    // Get Medicine Rating
    public Double getMedicineRating(int medicineID){
        String rating = getColumnValueById(TABLE_MEDICINE, "Rating", medicineID);
        return Double.parseDouble(rating);
    }

    // Get Medicine Description
    public String getMedicineDescription(int medicineID){
        return getColumnValueById(TABLE_MEDICINE, "Description", medicineID);
    }

    // Insert Cart Data
    public boolean insertCart(String name, byte[] image, int medicineID, int userId, int quantity, double price){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Name", name);
        values.put("Image", image);
        values.put("MedicineID", medicineID);
        values.put("UserID", userId);
        values.put("Quantity", quantity);
        values.put("Price", price);

        long result = db.insert(TABLE_CART, null, values);
        return result != -1;
    }

    // Update Cart Data
    public boolean updateCart(int id, int quantity, double price){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Quantity", quantity);
        values.put("Price", price);
        int result = db.update(TABLE_CART, values, "ID=?", new String[]{String.valueOf(id)});
        return result>0;
    }

    // Delete Cart
    public boolean deleteCart(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_CART, "ID=?", new String[]{String.valueOf(id)});
        return rows>0;
    }

    public void deleteAllCart(int UserID){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, "UserID=?", new String[]{String.valueOf(UserID)});
    }

    // Select All Cart Items
    public Cursor getAllCartItems(int userId) {
        String query = "SELECT * FROM " + TABLE_CART + " WHERE UserID = ?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        }
        return cursor;
    }

    // get cart item price
    public double getCartItemPrice(int cartItemId) {
        String price = getColumnValueById(TABLE_CART, "Price", cartItemId);
        return Double.parseDouble(price);
    }

    // get cart item quantity
    public int getCartItemQuantity(int cartItemId) {
        String quantity = getColumnValueById(TABLE_CART, "Quantity", cartItemId);
        return Integer.parseInt(quantity);
    }

    public int getCountByValue(int userId) {
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT COUNT(*) FROM " + TABLE_CART + " WHERE UserID = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return count;
    }

    public boolean isItemInCart(int userId, int medicineId) {
        boolean exists = false;
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT 1 FROM " + TABLE_CART + " WHERE UserID = ? AND MedicineID = ? LIMIT 1";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId), String.valueOf(medicineId)});

        if (cursor != null && cursor.moveToFirst()) {
            exists = true;
        }

        if (cursor != null){
            cursor.close();
        }
        db.close();

        return exists;
    }


    // Insert Data in Medicine Search History
    public boolean insertMedicineSearchHistory(String name, int userId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MedicineName", name);
        values.put("UserID", userId);
        long result = db.insert(TABLE_MEDICINE_SEARCH_HISTORY, null, values);
        return result != -1;
    }

    // Delete Medicine Search history
    public boolean deleteMedicineSearchHistory(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        int row =  db.delete(TABLE_MEDICINE_SEARCH_HISTORY, "ID=?", new String[]{String.valueOf(id)});
        return row>0;
    }

    // Select all from medicine search history
    public Cursor getAllMedicineSearchHistory(int userId){
        String query = "SELECT * FROM " + TABLE_MEDICINE_SEARCH_HISTORY + " WHERE UserID=?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        }
        return cursor;
    }

    // Check is History in Database
    public boolean isMedicineHistoryIn(String medicineHistory, int userId){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT 1 FROM " + TABLE_MEDICINE_SEARCH_HISTORY + " WHERE MedicineName = ? AND UserID = ? LIMIT 1";
        Cursor cursor = db.rawQuery(query, new String[]{medicineHistory, String.valueOf(userId)});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }

    // Insert Data in Doctor Search History
    public boolean insertDoctorSearchHistory(String name, int userId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("DoctorNameSpecification", name);
        values.put("UserID", userId);
        long result = db.insert(TABLE_DOCTOR_SEARCH_HISTORY, null, values);
        return result != -1;
    }

    // Delete Doctor Search history
    public boolean deleteDoctorSearchHistory(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        int row =  db.delete(TABLE_DOCTOR_SEARCH_HISTORY, "ID=?", new String[]{String.valueOf(id)});
        return row>0;
    }

    // Select all from doctor search history
    public Cursor getAllDoctorSearchHistory(int userId){
        String query = "SELECT * FROM " + TABLE_DOCTOR_SEARCH_HISTORY + " WHERE UserID=?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        }
        return cursor;
    }

    // Check is History in Database
    public boolean isDoctorHistoryIn(String doctorHistory){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT 1 FROM " + TABLE_DOCTOR_SEARCH_HISTORY + " WHERE DoctorNameSpecification = ? LIMIT 1";
        Cursor cursor = db.rawQuery(query, new String[]{doctorHistory});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }

    // Insert data on Medicine Table
    public boolean insertMedicineOrder(int userID, int medicineID, double price, int quantity, String orderDate){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("UserID", userID);
        values.put("MedicineID", medicineID);
        values.put("Price", price);
        values.put("Quantity", quantity);
        values.put("OrderDate", orderDate);

        Long result = db.insert(TABLE_MEDICINE_ORDER, null, values);
        return result != -1;
    }

    // Select data from Medicine Order
    public Cursor getOrderByUserID(int userID){
        String query = "SELECT * FROM " + TABLE_MEDICINE_ORDER + " WHERE UserID=?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, new String[]{String.valueOf(userID)});
        }
        return cursor;
    }

    // get all orders
    public Cursor getAllMedicineOrder(){
        String query = "SELECT * FROM " + TABLE_MEDICINE_ORDER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor getAllMedicineOrderByUserId(int userID){
        String query = "SELECT * FROM " + TABLE_MEDICINE_ORDER + " WHERE UserID=?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, new  String[]{String.valueOf(userID)});
        }
        return cursor;
    }

    // delete order
    public boolean deleteMedicineOrder(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        int row = db.delete(TABLE_MEDICINE_ORDER, "ID=?", new String[]{String.valueOf(id)});
        return row>0;
    }

    // get user id
    public int getUserIDbyOrderID(int orderID){
        String userId = getColumnValueById(TABLE_MEDICINE_ORDER, "UserID", orderID);
        return Integer.parseInt(userId);
    }

    // get medicine id
    public int getMedicineIDbyOrderID(int orderID){
        String medicineID = getColumnValueById(TABLE_MEDICINE_ORDER, "MedicineID", orderID);
        return Integer.parseInt(medicineID);
    }

    public double getPricebyOrderID(int orderID){
        String price = getColumnValueById(TABLE_MEDICINE_ORDER, "Price", orderID);
        return Double.parseDouble(price);
    }

    // get quantity of medicine
    public int getQuantityByOrderID(int orderID){
        String quantity = getColumnValueById(TABLE_MEDICINE_ORDER, "Quantity", orderID);
        return Integer.parseInt(quantity);
    }

    // get order date
    public String getOrderDateByOrderID(int orderID){
        return getColumnValueById(TABLE_MEDICINE_ORDER, "OrderDate", orderID);
    }

    // Insert into Notification Table
    public boolean insertNotification(int userId, int medicineId, String title, String message){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("UserID", userId);
        values.put("MedicineID", medicineId);
        values.put("Title", title);
        values.put("Message", message);

        long result = db.insert(TABLE_NOTIFICATION, null, values);
        return result != -1;
    }

    // Select Notification
    public Cursor getNotificationByUserID(int userId){
        String query = "SELECT * FROM " + TABLE_NOTIFICATION + " WHERE UserID = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        }
        return cursor;
    }

    // Delete Notification
    public boolean deleteNotificationById(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        int row = db.delete(TABLE_NOTIFICATION, "ID=?", new String[]{String.valueOf(id)});
        return row > 0;
    }

    // For Sign In Use
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT 1 FROM " + TABLE_USER + " WHERE Email = ? AND Password = ? LIMIT 1";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }

    // Email Verification Check
    public boolean isEmailInDB(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT 1 FROM " + TABLE_USER + " WHERE Email=? LIMIT 1";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Get Data Column wise
    public String getColumnValueById(String TABLE_NAME, String columnName, int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String value = null;

        Cursor cursor = db.rawQuery(
                "SELECT " + columnName + " FROM " + TABLE_NAME + " WHERE ID = ?",
                new String[]{String.valueOf(id)}
        );

        if (cursor != null && cursor.moveToFirst()) {
            value = cursor.getString(0);
            cursor.close();
        }
        db.close();
        return value;
    }

    // Get Image (BLOB) by ID
    public byte[] getImageByID(String TABLE_NAME, int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        byte[] image = null;

        Cursor cursor = db.rawQuery(
                "SELECT Image FROM " + TABLE_NAME + " WHERE ID = ?",
                new String[]{String.valueOf(userId)}
        );

        if (cursor != null && cursor.moveToFirst()) {
            image = cursor.getBlob(0); // first column (Image)
            cursor.close();
        }
        db.close();
        return image;
    }
}

