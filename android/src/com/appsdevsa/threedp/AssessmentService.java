package com.appsdevsa.threedp;

import com.appsdevsa.threedp.models.Model;

import java.util.List;
import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AssessmentService {

    @GET("products/models")
    Call<List<Model>> getModelAssessment();

}
