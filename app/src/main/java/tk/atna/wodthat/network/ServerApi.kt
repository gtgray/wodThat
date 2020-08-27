package tk.atna.wodthat.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import tk.atna.wodthat.model.*
import tk.atna.wodthat.network.model.*


interface ServerApi {

    @Headers("Content-Type:application/json")
    @GET("api/w/v3/users/{username}/")
    suspend fun getUserProfile(
        @Path("username") username: String
    ): Response<UserProfile>

    @Headers("Content-Type:application/json")
    @GET("api/w/v3/exercises/")
    suspend fun getExercises(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("ordering") ordering: String
    ): Response<ExercisesWrapper>

    @Headers("Content-Type:application/json")
    @GET("api/w/v3/exercises/{exercise_code}/")
    suspend fun getExerciseDetails(
        @Path("exercise_code") exerciseCode: String
    ): Response<ExerciseDetailed>

    @Headers("Content-Type:application/json")
    @GET("api/w/v1/wods")
    suspend fun getWods(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("ordering") ordering: String = "-last_result",
        @Query("units") units: String = "undefined"
    ): Response<WodsWrapper>

    @Headers("Content-Type:application/json")
    @GET("api/w/v1/wods/{wod_code}")
    suspend fun getWodDetails(
        @Path("wod_code") wodCode: String,
        @Query("units") units: String = "undefined"
    ): Response<WodDetailedModel>

    @Headers("Content-Type:application/json")
    @GET("api/w/v3/comments/")
    suspend fun getWodComments(
        @Query("wod") wodId: Int
    ): Response<WodCommentsWrapper>

    @Headers("Content-Type:application/json")
    @GET("api/w/v3/results/")
    suspend fun getWodResults(
        @Query("wod") wodId: Int,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("ordering") ordering: String? = null
    ): Response<WodResultsWrapper>

}