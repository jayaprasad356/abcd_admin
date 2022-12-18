package com.app.abcdadmin.fcm;


import com.app.abcdadmin.fcmmodels.MyResponse;
import com.app.abcdadmin.fcmmodels.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization: key=AAAADCXBTBU:APA91bFLD3BdccE4tSs8sO0GBgK0erfAJDto-EacU5K4Dj-qp6kuPPyA3a7o-Gp0H5gH0H683vrpJ0Ussj06qEvELOuq8j8y5ndPPgGW9nSdNRRIvwWDGqfKqO6qWIVyqpdMV1L3AMgl"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
