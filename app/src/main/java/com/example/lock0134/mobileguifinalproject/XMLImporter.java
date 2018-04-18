package com.example.lock0134.mobileguifinalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Created by William on 2018-04-14.
 */

public class XMLImporter extends AsyncTask<String, Integer, String> {
    private ProgressBar pBar = null;
    URL url;
    List<Question> questions = new ArrayList();
    InputStream is;
    XmlPullParser parser;


    public XMLImporter(URL url){
     this.url = url;
    }
    public void setpBar(ProgressBar pBar){
        this.pBar = pBar;
    }
    public List<Question> getQuestions(){
        return questions;
    }
    @Override
    protected String doInBackground(String... args) {

        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            conn.connect();
            is = conn.getInputStream();

            parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);
            int eventType = parser.getEventType();
            String text = null;
            while(parser.next() != XmlPullParser.END_DOCUMENT) {
                eventType = parser.getEventType();
                String tagName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if(tagName.equals("MultipleChoiceQuestion") || tagName.equals("NumericQuestion") ||
                        tagName.equals("TrueFalseQuestion")) {
                            questions.add(checkStartTag(tagName));
                            onProgressUpdate(25);
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:

                        if  (tagName.equalsIgnoreCase("Answer")) {
                            questions.get(questions.size()-1).getAnswers().add(text);


                        }

                        break;

                    default:
                        break;
                }



            }
onProgressUpdate(100);
            return null;
        }catch(XmlPullParserException e2){
        }catch(IOException e) {


        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch(IOException e) {

            }
        }

        onPostExecute("s");
        return null;

    }

    @Override
    protected void onPostExecute(String result) {
        pBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        pBar.setProgress(values[0]);
    }

    private Question checkStartTag(String tagName){
        Question q = new Question();
        switch(tagName){
            case "MultipleChoiceQuestion":
                q.setType("multipleChoice");
                q.setRightAnswer(parser.getAttributeValue(null , "correct"));
                q.setQuestion(parser.getAttributeValue(null , "question"));
                break;

            case "NumericQuestion":
                q.setType("numeric");
                q.setDigits(Integer.parseInt(parser.getAttributeValue(null , "accuracy")));
                q.setRightAnswer(parser.getAttributeValue(null , "answer"));
                q.setQuestion(parser.getAttributeValue(null , "question"));
                break;
            case "TrueFalseQuestion":
                q.setType("trueFalse");

                q.setRightAnswer(parser.getAttributeValue(null , "answer"));
                q.setQuestion(parser.getAttributeValue(null , "question"));
        }
        return q;
    }
}
