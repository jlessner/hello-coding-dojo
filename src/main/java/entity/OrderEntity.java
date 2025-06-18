package entity;

import java.math.BigDecimal;
import java.util.Date;

public class OrderEntity {
  private int customerId;
  private int orderNumber;
  private Date orderDate;
  private int articleId;
  private int quantity;
  private BigDecimal price;

  public int getCustomerId() {
    return customerId;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  public int getArticleId() {
    return articleId;
  }

  public void setArticleId(int articleId) {
    this.articleId = articleId;
  }

  public Date getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(Date orderDate) {
    this.orderDate = orderDate;
  }

  public int getOrderNumber() {
    return orderNumber;
  }

  public void setOrderNumber(int orderNumber) {
    this.orderNumber = orderNumber;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
}
