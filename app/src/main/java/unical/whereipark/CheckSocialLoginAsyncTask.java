package unical.whereipark;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Debug;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;

import javax.sql.RowSet;

import unical.whereipark.utility.MessagesHendler;
import unical.whereipark.utility.RequestHendler;
import unical.whereipark.utility.User;

/**
 * Created by Elena Mastria on 21/08/2018.
 */
public class CheckSocialLoginAsyncTask extends AsyncTask<Void, Void, Boolean> {

    private JSONObject param=new JSONObject();
    private String typeSocial,id,email,name;
    private Context context;
    public CheckSocialLoginAsyncTask(String typeSocial, String id,String email,String name, Context context) {
        this.context=context;
        try {
            param.put("Social", this.typeSocial=typeSocial);
            param.put("id", this.id=id);
            this.email=email;
            this.name=name;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        String result= "ok";
                //todo
            // RequestHendler.getInstance().makeRequest(RequestHendler.CHECK_SOCIAL_LOGIN,param);
        if (!result.equals(MessagesHendler.ERROR_INCORRECT_LOGIN)){
            User.init(this.email, this.name, this.typeSocial, null);
            return true;
        }
        return false;
    }

    @Override
    protected void onPostExecute(final Boolean exists) {
        if(exists){
            Log.e("jfjf","in ok");
            context.startActivity(new Intent(context, Main2Activity.class));
        }
        else{
            Intent intent=new Intent(context, RegistrationActivity.class);
            intent.putExtra("tipe-accout",typeSocial);
            context.startActivity(intent);
        }

    }

}

