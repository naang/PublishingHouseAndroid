package com.androidwave.recyclerviewpagination;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.androidwave.recyclerviewpagination.PaginationListener.PAGE_START;

public class Main3Activity extends AppCompatActivity
    implements SwipeRefreshLayout.OnRefreshListener {

  private static final String TAG = "Main3Activity";

  @BindView(R.id.recyclerView)
  RecyclerView mRecyclerView;
  //@BindView(R.id.swipeRefresh)
  //SwipeRefreshLayout swipeRefresh;
  private PostRecyclerAdapter adapter;
  public static RequestQueue queue;
  private int currentPage = PAGE_START;
  private boolean isLastPage = false;
  public static int totalPage;
  private Double timeDifference;
  private boolean isLoading = false;
  private ProgressDialog pDialog;
  public static String returned;
  public static String xmlType;
  public static int docNumber;
  public static String docName;
  public static String docStatus;
  public static String docId;
  public  static  ArrayList<PostItem> items ;
          //= new ArrayList<>();
  int itemCount = 0;
  Integer[] imageId = {
          R.drawable.icdelivery,
          R.drawable.icmove,
          R.drawable.icreturn
          //R.drawable.remainder,
          //R.drawable.checkmark
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    actionBarSetup();
    setContentView(R.layout.activity_main3);
    ButterKnife.bind(this);
    //swipeRefresh.setOnRefreshListener(this);
    mRecyclerView.setHasFixedSize(true);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    queue = Volley.newRequestQueue(getApplicationContext());
    mRecyclerView.setLayoutManager(layoutManager);
    adapter = new PostRecyclerAdapter(new ArrayList<>());
    mRecyclerView.setAdapter(adapter);
    items =  new ArrayList<>();
    pDialog = new ProgressDialog(this);
    //pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color));
    pDialog.setCancelable(false);
    pDialog.setMessage("იტვირთება...");
    pDialog.show();
    getDocuments();
            final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkUpdate(MainActivity.currentUser);
                handler.postDelayed(this, 6000);
            }
        }, 6000);

    mRecyclerView.addOnScrollListener(new PaginationListener(layoutManager) {
      @Override
      protected void loadMoreItems() {
        isLoading = true;
        currentPage++;
        getDocuments();
      }

      @Override
      public boolean isLastPage() {
        return isLastPage;
      }

      @Override
      public boolean isLoading() {
        return isLoading;
      }
    });


    mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(View view, int position) {
                ViewGroup row = (ViewGroup) mRecyclerView.getChildAt(position);
                docName = ((TextView) view.findViewById(R.id.desc)).getText().toString();
                //String docNum = ((TextView) view.findViewById(R.id.title)).getText().toString();
                docId = ((TextView) view.findViewById(R.id.id)).getText().toString();
                docNumber = Integer.parseInt(docId);
                //Toast.makeText(getApplicationContext(), String.valueOf(docNumber), Toast.LENGTH_LONG).show();
                if (docNumber > 0) {
                  //Toast.makeText(getApplicationContext(), "aaaaaa", Toast.LENGTH_LONG).show();
                  Intent intent = new Intent("com.androidwave.recyclerviewpagination.ScanManagerDemo");
                  startActivity(intent);
                } else {
                  Toast.makeText(getApplicationContext(), "მოხდა შეცდომა!", Toast.LENGTH_LONG).show();
                  pDialog.dismiss();
                }
              }

              @Override
              public void onLongItemClick(View view, int position) {
                docId = ((TextView) view.findViewById(R.id.id)).getText().toString();
                if (docId.trim().equals("")) {
                  Toast.makeText(getApplicationContext(), "მოხდა შეცდომა!", Toast.LENGTH_LONG).show();
                  pDialog.dismiss();
                  //refresh();
                }

                new AlertDialog.Builder(Main3Activity.this)
                        .setTitle("ნამდვილად გსურთ დოკუმენტის წაშლა?")
                        .setIcon(android.R.drawable.ic_delete)
                        .setNegativeButton("არა", null)
                        .setPositiveButton("დიახ", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                            pDialog.show();
                            deleteOrder(docId,MainActivity.currentUserId);
                          }

                        })
                        .show();

                Log.v("long clicked", "pos: " + position);
              }
            }));
  }

  private void getDocuments() {
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        String url ="http://92.241.77.162:1188//Home/GetOrders?userId=" + MainActivity.currentUserId+"&pageNumber="+currentPage;
        //queue.getCache().remove(url);
        //queue.getCache().clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                  @Override
                  public void onResponse(String response) {
                    try{
                      JSONObject jsonResult = null;
                      JSONObject myJsonObject = new JSONObject(response);
                      jsonResult = myJsonObject.getJSONObject("jsonResponse");
                      String sauccsses = jsonResult.getString("IsSuccess");
                      String message = jsonResult.getString("Message");
                      if(sauccsses == "true") {
                        JSONArray jr = jsonResult.getJSONArray("Data");
                        for (int j = 0; j < jr.length(); j++) {
                          JSONObject jb = (JSONObject) jr.get(j);
                          String id = jb.getString("Id");
                          totalPage = Integer.parseInt(jb.getString("PageCount"));
                          String docNum = jb.getString("DocNumber");
                          String comment = jb.getString("Comment");
                          String receiver = jb.getString("Receiver");
                          //String time = jb.getString("PerformedTime");
                          //String timeDiff = jb.getString("diff");
                          returned = jb.getString("isReturn");
                          String finished = jb.getString("IsFinished");
                          String inProgress = jb.getString("InPorogress");
                          String isTo = jb.getString("isTo");
                          String status = jb.getString("status");
                          String statusTo = jb.getString("statusTo");
                          xmlType = jb.getString("xmlType");
                          //timeDifference = Double.parseDouble(timeDiff);
                          itemCount++;
                          PostItem postItem = new PostItem("","","","",0,"","",isTo,status,statusTo,"");
                          postItem.setId(id);
                          postItem.setTitle(docNum);
                          docStatus = jb.getString("checkStatus");
                          postItem.setDescription(receiver);
                          if (!comment.equals("")) {
                            postItem.setComment("/" +comment+ "/");
                          }
                          postItem.setIsreturned(returned);
                          postItem.setInProgress(inProgress);
                          //postItem.setTime(time);

                         /* if (timeDifference < 0.5) {
                            postItem.setImageId(imageId[3]);
                          }*/
                          /*else {
                            postItem.setImageId(imageId[0]);
                          }*/
                          if(xmlType.equalsIgnoreCase("1")){
                            postItem.setImageId(imageId[0]);
                          }
                          if(xmlType.equalsIgnoreCase("2")){
                            postItem.setImageId(imageId[1]);
                          }
                          if(xmlType.equalsIgnoreCase("3")){
                            postItem.setImageId(imageId[2]);
                          }
                          postItem.setIsfinished(finished);
                          items.add(postItem);
                        }
                        //Toast.makeText(getApplicationContext(), String.valueOf(totalPage), Toast.LENGTH_LONG).show();
                        if (currentPage != PAGE_START) {adapter.removeLoading();adapter.clear();}
                        adapter.addItems(items);
                        //swipeRefresh.setRefreshing(false);
                        if (currentPage < totalPage) {
                          adapter.addLoading();
                          //currentPage++;
                        } else {
                          isLastPage = true;
                        }
                        isLoading = false;
                        pDialog.dismiss();
                      }
                    } catch (JSONException e) {
                      pDialog.dismiss();
                      e.printStackTrace();
                    }
                  }
                }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
            pDialog.dismiss();
          }
        });
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
        stringRequest.setRetryPolicy(new RetryPolicy() {
          @Override
          public int getCurrentTimeout() {
            pDialog.dismiss();
            return 50000;
          }
          @Override
          public int getCurrentRetryCount() {
            pDialog.dismiss();
            return 50000;
          }
          @Override
          public void retry(VolleyError error) throws VolleyError {
            pDialog.dismiss();
          }
        });
      }
    }, 1500);
  }

  private void deleteOrder(String orderId,String userId ) {
    String url ="http://92.241.77.162:1188//Home/SetDocInvisible?docId="+orderId+"&userId="+userId;
    queue.getCache().clear();
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {
                try{
                  JSONObject jsonResult = null;
                  JSONObject myJsonObject = new JSONObject(response);
                  jsonResult = myJsonObject.getJSONObject("jsonResponse");
                  String sauccsses = jsonResult.getString("IsSuccess");
                  String message = jsonResult.getString("Message");

                  if(sauccsses == "true")
                  {
                    refresh();
                  }
                  else
                  {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    pDialog.dismiss();
                  }
                } catch (JSONException e) {
                  pDialog.dismiss();
                  e.printStackTrace();
                }
                //adapter.notifyDataSetChanged();
              }
            },
            new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {

                Log.e(error.getMessage(), "getOrders Method");
                if (error instanceof NoConnectionError) {
                  Toast.makeText(getApplicationContext(), "NoConnection Error", Toast.LENGTH_LONG).show();
                  pDialog.dismiss();
                } else if (error instanceof AuthFailureError) {
                  Toast.makeText(getApplicationContext(), "AuthFailure Error", Toast.LENGTH_LONG).show();
                  pDialog.dismiss();
                } else if (error instanceof ServerError) {
                  Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
                  pDialog.dismiss();
                } else if (error instanceof NetworkError) {
                  Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
                  pDialog.dismiss();
                }
              }
            }
    );
    stringRequest.setShouldCache(false);
    queue.add(stringRequest);

