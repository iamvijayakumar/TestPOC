package nanohttpd.test.com.postimagesfromlocal;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.esafirm.imagepicker.features.ImagePicker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements Callback<String> {
    public  static int REQUEST_CODE_PICKER =103;
    Button btn,submitRequest;
    String imagePath ="";
    EditText message,id,sellingPrice, mrp,ipAddress;
    CampaignsAPI campaignsAPI ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        campaignsAPI = initRetroFit();
        btn = (Button)findViewById(R.id.button);
        message = (EditText)findViewById(R.id.editText);
        id = (EditText)findViewById(R.id.editText2);
        sellingPrice = (EditText)findViewById(R.id.editText4);
        mrp = (EditText)findViewById(R.id.editText3);

        ipAddress = (EditText)findViewById(R.id.editText8);
        submitRequest = (Button)findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.create(MainActivity.this) // Activity or Fragment
                        .start(REQUEST_CODE_PICKER);
            }
        });

        submitRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("MainActivity","imagePath :: " +imagePath);
                File file = new File(imagePath);
                RequestBody file1 = RequestBody.create(MediaType.parse("image/*"), file);
                Log.e("MainActivity","imagePath :: " +file1.toString());
                Call<String> call = campaignsAPI.getNewCampaigns(file1,id.getText().toString(),"product","percentage",message.getText().toString(),sellingPrice.getText().toString(),mrp.getText().toString(),"000");
               // Call<String> call = campaignsAPI.getNewCampaigns1(file1);
                call.enqueue(MainActivity.this);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICKER && resultCode == RESULT_OK && data != null) {
            //  printImages(images);
            imagePath = ""+ ImagePicker.getImages(data).get(0).getPath();
            return;
        }
    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        Toast.makeText(MainActivity.this,"Response :: " +response.body(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        Log.e("Local","Failure msg :: " +t.toString());
        Toast.makeText(MainActivity.this,"Response :: " +t.toString(),Toast.LENGTH_LONG).show();
    }

    public static CampaignsAPI initRetroFit() {
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        // Fetch campaigns from server
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.178:1507")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        // prepare call in Retrofit 2.0
        CampaignsAPI campaignsAPI = retrofit.create(CampaignsAPI.class);
        return campaignsAPI;
    }
}
