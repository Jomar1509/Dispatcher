package com.inkafarma.dispatcher.data.fireBase;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.inkafarma.dispatcher.data.model.FirebaseDispatcherDeviceModel;
import com.inkafarma.dispatcher.data.model.MotorizedOrderModel;
import com.inkafarma.dispatcher.presenter.util.Util;

import timber.log.Timber;

import static com.inkafarma.dispatcher.presenter.util.Constants.DATABASE_DISPATCHER;
import static com.inkafarma.dispatcher.presenter.util.DispatcherGlobal.eventListenerDispatcher;
import static com.inkafarma.dispatcher.presenter.util.DispatcherGlobal.dispatcherId;
import static com.inkafarma.dispatcher.presenter.util.DispatcherGlobal.dispatcher;

/**
 * <p>
 * FireBaseListener, contiene la lógica de conección con fireBase,
 * recibe he inyecta inicio de métodos en  @{Link
 * com.inkafarma.dispatcher.presenter.LoginPresenterImpl}.
 * </p>
 */


public class FireBaseListener implements FireBaseInteractor {
    private FirebaseAuth auth;
    private DatabaseReference databaseDispatcherReference;
    private FireBaseLoginView loginView;
    private FirebaseDashBoardView viewDashBoard;
    private FirebaseScanView viewScan;

    public FireBaseListener(FireBaseLoginView view) {
        auth = FirebaseAuth.getInstance();
        this.loginView = view;
        this.viewDashBoard = null;
        this.viewScan = null;
    }

    public FireBaseListener(FirebaseDashBoardView view) {
        auth = FirebaseAuth.getInstance();
        this.viewDashBoard = view;
        this.loginView = null;
        this.viewScan = null;
    }

    public FireBaseListener(FirebaseScanView view) {
        auth = FirebaseAuth.getInstance();
        this.viewDashBoard = null;
        this.loginView = null;
        this.viewScan = view;
    }

    public FireBaseListener() {
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void stopListeners() {
        if (dispatcherId == null)
            return;

        Query getDataDispatcherId = databaseDispatcherReference.child((dispatcherId));

        if (eventListenerDispatcher != null)
            getDataDispatcherId.removeEventListener(eventListenerDispatcher);

        dispatcher = null;
        dispatcherId = null;
    }

    @Override
    public void getDispatcherFromServer(String dispatcherId) {
        if (databaseDispatcherReference == null) {
            databaseDispatcherReference = FirebaseDatabase.getInstance().getReference()
                    .child(DATABASE_DISPATCHER);
        }
        System.out.println("**** dispatcherId --> " + dispatcherId);
        Query getDataDispatcherId = databaseDispatcherReference.child((dispatcherId));

        if (eventListenerDispatcher != null)
            getDataDispatcherId.removeEventListener(eventListenerDispatcher);

        eventListenerDispatcher = getDataDispatcherId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (loginView != null) {
                    loginView.fireBaseDataSnapshotDispatcher(dataSnapshot);
                } else if (viewDashBoard != null) {
                    viewDashBoard.fireBaseDataSnapshotDispatcher(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Timber.d("ERROR RETRIEVING DISPATCHER FROM FIREBASE", databaseError.toString());
            }
        });
    }

    @Override
    public void obtainTokenUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }

        user.getToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (task.isSuccessful()) {
                    if (viewDashBoard != null)
                        viewDashBoard.logoutEndPoint(task.getResult().getToken());

                    return;
                }
            }
        });
    }

    @Override
    public void obtainMotorizedOrder(final String dni) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            viewScan.dismissDialog();
            return;
        }

        user.getToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (task.isSuccessful()) {
                    System.out.println("***** OBTAIN TOKEN *****");
                    if (viewScan != null)
                        viewScan.getDispatcher(task.getResult().getToken(), dni);

                    return;
                }
            }
        });
    }

    @Override
    public void setlogoutPicker() {
        stopListeners();
    }

    @Override
    public void loginFireBase(String user, String pass) {
        auth.signInWithEmailAndPassword(user, pass)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Timber.d("signInWithEmailAndPassword:successful");
                        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (firebaseUser == null) {
                            loginView.onFailureFireBaseToken("No token");
                            return;
                        }

                        firebaseUser.getToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                            @Override
                            public void onComplete(@NonNull Task<GetTokenResult> task) {
                                if (task.isSuccessful()) {
                                    loginView.loginFireBaseOk(task.getResult().getToken(), firebaseUser.getUid());
                                    loginView.userId(firebaseUser.getUid());
                                } else {
                                    loginView.onFailureFireBaseToken("No token");
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Timber.d("signInWithEmailAndPassword:error");
                        if (exception instanceof FirebaseAuthInvalidCredentialsException
                                || exception instanceof FirebaseAuthInvalidUserException) {
                            loginView.onFailureFireBaseToken("No token");
                            return;
                        }
                    }
                });
    }


    public interface FireBaseLoginView {

        void loginFireBaseOk(String token, String uid);

        void onFailureFireBaseToken(String error);

        void userId(String userId);

        void fireBaseDataSnapshotDispatcher(DataSnapshot dataSnapshot);

        void loginFireBaseImeiOk();

        void loginDevice(FirebaseDispatcherDeviceModel firebaseDeviceModel);

    }

    public interface FirebaseDashBoardView {

        void logoutEndPoint(String token);

        void fireBaseDataSnapshotDispatcher(DataSnapshot dataSnapshot);

        void showMessage(boolean exists);
    }

    public interface FirebaseScanView {

        void getDispatcher(String token, String dni);

        void showMessage();

        void dismissDialog();

        void startActivity(MotorizedOrderModel motorizedOrderModel);
    }

}
