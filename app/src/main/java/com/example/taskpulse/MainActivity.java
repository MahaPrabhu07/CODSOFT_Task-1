package com.example.taskpulse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TaskRepository taskRepository;
    private TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskRepository = new TaskRepository(this);

        FloatingActionButton buttonAddTask = findViewById(R.id.addTaskButton);
        buttonAddTask.setOnClickListener(v -> openTaskDialog(null));

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        taskAdapter = new TaskAdapter();
        recyclerView.setAdapter(taskAdapter);

        loadTasks();

        taskAdapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Task task) {
                openTaskDialog(task);
            }

            @Override
            public void onCompleteClick(Task task, boolean isCompleted) {
                task.setCompleted(isCompleted);
                taskRepository.update(task);
                loadTasks();
            }

            @Override
            public void onDeleteClick(Task task) {
                taskRepository.delete(task);
                loadTasks();
            }
        });
    }

    private void loadTasks() {
        List<Task> tasks = taskRepository.getAllTasks();
        taskAdapter.setTasks(tasks);
    }

    private void openTaskDialog(Task task) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_task, null);
        dialogBuilder.setView(dialogView);

        EditText editTextTitle = dialogView.findViewById(R.id.etTaskTitle);
        EditText editTextDescription = dialogView.findViewById(R.id.etTaskDescription);
        EditText editTextDueDate = dialogView.findViewById(R.id.etTaskDueDate);
        EditText editTextPriority = dialogView.findViewById(R.id.etTaskPriority);

        String dialogTitle = task == null ? "Add Task" : "Edit Task";
        dialogBuilder.setTitle(dialogTitle);
        dialogBuilder.setPositiveButton(dialogTitle, (dialog, which) -> {
            String title = editTextTitle.getText().toString();
            String description = editTextDescription.getText().toString();
            String dueDate = editTextDueDate.getText().toString();
            String priority = editTextPriority.getText().toString();

            if (title.trim().isEmpty() || description.trim().isEmpty() || dueDate.trim().isEmpty() || priority.trim().isEmpty()) {
                Toast.makeText(MainActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (task == null) {
                Task newTask = new Task(title, description, dueDate, priority);
                taskRepository.insert(newTask);
            } else {
                task.setTitle(title);
                task.setDescription(description);
                task.setDueDate(dueDate);
                task.setPriority(priority);
                taskRepository.update(task);
            }

            loadTasks();
        });
        dialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        if (task != null) {
            editTextTitle.setText(task.getTitle());
            editTextDescription.setText(task.getDescription());
            editTextDueDate.setText(task.getDueDate());
            editTextPriority.setText(task.getPriority());
        }
    }
}
