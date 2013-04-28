package com.example.aeroclub

import android.os.AsyncTask
import java.io.IOException
import scala.io._

// http://developer.android.com/reference/android/os/AsyncTask.html
class JsonAsyncTask(callback: String => Unit) extends AsyncTask[AnyRef, Int, String] {
  
  // http://piotrbuda.eu/2012/12/scala-and-android-asynctask-implementation-problem.html
  override protected def doInBackground(url: AnyRef*): String = {
    try {
      Source.fromURL(url(0).asInstanceOf[String]).mkString
    }
    catch {
      case e: IOException => e.getMessage
    }
  }
  
  override protected def onPostExecute(data: String) = callback(data)
}