package usecase;

import builder.SmgwCertificateStateEntityBuilder;
import gwa.SmgwCertificateStateEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UC201Test {

  @Test
  void test() {
    SmgwCertificateStateEntity entity = new SmgwCertificateStateEntityBuilder().persist();
    assertNotNull(entity);
  }
}
