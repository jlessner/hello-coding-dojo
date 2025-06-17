package builder;

abstract public class AbstractEntityBuilder {

  private boolean hasBuilt;

  protected void assertHasBuilt() {
    if (!hasBuilt) {
      throw new RuntimeException("please run build() or persist() before calling entity().");
    }
  }

  protected void assertHasNotBuilt() {
    if (hasBuilt) {
      throw new RuntimeException("please don't run build() or persist() twice.");
    }
  }

  protected void markAsBuilt() {
    hasBuilt = true;
  }

}
