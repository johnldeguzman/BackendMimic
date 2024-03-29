package com.mimic.accesrest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

public class uploadfile {



public void doFileUpload(String path){
    HttpURLConnection conn = null;
    DataOutputStream dos = null;
    DataInputStream inStream = null;
    String lineEnd = "\r\n";
    int bytesRead, bytesAvailable, bufferSize;
    byte[] buffer;
    int maxBufferSize = 1*1024*1024;
    String urlString = "http://192.168.1.131:8000/";   // server ip
    try
    {
     //------------------ CLIENT REQUEST
    FileInputStream fileInputStream = new FileInputStream(new File(path) );
     // open a URL connection to the Servlet
     URL url = new URL(urlString);
     // Open a HTTP connection to the URL
     conn = (HttpURLConnection) url.openConnection();
     // Allow Inputs
     conn.setDoInput(true);
     // Allow Outputs
     conn.setDoOutput(true);
     // Don't use a cached copy.
     conn.setUseCaches(false);
     // Use a post method.
     conn.setRequestMethod("POST");
     conn.setRequestProperty("Connection", "Keep-Alive");
     conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+"    ");
     dos = new DataOutputStream( conn.getOutputStream() );
     dos.writeBytes(lineEnd);
     dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + path + "\"" + lineEnd);
     dos.writeBytes(lineEnd);

     // create a buffer of maximum size
     bytesAvailable = fileInputStream.available();
     bufferSize = Math.min(bytesAvailable, maxBufferSize);
     buffer = new byte[bufferSize];

     // read file and write it into form...
     bytesRead = fileInputStream.read(buffer, 0, bufferSize);
     while (bytesRead > 0)
     {
      dos.write(buffer, 0, bufferSize);
      bytesAvailable = fileInputStream.available();
      bufferSize = Math.min(bytesAvailable, maxBufferSize);
      bytesRead = fileInputStream.read(buffer, 0, bufferSize);
     }

     // send multipart form data necesssary after file data...
     dos.writeBytes(lineEnd);
     dos.writeBytes(lineEnd);

     // close streams
     Log.e("Debug","File is written");
     fileInputStream.close();
     dos.flush();
     dos.close();
    }
    catch (MalformedURLException ex)
    {
         Log.e("Debug", "error: " + ex.getMessage(), ex);
    }
    catch (IOException ioe)
    {
         Log.e("Debug", "error: " + ioe.getMessage(), ioe);
    }

    //------------------ read the SERVER RESPONSE
    try {
          inStream = new DataInputStream ( conn.getInputStream() );
          String str;
          while (( str = inStream.readLine()) != null)
          {
               Log.e("Debug","Server Response "+str);
          }
          inStream.close();
    }
    catch (IOException ioex){
         Log.e("Debug", "error: " + ioex.getMessage(), ioex);
    }
  }
}
