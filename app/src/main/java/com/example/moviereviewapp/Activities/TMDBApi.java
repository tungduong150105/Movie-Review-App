package com.example.moviereviewapp.Activities;


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




}