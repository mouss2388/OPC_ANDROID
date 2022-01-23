package com.example.mareu.controllers.activity;


import static com.example.mareu.controllers.activity.AddMeetingActivity.convertMillisToStr;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mareu.R;
import com.example.mareu.model.Meeting;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.ViewHolder> {

    private final ArrayList<Meeting> mMeetings;

    public MeetingAdapter(ArrayList<Meeting> meetings) {
        this.mMeetings = meetings;
        Log.i("MeetingAdapter size", String.valueOf(mMeetings.size()));

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meeting, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.displayMail(mMeetings.get(position));
    }

    @Override
    public int getItemCount() {
        return mMeetings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView circle;
        public TextView meeting_info;
        public TextView list_emails;
        public ImageView trash;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circle = itemView.findViewById(R.id.circle);
            meeting_info = itemView.findViewById(R.id.meeting_info);
            list_emails = itemView.findViewById(R.id.list_emails);
            trash = itemView.findViewById(R.id.trash);
        }

        public void displayMail(Meeting meeting) {
            Log.i("MeetingAdapter getSubj", meeting.getSubject());
            Log.i("MeetingAdapter getColor", String.valueOf(meeting.getColor()));
            circle.setColorFilter(meeting.getColor());
            meeting_info.setText(getHeaderInfo(meeting));
            list_emails.setText(getEmailList(meeting.getEmails()));
            trash.setBackgroundResource(R.drawable.baseline_delete_24);
        }

        private String getHeaderInfo(Meeting meeting) {
            long milliHourly = meeting.getHour().getTime();

            String hourly = convertMillisToStr(milliHourly);

            return meeting.getSubject().toUpperCase().charAt(0) +
                    meeting.getSubject().substring(1) +
                    " - " +
                    hourly +
                    " - " +
                    meeting.getRoom();
        }

        private String getEmailList(ArrayList<String> emailList) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return emailList.stream().map(Objects::toString).collect(Collectors.joining(", "));
            } else {
                StringBuilder strListEmail = new StringBuilder();
                for (int i = 0; i < emailList.size(); i++) {
                    strListEmail.append(emailList.get(i));
                    if (i < emailList.size() - 1)
                        strListEmail.append(", ");
                }
                return strListEmail.toString();
            }
        }

    }
}
