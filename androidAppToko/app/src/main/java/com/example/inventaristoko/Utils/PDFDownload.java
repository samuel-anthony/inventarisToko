package com.example.inventaristoko.Utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;

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

    public void download(List<String> columnName,List<String> jsonKey, JSONArray data) throws JSONException {

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
                     canvas.drawText(obj.getString(jsonKey.get(j)),20+startPositionX+(myPageInfo.getPageWidth()/columnName.size()*j),startPositionY,myPaint);
                 }
                 startPositionY+=25;
                 canvas.drawLine(startPositionX,startPositionY,endPositionX,startPositionY,myPaintStroke);
                 startPositionY+=25;
             }
             catch (Exception e){

             }
         }

         pdfDocument.finishPage(myPage);
         File file = new File(Environment.getExternalStorageDirectory(),"/testAja.pdf");
         try {
             pdfDocument.writeTo(new FileOutputStream(file));
         }catch (IOException e){
             e.printStackTrace();
         }
         pdfDocument.close();
    }
}
