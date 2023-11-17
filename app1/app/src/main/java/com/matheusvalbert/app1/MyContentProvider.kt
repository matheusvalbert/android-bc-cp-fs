package com.matheusvalbert.app1

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri

class MyContentProvider : ContentProvider() {

    private lateinit var db: SQLiteDatabase

    companion object {
        private const val AUTHORITY = "com.matheusvalbert.app1.mycp"
        private const val DB_NAME = "test_db"
        private const val DB_TABLE = "test_table"
        private const val DB_VER = 1
        private val CONTENT_URI = Uri.parse("content://$AUTHORITY/$DB_TABLE")
    }

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITY, DB_TABLE, 1)
        addURI(AUTHORITY, "$DB_TABLE/#", 2)
    }

    private class MyCpDatabase(context: Context) :
        SQLiteOpenHelper(context, DB_NAME, null, DB_VER) {


        override fun onCreate(sql: SQLiteDatabase) {
            sql.execSQL("CREATE TABLE $DB_TABLE (_id integer primary key autoincrement, title text, description text)")
        }

        override fun onUpgrade(sql: SQLiteDatabase, p1: Int, p2: Int) {
            sql.execSQL("DROP TABLE IF EXISTS $DB_TABLE")
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return db.delete(DB_TABLE, "_id=?", selectionArgs)
    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                "at the given URI"
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val row = db.insert(DB_TABLE, null, values)

        var newUri: Uri? = null

        if (row > 0) {
            newUri = ContentUris.withAppendedId(CONTENT_URI, row)
            context?.contentResolver?.notifyChange(uri, null)
        }

        return newUri
    }

    override fun onCreate(): Boolean {
        val dbHelper = MyCpDatabase(context!!)
        db = dbHelper.readableDatabase
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor {
        val qb = SQLiteQueryBuilder().apply {
            tables = DB_TABLE
        }

        val cr = qb.query(db, null, null, null, null, null, "_id").apply {
            setNotificationUri(context?.contentResolver, uri)
        }

        return cr
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return db.update(DB_TABLE, values, "_id=?", selectionArgs)
    }
}