/*    {
      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        //String auth = "Bearer " + MainActivity.token;
        // headers.put("Authorization", auth);
        return headers;
      }
    };*/
  }

  private void deleteAllOrder(String userId ) {
    //pDialog.show();
    String url ="http://92.241.77.162:1188//Home/SetAllDocInvisible?userId="+userId;
    queue.getCache().clear();
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {
                try{
                  //Toast.makeText(getApplicationContext(), "AAAAAAA", Toast.LENGTH_LONG).show();
                  JSONObject jsonResult = null;
                  JSONObject myJsonObject = new JSONObject(response);
                  jsonResult = myJsonObject.getJSONObject("jsonResponse");
                  String sauccsses = jsonResult.getString("IsSuccess");
                  String message = jsonResult.getString("Message");
                  if(sauccsses == "true")
                  {
                    refresh();
                  }
                  else
                  {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    pDialog.dismiss();
                  }
                } catch (JSONException e) {
                  pDialog.dismiss();
                  e.printStackTrace();
                }
                //adapter.notifyDataSetChanged();
              }
            },
            new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {

                Log.e(error.getMessage(), "getOrders Method");
                if (error instanceof NoConnectionError) {
                  Toast.makeText(getApplicationContext(), "NoConnection Error", Toast.LENGTH_LONG).show();
                  pDialog.dismiss();
                } else if (error instanceof AuthFailureError) {
                  Toast.makeText(getApplicationContext(), "AuthFailure Error", Toast.LENGTH_LONG).show();
                  pDialog.dismiss();
                } else if (error instanceof ServerError) {
                  Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
                  pDialog.dismiss();
                } else if (error instanceof NetworkError) {
                  Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
                  pDialog.dismiss();
                }
              }
            });
    stringRequest.setShouldCache(false);
    queue.add(stringRequest);
    stringRequest.setRetryPolicy(new RetryPolicy() {
      @Override
      public int getCurrentTimeout() {
        //pDialog.dismiss();
        return 50000;
      }
      @Override
      public int getCurrentRetryCount() {
        //pDialog.dismiss();
        return 50000;
      }
      @Override
      public void retry(VolleyError error) throws VolleyError {
        //pDialog.dismiss();
      }
    });
    /*{
      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        //String auth = "Bearer " + MainActivity.token;
        // headers.put("Authorization", auth);
        return headers;
      }
    };*/
  }
  @Override
  public void onRefresh() {
    itemCount = 0;
    currentPage = PAGE_START;
    isLastPage = false;
    adapter.clear();
    getDocuments();
  }

  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main3, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_refresh:
        item.setEnabled(false);
        refresh();
        return true;
      case R.id.action_delete:
        AlertDeleteOrder();
        return true;
      case R.id.action_logout:
        logOut();
      default:
        // If we got here, the user's action was not recognized.
        // Invoke the superclass to handle it.
        return super.onOptionsItemSelected(item);

    }
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  private void actionBarSetup() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
      getSupportActionBar().setTitle(Html.fromHtml("<small>დოკუმენტები</small>"));
      getSupportActionBar().setSubtitle(MainActivity.currentUser);

    }
  }
  private void refresh()
  {
    adapter.clear();
    Intent intent;
    intent = getIntent();
    finish();
    startActivity(intent);
  }
  public static void playNewMessageAudio() {

    try{
      MediaPlayer mp = new MediaPlayer();
      mp.setDataSource("http://92.241.77.162:1155/audio/NewMessage.mp3");
      mp.prepare();
      mp.start();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public  void  AlertDeleteOrder()
  {

    new AlertDialog.Builder(Main3Activity.this)
            .setTitle("წაიშალოს დასრულებული დოკუმენტები?")
            .setIcon(android.R.drawable.ic_delete)
            .setNegativeButton("არა", null)
            .setPositiveButton("დიახ", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getApplicationContext(), "SSSSS", Toast.LENGTH_LONG).show();
                pDialog.show();
                deleteAllOrder(MainActivity.currentUserId);
              }

            })
            .show();
  }

  private void checkUpdate(String userId) {
    String url ="http://92.241.77.162:1188///Home/checkUpdate?username=" + userId;
    //queue.getCache().clear();
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {
                try{
                  JSONObject jsonResult = null;
                  JSONObject myJsonObject = new JSONObject(response);
                  jsonResult = myJsonObject.getJSONObject("jsonResponse");
                  String sauccsses = jsonResult.getString("IsSuccess");
                  String message = jsonResult.getString("Message");
                  String CounterStr = jsonResult.getString("Counter");
                  if(sauccsses == "true") {
                    int counter = Integer.parseInt(CounterStr);
                    if(counter > 0)
                    {
                      playNewMessageAudio();
                    }
                  }
                  else
                  {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    pDialog.dismiss();
                  }

                } catch (JSONException e) {
                  pDialog.dismiss();
                  e.printStackTrace();
                }
                //adapter.notifyDataSetChanged();
              }
            },
            new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {

                Log.e(error.getMessage(), "checkUpdate Method");
                if (error instanceof NoConnectionError) {
                  Toast.makeText(getApplicationContext(), "NoConnection Error", Toast.LENGTH_LONG).show();
                  pDialog.dismiss();
                } else if (error instanceof AuthFailureError) {
                  Toast.makeText(getApplicationContext(), "AuthFailure Error", Toast.LENGTH_LONG).show();
                  pDialog.dismiss();
                } else if (error instanceof ServerError) {
                  Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
                  pDialog.dismiss();
                } else if (error instanceof NetworkError) {
                  Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
                  pDialog.dismiss();
                }
              }
            }
    );

    /*{
      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        //String auth = "Bearer " + MainActivity.token;
        // headers.put("Authorization", auth);
        return headers;
      }
    };*/
    //requestQueue.add(jsObjRequest);
  }

  public  void  logOut()
  {
    AlertDialog show = new AlertDialog.Builder(Main3Activity.this)
            .setTitle("ნამდვილად გსურთ გასვლა?")
            .setIcon(android.R.drawable.ic_lock_power_off)
            .setNegativeButton("არა", null)
            .setPositiveButton("დიახ", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                //dLoading.show();
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
              }
            })
            .show();
  }
}
