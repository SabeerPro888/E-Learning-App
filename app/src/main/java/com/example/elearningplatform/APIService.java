package com.example.elearningplatform;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface APIService {

    @GET("ELearning/api/Index/Login")
    Call<String> Login(
            @Query("Email") String email,
            @Query("Password") String password
    );

    @GET("ELearning/api/Index/TeacherCourses")
    Call<List<Courses>> getTeacherCourses(
            @Query("Email") String email
    );


    @POST("ELearning/api/Index/SubmitQuiz")
    Call<String> submitQuiz(
            @Query("QuizTitle") String quizTitle,
            @Query("Weekno") String weekno,
            @Query("TotalQuestions") int totalQuestions,
            @Query("questions") List<Quiz_Question> questions,
            @Query("cid") int cid


            );


    @POST("ELearning/api/Index/SubmitQuiz")
    Call<String> submitQuiz1(
            @Body Quiz_New quizData
    );


    @GET("ELearning/api/Index/getNotes")
    Call<List<Notes>> getNotes(
            @Query("cid") int cid,
            @Query("week") String week

    );

    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);

    @GET("ELearning/api/Index/getVideos")
    Call<List<Video>> getVideos(
            @Query("cid") int cid,
            @Query("week") String week

    );

    @GET("ELearning/api/Index/viewAllCourses")
    Call<List<Courses>> viewAllCourses();


    @GET("ELearning/api/Index/viewAllQuizes")
    Call<List<Quiz_New>> getQuizzes(@Query("cid") int cid, @Query("weekno") String weekno);


    @GET("ELearning/api/Index/viewQuizById")
    Call<Quiz_New> viewQuizById(@Query("qid") int qid);


    @POST("ELearning/api/Index/SubmitQuizStudent")
    Call<Void> SubmitQuizStudent(
            @Query("qid") int qid,
            @Query("totalmarks") int totalmarks,
            @Query("obmarks") int obmarks,
            @Query("email") String email




            );


    @POST("ELearning/api/Index/EnrollInCourse")
    Call<String> EnrollInCourse(
            @Query("email") String email,
            @Query("cid") int cid
    );

    @POST("ELearning/api/Index/checkEnrollment")
    Call<String> checkEnrollment(
            @Query("email") String email,
            @Query("cid") int cid
    );


    @GET("ELearning/api/Index/CheckReadContent")
    Call<String> CheckReadContent(
            @Query("contentId") int contentId,
            @Query("email") String email
    );

    @POST("ELearning/api/Index/ReadContent")
    Call<String> ReadContent(
            @Query("contentId") int contentId,
            @Query("email") String email
    );


    @GET("ELearning/api/Index/CheckIfAllContentRead")
    Call<String> CheckIfAllContentRead(
            @Query("cid") int cid,
            @Query("week") String week,
            @Query("email") String email

    );



}
