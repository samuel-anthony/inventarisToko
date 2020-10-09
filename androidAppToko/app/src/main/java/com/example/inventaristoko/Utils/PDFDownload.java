package com.example.inventaristoko.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;

import com.example.inventaristoko.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class PDFDownload {

    String header_title = "Warung Bakso Mbah Welo";
    String file_name;
    public PDFDownload(String file_name){
        this.file_name = file_name+" "+CommonUtils.dateFormat();
    }

    public void download(List<String> columnName, List<String> jsonKey, JSONArray data, Context appContext) throws JSONException {

         PdfDocument pdfDocument = new PdfDocument();
         Paint myPaint = new Paint();
         Paint myPaintStroke = new Paint();

         PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(1200,1800,1).create();
         PdfDocument.Page myPage = pdfDocument.startPage(myPageInfo);
         Canvas canvas = myPage.getCanvas();

         myPaint.setTextSize(12f);
         myPaint.setTextAlign(Paint.Align.CENTER);
         canvas.drawText(header_title,myPageInfo.getPageWidth()/2,40,myPaint);


         myPaint.setTextSize(10f);
         int differ = 0;
         myPaint.setTextAlign(Paint.Align.LEFT);
         myPaintStroke.setStrokeWidth(1);
         myPaintStroke.setStyle(Paint.Style.STROKE);
         int startPositionY = 100,startPositionX = 10,endPositionX = myPageInfo.getPageWidth() - 10;
         canvas.drawLine(startPositionX,startPositionY-25,endPositionX,startPositionY-25,myPaintStroke);
         for(int i = 0; i < columnName.size(); i++){
             canvas.drawText(columnName.get(i),20+startPositionX+(myPageInfo.getPageWidth()/columnName.size()*i),startPositionY,myPaint);
             canvas.drawLine(startPositionX+(myPageInfo.getPageWidth()/columnName.size()*(i+1)),startPositionY-25,startPositionX+(myPageInfo.getPageWidth()/columnName.size()*(i+1)),startPositionY+(50*(data.length()+1))-25,myPaintStroke);
         }
         startPositionY+=25;
         canvas.drawLine(startPositionX,startPositionY,endPositionX,startPositionY,myPaintStroke);
         startPositionY+=25;
         for(int i = 0; i < data.length(); i++){
             try{
                 JSONObject obj = data.getJSONObject(i);
                 for(int j = 0; j < jsonKey.size();j++) {
                     String[] a = obj.getString(jsonKey.get(j)).split("\n");
                     for(int k = 0; k < a.length; k++){
                        canvas.drawText(a[k],20+startPositionX+(myPageInfo.getPageWidth()/columnName.size()*j),k*25+startPositionY,myPaint);
                     }
                 }
                 int enters = obj.has("enter") ? obj.getInt("enter") :1;
                 startPositionY+=enters*25;
                 canvas.drawLine(startPositionX,startPositionY,endPositionX,startPositionY,myPaintStroke);
                 startPositionY+=25;
                 differ+=enters-1;
             }
             catch (Exception e){
                e.printStackTrace();
             }
         }
         for(int i = 0; i < columnName.size(); i++){
             canvas.drawLine(startPositionX+(myPageInfo.getPageWidth()/columnName.size()*(i+1)),startPositionY-(differ*25)-25,startPositionX+(myPageInfo.getPageWidth()/columnName.size()*(i+1)),startPositionY-25,myPaintStroke);
         }
         pdfDocument.finishPage(myPage);
         File file = new File(Environment.getExternalStorageDirectory(),"/"+file_name+".pdf");
         int i = 1;
         while(file.exists()){
             file = new File(Environment.getExternalStorageDirectory(),"/"+file_name+"("+i+").pdf");
             i++;
         }
         try {
             pdfDocument.writeTo(new FileOutputStream(file));
             CommonUtils.showToast(appContext, appContext.getString(R.string.label_berhasil_download) + " " + file);
         }catch (IOException e){
             e.printStackTrace();
         }
         pdfDocument.close();
    }
}
