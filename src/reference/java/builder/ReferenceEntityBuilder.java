package builder;

import entity.ReferenceEntity;

public class ReferenceEntityBuilder extends AbstractEntityBuilder {
  ReferenceEntity referenceEntity;

  public ReferenceEntityBuilder() {
    referenceEntity = new ReferenceEntity();
    withDefaults();
  }

  /** Declared protected to make clear that this method does not need to by called by the user,
   * as the default attributes are already set in the constructor. */
  protected ReferenceEntityBuilder withDefaults() {
    return ReferenceEntityBuilderDefaults.withDefaults(this);
  }

  public ReferenceEntityBuilder withReferenceAttribute(String referenceAttribute) {
    referenceEntity.setReferenceAttribute(referenceAttribute);
    return this;
  }

  public ReferenceEntity entity() {
    assertHasBuilt();
    return referenceEntity;
  }

  public ReferenceEntity build() {
    assertHasNotBuilt();
    markAsBuilt();
    // Add additional logic for associated entities here if needed
    return entity();
  }

  public ReferenceEntity persist() {
    assertHasNotBuilt();
    // TODO: Add logic to persist the entity to the database here
    markAsBuilt();
    // Add additional logic for associated entities here if needed
    return entity();
  }
}
