package unical.whereipark.utility;
/**
 * Created by Elena on 21/08/2018.
 */


import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class RequestHendler {
    private static final String HOST = "http://whereisave.000webhostapp.com/Request/";
    public static final String LOGIN_REQUEST = HOST + "login.php";
    public static final String CHECK_SOCIAL_LOGIN = HOST + "CheckSL.php";
    public static final String READ_CATEGORY_LIST = HOST + "categoryList.php";
    public static final String STOREINFO_REQUEST = HOST + "StoreInformations.php";
    public static final String STORELIKES_REQUEST = HOST + "StoreLikes.php";
    public static final String STOREOFFERS_REQUEST = HOST + "StoreOffers.php";
    public static final String REGISTRATION = HOST+"registerUser.php";
    public static String CHECK_EMAIL_EXISTS=HOST+"checkEmailExists.php";

    public static final String PROD_BY_CAT_REQUEST = HOST + "ProductsByCategory.php";
    public static final String CATEGORY_OFFERS = HOST + "OffersCategory.php";
    public static final String OFFERS_BY_CATEGORY = HOST + "OffersByCategory.php";
    public static final String SAVENXMOFFER = HOST + "SaveNxMOffer.php";
    public static final String SAVE50OFFER = HOST + "Save50Offer.php";
    private static RequestHendler requestHendler;

    private RequestHendler() {
    }

    public static RequestHendler getInstance() {
        return (requestHendler == null ? new RequestHendler() : requestHendler);
    }

    /*THIS IS THE CORRECT METHOD TO CALL*/
    public String makeRequest(String typeRequest, JSONObject param) {
        String response = "";
        Log.e("in request",typeRequest);
        try {

            URL url = new URL(typeRequest);
            System.out.println(url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            wr.write(param.toString());
            wr.flush();
            wr.close();
            InputStream in = new BufferedInputStream(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String s = "";
            while ((s = bufferedReader.readLine()) != null) {
                System.out.print("quihttp" + response + "risp");
                response += s + "\n";
            }
            System.out.print("quihttp" + response + "risp");
            System.out.print(response);
            Log.d("risp", response);
        } catch (IOException e) {
            System.out.println(e.getMessage());

            response += MessagesHendler.CONNECTION_ERROR;
//            response += e.toString();
        }
        System.out.println(response);
        return response.replaceAll("\r\n|\r|\n", "");
    }

    public String makeRequest(String typeRequest) {
        String response = "";

        try {

            URL url = new URL(typeRequest);
            System.out.println(url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(false);
//            connection.setRequestProperty("Content-Type", "application/json");
//            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
//            wr.write(param.toString());
//            wr.flush();
//            wr.close();
            InputStream in = new BufferedInputStream(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String s = "";
            while ((s = bufferedReader.readLine()) != null) {
                System.out.print("quihttp" + response + "risp");
                response += s + "\n";
            }
            System.out.print("quihttp" + response + "risp");
            System.out.print(response);
            Log.d("risp", response);
        } catch (IOException e) {
//            response += MessagesHendler.CONNECTION_ERROR;
            response += e.toString();
        }
        System.out.println(response);
        return response.replaceAll("\r\n|\r|\n", "");
    }
}
