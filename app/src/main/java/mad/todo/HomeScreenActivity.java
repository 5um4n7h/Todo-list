package mad.todo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mad.todo.auth.LoginActivity;
import mad.todo.notify.AlaramReceiver;
import mad.todo.todo.AddTodoActivity;
import mad.todo.todo.TodoAdapter;
import mad.todo.todo.Todos;
import mad.todo.todo.UpdateTodoActivity;


public class HomeScreenActivity extends AppCompatActivity {
 ListView lvTodos;
 FirebaseFirestore firebaseFirestore;
 FirebaseAuth firebaseAuth;
 FirebaseUser firebaseUser;
 FloatingActionButton floatingActionButton;

 public static Context context;
 List<Todos> todosList;
 TodoAdapter adapter;
 public static List<DocumentSnapshot> list;
 public static String docID;
 String notify = "You have an upcoming task due !";


 @RequiresApi(api = Build.VERSION_CODES.O)
 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_home_screen);

  setSupportActionBar(findViewById(R.id.toolbar));
  lvTodos = findViewById(R.id.lvNotes);
  floatingActionButton = findViewById(R.id.fabAddNotes);


  context = getApplicationContext();
  firebaseAuth = FirebaseAuth.getInstance();
  firebaseUser = firebaseAuth.getCurrentUser();
  firebaseFirestore = FirebaseFirestore.getInstance();

  floatingActionButton.setOnClickListener(v -> {
   Intent intent = new Intent(this, AddTodoActivity.class);
   startActivity(intent);
   finish();

  });


  firebaseFirestore.collection("todos").document(firebaseUser.getUid()).collection("myTodos").get()
    .addOnSuccessListener(queryDocumentSnapshots -> {
     todosList = new ArrayList<>();
     // after getting the data we are calling on success method
     // and inside this method we are checking if the received
     // query snapshot is empty or not.
     if (!queryDocumentSnapshots.isEmpty()) {
      // if the snapshot is not empty we are hiding
      // our progress bar and adding our data in a list.

      list = queryDocumentSnapshots.getDocuments();

      int index = 0;
      int mills = 2000;
      for (DocumentSnapshot d : list) {


       AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
       Intent intent = new Intent(getApplicationContext(), AlaramReceiver.class);


       // after getting this list we are passing
       // that list to our object class.

       Todos todos = d.toObject(Todos.class);
       int id = todos.getId();
       intent.setAction(String.valueOf(id));
       Log.d("TAG", "onSuccess: todos  : " + index + " " + todos.toString());
       Log.d("TAG", "onSuccess: isCompleted = " + index + " " + todos.getCompleted());
       Log.d("TAG", "onSuccess: isAlertOn = " + index + " " + todos.getAlertOn());


       if (todos.getAlertOn()) {

        notify = notify + "\n" + todos.getTitle();


        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id, intent, PendingIntent.FLAG_CANCEL_CURRENT);//used unique ID as 1001

        boolean isWorking = (PendingIntent.getBroadcast(getApplicationContext(), id, intent, PendingIntent.FLAG_NO_CREATE) != null);//just changed the flag
        Log.d("TAG", "alarm is " + (isWorking ? "" : "not") + " working...");
        if (isWorking) {
         alarmManager.cancel(pendingIntent);//important
         pendingIntent.cancel();//important
         Log.d("TAG", "onSuccess: Alaram got cancelled" + index + " ");
        }

        intent = new Intent(getApplicationContext(), AlaramReceiver.class);
        intent.putExtra("title", todos.getTitle());
        String strDueHour;
        String AmOrPM;
        if (todos.getDhour() > 12) {
         strDueHour = String.valueOf((todos.getDhour() - 12));
         AmOrPM = "Pm";
        } else {
         strDueHour = String.valueOf(todos.getDhour());
         AmOrPM = "Am";
        }
        String due = "Due : " + todos.getDday() + "/" + (todos.getDmonth() + 1) + "/" + todos.getDyear() + " " + strDueHour + ":" + todos.getDmin() + " " + AmOrPM;
        intent.putExtra("due", due);
        intent.putExtra("id", id);
        intent.putExtra("priority", todos.getPriority());
        alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar alertDay = Calendar.getInstance();
        LocalDateTime now = LocalDateTime.now();
        Calendar currentDay = Calendar.getInstance();

        alertDay.set(todos.getAyear(), todos.getAmonth(), todos.getAday(), todos.getAhour(), todos.getAmin());
        currentDay.set(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(), now.getMinute());


        long millis1 = alertDay.getTimeInMillis();
        long millis2 = currentDay.getTimeInMillis();

        Log.d("TAG", "onCreate: current : " + millis2 + " alert : " + millis1);
        long diff = millis2 - millis1;
        Log.d("TAG", "onCreate: diff " + diff);

        if (alertDay.getTimeInMillis() > System.currentTimeMillis()) {
         alarmManager.set(AlarmManager.RTC_WAKEUP, alertDay.getTimeInMillis() - 10000, pendingIntent);

        }
        Log.d("TAG", "onSuccess: Alaram updated !" + index);
        mills = mills + 2000;


       }

       // after getting data from Firebase we are
       // storing that data in our array list
       todosList.add(todos);
       index++;
      }
      // after that we are passing our array list to our adapter class.
      adapter = new TodoAdapter(HomeScreenActivity.this, todosList);

      // after passing this array list to our adapter
      // class we are setting our adapter to our list view.
      lvTodos.setAdapter(adapter);
     } else {
      // if the snapshot is empty we are displaying a toast message.
      Toast.makeText(HomeScreenActivity.this, "No data found in Database", Toast.LENGTH_SHORT).show();
     }
    }).addOnFailureListener(new OnFailureListener() {
   @Override
   public void onFailure(@NonNull Exception e) {
    // we are displaying a toast message
    // when we get any error from Firebase.
    Toast.makeText(HomeScreenActivity.this, "Fail to load data..", Toast.LENGTH_SHORT).show();
   }
  });


  lvTodos.setOnItemClickListener((parent, view, position, id) -> {

   docID = list.get(position).getId();

   Log.d("TAG", "onCreate: LV clicked, docID = " + docID);

   Todos todo = todosList.get(position);

   finish();
   Intent intent = new Intent(this, UpdateTodoActivity.class);
   intent.putExtra("todo", (Serializable) todo);
   intent.putExtra("id", docID);
   startActivity(intent);


  });


 }

 @Override
 public boolean onCreateOptionsMenu(Menu menu) {
  getMenuInflater().inflate(R.menu.menu, menu);

  //MenuItem logoutItem = menu.findItem(R.id.logout);

  // Configure the search info and add any event listeners...

  return super.onCreateOptionsMenu(menu);


 }

 @Override
 public boolean onOptionsItemSelected(MenuItem item) {
  switch (item.getItemId()) {
   case R.id.logout:


    LoginActivity.mAuth.signOut();
    LoginActivity.mGoogleSignInClient.signOut();

    finish();
    Intent intent = new Intent(this, LoginActivity.class);
    startActivity(intent);

    return super.onOptionsItemSelected(item);

   case R.id.refresh:


    finish();
    intent = new Intent(this, HomeScreenActivity.class);
    startActivity(intent);

   default:
    // If we got here, the user's action was not recognized.
    // Invoke the superclass to handle it.
    return super.onOptionsItemSelected(item);

  }

 }


}