package com.example.roicamera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TouchDraw extends View {
    private float x, y; //좌표점을 받기위한 변수 선언

    String tmp;

    private Paint paint = new Paint(); //onDraw()에서 그림 그리기 위해서 필요한 Paint 객체 생성
    private Path path = new Path(); //터치된 좌표를 저장하기 위한 Path 객체 생성

    Bitmap bg;

    ArrayList<String> roi = new ArrayList<String>();

    public TouchDraw(Context context) {
        super(context);

        paint.setAntiAlias(true);//선을 매끄럽게 하기 위해서
        paint.setStrokeWidth(55f);//선의 두께를 55으로 설정
        paint.setColor(Color.YELLOW);//선의 색깔을 노란색으로
        paint.setAlpha(150);//투명도 150으로 설정
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    public TouchDraw(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        paint.setAntiAlias(true);//선을 매끄럽게 하기 위해서
        paint.setStrokeWidth(55f);//선의 두께를 55으로 설정
        paint.setColor(Color.YELLOW);//선의 색깔을 노란색으로
        paint.setAlpha(150);//투명도 150으로 설정
        paint.setStyle(Paint.Style.STROKE); //안의 내용을 채우지 않고 외곽선만 그림
        paint.setStrokeJoin(Paint.Join.ROUND); //
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bg, 0, 0, null);
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        x = event.getX();
        y = event.getY();
        int rx, ry;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //터치됐을때 터치된 좌표를 저장해야함
                path.moveTo(x, y);//moveTo(x,y) -> 다음 윤곽점 시작점을 x, y로 설정한다.
                rx = (int) x;
                ry = (int) y;
                tmp = "[" + rx + ", " + ry;
                return true; //다른 이벤트 감지하기 위해서
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);//마지막으로 지정된 점에서 x,y 지점까지 선을 추가하는것
                break;
            case MotionEvent.ACTION_UP:
                rx = (int) x;
                ry = (int) y;
                tmp += ", " + rx + ", " + ry + "]";
                roi.add(tmp);
                break;
            default:
                return false;
            //해당 이벤트가 끝나지 않았음으로 인지하게 해서 다른 이벤트 감지 못하게 하려고
        }
        invalidate();
        return true;
    }

    public void reset(){
        roi.clear();
        path.reset(); //path에 저장된 모든 경로 지워주는 부분
        invalidate(); //다시 onDraw() 시켜주는 부분
    }

    void setBackGround(Bitmap bitmap){
        bg = bitmap;
    }

    public String MakeJson(){
        String json = "";

        JSONArray jsonarr = new JSONArray();
        for(int i = 0; i < roi.size(); i++){
            jsonarr.put(roi.get(i));
        }
        JSONObject jsonob = new JSONObject();
        try{
            jsonob.put("pos", jsonarr);
        }catch(JSONException e){
            e.printStackTrace();
        }

        json = jsonob.toString();
        return json;
    }

}

