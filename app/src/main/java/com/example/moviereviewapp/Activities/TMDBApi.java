package com.example.moviereviewapp.Activities;


import com.example.moviereviewapp.Models.MovieImages;
import com.example.moviereviewapp.Models.MovieKeywordResponse;
import com.example.moviereviewapp.Models.PersonImagesResponse;
import com.example.moviereviewapp.Models.ReviewsResponse;
import com.example.moviereviewapp.Models.SimilarItemsResponse;
import com.example.moviereviewapp.Models.TvShowImages;
import com.example.moviereviewapp.Models.TvShowKeywordResponse;
import com.example.moviereviewapp.Models.VideoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDBApi {
    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/upcoming")
    Call<MovieResponse> getUpcomingMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("trending/movie/day")
    Call<MovieResponse> getTrendingMovies(@Query("api_key") String apiKey);


    @GET("movie/now_playing")
    Call<MovieResponse> getNowPlayingMovies(@Query("api_key") String apiKey
            , @Query("page") int page

    );

    @GET("movie/{movie_id}")
    Call<MovieDetail> getMovieDetail(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey
    );

    @GET("trending/all/day")
    Call<trendingresponse> getTrendingAll(
            @Query("api_key") String apiKey
    );

    @GET("person/popular")
    Call<PersonResoponse> getPerson(
            @Query("api_key") String apiKey
            , @Query("page") int page
    );

    @GET("tv/airing_today")
    Call<tvseriesresponse> getTvSeriesontheair(
            @Query("api_key") String apiKey
            , @Query("page") int page
    );

    @GET("tv/top_rated")
    Call<tvseriesresponse> getTvSeriesTopRated(
            @Query("api_key") String apiKey
            , @Query("page") int page
    );

    @GET("tv/{series_id}")
    Call<tvseriesdetail> getTvSeriesDetail(

            @Path("series_id") int Id,
            @Query("api_key") String apiKey
    );


    @GET("person/{person_id}")
    Call<PersonDetail> getPersonDetail(
            @Path("person_id") String personId,
            @Query("api_key") String apiKey
    );

    @GET("movie/{movie_id}")
    Call<MovieDetail> getMovieDetailWithCreditsAndGenres(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey,
            @Query("append_to_response") String appendToResponse
    );

    @GET("tv/{tv_id}")
    Call<tvseriesdetail> getTvSeriesDetailWithCreditsAndGenres(
            @Path("tv_id") int tvId,
            @Query("api_key") String apiKey,
            @Query("append_to_response") String appendToResponse
    );

    @GET("movie/{movie_id}/images")
    Call<MovieImages> getMovieImages(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    @GET("tv/{tv_id}/images")
    Call<TvShowImages> getTvShowImages(@Path("tv_id") int tvId, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/keywords")
    Call<MovieKeywordResponse> getMovieKeywords(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    @GET("tv/{tv_id}/keywords")
    Call<TvShowKeywordResponse> getTvShowKeywords(@Path("tv_id") int tvId, @Query("api_key") String apiKey);


    @GET("movie/{movie_id}/similar")
    Call<SimilarItemsResponse> getSimilarMovies(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("tv/{tv_id}/similar")
    Call<SimilarItemsResponse> getSimilarTvShows(
            @Path("tv_id") int tvId,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );


    @GET("movie/{movie_id}/videos")
    Call<VideoResponse> getMovieVideos(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("tv/{tv_id}/videos")
    Call<VideoResponse> getTvShowVideos(
            @Path("tv_id") int tvId,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("person/{person_id}")
    Call<PersonDetail> getPersonDetailComplete(
            @Path("person_id") String personId,
            @Query("api_key") String apiKey,
            @Query("append_to_response") String appendToResponse
    );

    @GET("person/{person_id}/images")
    Call<PersonImagesResponse> getPersonImages(
            @Path("person_id") String personId,
            @Query("api_key") String apiKey
    );

    @GET("movie/{movie_id}/reviews")
    Call<ReviewsResponse> getMovieReviews(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("tv/{tv_id}/reviews")
    Call<ReviewsResponse> getTvShowReviews(
            @Path("tv_id") int tvId,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );
}