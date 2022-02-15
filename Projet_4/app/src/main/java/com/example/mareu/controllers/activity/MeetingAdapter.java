package com.example.mareu.controllers.activity;


import static com.example.mareu.controllers.activity.MainActivity.updateRecyclerView;
import static com.example.mareu.utlis.CustomTimePicker.convertMillisToStr;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mareu.DI.DI;
import com.example.mareu.R;
import com.example.mareu.model.Meeting;
import com.example.mareu.service.MeetingApiService;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.ViewHolder> {

    private final ArrayList<Meeting> meetings;
    private static final MeetingApiService meetingApiService = DI.getMeetingApiService();

    public MeetingAdapter(ArrayList<Meeting> meetings) {
        this.meetings = meetings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meeting, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.displayMeeting(meetings.get(position));
    }

    @Override
    public int getItemCount() {
        return meetings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView circle;
        public TextView meeting_header_info;
        public TextView list_emails;
        public ImageView trash;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circle = itemView.findViewById(R.id.circle);
            meeting_header_info = itemView.findViewById(R.id.meeting_header_info);
            list_emails = itemView.findViewById(R.id.list_emails);
            trash = itemView.findViewById(R.id.trash);
        }

        public void displayMeeting(Meeting meeting) {

            circle.setColorFilter(meeting.getColor());
            meeting_header_info.setText(getHeaderInfo(meeting));
            list_emails.setText(getEmailList(meeting.getEmails()));
            trash.setBackgroundResource(R.drawable.baseline_delete_24);

            trash.setOnClickListener(v -> {
                deleteMeeting(meeting);
                Toast.makeText(v.getContext(), meeting.getSubject() + " Supprimée",
                        Toast.LENGTH_SHORT).show();

            });
        }

        private void deleteMeeting(Meeting meeting) {
            meetingApiService.deleteMeeting(meeting);
            updateRecyclerView();
        }

        private String getHeaderInfo(Meeting meeting) {

            long milliHour = meeting.getHour().getTime();

            String hour = convertMillisToStr(milliHour);

            return meeting.getSubject().toUpperCase().charAt(0) +
                    meeting.getSubject().substring(1) +
                    " - " +
                    hour +
                    " - " +
                    meeting.getRoom();
        }

        private String getEmailList(ArrayList<String> emailList) {

            StringBuilder strListEmail = new StringBuilder();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return emailList.stream().map(Objects::toString).collect(Collectors.joining(", "));
            } else {
                for (int index = 0; index < emailList.size(); index++) {
                    strListEmail.append(emailList.get(index));
                    if (index < emailList.size() - 1) {
                        strListEmail.append(", ");
                    }
                }
                return strListEmail.toString();
            }
        }

    }
}
