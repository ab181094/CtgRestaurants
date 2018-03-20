package com.csecu.amrit.ctgrestaurants.fragments;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.csecu.amrit.ctgrestaurants.R;
import com.csecu.amrit.ctgrestaurants.activities.HomeActivity;
import com.csecu.amrit.ctgrestaurants.activities.OwnerActivity;
import com.csecu.amrit.ctgrestaurants.asyncTasks.AddFood;
import com.csecu.amrit.ctgrestaurants.asyncTasks.Registration;
import com.csecu.amrit.ctgrestaurants.asyncTasks.UpdateFood;
import com.csecu.amrit.ctgrestaurants.asyncTasks.UpdateRestaurant;
import com.csecu.amrit.ctgrestaurants.controllers.NetworkController;
import com.csecu.amrit.ctgrestaurants.controllers.ToastController;
import com.csecu.amrit.ctgrestaurants.interfaces.AsyncResponse;
import com.csecu.amrit.ctgrestaurants.models.Food;
import com.csecu.amrit.ctgrestaurants.models.ResAndOwner;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Calendar;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment implements AsyncResponse {
    ResAndOwner resAndOwner;
    Food food;
    ImageView imageView;
    private static final int GALLERY_INTENT = 2;
    Uri uri;
    String picture, op, previous;
    NetworkController networkController;
    ToastController toastController;
    Registration registration;
    UpdateRestaurant updateRestaurant;
    private static int TIME_OUT = 2000;
    AddFood addFood;
    UpdateFood updateFood;

    public ImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image, container, false);

        imageView = view.findViewById(R.id.imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mayRequestStorage()) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, GALLERY_INTENT);
                } else {
                    ToastController toastController = new ToastController(getActivity());
                    toastController.errorToast("Set the permissions first");
                }
            }
        });

        registration = new Registration(getActivity());
        registration.delegate = this;

        updateRestaurant = new UpdateRestaurant(getActivity());
        updateRestaurant.delegate = this;

        addFood = new AddFood(getActivity());
        addFood.delegate = this;

        updateFood = new UpdateFood(getActivity());
        updateFood.delegate = this;

        FloatingActionButton fab = view.findViewById(R.id.fab_image);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                networkController = new NetworkController(getActivity());
                toastController = new ToastController(getActivity());

                if (networkController.isNetworkAvailable()) {
                    if (resAndOwner != null) {
                        if (op == null) {
                            if (uri != null) {
                                StorageReference reference = FirebaseStorage.getInstance().getReference();
                                StorageReference imageRef = reference.child("Photos").child(resAndOwner.getImage());
                                imageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        toastController.successToast("Image uploaded successfully");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        toastController.errorToast("Image upload failed");
                                    }
                                });
                            } else {
                                toastController.warningToast("You've not selected a photo");
                            }

                            registration.execute(resAndOwner);
                        } else {
                            if (uri != null) {
                                StorageReference reference = FirebaseStorage.getInstance().getReference();
                                try {
                                    Task<Void> preRef = reference.child("Photos").child(previous).delete();
                                } catch (Exception e) {
                                    toastController = new ToastController(getActivity());
                                    toastController.errorToast(e.toString());
                                }
                                StorageReference imageRef = reference.child("Photos").child(resAndOwner.getImage());
                                imageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        toastController.successToast("Image uploaded successfully");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        toastController.errorToast("Image upload failed");
                                    }
                                });
                            } else {
                                toastController.warningToast("You've not selected a photo");
                            }

                            updateRestaurant.execute(resAndOwner);
                        }
                    } else {
                        if (op == null) {
                            if (uri != null) {
                                StorageReference reference = FirebaseStorage.getInstance().getReference();
                                StorageReference imageRef = reference.child("Foods").child(food.getImage());
                                imageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        toastController.successToast("Image uploaded successfully");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        toastController.errorToast("Image upload failed");
                                    }
                                });
                            } else {
                                toastController.warningToast("You've not selected a photo");
                            }

                            addFood.execute(food);
                        } else {
                            if (uri != null) {
                                StorageReference reference = FirebaseStorage.getInstance().getReference();
                                try {
                                    Task<Void> preRef = reference.child("Foods").child(previous).delete();
                                } catch (Exception e) {
                                    toastController = new ToastController(getActivity());
                                    toastController.errorToast(e.toString());
                                }
                                StorageReference imageRef = reference.child("Foods").child(food.getImage());
                                imageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        toastController.successToast("Image uploaded successfully");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        toastController.errorToast("Image upload failed");
                                    }
                                });
                            } else {
                                toastController.warningToast("You've not selected a photo");
                            }
                            updateFood.execute(food);
                        }
                    }
                } else {
                    toastController.errorToast("Check your internet connection");
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            if (uri != null) {
                imageView.setImageURI(uri);
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = null;
                try {
                    cursor = getActivity().getContentResolver().query(uri,
                            filePathColumn, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        File file = new File(picturePath);
                        String filePath = file.getName();

                        if (resAndOwner != null) {
                            Calendar calendar = Calendar.getInstance();
                            String name = String.valueOf(calendar.getTimeInMillis());
                            picture = name + filePath.substring(filePath.lastIndexOf("."));
                            resAndOwner.setImage(picture);
                        } else {
                            Calendar calendar = Calendar.getInstance();
                            String name = String.valueOf(calendar.getTimeInMillis());
                            picture = name + filePath.substring(filePath.lastIndexOf("."));
                            food.setImage(picture);
                        }
                    }
                } catch (Exception e) {
                    ToastController toastController = new ToastController(getActivity());
                    toastController.errorToast("" + e.toString());
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        }
    }

    private boolean mayRequestStorage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (getActivity().checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
            Snackbar.make(imageView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, 1);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, 1);
        }
        return false;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            resAndOwner = bundle.getParcelable("ResAndOwner");
            food = bundle.getParcelable("food");
            op = bundle.getString("op");

            if (resAndOwner != null) {
                previous = resAndOwner.getImage();
            } else if (food != null) {
                previous = food.getImage();
            }
        }
    }

    @Override
    public void processFinish(Object output) {
        if (output != null) {
            SharedPreferences sharedpreferences = getActivity().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            Boolean auth = sharedpreferences.getBoolean(getString(R.string.auth), false);
            if (auth) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().recreate();
                    }
                }, TIME_OUT);
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }, TIME_OUT);
            }
        }
    }
}
