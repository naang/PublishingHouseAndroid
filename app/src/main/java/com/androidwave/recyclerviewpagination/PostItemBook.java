package com.androidwave.recyclerviewpagination;

public class PostItemBook {
  private String bookid;
  private String name;
  private String code;
  private String price;
  private String handlingQuantity;
  private String quantity;
  private String shalfNumber;
  private String balance;

  public PostItemBook(String bookid, String name, String code, String price, String handlingQuantity, String quantity, String shalfNumber, String balance ) {
    this.bookid = bookid;
    this.name = name;
    this.code = code;
    this.price = price;
    this.handlingQuantity = handlingQuantity;
    this.quantity = quantity;
    this.shalfNumber = shalfNumber;
    this.balance = balance;
  }

  public String getBookId() {
    return bookid;
  }
  public void setBookId(String bookid) {
    this.bookid = bookid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public String getHandlingQuantity() {
    return handlingQuantity;
  }

  public void setHandlingQuantity(String handlingQuantity) {
    this.handlingQuantity = handlingQuantity;
  }
  public String getQuantity() {
    return quantity;
  }

  public void setQuantity(String quantity) {
    this.quantity = quantity;
  }

  public String getShalfNumber() {
    return shalfNumber;
  }

  public void setShalfNumber(String shalfNumber) {
    this.shalfNumber = shalfNumber;
  }

  public String getBalance() {
    return balance;
  }

  public void setBalance(String balance) {
    this.balance = balance;
  }
}
