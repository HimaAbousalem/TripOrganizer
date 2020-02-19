package com.iti.mobile.triporganizer.data.repository.auth;

import androidx.lifecycle.LiveData;

import com.iti.mobile.triporganizer.data.firebase.AuthenticationFirebase;

public class AuthenticationRepositoryImp implements AuthenticationRepository{
    AuthenticationFirebase authenticationFirebase;

    public AuthenticationRepositoryImp() {
        authenticationFirebase=AuthenticationFirebase.getInstance();
    }

    @Override
    public LiveData<String> signInWithEmailAndPasswordFunc(String email, String password) {
        return authenticationFirebase.signInWithEmailAndPasswordFunc(email,password);
    }

    @Override
    public LiveData<String> getCurrentUserId() {
        return authenticationFirebase.getCurrentUserId();
    }
}
