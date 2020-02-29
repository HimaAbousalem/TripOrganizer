package com.iti.mobile.triporganizer.data.repository.trips;

import com.iti.mobile.triporganizer.data.entities.Note;
import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.data.entities.TripAndLocation;
import com.iti.mobile.triporganizer.data.room.NotesRoom;
import com.iti.mobile.triporganizer.data.room.TripsRoom;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;

public class TripRepositoryRoomImp implements TripsRepository {

    private TripsRoom tripsRoom;

    @Inject
    public TripRepositoryRoomImp(TripsRoom tripsRoom) {
        this.tripsRoom = tripsRoom;
    }

    @Override
    public boolean addTrip(Trip trip) {
        tripsRoom.addTrip(trip);
        return true;
    }

    @Override
    public boolean deleteTrip(Trip trip) {
        tripsRoom.deleteTrip(trip);
        return false;
    }

    @Override
    public boolean updateTrip(Trip trip) {
        tripsRoom.updateTrip(trip);
        return false;
    }

    @Override
    public LiveData<List<TripAndLocation>> getTripsFromRoom(String userId) {
        return tripsRoom.getAllTrips(userId);
    }

    @Override
    public LiveData<List<Trip>> getTripsFromFirebase(String userId) {
        return null;
    }

    @Override
    public void addTripAndNotes(Trip trip, List<Note> notes) {
        tripsRoom.addTripAndNotes(trip, notes);
    }

}