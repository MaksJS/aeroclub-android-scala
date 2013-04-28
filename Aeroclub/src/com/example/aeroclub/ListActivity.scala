package com.example.aeroclub

import android.app.Activity
import android.os.Bundle
import android.widget.{Button, Toast, ListView, ArrayAdapter}
import android.view.View
import FindView._
import scala.util.parsing.json._
import scala.collection.immutable.Map
import android.content.Intent
import android.util.Log

class ListActivity extends Activity with FindView {
  
  val url      = "http://192.168.1.20:9000"
  var listType = ""
    
  override def onCreate(savedInstanceState : Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.list)
    
    listType = getIntent.getStringExtra("listType")
    new JsonAsyncTask(updateListView).execute(s"$url/$listType.json")
    
    findView[Button](R.id.back_button).onClick { view: View =>
      startActivity(new Intent(ListActivity.this, classOf[MainActivity]))
    }
  }
  
  def updateListView(data: String) {
    Toast.makeText(this, R.string.loaded, Toast.LENGTH_LONG).show
    val list = JSON.parseFull(data) match {
      case Some(l: List[Map[String, Any]]) => l
      case None => List(Map(R.string.error.toString -> R.string.no_item.toString))
    }
    val listView = findView[ListView](R.id.listView)
    val adapter = new ItemAdapter(this, list, listType)
    listView.setAdapter(adapter)
  }
}