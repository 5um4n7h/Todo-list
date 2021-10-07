package mad.todo.todo;

import java.io.Serializable;

public class Todos implements Serializable {
 public int id;
 String title, priority;
 Boolean isAlertOn, isCompleted;
 Integer dday, dmonth, dyear, dhour, dmin, aday, amonth, ayear, ahour, amin;

 public Todos() {
 }

 public Todos(int id, String title, String priority, Integer dday, Integer dmonth, Integer dyear, Integer dhour, Integer dmin, Boolean isAlertOn, Integer aday, Integer amonth, Integer ayear, Integer ahour, Integer amin, Boolean isCompleted) {
  this.id = id;
  this.title = title;
  this.dday = dday;
  this.dmonth = dmonth;
  this.dyear = dyear;
  this.dhour = dhour;
  this.dmin = dmin;
  this.isAlertOn = isAlertOn;
  this.aday = aday;
  this.amonth = amonth;
  this.ayear = ayear;
  this.ahour = ahour;
  this.amin = amin;
  this.isCompleted = isCompleted;
  this.priority = priority;
 }

 public String getPriority() {
  return priority;
 }

 public void setPriority(String priority) {
  this.priority = priority;
 }

 public Boolean getCompleted() {
  return isCompleted;
 }

 public void setCompleted(Boolean completed) {
  isCompleted = completed;
 }

 public int getId() {
  return id;
 }

 public void setId(int id) {
  this.id = id;
 }

 public String getTitle() {
  return title;
 }

 public Boolean getAlertOn() {
  return isAlertOn;
 }

 public Integer getDday() {
  return dday;
 }

 public Integer getDmonth() {
  return dmonth;
 }

 public Integer getDyear() {
  return dyear;
 }

 public Integer getDhour() {
  return dhour;
 }

 public Integer getDmin() {
  return dmin;
 }

 public Integer getAday() {
  return aday;
 }

 public Integer getAmonth() {
  return amonth;
 }

 public Integer getAyear() {
  return ayear;
 }

 public Integer getAhour() {
  return ahour;
 }

 public Integer getAmin() {
  return amin;
 }

 public void setTitle(String title) {
  this.title = title;
 }

 public void setAlertOn(Boolean alertOn) {
  isAlertOn = alertOn;
 }

 public void setDday(Integer dday) {
  this.dday = dday;
 }

 public void setDmonth(Integer dmonth) {
  this.dmonth = dmonth;
 }

 public void setDyear(Integer dyear) {
  this.dyear = dyear;
 }

 public void setDhour(Integer dhour) {
  this.dhour = dhour;
 }

 public void setDmin(Integer dmin) {
  this.dmin = dmin;
 }

 public void setAday(Integer aday) {
  this.aday = aday;
 }

 public void setAmonth(Integer amonth) {
  this.amonth = amonth;
 }

 public void setAyear(Integer ayear) {
  this.ayear = ayear;
 }

 public void setAhour(Integer ahour) {
  this.ahour = ahour;
 }

 public void setAmin(Integer amin) {
  this.amin = amin;
 }
}
