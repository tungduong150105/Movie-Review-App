package com.example.moviereviewapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.moviereviewapp.Adapters.PhotoAdapter;
import com.example.moviereviewapp.Adapters.SimilarItemsAdapter;
import com.example.moviereviewapp.Models.Image;
import com.example.moviereviewapp.Models.PersonImagesResponse;
import com.example.moviereviewapp.Models.PhotoItem;
import com.example.moviereviewapp.Models.SimilarItem;
import com.example.moviereviewapp.R;
import com.example.moviereviewapp.databinding.ActivityPersonDetailBinding;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonDetailActivity extends AppCompatActivity {
    private static final String TAG = "PersonDetailActivity";
    private static final String TMDB_API_KEY = "75d6190f47f7d58c6d0511ca393d2f7d";
    private static final String TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    private ActivityPersonDetailBinding binding;
    private String personId;
    private SimilarItemsAdapter filmographyAdapter;
    private PhotoAdapter photoAdapter;
    private List<Call<?>> apiCalls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPersonDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        findViewById(R.id.back_PersonDetail_Btn).setOnClickListener(v -> finish());
        if (!parseIntentExtras()) {
            showErrorAndFinish("Could not load details. Essential information missing.");
            return;
        }

        setupRecyclerViews();
        fetchPersonDetails();
        setupClickListeners();
    }

    private boolean parseIntentExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            personId = extras.getString("personId");
            return personId != null;
        }
        return false;
    }

    private void setupRecyclerViews() {
        filmographyAdapter = new SimilarItemsAdapter(this, new ArrayList<>(), item -> {
            Intent intent = new Intent(PersonDetailActivity.this, TitleDetailActivity.class);
            intent.putExtra("itemId", item.getId());
            // Determine if it's a movie or TV show
            String itemType = item.getTitle() != null ? "movie" : "tv";
            intent.putExtra("itemType", itemType);
            startActivity(intent);
        }, true);

        binding.filmographyRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.filmographyRecyclerView.setAdapter(filmographyAdapter);

        photoAdapter = new PhotoAdapter(new ArrayList<>(), this::showPhotoOverlay);
        binding.photosRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.photosRecyclerView.setAdapter(photoAdapter);
    }

    private void setupClickListeners() {
        binding.addToFavoritesButton.setOnClickListener(v -> {
            Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
        });

        binding.seeAllFilmographyTextView.setOnClickListener(v -> {
            if (filmographyAdapter.getItemCount() > 0) {
                Intent intent = new Intent(PersonDetailActivity.this, SeeAllActivity.class);
                intent.putExtra("type", "similaritem");
                intent.putExtra("title", "Filmography");
                intent.putExtra("movieList", (Serializable) filmographyAdapter.getItems());
                startActivity(intent);
            }
        });

        binding.seeAllPhotosTextView.setOnClickListener(v -> {
            List<PhotoItem> photos = photoAdapter.getPhotoList();
            if (photos != null && !photos.isEmpty()) {
                Intent intent = new Intent(PersonDetailActivity.this, SeeAllActivity.class);
                intent.putExtra("photoList", (Serializable) photos);
                intent.putExtra("title", "Photos");
                intent.putExtra("type", "photos");
                startActivity(intent);
            }
        });
        binding.closePhotoOverlayButton.setOnClickListener(v -> hidePhotoOverlay());
    }
    private void showPhotoOverlay(String photoUrl) {
        if (TextUtils.isEmpty(photoUrl)) {
            Toast.makeText(this, "Photo not available for overlay.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Make overlay visible
        binding.photoOverlayContainer.setVisibility(View.VISIBLE);

        // Load the photo into the overlay ImageView
        Glide.with(this)
                .load(photoUrl)
                .into(binding.overlayPhotoView);
    }

    private void hidePhotoOverlay() {
        binding.photoOverlayContainer.setVisibility(View.GONE);
    }

    private void fetchPersonDetails() {
        TMDBApi api = RetrofitClient.getApiService();

        // Fetch person details with combined credits and images
        Call<PersonDetail> detailCall = api.getPersonDetailComplete(
                personId,
                TMDB_API_KEY,
                "combined_credits,images");
        apiCalls.add(detailCall);
        detailCall.enqueue(new Callback<PersonDetail>() {
            @Override
            public void onResponse(@NonNull Call<PersonDetail> call, @NonNull Response<PersonDetail> response) {
                apiCalls.remove(call);
                if (response.isSuccessful() && response.body() != null) {
                    PersonDetail person = response.body();
                    updateUIWithPersonDetails(person);

                    // Process combined credits
                    if (person.getCombinedCredits() != null && person.getCombinedCredits().getCast() != null) {
                        updateFilmography(person.getCombinedCredits().getCast());
                    }

                    // Process images
                    if (person.getImages() != null && person.getImages().getProfiles() != null) {
                        updatePhotos(person.getImages().getProfiles());
                    }
                } else {
                    Log.e(TAG, "Failed to fetch person details: " + response.code());
                    showError("Failed to load person details");
                }
            }

            @Override
            public void onFailure(@NonNull Call<PersonDetail> call, @NonNull Throwable t) {
                apiCalls.remove(call);
                Log.e(TAG, "Error fetching person details", t);
                showError("Error loading person details");
            }
        });

        // Fetch person images if not included in the append_to_response
        Call<PersonImagesResponse> imagesCall = api.getPersonImages(personId, TMDB_API_KEY);
        apiCalls.add(imagesCall);
        imagesCall.enqueue(new Callback<PersonImagesResponse>() {
            @Override
            public void onResponse(@NonNull Call<PersonImagesResponse> call, @NonNull Response<PersonImagesResponse> response) {
                apiCalls.remove(call);
                if (response.isSuccessful() && response.body() != null) {
                    updatePhotos(response.body().getProfiles());
                } else {
                    Log.e(TAG, "Failed to fetch person images: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<PersonImagesResponse> call, @NonNull Throwable t) {
                apiCalls.remove(call);
                Log.e(TAG, "Error fetching person images", t);
            }
        });
    }

    private void updateUIWithPersonDetails(PersonDetail person) {
        binding.textViewTitlePersonDetail.setText(person.getName());
        // Basic information
        binding.personNameTextView.setText(person.getName());
        binding.personProfessionTextView.setText(person.getKnownForDepartment());

        // Biography summary
        if (!TextUtils.isEmpty(person.getBiography())) {
            binding.personBioTextView.setText(person.getBiography());
            binding.fullBiographyTextView.setText(person.getBiography());
            setViewVisibility(binding.biographySection, true);
        } else {
            binding.personBioTextView.setText("No biography available");
            binding.fullBiographyTextView.setText("No biography available");
            setViewVisibility(binding.biographySection, false);
        }

        // Profile image
        if (person.getProfilePath() != null) {
            Glide.with(this)
                    .load(TMDB_IMAGE_BASE_URL + person.getProfilePath())
                    .placeholder(R.drawable.rounded_image_background)
                    .error(R.drawable.rounded_image_background)
                    .into(binding.personImageView);
        }

        // Birth info
        if (person.getBirthday() != null) {
            // Format birthday for display
            String formattedBirthday = formatDate(person.getBirthday());
            binding.birthDateTextView.setText(formattedBirthday);

            // Set up born info below bio
            String bornInfo = "Born: " + formattedBirthday;
            if (person.getPlaceOfBirth() != null) {
                bornInfo += " in " + person.getPlaceOfBirth();
            }
            binding.bornInfoTextView.setText(bornInfo);

            setViewVisibility(binding.birthLabelTextView, true);
            setViewVisibility(binding.birthDateTextView, true);
            setViewVisibility(binding.bornInfoTextView, true);
        } else {
            setViewVisibility(binding.birthLabelTextView, false);
            setViewVisibility(binding.birthDateTextView, false);
            setViewVisibility(binding.bornInfoTextView, false);
        }

        if (person.getPlaceOfBirth() != null) {
            binding.birthPlaceTextView.setText(person.getPlaceOfBirth());
            setViewVisibility(binding.birthPlaceTextView, true);
        } else {
            setViewVisibility(binding.birthPlaceTextView, false);
        }

        // Death info
        if (person.getDeathday() != null) {
            // Format deathday for display
            String formattedDeathday = formatDate(person.getDeathday());

            // Calculate age at death if birthdate is available
            String ageAtDeath = "";
            if (person.getBirthday() != null) {
                int age = calculateAge(person.getBirthday(), person.getDeathday());
                ageAtDeath = " (age " + age + ")";
            }

            binding.deathDateTextView.setText(formattedDeathday + ageAtDeath);

            // Set up died info below bio
            binding.diedInfoTextView.setText("Died: " + formattedDeathday + ageAtDeath);

            setViewVisibility(binding.deathLabelTextView, true);
            setViewVisibility(binding.deathDateTextView, true);
            setViewVisibility(binding.diedInfoTextView, true);
        } else {
            setViewVisibility(binding.deathLabelTextView, false);
            setViewVisibility(binding.deathDateTextView, false);
            setViewVisibility(binding.diedInfoTextView, false);
        }
    }

    private void updateFilmography(List<SimilarItem> credits) {
        if (credits != null && !credits.isEmpty()) {
            filmographyAdapter.updateData(credits);
            setViewVisibility(binding.filmographyRecyclerView, true);
            setViewVisibility(binding.knownForTextView, true);
            binding.filmographyTextView.setVisibility(View.VISIBLE);
        } else {
            setViewVisibility(binding.filmographyRecyclerView, false);
            setViewVisibility(binding.knownForTextView, false);
            binding.filmographyTextView.setVisibility(View.GONE);
        }
    }

    private void updatePhotos(List<Image> images) {
        if (images != null && !images.isEmpty()) {
            List<PhotoItem> photoItems = new ArrayList<>();
            for (Image image : images) {
                if (image.getFilePath() != null) {
                    photoItems.add(new PhotoItem(TMDB_IMAGE_BASE_URL + image.getFilePath(), image.getAspectRatio()));
                }
            }

            photoAdapter.setPhotoList(photoItems);

            // Update photos count
            if (binding.photosCountTextView != null) {
                binding.photosCountTextView.setText(String.valueOf(images.size()));
            }

            setViewVisibility(binding.photosRecyclerView, true);
            setViewVisibility(binding.photosTextView, true);
            setViewVisibility(binding.seeAllPhotosTextView, images.size() > 5);
        } else {
            setViewVisibility(binding.photosRecyclerView, false);
            setViewVisibility(binding.photosTextView, false);
            setViewVisibility(binding.seeAllPhotosTextView, false);
        }
    }
    private String formatDate(String dateString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
            Date date = inputFormat.parse(dateString);
            return date != null ? outputFormat.format(date) : dateString;
        } catch (ParseException e) {
            return dateString;
        }
    }

    // Helper method to calculate age between two dates
    private int calculateAge(String birthDateString, String deathDateString) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date birthDate = format.parse(birthDateString);
            Date deathDate = format.parse(deathDateString);

            if (birthDate != null && deathDate != null) {
                Calendar birthCal = Calendar.getInstance();
                Calendar deathCal = Calendar.getInstance();
                birthCal.setTime(birthDate);
                deathCal.setTime(deathDate);

                int age = deathCal.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR);
                if (deathCal.get(Calendar.DAY_OF_YEAR) < birthCal.get(Calendar.DAY_OF_YEAR)) {
                    age--;
                }
                return age;
            }
        } catch (ParseException e) {
            Log.e(TAG, "Error calculating age", e);
        }
        return 0;
    }

    private void setViewVisibility(View view, boolean visible) {
        if (view != null) {
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void showErrorAndFinish(String message) {
        showError(message);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (Call<?> call : apiCalls) {
            if (call != null && !call.isCanceled()) {
                call.cancel();
            }
        }
        apiCalls.clear();
    }
}