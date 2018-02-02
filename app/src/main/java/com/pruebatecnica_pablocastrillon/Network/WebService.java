package com.pruebatecnica_pablocastrillon.Network;

import android.content.Context;
import android.net.Uri;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.pruebatecnica_pablocastrillon.model.GetNotification;
import com.pruebatecnica_pablocastrillon.model.GetNotifications;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by pablo.castrillon on 31/01/2018.
 */

public class WebService {
    private static final int VOLLEY_TIME_OUT = 10000;
    private RequestQueue mainQueue;

    public WebService(Context context) {
        mainQueue = Volley.newRequestQueue(context);
    }


    private String getRestUrl() {

        return "http://proyectos.tekus.co/Test/api/notifications";
    }


    public void postNotificationRequest(GetNotification getNotification) {
        try {

            Gson gson = new GsonBuilder().create();

            JSONObject data = new JSONObject(gson.toJson(getNotification));
            Uri.Builder uriBuilder = Uri.parse(getRestUrl())
                    .buildUpon()
                    .appendPath("");


            Request jsObjRequest = new JsonObjectRequest
                    (Request.Method.POST, uriBuilder.toString(), data, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                            } catch (Exception ex) {
                                Log.e("onResponse", ex.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("onErrorResponse", error.getMessage());
                        }
                    }) {
                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    return handleEmptyObjectResponse(response);
                }

                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new ArrayMap<>();
                    headers.put("Authorization", "Basic 1036612823");

                    return headers;
                }
            };

            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                    VOLLEY_TIME_OUT,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            jsObjRequest.setShouldCache(false);

            mainQueue.add(jsObjRequest);

        } catch (Exception ex) {
            Log.e(this.toString(), ex.getMessage());
        }
    }


    public void GetNotificationRequest(String notificationId) {
        try {

            Uri.Builder uriBuilder = Uri.parse(getRestUrl())
                    .buildUpon()
                    .appendPath(notificationId);

            String uri = uriBuilder.toString();

            Request request = new JsonObjectRequest(Request.Method.GET, uri, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            Gson gson = new GsonBuilder().create();
                            GetNotification resp;

                            try {
                                resp = gson.fromJson(response.toString(), GetNotification.class);
                                ;

                            } catch (Exception ex) {
                                Log.e(this.toString(), ex.getMessage());
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.e(this.toString(), error.getMessage());
                        }
                    }) {
                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    return handleEmptyObjectResponse(response);
                }

                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new ArrayMap<>();
                    headers.put("Authorization", "Basic 1036612823");
                    return headers;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(
                    VOLLEY_TIME_OUT,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            request.setShouldCache(false);
            mainQueue.add(request);

        } catch (Exception ex) {
            Log.e(this.toString(), ex.getMessage());
        }
    }

    public void GetNotificationSRequest() {
        try {

            Uri.Builder uriBuilder = Uri.parse(getRestUrl())
                    .buildUpon();

            String uri = uriBuilder.toString();

            Request request = new JsonObjectRequest(Request.Method.GET, uri, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            Gson gson = new GsonBuilder().create();
                            GetNotifications resp;

                            try {
                                resp = gson.fromJson(response.toString(), GetNotifications.class);

                            } catch (Exception ex) {
                                Log.e(this.toString(), ex.getMessage());
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.e(this.toString(), error.getMessage());
                        }
                    }) {
                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    return handleEmptyObjectResponse(response);
                }

                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new ArrayMap<>();
                    headers.put("Authorization", "Basic 1036612823");
                    return headers;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(
                    VOLLEY_TIME_OUT,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            request.setShouldCache(false);
            mainQueue.add(request);

        } catch (Exception ex) {
            Log.e(this.toString(), ex.getMessage());
        }
    }


    public void putNotification(String notificationId, GetNotification getNotification) {
        try {

            Gson gson = new GsonBuilder().create();

            JSONObject data = new JSONObject(gson.toJson(getNotification));


            Uri.Builder uriBuilder = Uri.parse(getRestUrl())
                    .buildUpon()
                    .appendPath(notificationId);


            Request jsObjRequest = new JsonObjectRequest
                    (Request.Method.PUT, uriBuilder.toString(), data, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            GetNotification resp;
                            try {
                                String date;
                                Gson gson = new GsonBuilder().create();
                                resp = gson.fromJson(response.toString(), GetNotification.class);
                                date = resp.getDate();
                                System.out.println(date);

                            } catch (Exception ex) {
                                Log.e(this.toString(), ex.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.e(this.toString(), error.getMessage());
                        }
                    }) {
                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    return handleEmptyObjectResponse(response);
                }

                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new ArrayMap<>();
                    headers.put("Authorization", "Basic 1036612823");

                    return headers;
                }
            };

            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                    VOLLEY_TIME_OUT,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            jsObjRequest.setShouldCache(false);

            mainQueue.add(jsObjRequest);

        } catch (Exception ex) {
            Log.e(this.toString(), ex.getMessage());
        }
    }

    public void delNotificationRequest(String notificationId) {
        try {

            Uri.Builder uriBuilder = Uri.parse(getRestUrl())
                    .buildUpon()
                    .appendPath(notificationId);

            String uri = uriBuilder.toString();

            Request request = new JsonObjectRequest(Request.Method.DELETE, uri, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                            } catch (Exception ex) {
                                Log.e(this.toString(), ex.getMessage());
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.e(this.toString(), error.getMessage());
                        }
                    }) {
                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    return handleEmptyObjectResponse(response);
                }

                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new ArrayMap<>();
                    headers.put("Authorization", "Basic 1036612823");
                    return headers;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(
                    VOLLEY_TIME_OUT,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            request.setShouldCache(false);
            mainQueue.add(request);

        } catch (Exception ex) {
            Log.e(this.toString(), ex.getMessage());
        }
    }

    private Response<JSONObject> handleEmptyObjectResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
            JSONObject result = null;
            if (jsonString.length() > 0)
                result = new JSONObject(jsonString);
            return Response.success(result,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        }
    }

}
