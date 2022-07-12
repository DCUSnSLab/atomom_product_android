package com.example.roicamera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class RoiActivity extends AppCompatActivity {
    Bitmap image;

    String roi_json = "";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roi);

        Intent intent = getIntent();
        byte[] arr = intent.getByteArrayExtra("roi");
        image= BitmapFactory.decodeByteArray(arr, 0, arr.length);

        Button b1 = (Button) findViewById(R.id.clear);
        Button b2 = (Button) findViewById(R.id.send);
        final TouchDraw td = (TouchDraw) findViewById(R.id.draw);

        td.setBackGround(image);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                td.reset();
                Toast.makeText(getApplicationContext(), "완료", Toast.LENGTH_SHORT).show();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(() -> {
//                    td.SendJson();
                    roi_json = td.MakeJson();
                    UploadFile(image, roi_json);
//                    Toast.makeText(getApplicationContext(), "전송중", Toast.LENGTH_SHORT).show();
                }).start();
            }
        });

    }

    public static String convertString(String val) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < val.length(); i++) {
            if ('\\' == val.charAt(i) && 'u' == val.charAt(i + 1)) {
                Character r = (char) Integer.parseInt(val.substring(i + 2, i + 6), 16);
                sb.append(r);
                i += 5;
            } else {
                sb.append(val.charAt(i));
            }
        }
        return sb.toString();
    }

    public void UploadFile(final Bitmap bitmap, String string){
        HttpURLConnection con = null;

        String text="";
        String text2="";
        String page = "";

        Intent intent = new Intent(getApplicationContext(),ResultActivity.class);

        try {
            //URL url = new URL("http:// your ip : port /api");
            URL url = new URL("http:// : /api");
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";

            con = (HttpURLConnection)url.openConnection();
            con.setDoInput(true); //input 허용
            con.setDoOutput(true);  // output 허용
            con.setUseCaches(false);   // cache copy를 허용하지 않는다.
            con.setRequestMethod("POST");
            con.setConnectTimeout(10000);
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            // write data
            DataOutputStream dos = new DataOutputStream(con.getOutputStream());
            ByteArrayOutputStream blob = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, blob);
            byte[] bytes = blob.toByteArray();
            byte[] input = string.getBytes("UTF-8");

            dos.writeBytes(twoHyphens + boundary + lineEnd);

            // 파일 전송시 파라메터명은 media 파일명은 camera.jpg로 설정하여 전송
            dos.writeBytes("Content-Disposition: form-data; name=\"media\";filename=\"camera.jpg\""
                    + lineEnd);

            dos.writeBytes(lineEnd);
            dos.write(bytes);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            dos.flush(); // finish upload...

            //Json파일 전송시 파라메터명은 pos 파일명은 test.json으로 설정하여 전송송
            dos.writeBytes("Content-Disposition: form-data; name=\"pos\";filename=\"test.json\""
                    + lineEnd);

            dos.writeBytes(lineEnd);
            dos.write(input);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            dos.flush(); // finish upload...
            dos.close();

            if (con.getResponseCode() != HttpURLConnection.HTTP_OK){
                text2+="Http URL not connected!\n";
                intent.putExtra("text", text2);
            }

            InputStreamReader tmp = new InputStreamReader(con.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();

            String str;

            while ((str = reader.readLine()) != null) {
                builder.append(str + "\n");
            }

            page = builder.toString();
            text2 = convertString(page);
            JSONObject jObject = new JSONObject(text2);


            String nproduct;

            for(int i = 0; i < 4; i++){
                String count = Integer.toString(i);
                nproduct = jObject.getString(count);

                JSONObject products = new JSONObject(nproduct);
                String inproducts = products.getString("products");

                JSONObject product = new JSONObject(inproducts);
                double similarity = product.getDouble("similarity");

                String mainproduct = product.getString("mainProduct");

                JSONObject inmainproduct = new JSONObject(mainproduct);
                String brand = inmainproduct.getString("brand");
                String productname = inmainproduct.getString("productName");

                text += "\n*******************************************************************\n"
                        + "유사도 : " + similarity + "\n" + "브랜드 : " + brand + "\n" + "상품명 : " + productname
                        + "\n*******************************************************************\n";
            }

            intent.putExtra("text", text);

        } catch (MalformedURLException e) { // for URL.
            text=text+"Malformed URL Exception : \n"+e;
            intent.putExtra("text", text);
        } catch (IOException e) { // for openConnection().
            text=text+"IO Exception : \n"+e;
            intent.putExtra("text", text);
        } catch (Exception e) {
            text=text+"Exception : \n"+e;
            intent.putExtra("text", text);
        } finally {
            if (con != null)
                con.disconnect();
        }

        startActivity(intent);
    }

}