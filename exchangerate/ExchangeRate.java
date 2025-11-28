package de.sundn.bars.server.business.objects.businessobjects;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

public class ExchangeRate extends BusinessObject<ExchangeRate> {

  private String ccyIsoCodeSource = null;
  private String ccyIsoCodeTarget = null;
  private LocalDate validFrom = null;
  private double factor = 1.0;
  private int parity = 1;
  private LocalDate validTo = null;

  public ExchangeRate() {
  }

  public ExchangeRate(final String isoCodeSource, final String isoCodeTarget, final double factor) {
    setCcyIsoCodeSource(isoCodeSource);
    setCcyIsoCodeTarget(isoCodeTarget);
    setFactor(factor);
  }

  @Override
  public @NotNull ExchangeRate copy() {
    return super.copy(this, new ExchangeRate());
  }

  public LocalDate getValidFrom() {
    return validFrom;
  }

  public void setValidFrom(LocalDate validFrom) {
    this.validFrom = validFrom;
  }

  public void setValidFrom(String validFrom) {
    this.setValidFrom(LocalDate.parse(validFrom));
  }

  public String getCcyIsoCodeTarget() {
    return ccyIsoCodeTarget;
  }

  public void setCcyIsoCodeTarget(String aCcyIsoCodeTarget) {
    ccyIsoCodeTarget = aCcyIsoCodeTarget;
  }

  public String getCcyIsoCodeSource() {
    return ccyIsoCodeSource;
  }

  public void setCcyIsoCodeSource(String aCcyIsoCodeSource) {
    ccyIsoCodeSource = aCcyIsoCodeSource;
  }

  public double getFactor() {
    return factor;
  }

  public void setFactor(double aFactor) {
    factor = aFactor;
  }

  public int getParity() {
    return parity;
  }

  public void setParity(int aParity) {
    parity = aParity;
  }

  public LocalDate getValidTo() {
    return validTo;
  }

  public void setValidTo(LocalDate aValidTo) {
    validTo = aValidTo;
  }

  public ExchangeRate getInverted() {
    ExchangeRate inverted = this.copy();

    inverted.setCcyIsoCodeSource(this.ccyIsoCodeTarget);
    inverted.setCcyIsoCodeTarget(this.ccyIsoCodeSource);

    if (this.factor != 0) {
      inverted.setFactor(1 / this.factor);
    }

    return inverted;
  }

}
