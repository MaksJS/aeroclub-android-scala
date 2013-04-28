package com.example.aeroclub

import android.app.Activity
import android.os.Bundle
import android.widget.{Button, Toast}
import android.view.View
import FindView._
import android.content.Intent
import android.util.Log

class MainActivity extends Activity with FindView {
  
  override def onCreate(savedInstanceState : Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)
    
    findView[Button](R.id.button_flights).onClick { view: View =>
      goToList("flights")
    }
    findView[Button](R.id.button_planes).onClick { view: View =>
      goToList("planes")
    }
  }
  
  private def goToList(listType: String) = {
    val intent = new Intent(MainActivity.this, classOf[ListActivity])
    intent.putExtra("listType", listType)
    startActivity(intent)
  }
}