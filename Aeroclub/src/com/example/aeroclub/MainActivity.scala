package com.example.aeroclub

import android.app.{Activity, ListActivity}
import android.os.Bundle
import android.widget.{Button, Toast, ListView, ArrayAdapter}
import android.view.View
import FindView._
import org.json.{JSONObject, JSONArray}
import scala.util.parsing.json.JSON.parseFull
import scala.collection.immutable.Map
import java.util.ArrayList

class MainActivity extends Activity with FindView {
  
  override def onCreate(savedInstanceState : Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)
    
    new JsonAsyncTask(updateListView).execute("https://raw.github.com/MaksJS/express-simple-mvc/master/package.json")
    
    findView[Button](R.id.button).onClick { view: View =>
      Toast.makeText(this, R.string.validation_message, Toast.LENGTH_LONG).show
    }
  }
  
  def updateListView(data: String) {
    val list = parseFull(data) match {
      case Some(m: Map[String, Any]) => m.keys.map(_.toString).toArray
      case None => Array(R.string.no_item.toString)
    }
    val listView = findView[ListView](R.id.listView)
    val adapter = new ArrayAdapter[String](this, R.layout.item, list)
    listView.setAdapter(adapter)
  }
}