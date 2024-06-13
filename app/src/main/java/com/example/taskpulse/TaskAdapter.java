package com.example.taskpulse;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {
    private List<Task> tasks = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Task currentTask = tasks.get(position);
        holder.textViewTitle.setText(currentTask.getTitle());
        holder.textViewDescription.setText(currentTask.getDescription());
        holder.textViewDueDate.setText(currentTask.getDueDate());
        holder.textViewPriority.setText(currentTask.getPriority());
        holder.checkboxComplete.setChecked(currentTask.isCompleted());

        holder.buttonEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(currentTask);
            }
        });
        holder.checkboxComplete.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) {
                listener.onCompleteClick(currentTask, isChecked);
            }
        });
        holder.buttonDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(currentTask);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onEditClick(Task task);
        void onCompleteClick(Task task, boolean isCompleted);
        void onDeleteClick(Task task);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    class TaskHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewDueDate;
        private TextView textViewPriority;
        private Button buttonEdit;
        private CheckBox checkboxComplete;
        private Button buttonDelete;

        public TaskHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.taskTitle);
            textViewDescription = itemView.findViewById(R.id.taskDescription);
            textViewDueDate = itemView.findViewById(R.id.taskDueDate);
            textViewPriority = itemView.findViewById(R.id.taskPriority);
            buttonEdit = itemView.findViewById(R.id.btnEdit);
            checkboxComplete = itemView.findViewById(R.id.checkboxComplete);
            buttonDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}



