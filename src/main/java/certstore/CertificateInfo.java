package certstore;

import java.util.Date;
import java.util.Set;

public class CertificateInfo {
  public String ski;
  public long id;
  public String thumbPrint;
  public String alias;
  public byte[] certificate;
  public String email;
  public boolean active;
  public String description;
  public String state;
  public Date validFrom;
  public Date validTo;
  public Date sliceFrom;
  public Date sliceTo;
  public boolean delete;
  public boolean update;
  public Set<CertificatePurposeType> certificatePurposes;
  public String aki;

  public void setClient(Client client) {

  }

  public void setPrivateKey(boolean privateKey) {

  }

  public void setCrlDistributionPoints(Set<CrlDistributionPoint> crlDistributionPoints) {

  }

  public void setStoredOnHsm(boolean storedOnHsm) {

  }

  public void setPublicKeyToPrivateKey(boolean publicKeyToPrivateKey) {

  }
}
