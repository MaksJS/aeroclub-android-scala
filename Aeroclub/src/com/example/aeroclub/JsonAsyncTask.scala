package com.example.aeroclub

import android.os.AsyncTask
import java.io.{IOException, BufferedReader, InputStreamReader}
import org.json.{JSONObject, JSONArray, JSONException}
import org.apache.http.{HttpEntity, HttpResponse, HttpStatus}
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import android.util.Log

// http://developer.android.com/reference/android/os/AsyncTask.html
class JsonAsyncTask(callback: String => Unit) extends AsyncTask[AnyRef, Int, String] {
  
  override protected def doInBackground(url: AnyRef*): String = {
    val httpClient = new DefaultHttpClient
    // http://piotrbuda.eu/2012/12/scala-and-android-asynctask-implementation-problem.html
    val request = new HttpGet(url(0).asInstanceOf[String])
    try {
      val response = httpClient.execute(request)
      val entity   = response.getEntity
      val content  = entity.getContent
      val reader   = new BufferedReader(new InputStreamReader(content, "UTF-8"))
      convertBufferToString(reader)
    }
    catch {
      case e: IOException => e.getMessage
    }
  }
  
  override protected def onPostExecute(data: String) = callback(data)
  
  private def convertBufferToString(reader: BufferedReader): String = {
	var str = ""
	var ok  = true
	while (ok) {
	  val line = reader.readLine()
	  ok = line != null
      if (ok) {
        str += line + "\n"
      }
    }
	Log.i("json", str)
	str
  }
}