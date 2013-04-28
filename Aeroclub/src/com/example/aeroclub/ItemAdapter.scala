package com.example.aeroclub

import android.widget.{BaseAdapter, ArrayAdapter}
import android.content.Context
import android.widget.TextView
import android.view.{View, ViewGroup, LayoutInflater}
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date

// https://bitbucket.org/synic/dupacount/src/bb18dcde97d3/src/main/scala/timeradapter.scala
// http://www.ace-art.fr/wordpress/2010/07/21/tutoriel-android-partie-6-les-listview/
class ItemAdapter(context: Context, list: List[Map[String, Any]], listType: String) extends BaseAdapter {

  override def getCount: Int = list.size
  override def getItem(position: Int) : Map[String, Any] = list(position)
  override def getItemId(position: Int): Long = position
  
  val translatedKeys = Map(
    "matriculation"   -> "Immatriculation",
    "price"           -> "Prix",
    "weekReduction"   -> "Réduction semaine",
    "duration"        -> "Durée",
    "specialPrice"    -> "Prix spécial",
    "date"            -> "Date",
    "flightReduction" -> "Réduction vol",
    "initiationFee"   -> "Forfait initiation",
    "flights"         -> "Vol",
    "planes"          -> "Avion"
  )
  
  override def getView(position: Int, convertView: View, parent: ViewGroup): View = {
    val inflater     = LayoutInflater.from(context)
    val view         = inflater.inflate(R.layout.item, parent, false)
    val title        = view.findViewById(R.id.title).asInstanceOf[TextView]
    val informations = view.findViewById(R.id.informations).asInstanceOf[TextView]
    val map          = getItem(position)
    
    title.setText(translatedKeys(listType) + " n°" + map("id").asInstanceOf[Double].toInt + "\n")
    
    map.foreach { keyVal =>
      val key = translatedKeys get keyVal._1 match {
        case Some(k) => k
        case None    => keyVal._1
      }
      val value = keyVal._2 match {
        case b: Boolean => if (b) "oui" else "non"
        case d: Double  => d.toString
        case s: String  => s
      }
      if (key != "id")
        informations.append(s"$key : $value\n")
    }
    view
  }
}