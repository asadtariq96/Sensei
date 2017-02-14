package com.sensei.Activities.Settings;

import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.sensei.Application.Constants;
import com.sensei.DataModelClasses.SemesterDataModel;
import com.sensei.R;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class SemesterAdapter extends RecyclerView.Adapter<SemesterAdapter.ViewHolder> {

    interface SemesterCallback {

        void onItemClicked(int itemIndex);
    }


    private SemesterCallback semesterCallback;
    List<SemesterDataModel> SemesterList = new ArrayList<>();
    Map<String, SemesterDataModel> SemesterMap = new ArrayMap<>();

    public int selectedPosition;


    SemesterAdapter(List<SemesterDataModel> SemesterList, Map<String, SemesterDataModel> SemesterMap) {
        this.SemesterList = SemesterList;
        this.SemesterMap = SemesterMap;
        selectedPosition = SemesterList.indexOf(SemesterMap.get(Constants.SELECTED_SEMESTER));

    }

    void setCallbacks(SemesterCallback semesterCallback) {
        this.semesterCallback = semesterCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.change_semester_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.checkBox.setOnCheckedChangeListener(null);

        holder.semesterName.setText(SemesterList.get(position).getSemesterName());
        holder.startDate.setText(new LocalDate().parse(SemesterList.get(position).getStartDate()).toString("EEE, dd MMM yyyy"));
        holder.checkBox.setChecked(selectedPosition == position);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                selectedPosition = position;
                notifyDataSetChanged();
            }
        });


    }


    @Override
    public int getItemCount() {
        return SemesterList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView semesterName;
        TextView startDate;
        CheckBox checkBox;

        ViewHolder(View itemView) {
            super(itemView);
            semesterName = (TextView) itemView.findViewById(R.id.semester_name);
            startDate = (TextView) itemView.findViewById(R.id.semester_start_date);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            semesterCallback.onItemClicked(getAdapterPosition());
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    checkBox.setChecked(true);

                }
            }, 250);


        }
    }
}