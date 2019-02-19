package com.example.barea.notelist;


import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.barea.notelist.Model.Note;
import com.example.barea.notelist.Model.User;
import com.example.barea.notelist.Network.ApiClint;
import com.example.barea.notelist.Network.ApiService;
import com.example.barea.notelist.Utils.PrefUtils;
import com.example.barea.notelist.view.NoteAdapter;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ManageNote extends Application{

    public ApiService apiService;
    public CompositeDisposable compositeDisposable = new CompositeDisposable();
    List<Note> notes = new ArrayList<>();
    private NoteAdapter noteAdapter ;

    public ManageNote(){}

    public ManageNote(Context context){
        apiService = ApiClint.getClient(context).create(ApiService.class);

    }

    public List<Note> FechAllNote(final Context context){
        compositeDisposable.add(
                apiService.fetchAllNotes()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<List<Note>, List<Note>>() {
                            @Override
                            public List<Note> apply(List<Note> lnotes) throws Exception {
                                // TODO - note about sort
                                Collections.sort(lnotes, new Comparator<Note>() {
                                    @Override
                                    public int compare(Note n1, Note n2) {
                                        return n2.getId() - n1.getId();
                                    }
                                });
                                return lnotes;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<List<Note>>() {
                            @Override
                            public void onSuccess(List<Note> lnotes) {

                                notes.clear();
                                notes.addAll(lnotes);
                                notes = lnotes;
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(context,
                                      e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }));
return notes;

    }

    public void CreateUser(final Context context) {
        String uniqueId = UUID.randomUUID().toString();
        compositeDisposable.add(
                apiService.register(uniqueId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<User>() {
                            @Override
                            public void onSuccess(User user) {
                                PrefUtils.storeApiKey(context, user.getApiKey());
                                Toast.makeText(context,
                                        "Device is registered successfully! ApiKey: " + PrefUtils.getApiKey(context),
                                        Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onError(Throwable e) {

                                Toast.makeText(context,
                                        e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                                  }
                        })
        );
    }

    public void CreateNote(String note){
        compositeDisposable.add(
                apiService.createNote(note)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Note>() {

                            @Override
                            public void onSuccess(Note note) {

                                notes.add(0,note);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        }));
    }

    public void UpdateNote(int id, String note){
        compositeDisposable.add(
                apiService.updateNote(id, note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                })
        );
    }

    public void DeleteNote(int id){
        compositeDisposable.add(
                apiService.deleteNote(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                })
        );
    }
    }
