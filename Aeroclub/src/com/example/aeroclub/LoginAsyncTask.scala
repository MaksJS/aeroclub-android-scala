package com.example.aeroclub

import android.os.AsyncTask
import org.apache.http.{HttpEntity, HttpResponse, HttpStatus, NameValuePair}
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient
import java.io.IOException
import org.apache.http.client.ClientProtocolException
import scala.io._
import org.apache.http.entity.StringEntity
import android.util.Log

class LoginAsyncTask(callback: String => Unit) extends AsyncTask[AnyRef, Int, String] {
  
  override protected def doInBackground(credentials: AnyRef*): String = {
    
    val username = credentials(0).asInstanceOf[String]
    val password = credentials(1).asInstanceOf[String]
    val json = s"""{"username": "$username","password": "$password"}"""
    val httpClient = new DefaultHttpClient
    val request = new HttpPost("http://192.168.1.20:9000/login")
    request.setHeader("Content-type", "application/json")
    request.setHeader("Accept-Language", "fr") // get the i18n message in french
    request.setEntity(new StringEntity(json))
    
    try {
      val response = httpClient.execute(request)
      val entity   = response.getEntity
      val content  = entity.getContent
      Source.fromInputStream(content).getLines().mkString
    }
    catch {
      case e: IOException             => e.getMessage
      case e: ClientProtocolException => e.getMessage
    }
    finally {
      httpClient.getConnectionManager.shutdown
    }
  }
  
  override protected def onPostExecute(data: String) = callback(data)
}