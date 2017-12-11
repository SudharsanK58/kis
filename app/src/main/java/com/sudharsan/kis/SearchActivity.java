package com.sudharsan.kis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private  WebOperation webOperation;
    private RecyclerView imageRecyclerView;
    private ImageAdapter imageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final EditText eText;
        ImageButton btn;
        webOperation = WebOperation.getInstance(this);

        imageRecyclerView = findViewById(R.id.recycler_view_images);
        imageAdapter = new ImageAdapter(this);
        imageRecyclerView.setAdapter(imageAdapter);
        imageRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayout.VERTICAL));

        eText = findViewById(R.id.edittext_input);
        btn =  findViewById(R.id.button_search);
        final TextView result =  findViewById(R.id.textView_result);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final String str = eText.getText().toString();
                Log.d("search", "Searching for :" + str);
                result.setText("Searching for :" + str);


                            String strNoSpaces = str.replace(" ", "+");
                            //  API key
                            String key="AIzaSyCdDA2LZ3JFsmyVlaZbeGe58ENWv0pkQYk";

                            // Search Engine ID
                            String cx = "009693805969653540186:58qkkokjzlk";

                            String url2 = "https://www.googleapis.com/customsearch/v1?q=" + strNoSpaces + "&safe=high" + "&key=" + key + "&cx=" + cx + "&alt=json";
                            Log.d("search", "Url = "+  url2);

                webOperation.onImageRequest(url2, new WebOperation.IImageListener() {
                    @Override
                    public void onSuccess(List<String> urlList) {
                        result.setVisibility(View.GONE);
                        imageAdapter.addAll(urlList);
                    }

                    @Override
                    public void onError(String error) {
                       result.setVisibility(View.VISIBLE);
                       result.setText(error);
                    }
                });

            }
        });
    }

}
