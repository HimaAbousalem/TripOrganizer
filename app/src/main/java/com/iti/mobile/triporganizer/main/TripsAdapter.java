package com.iti.mobile.triporganizer.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Parcel;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.app.TripOrganizerApp;
import com.iti.mobile.triporganizer.appHead.ChatHeadService;
import com.iti.mobile.triporganizer.data.entities.LocationData;
import com.iti.mobile.triporganizer.data.entities.MapperClass;
import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.data.entities.TripAndLocation;
import com.iti.mobile.triporganizer.utils.AlarmUtils;
import com.iti.mobile.triporganizer.utils.Constants;
import com.iti.mobile.triporganizer.utils.DateUtils;
import com.iti.mobile.triporganizer.utils.NotificationsUtils;

import java.text.SimpleDateFormat;
import java.util.Objects;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.iti.mobile.triporganizer.alarm.AlarmService.foregroundId;

public class TripsAdapter extends ListAdapter<TripAndLocation,  RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;
    public static final int TYPE_Text = 2;
    public static  int viewType;

    public TripsAdapter() {
        super(diffCallback);
    }

    private static final DiffUtil.ItemCallback<TripAndLocation> diffCallback = new DiffUtil.ItemCallback<TripAndLocation>() {
        @Override
        public boolean areItemsTheSame(@NonNull TripAndLocation oldItem, @NonNull TripAndLocation newItem) {
            return Objects.equals(oldItem.getTrip().getId(), newItem.getTrip().getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull TripAndLocation oldItem, @NonNull TripAndLocation newItem) {
            return oldItem.getTrip().getStatus().equals(newItem.getTrip().getStatus())&&
                    oldItem.getTrip().getUserId().equals(newItem.getTrip().getUserId())&&
                    oldItem.getLocationDataList().getStartDate().getTime()==newItem.getLocationDataList().getStartDate().getTime()&&
                    oldItem.getTrip().getTripName().equals(newItem.getTrip().getTripName())&&
                    oldItem.getLocationDataList().getId() == newItem.getLocationDataList().getId();
        }
    };

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_HEADER:
                View upcomingView = LayoutInflater.from(parent.getContext()).inflate(R.layout.first_trip_card, parent, false);
                return new UpcomingTripViewHolder(upcomingView);
            case TYPE_ITEM:
                View textView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_card, parent, false);
                return new TripsViewHolder(textView);
            case TYPE_Text:
                View pastTripsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcomig_trips_text_item, parent, false);
                return new UpcomingTripsTextViewHolder(pastTripsView);
            default: return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof  UpcomingTripViewHolder){
            TripAndLocation trip = getItem(position);
            UpcomingTripViewHolder upcomingTripViewHolder = (UpcomingTripViewHolder) holder;
            upcomingTripViewHolder.setTripNameTv(trip.getTrip().getTripName());
            upcomingTripViewHolder.setTripDateTv(DateUtils.simpleDateFormatForYears_Months.format(trip.getLocationDataList().getStartDate()));
            upcomingTripViewHolder.setTripTimeTv(DateUtils.simpleDateFormatForHours_Minutes.format(trip.getLocationDataList().getStartDate()));
            upcomingTripViewHolder.setTripLocTv(trip.getLocationDataList().getStartTripAddressName());

            upcomingTripViewHolder.itemView.setOnClickListener(
                    view -> {
                        HomeFragmentDirections.ActionHomeFragmentToDetailsFragment action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(trip);
                        Navigation.findNavController(view).navigate(action);
                    });

        }else if (holder instanceof  TripsViewHolder){
            TripAndLocation trip = getItem(position);
            TripsViewHolder tripsViewHolder = (TripsViewHolder) holder;
            tripsViewHolder.setTripNameTv(trip.getTrip().getTripName());
            tripsViewHolder.setTripDateTv(DateUtils.simpleDateFormatForYears_MonthsHours_Minutes.format(trip.getLocationDataList().getStartDate()));
            tripsViewHolder.setTripLocTv(trip.getLocationDataList().getStartTripAddressName());
            tripsViewHolder.itemView.setOnClickListener(
                    view -> {
                        HomeFragmentDirections.ActionHomeFragmentToDetailsFragment action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(trip);
                        Navigation.findNavController(view).navigate(action);
                    });
        }else if (holder instanceof  UpcomingTripsTextViewHolder){
            TripAndLocation trip = getItem(position);
            UpcomingTripsTextViewHolder upcomingTripsTextViewHolder = (UpcomingTripsTextViewHolder) holder;
            upcomingTripsTextViewHolder.setTripNameTv(trip.getTrip().getTripName());
            upcomingTripsTextViewHolder.setTripDateTv(DateUtils.simpleDateFormatForYears_MonthsHours_Minutes.format(trip.getLocationDataList().getStartDate()));
            upcomingTripsTextViewHolder.setTripLocTv(trip.getLocationDataList().getStartTripAddressName());
            upcomingTripsTextViewHolder.itemView.setOnClickListener(
                    view -> {
                        HomeFragmentDirections.ActionHomeFragmentToDetailsFragment action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(trip);
                        Navigation.findNavController(view).navigate(action);
                    });
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position){
            case 0:
                return TYPE_HEADER;
            case 1:
                return TYPE_Text;
            case 2:
            default:
                return TYPE_ITEM;
        }

    }

     public class UpcomingTripViewHolder extends RecyclerView.ViewHolder {
        private TextView tripNameTv, tripDateTv, tripTimeTv, tripLocTv;
        private Button startBtn, viewBtn;
        private UpcomingTripViewHolder(@NonNull View itemView) {
            super(itemView);
            tripNameTv = itemView.findViewById(R.id.tripNameTv);
            tripDateTv = itemView.findViewById(R.id.tripDateTv);
            tripTimeTv = itemView.findViewById(R.id.tripTimeTv);
            tripLocTv = itemView.findViewById(R.id.tripLocTv);
            startBtn = itemView.findViewById(R.id.startBtn);
            viewBtn = itemView.findViewById(R.id.viewBtn);
            startBtn.setOnClickListener((view)->{
                AlarmUtils.cancelAlarm(itemView.getContext().getApplicationContext(), getItem(getAdapterPosition()).getTrip());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Settings.canDrawOverlays(itemView.getContext())) {
                        Intent chatHeadIntent = new Intent(itemView.getContext(), ChatHeadService.class);
                        Parcel parcel = Parcel.obtain();
                        Trip tripData= MapperClass.mapTripAndLocationObject(getItem(getAdapterPosition()));
                        tripData.writeToParcel(parcel, 0);
                        parcel.setDataPosition(0);
                        chatHeadIntent.putExtra(Constants.TRIP_INTENT, parcel.marshall());
                        chatHeadIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                        itemView.getContext().startService(chatHeadIntent);
                    }
                }else{
                    Intent chatHeadIntent = new Intent(itemView.getContext(), ChatHeadService.class);
                    Parcel parcel = Parcel.obtain();
                    Trip tripData= MapperClass.mapTripAndLocationObject(getItem(getAdapterPosition()));
                    tripData.writeToParcel(parcel, 0);
                    parcel.setDataPosition(0);
                    chatHeadIntent.putExtra(Constants.TRIP_INTENT, parcel.marshall());
                    chatHeadIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    itemView.getContext().startService(chatHeadIntent);
                }
                if (((TripOrganizerApp)(itemView.getContext().getApplicationContext())).getAlarmService()!=null){
                    ((TripOrganizerApp)(itemView.getContext().getApplicationContext())).stopAlarmService();
                }
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+ getItem(getAdapterPosition()).getLocationDataList().getStartTripEndPointLat() +","+ getItem(getAdapterPosition()).getLocationDataList().getStartTripEndPointLng());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri).setFlags(FLAG_ACTIVITY_NEW_TASK);
                mapIntent.setPackage("com.google.android.apps.maps");
                itemView.getContext().startActivity(mapIntent);


            });
        }


        private void setTripNameTv(String tripName) {
            tripNameTv.setText(tripName);
        }
        private void setTripDateTv(String tripDate) {
            tripDateTv.setText(tripDate);
        }
        private void setTripTimeTv(String time) {
            tripTimeTv.setText(time);
        }
        public void setTripLocTv(String location) {
            tripLocTv.setText(location);
        }
    }
    public class TripsViewHolder extends RecyclerView.ViewHolder{
        private TextView tripNameTv, tripDateTv, tripLocTv;
        private TripsViewHolder(@NonNull View itemView) {
            super(itemView);
            tripNameTv = itemView.findViewById(R.id.tripNameTv);
            tripDateTv = itemView.findViewById(R.id.tripDateTv);
            tripLocTv = itemView.findViewById(R.id.tripLocTv);
          //  itemView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_mainFragment2_to_detailsFragment));
        }

        private void setTripNameTv(String tripName) {
            tripNameTv.setText(tripName);
        }
        private void setTripDateTv(String tripDate) {
            tripDateTv.setText(tripDate);
        }
        private void setTripLocTv(String location) {
            tripLocTv.setText(location);
        }
    }
    public class UpcomingTripsTextViewHolder extends RecyclerView.ViewHolder{
        private TextView upcomig_textView;
        private TextView tripNameTv, tripDateTv, tripLocTv;
        private UpcomingTripsTextViewHolder(@NonNull View itemView) {
            super(itemView);
            upcomig_textView = itemView.findViewById(R.id.upcomig_textView);
            tripNameTv = itemView.findViewById(R.id.tripNameTv);
            tripDateTv = itemView.findViewById(R.id.tripDateTv);
            tripLocTv = itemView.findViewById(R.id.tripLocTv);
           // itemView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_historyFragment));
        }
        private void setTripNameTv(String tripName) {
            tripNameTv.setText(tripName);
        }
        private void setTripDateTv(String tripDate) {
            tripDateTv.setText(tripDate);
        }
        private void setTripLocTv(String location) {
            tripLocTv.setText(location);
        }
    }
}
