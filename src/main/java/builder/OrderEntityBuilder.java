package builder;

import entity.OrderEntity;
import java.math.BigDecimal;
import java.util.Date;

public class OrderEntityBuilder extends AbstractEntityBuilder {
  OrderEntity orderEntity;

  public OrderEntityBuilder() {
    orderEntity = new OrderEntity();
    withDefaults();
  }

  /** Declared protected to make clear that this method does not need to by called by the user,
   * as the default attributes are already set in the constructor. */
  protected OrderEntityBuilder withDefaults() {
    return OrderEntityBuilderDefaults.withDefaults(this);
  }

  public OrderEntityBuilder withOrderNumber(int orderNumber) {
    orderEntity.setOrderNumber(orderNumber);
    return this;
  }

  public OrderEntityBuilder withOrderDate(Date orderDate) {
    orderEntity.setOrderDate(orderDate);
    return this;
  }

  public OrderEntityBuilder withArticleId(int articleId) {
    orderEntity.setArticleId(articleId);
    return this;
  }

  public OrderEntityBuilder withQuantity(int quantity) {
    orderEntity.setQuantity(quantity);
    return this;
  }

  public OrderEntityBuilder withPrice(BigDecimal price) {
    orderEntity.setPrice(price);
    return this;
  }

  public OrderEntity entity() {
    assertHasBuilt();
    return orderEntity;
  }

  public OrderEntity build() {
    assertHasNotBuilt();
    markAsBuilt();
    // Add additional logic for associated entities here if needed
    return entity();
  }

  public OrderEntity persist() {
    assertHasNotBuilt();
    // TODO: Add logic to persist the entity to the database here
    markAsBuilt();
    // Add additional logic for associated entities here if needed
    return entity();
  }
}
