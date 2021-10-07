package mad.todo.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicReference;

import mad.todo.HomeScreenActivity;
import mad.todo.R;

public class UpdateTodoActivity extends AppCompatActivity {

 MaterialButton btnDueDateTime, btnAlertDateTime, btnSave, btnDelete;
 TextInputEditText etTitle;
 boolean isAlertOn;
 int dyear, dmonth, dday, dhour, dminute;
 int ayear, amonth, aday, ahour, aminute;
 Integer myDYear, myDMonth, myDDay, myDhour, myDMinute;
 Integer myAYear, myAMonth, myADay, myAhour, myAMinute;
 String AmOrPm, strTitle, priority;
 DatePickerDialog.OnDateSetListener dueDateListener, alertDateListener;
 TimePickerDialog.OnTimeSetListener dueTimeListener, alertTimeListener;
 SwitchMaterial swAlert;
 ProgressBar pbSave;
 FirebaseFirestore firebaseFirestore;
 FirebaseAuth firebaseAuth;
 FirebaseUser firebaseUser;
 String docId;
 RadioGroup rgPriority;
 MaterialRadioButton rbLow, rbHigh, rbMedium;

 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_update_todo);

  etTitle = findViewById(R.id.etTitle);
  btnDueDateTime = findViewById(R.id.btnDueDateTime);
  btnAlertDateTime = findViewById(R.id.btnAlertDateTime);
  swAlert = findViewById(R.id.swAlert);
  btnSave = findViewById(R.id.btnSave);
  pbSave = findViewById(R.id.pbSave);
  btnDelete = findViewById(R.id.btnDelete);
  rgPriority = findViewById(R.id.rgPriority);
  rbHigh = findViewById(R.id.rbHigh);
  rbMedium = findViewById(R.id.rbMedium);
  rbLow = findViewById(R.id.rbLow);

  AtomicReference<Intent> intent = new AtomicReference<>(getIntent());
  Bundle bundle = intent.get().getExtras();
  docId = bundle.getString("id");
  Todos todo = (Todos) intent.get().getSerializableExtra("todo");
  etTitle.setText(todo.getTitle());
  if (todo.priority.equals("low"))
   rbLow.setChecked(true);
  else if (todo.priority.equals("medium"))
   rbMedium.setChecked(true);
  else
   rbHigh.setChecked(true);


  myDDay = todo.getDday();
  myDMonth = todo.getDmonth();
  myDYear = todo.getDyear();
  btnDueDateTime.setText("Due:" + myDDay + "/" + (myDMonth + 1) + "/" + myDYear);

  myDMinute = todo.getDmin();
  String strMyMinute = String.format("%02d", myDMinute);
  myDhour = todo.getDhour();


  if (myDhour >= 12) {
   AmOrPm = "pm";
   if (myDhour > 12) {
    myDhour = myDhour - 12;
   }

  } else
   AmOrPm = "am";
  String strMyHour = String.format("%02d", myDhour);
  btnDueDateTime.setText(btnDueDateTime.getText() + " " + strMyHour + ":" + strMyMinute + AmOrPm);
  Log.d("TAG", "getView: todo = " + todo.getAlertOn());

  if (todo.isAlertOn) {
   swAlert.setChecked(true);
   isAlertOn = true;


   myADay = todo.getAday();
   myAMonth = todo.getAmonth();
   myAYear = todo.getAyear();
   btnAlertDateTime.setText("Alert:" + myADay + "/" + (myAMonth + 1) + "/" + myAYear);
   myAMinute = todo.getAmin();
   strMyMinute = String.format("%02d", myAMinute);
   myAhour = todo.getAhour();


   if (myAhour >= 12) {
    AmOrPm = "pm";
    if (myAhour > 12) {
     myAhour = myAhour - 12;
    }

   } else
    AmOrPm = "am";
   String strMyHour2 = String.format("%02d", myAhour);
   btnAlertDateTime.setText(btnAlertDateTime.getText() + " " + strMyHour2 + ":" + strMyMinute + AmOrPm);


  } else {
   swAlert.setChecked(false);
   isAlertOn = false;
  }


  Log.d("TAG", "onCreate: todo = " + todo.getTitle());

  firebaseAuth = FirebaseAuth.getInstance();
  firebaseFirestore = FirebaseFirestore.getInstance();

  firebaseUser = firebaseAuth.getCurrentUser();
  //btnAlertDateTime.setEnabled(false);
  pbSave.setVisibility(View.INVISIBLE);


  swAlert.setOnClickListener(v -> {
   if (swAlert.isChecked()) {
    btnAlertDateTime.setEnabled(true);
    isAlertOn = true;

   } else {
    btnAlertDateTime.setEnabled(false);
    isAlertOn = false;
   }
  });


  btnDueDateTime.setOnClickListener(v -> {

   Calendar calendar = Calendar.getInstance();
   dyear = calendar.get(Calendar.YEAR);
   dmonth = calendar.get(Calendar.MONTH);
   dday = calendar.get(Calendar.DAY_OF_MONTH);
   DatePickerDialog datePickerDialog1 = new DatePickerDialog(this, dueDateListener, dyear, dmonth, dday);
   datePickerDialog1.show();

  });

  btnAlertDateTime.setOnClickListener(v -> {

   Calendar calendar = Calendar.getInstance();
   ayear = calendar.get(Calendar.YEAR);
   amonth = calendar.get(Calendar.MONTH);
   aday = calendar.get(Calendar.DAY_OF_MONTH);
   DatePickerDialog datePickerDialog2 = new DatePickerDialog(this, alertDateListener, ayear, amonth, aday);
   datePickerDialog2.show();

  });


  dueDateListener = ((view, year, month, dayOfMonth) -> {
   myDDay = dayOfMonth;
   myDMonth = month;
   myDYear = year;
   btnDueDateTime.setText("Due:" + myDDay + "/" + (myDMonth + 1) + "/" + myDYear);

   Calendar c = Calendar.getInstance();
   dhour = c.get(Calendar.HOUR);
   dminute = c.get(Calendar.MINUTE);
   TimePickerDialog timePickerDialog1 = new TimePickerDialog(this, dueTimeListener, dhour, dminute, DateFormat.is24HourFormat(this));
   timePickerDialog1.show();

  });

  dueTimeListener = ((view, hourOfDay, minute1) -> {
   myDMinute = minute1;
   String strMyMinute2 = String.format("%02d", myDMinute);
   myDhour = hourOfDay;

   int myDhour2;
   if (myDhour >= 12) {
    AmOrPm = "pm";
    myDhour2 = myDhour;
    if (myDhour > 12) {
     myDhour2 = myDhour - 12;
    }

   } else {
    myDhour2 = myDhour;
    AmOrPm = "am";
   }
   String strMyHour2 = String.format("%02d", myDhour2);
   btnDueDateTime.setText(btnDueDateTime.getText() + " " + strMyHour2 + ":" + strMyMinute2 + AmOrPm);

  });

  alertDateListener = ((view, year1, month1, dayOfMonth) -> {
   myADay = dayOfMonth;
   myAMonth = month1;
   myAYear = year1;
   btnAlertDateTime.setText("Alert:" + myADay + "/" + (myAMonth + 1) + "/" + myAYear);

   Calendar c = Calendar.getInstance();
   ahour = c.get(Calendar.HOUR);
   aminute = c.get(Calendar.MINUTE);
   TimePickerDialog timePickerDialog2 = new TimePickerDialog(this, alertTimeListener, ahour, aminute, DateFormat.is24HourFormat(this));
   timePickerDialog2.show();


  });

  alertTimeListener = ((view, hourOfDay, minute1) -> {
   int myAhour2;
   myAMinute = minute1;
   String strMyMinute2 = String.format("%02d", myAMinute);
   myAhour = hourOfDay;


   if (myAhour >= 12) {
    AmOrPm = "pm";
    myAhour2 = myAhour;
    if (myAhour > 12) {
     myAhour2 = myAhour - 12;
    }

   } else {
    myAhour2 = myAhour;
    AmOrPm = "am";
   }
   String strMyHour2 = String.format("%02d", myAhour2);
   btnAlertDateTime.setText(btnAlertDateTime.getText() + " " + strMyHour2 + ":" + strMyMinute2 + AmOrPm);


  });


  btnSave.setOnClickListener(v -> {
   int id = rgPriority.getCheckedRadioButtonId();
   if (id == R.id.rbLow) {
    priority = "low";
   } else if (id == R.id.rbMedium)
    priority = "medium";
   else
    priority = "high";

   try {
    strTitle = etTitle.getText().toString();

   } catch (Exception e) {
    Toast.makeText(this, "Please give valid title !", Toast.LENGTH_SHORT).show();
    return;
   }


   if (etTitle.getText().toString().isEmpty()) {
    Toast.makeText(this, "Please give valid title !", Toast.LENGTH_SHORT).show();
    return;
   }

   if (myDDay == null || myDMinute == null) {

    Toast.makeText(this, "Please set Due date and time properly !", Toast.LENGTH_SHORT).show();
    return;


   }

   if (swAlert.isChecked()) {
    if (myADay == null || myAMinute == null) {
     Toast.makeText(this, "Please set Alert date and time properly !", Toast.LENGTH_SHORT).show();
     return;
    }


   }

   pbSave.setVisibility(View.VISIBLE);
   DocumentReference documentReference = firebaseFirestore.collection("todos").document(firebaseUser.getUid()).collection("myTodos").document(HomeScreenActivity.docID);
   todo.setTitle(strTitle);
   todo.setPriority(priority);
   todo.setDday(myDDay);
   todo.setDmonth(myDMonth);
   todo.setDyear(myDYear);
   todo.setDhour(myDhour);
   todo.setDmin(myDMinute);
   todo.setAlertOn(isAlertOn);
   todo.setAday(myADay);
   todo.setAmonth(myAMonth);
   todo.setAyear(myAYear);
   todo.setAhour(myAhour);
   todo.setAmin(myAMinute);
   todo.setCompleted(false);

   Calendar dueDay = Calendar.getInstance();

   dueDay.set(todo.getDyear(), todo.getDmonth(), todo.getDday(), todo.getDhour(), todo.getDmin());


   long millis1 = dueDay.getTimeInMillis();


   if (System.currentTimeMillis() > millis1) {
    Toast.makeText(this, "Enter Valid Due date and time !", Toast.LENGTH_SHORT).show();
    pbSave.setVisibility(View.INVISIBLE);
    return;
   }


   if (isAlertOn) {
    Calendar alertDay = Calendar.getInstance();

    alertDay.set(todo.getAyear(), todo.getAmonth(), todo.getAday(), todo.getAhour(), todo.getAmin());


    millis1 = alertDay.getTimeInMillis();


    if (System.currentTimeMillis() > millis1) {
     Toast.makeText(this, "Enter Valid Alert date and time !", Toast.LENGTH_SHORT).show();
     pbSave.setVisibility(View.INVISIBLE);
     return;
    }
   }


   documentReference.set(todo).addOnSuccessListener(aVoid -> {
    Toast.makeText(this, "Todo updated successfully.", Toast.LENGTH_SHORT).show();
    intent.set(new Intent(this, HomeScreenActivity.class));
    startActivity(intent.get());
    finish();


   }).addOnFailureListener(new OnFailureListener() {
    @Override
    public void onFailure(@NonNull Exception e) {
     Toast.makeText(UpdateTodoActivity.this, "Error, Try again.", Toast.LENGTH_SHORT).show();
     pbSave.setVisibility(View.VISIBLE);
    }
   });


  });

  btnDelete.setOnClickListener(v -> {
   Log.d("TAG", "onCreate: docID :" + docId);
   DocumentReference docRef = firebaseFirestore.collection("todos").document(firebaseUser.getUid()).collection("myTodos").document(HomeScreenActivity.docID);
   docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
    @Override
    public void onSuccess(Void aVoid) {
     finish();
     Toast.makeText(UpdateTodoActivity.this, "Todo Deleted Successfully !", Toast.LENGTH_SHORT).show();

     Intent intent1 = new Intent(UpdateTodoActivity.this, HomeScreenActivity.class);
     startActivity(intent1);
    }
   }).addOnFailureListener(new OnFailureListener() {
    @Override
    public void onFailure(@NonNull Exception e) {
     Toast.makeText(UpdateTodoActivity.this, "Error in Deleting Note.", Toast.LENGTH_SHORT).show();
    }
   });
  });


 }

 @Override
 public void onBackPressed() {
  super.onBackPressed();
  finish();
  Intent intent = new Intent(UpdateTodoActivity.this, HomeScreenActivity.class);
  startActivity(intent);
 }
}
