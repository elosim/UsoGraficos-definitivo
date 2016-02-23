package com.example.dam.usograficos;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;

import petrov.kristiyan.colorpicker.ColorPicker;


/**
 * Created by 2dam on 02/02/2016.
 */
public class Vista extends View {
    private int alto, ancho;
    private Paint pincel;
    private float x0, y0, x1, y1;
    private Bitmap mapaDeBits;
    static Canvas lienzoFondo;
    private Path rectaPoligonal = new Path();
    private int i=0;
    private FileOutputStream fos = null;
    File carpeta, archivo;
    static Context ctx = null;

    enum forma{
        circulo,linea,rectangulo,lineaRecta;
    }

    static forma f = forma.linea;


    public Vista(Context context) {
        super(context);
        ctx = context;
        pincel = new Paint();
        pincel.setColor(Color.RED);
        pincel.setAntiAlias(true);
        pincel.setStyle(Paint.Style.STROKE);
        pincel.setStrokeWidth(10);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mapaDeBits,0,0,null);
        canvas.drawBitmap(mapaDeBits, 0, 0, null);

        float aux;

        switch (f){
            case rectangulo:
                canvas.drawRect(Math.min(x0,x1),Math.min( y0,y1), Math.max(x1,x0), Math.max(y1,y0), pincel);
                break;
            case circulo:
                canvas.drawOval(Math.min(x0,x1),Math.min( y0,y1), Math.max(x1,x0), Math.max(y1,y0), pincel);
                break;
            case linea:
                canvas.drawPath(rectaPoligonal, pincel);
                break;
            case lineaRecta:
                canvas.drawLine(x0,y0,x1,y1,pincel);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        alto = h;
        ancho = w;
        mapaDeBits = Bitmap.createBitmap(w, h,
                Bitmap.Config.ARGB_8888);
        lienzoFondo = new Canvas(mapaDeBits);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x,y;
        switch (f) {
            case rectangulo:
                x = event.getX();
                y = event.getY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x0 = x;
                        y0 = y;
                        x1 = x;
                        y1 = y;

                        break;
                    case MotionEvent.ACTION_MOVE:

                        x1 = x;
                        y1 = y;
                        invalidate();

                        break;
                    case MotionEvent.ACTION_UP:
                        x1 = x;
                        y1 = y;
                        lienzoFondo.drawRect(x0, y0, x1, y1, pincel);
                        invalidate();

                        break;
                }
                break;
            case linea:
                x = event.getX();
                y = event.getY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x0 = x;
                        y0 = y;
                        x1 = x;
                        y1 = y;
                        rectaPoligonal.moveTo(x0, y0);
                    case MotionEvent.ACTION_MOVE:

                        x1 = x;
                        y1 = y;
                        rectaPoligonal.quadTo(x0, y0,(x + x1) / 2, (y + y1) / 2);
                        x0 = x1;
                        y0 = y1;
                        invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        x1 = x;
                        y1 = y;
                        lienzoFondo.drawPath(rectaPoligonal, pincel);
                        rectaPoligonal.reset();
                        invalidate();

                        break;


                }
            break;

            case circulo:
                x = event.getX();
                y = event.getY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x0 = x;
                        y0 = y;
                        x1 = x;
                        y1 = y;

                        break;
                    case MotionEvent.ACTION_MOVE:

                        x1 = x;
                        y1 = y;

                        invalidate();

                        break;
                    case MotionEvent.ACTION_UP:
                        x1 = x;
                        y1 = y;

                        lienzoFondo.drawOval(Math.min(x0, x1), Math.min(y0, y1), Math.max(x1, x0), Math.max(y1, y0), pincel);
                        invalidate();

                        break;





                }
                break;

            case lineaRecta:
                x = event.getX();
                y = event.getY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x0 = x;
                        y0 = y;
                        x1 = x;
                        y1 = y;

                        break;
                    case MotionEvent.ACTION_MOVE:

                        x1 = x;
                        y1 = y;

                        invalidate();

                        break;
                    case MotionEvent.ACTION_UP:
                        x1 = x;
                        y1 = y;

                        lienzoFondo.drawLine(x0,y0,x1,y1,pincel);
                        invalidate();

                        break;





                }
                break;
            }
            return true;

    }


    public void circulo(){
        f = forma.circulo;
        pincel.setStyle(Paint.Style.FILL);
    }
    public void linea(){
        f = forma.linea;
        pincel.setStyle(Paint.Style.STROKE);
    }
    public void rectangulo(){
        f = forma.rectangulo;
        pincel.setStyle(Paint.Style.FILL);
    }

    public void lineaRecta(){
        f = forma.lineaRecta;
        pincel.setStyle(Paint.Style.STROKE);
    }
    public void soloBordes(){
        pincel.setStyle(Paint.Style.STROKE);
    }
    public void conRelleno(){
        pincel.setStyle(Paint.Style.FILL);
    }
    public void nueva(){

        mapaDeBits = Bitmap.createBitmap(ancho, alto,
                Bitmap.Config.ARGB_8888);
        lienzoFondo = new Canvas(mapaDeBits);
        invalidate();
    }
    public void changeColor(int x){
        pincel.setColor(x);
    }

    public void importarN(File f){

        mapaDeBits = Bitmap.createBitmap(ancho, alto,
                Bitmap.Config.ARGB_8888);
        if (f.exists()){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable=true;
            mapaDeBits=BitmapFactory.decodeFile(
                    f.getAbsolutePath(),options);
        }
        lienzoFondo = new Canvas(mapaDeBits);
        invalidate();
    }

    public void exportar() {
        Bitmap imagen = getBitmap(lienzoFondo);
        File carpeta = new File(Environment.getExternalStorageDirectory().getPath());
        Calendar cal = new GregorianCalendar();
        java.util.Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-hh-mm");
        String format = df.format(date);
        String nombre = format + ".png";
        try {
            File archivo = new File(carpeta, nombre);
            FileOutputStream fos = new FileOutputStream(archivo);
            imagen.compress(Bitmap.CompressFormat.PNG, 0, fos);
            loadImg(archivo);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadImg(File f) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(f);
        intent.setData(uri);
        ctx.sendBroadcast(intent);
    }

    public static Bitmap getBitmap(Canvas canvas) {
        try {
            java.lang.reflect.Field field = Canvas.class.getDeclaredField("mBitmap");
            field.setAccessible(true);
            return (Bitmap) field.get(canvas);
        } catch (Throwable t) {
            return null;
        }
    }

}
