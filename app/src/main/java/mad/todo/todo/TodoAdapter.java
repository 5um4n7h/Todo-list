package mad.todo.todo;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mad.todo.HomeScreenActivity;
import mad.todo.R;


public class TodoAdapter extends ArrayAdapter<Todos> {
 public TodoAdapter(Context context, List<Todos> object) {
  super(context, 0, object);
 }

 @Override
 public View getView(int position, View convertView, ViewGroup parent) {
  if (convertView == null) {
   convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.row_item, parent, false);
  }

  TextView tvTitle = convertView.findViewById(R.id.tvTitle);
  TextView tvDueDateTime = convertView.findViewById(R.id.tvDueDateTime);
  TextView tvAlertDateTime = convertView.findViewById(R.id.tvAlertDateTime);
  TextView tvPriority = convertView.findViewById(R.id.tvPriority);
  ImageView ivAlert = convertView.findViewById(R.id.ivAlert);


  Todos todo = getItem(position);

  tvTitle.setText(todo.getTitle());
  tvPriority.setText("Priority : " + todo.getPriority());

  Integer dDay = todo.getDday();
  Integer dMonth = todo.getDmonth();
  Integer dYear = todo.getDyear();
  Integer dHour = todo.getDhour();
  String isAmOrPm;
  if (dHour >= 12) {
   isAmOrPm = "Pm";
   if (dHour > 12) {
    dHour = dHour - 12;
   }

  } else {
   isAmOrPm = "Am";
  }
  Integer dMin = todo.getDmin();


  tvDueDateTime.setText("Due : " + dDay + "/" + (dMonth + 1) + "/" + dYear + " " + dHour + ":" + dMin + isAmOrPm);
  Log.d("TAG", "getView: todo = " + todo.getAlertOn());
  Boolean isAlertOn = todo.getAlertOn();
  if (isAlertOn) {
   Integer aDay = todo.getAday();
   Integer aMonth = todo.getAmonth();
   Integer aYear = todo.getAyear();
   Integer aHour = todo.getAhour();
   Integer aMin = todo.getAmin();

   if (aHour >= 12) {
    isAmOrPm = "Pm";
    if (aHour > 12) {
     aHour = aHour - 12;
    }

   } else {
    isAmOrPm = "Am";

   }


   //String strAMin = String.format("02d%",aMin);

   tvAlertDateTime.setText("Alert : " + aDay + "/" + (aMonth + 1) + "/" + aYear + " " + aHour + ":" + aMin + isAmOrPm);


  } else {
   tvAlertDateTime.setVisibility(View.GONE);
   ivAlert.setVisibility(View.GONE);
  }


//
//
//		if(todo.isAlertOn==true) {
//			int reqcode = todo.reqCode;
//			int aday = todo.aday;
//			int amonth = todo.amonth;
//			int ayear = todo.ayear;
//			int ahour = todo.ahour;
//			int amin = todo.amin;
//
//			Notification notification = new Notification();
//
//			HomeScreenActivity homeScreenActivity = new HomeScreenActivity();
//			try {
//
//			} catch (Exception e) {
//				Log.d("TAG", "getView: " + e.toString());
//				e.printStackTrace();
//			}
  //}


  return convertView;
 }


}
