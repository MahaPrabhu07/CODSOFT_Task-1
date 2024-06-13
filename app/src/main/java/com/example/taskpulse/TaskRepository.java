package com.example.taskpulse;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {

    private SQLiteDatabase database;

    public TaskRepository(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public void insert(Task task) {
        ContentValues values = new ContentValues();
        values.put("title", task.getTitle());
        values.put("description", task.getDescription());
        values.put("dueDate", task.getDueDate());
        values.put("priority", task.getPriority());
        values.put("isCompleted", task.isCompleted() ? 1 : 0);
        database.insert("tasks", null, values);
    }

    public void update(Task task) {
        ContentValues values = new ContentValues();
        values.put("title", task.getTitle());
        values.put("description", task.getDescription());
        values.put("dueDate", task.getDueDate());
        values.put("priority", task.getPriority());
        values.put("isCompleted", task.isCompleted() ? 1 : 0);
        database.update("tasks", values, "id = ?", new String[]{String.valueOf(task.getId())});
    }

    public void delete(Task task) {
        database.delete("tasks", "id = ?", new String[]{String.valueOf(task.getId())});
    }

    @SuppressLint("Range")
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        Cursor cursor = database.query("tasks", null, null, null, null, null, "id DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Task task = new Task(
                        cursor.getString(cursor.getColumnIndex("title")),
                        cursor.getString(cursor.getColumnIndex("description")),
                        cursor.getString(cursor.getColumnIndex("dueDate")),
                        cursor.getString(cursor.getColumnIndex("priority")));
                task.setId(cursor.getInt(cursor.getColumnIndex("id")));
                task.setCompleted(cursor.getInt(cursor.getColumnIndex("isCompleted")) == 1);
                tasks.add(task);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return tasks;
    }
}



