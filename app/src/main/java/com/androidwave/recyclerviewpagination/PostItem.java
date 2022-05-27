package com.androidwave.recyclerviewpagination;

public class PostItem {
  private String id;
  private int imageId;
  private String title;
  private String description;
  private String time;
  private String comment;
  private String isfinished;
  private String isreturned;
  private String inProgress;
  private String isTo;
  private String status;
  private String statusTo;

  public PostItem(String id,String title, String description,String comment,int imageId,String isreturned,String isfinished,String isTo,String status,String statusTo,String inProgress) {
    this.imageId = imageId;
    this.id = id;
    this.title = title;
    this.description = description;
    //this.time = time;
    this.comment = comment;
    this.isfinished = isfinished;
    this.isreturned = isreturned;
    this.inProgress = inProgress;
    this.isTo = isTo;
    this.status = status;
    this.statusTo = statusTo;
  }

  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public int getImageId() {
    return imageId;
  }

  public void setImageId(int imageId) {
    this.imageId = imageId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }
  public String getCmment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }


  public String getIsfinished() {
    return isfinished;
  }

  public void setIsfinished(String isfinished) {
    this.isfinished = isfinished;
  }

  public String getIsreturned() {
    return isreturned;
  }

  public void setIsreturned(String isreturned) {
    this.isreturned = isreturned;
  }

  public String getInProgress() {
    return inProgress;
  }

  public void setInProgress(String inProgress) {
    this.inProgress = inProgress;
  }

  public String getIsTo() {
    return isTo;
  }

  public void setIsTo(String isTo) {
    this.isTo = isTo;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getStatusTo() {
    return statusTo;
  }

  public void setStatusTo(String statusTo) {
    this.statusTo = statusTo;
  }



}
