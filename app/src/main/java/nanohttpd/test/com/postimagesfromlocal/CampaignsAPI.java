package nanohttpd.test.com.postimagesfromlocal;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface CampaignsAPI {
    @Multipart
    @POST("/api/upload")
    Call<String> getNewCampaigns(
            @Part("images") RequestBody file,
            @Query("id")String id,
            @Query("type")String type,
            @Query("offer_type")String offer_type,
            @Query("message")String message,
            @Query("selling_price")String selling_price,
            @Query("mrp")String mrp,
            @Query("product_code")String product_code);
    @Multipart
    @POST("/api/upload")
    Call<String> getNewCampaigns1(
            @Part("images") RequestBody file);
}
