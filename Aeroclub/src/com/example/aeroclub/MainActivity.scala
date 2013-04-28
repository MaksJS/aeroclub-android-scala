package com.example.aeroclub

import android.app.Activity
import android.os.Bundle
import android.widget.{Button, Toast, EditText, TextView}
import android.view.View
import FindView._
import android.content.Intent
import android.util.Log
import scala.util.parsing.json._
import android.content.SharedPreferences

class MainActivity extends Activity with FindView {
  
  var settings: SharedPreferences = _
    
  override def onCreate(savedInstanceState : Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)
    
    settings = getSharedPreferences("session", 0)
    settings.getString("username", "") match {
      case "" =>
      case _  => loggedInLayout
    }
    
    findView[Button](R.id.button_flights).onClick { view: View =>
      goToList("flights")
    }
    findView[Button](R.id.button_planes).onClick { view: View =>
      goToList("planes")
    }
    findView[Button](R.id.sign_in).onClick { view: View =>
      new LoginAsyncTask(updateLoginView).execute(
        findView[EditText](R.id.username).getText.toString,
        findView[EditText](R.id.password).getText.toString
      )
    }
    findView[Button](R.id.button_logout).onClick { view: View =>
      settings.edit.remove("username").commit
      loggedOutLayout
    }
  }
  
  private def goToList(listType: String) = {
    val intent = new Intent(MainActivity.this, classOf[ListActivity])
    intent.putExtra("listType", listType)
    startActivity(intent)
  }

  private def loggedOutLayout() {
    findView[EditText](R.id.username).setVisibility(View.VISIBLE)
    findView[EditText](R.id.password).setVisibility(View.VISIBLE)
    findView[Button](R.id.sign_in).setVisibility(View.VISIBLE)
    findView[Button](R.id.button_flights).setVisibility(View.INVISIBLE)
    findView[Button](R.id.button_planes).setVisibility(View.INVISIBLE)
    findView[Button](R.id.button_logout).setVisibility(View.INVISIBLE)
    findView[TextView](R.id.title).setText(getString(R.string.sign_in))
  }
    
  private def loggedInLayout() {
    findView[EditText](R.id.username).setVisibility(View.INVISIBLE)
    findView[EditText](R.id.password).setVisibility(View.INVISIBLE)
    findView[Button](R.id.sign_in).setVisibility(View.INVISIBLE)
    findView[Button](R.id.button_flights).setVisibility(View.VISIBLE)
    findView[Button](R.id.button_planes).setVisibility(View.VISIBLE)
    findView[Button](R.id.button_logout).setVisibility(View.VISIBLE)
    findView[TextView](R.id.title).setText(getString(R.string.connected_as) + " " + settings.getString("username", ""))
  }
  
  def updateLoginView(data: String) {
    Toast.makeText(this, R.string.validation_message, Toast.LENGTH_LONG).show
    val map = JSON.parseFull(data) match {
      case Some(m: Map[String, String]) => m
      case None => Map(R.string.error.toString -> R.string.no_item.toString)
    }
    val title   = findView[TextView](R.id.title)
    val message = map("message")
    val result  = map("result") match {
      case "success" => {
        val editor = settings.edit
        editor.putString("username", findView[EditText](R.id.username).getText.toString).commit
        loggedInLayout
        title.append(s"\n$message")
      }
      case "error" => title.setText(message)
    }
  }
}