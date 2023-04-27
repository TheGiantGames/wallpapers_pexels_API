package com.thegiantgames.wallpapers;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.AbsListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;

import com.thegiantgames.wallpapers.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;



public class MainActivity extends AppCompatActivity {

    private Runnable runnable;
    long startTime;
    long time;
    Timer timer ;
    InterstitialAd mInterstitialAd;
    SearchView searchView;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    wallpaperAdaptor wallpaperAdaptor;
    List<wallpaperModel> wallpaperModelList;
    String s ;




    Boolean isScrolling = false ;
    int currentItem , totalItem , scrollOutItems;

    int pageNumber = 1 ;
    String url = "https://api.pexels.com/v1/curated/?page="+pageNumber+"&per_page=40";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.thegiantgames.wallpapers.R.layout.activity_main);


        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, "ca-app-pub-4358208602621175/5573889131", adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);

                        Log.e("Error" , "Error to load Ad");
                    }

                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        super.onAdLoaded(interstitialAd);

                       mInterstitialAd = interstitialAd;
                       mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                           @Override
                           public void onAdClicked() {
                               super.onAdClicked();
                           }

                           @Override
                           public void onAdDismissedFullScreenContent() {
                               super.onAdDismissedFullScreenContent();
                           }

                           @Override
                           public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                               super.onAdFailedToShowFullScreenContent(adError);
                           }

                           @Override
                           public void onAdImpression() {
                               super.onAdImpression();
                           }

                           @Override
                           public void onAdShowedFullScreenContent() {
                               super.onAdShowedFullScreenContent();

                           }
                       });


                    }
                }
        );








        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mInterstitialAd != null)
                    mInterstitialAd.show(MainActivity.this);
                else
                    Log.e("Error" , "Add not ready yet");

            }
        }, 60000 );









        recyclerView = findViewById(com.thegiantgames.wallpapers.R.id.recyclerView);
        wallpaperModelList = new ArrayList<>();
        wallpaperAdaptor = new wallpaperAdaptor(this , wallpaperModelList);

        recyclerView.setAdapter(wallpaperAdaptor);

        GridLayoutManager gridLayoutManager =new GridLayoutManager(this ,2);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                pageNumber = pageNumber+ 1 ;
                if (newState > AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){

                    url =  "https://api.pexels.com/v1/curated/?page="+pageNumber+"&per_page=40";
                    pageNumber = pageNumber + 1;
                    isScrolling =true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItem = gridLayoutManager.getChildCount();
                totalItem = gridLayoutManager.getItemCount();
                scrollOutItems = gridLayoutManager.findFirstVisibleItemPosition();
                pageNumber = pageNumber+ 1;
                if (isScrolling && (currentItem + scrollOutItems ==totalItem)){
                    isScrolling = false;
                    fetchRequest();
                    pageNumber = pageNumber+ 1;
                }
           pageNumber++; }
        });


        fetchRequest();
        pageNumber++;


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(com.thegiantgames.wallpapers.R.id.swiperefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Collections.shuffle(wallpaperModelList, new Random());
                wallpaperAdaptor.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                pageNumber++;


            }
        });


        searchView  = findViewById( R.id.searchView);
        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                wallpaperModelList.clear();
                s = searchView.getQuery().toString();
                url =   "https://api.pexels.com/v1/search?query="+s+"&per_page=40";

                fetchRequest();
                pageNumber = pageNumber = 1;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });




    }

    public void fetchRequest(){

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pageNumber = pageNumber + 1 ;

                try {
                    JSONObject jsonObject =  new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("photos");
                    int length = jsonArray.length();

                    for (int i = 0 ; i < length ; i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        int id = jsonObject1.getInt("id");
                        JSONObject object = jsonObject1.getJSONObject("src");
                        String originalurl = object.getString("original");
                        String mediumurl = object.getString("medium");
                        wallpaperModel wallpaperModel = new wallpaperModel(id , originalurl ,mediumurl);
                        wallpaperModelList.add(wallpaperModel);
                        pageNumber = pageNumber + 1 ;



                    }
                    wallpaperAdaptor.notifyDataSetChanged();
                    pageNumber = pageNumber + 1 ;


                } catch (JSONException e){}



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String ,String> params = new HashMap<>();
                params.put("Authorization", "HGol2w0Zj5kxpIbDM5bF4XodxJT5oTIdKoV1UHcN9RfPhi8cnSA1sunL");
                return params;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }



    }



