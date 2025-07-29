package builder; /**
 * Copyright (C) Next Level Integration GmbH Germany - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

import certstore.CertStoreFake;
import certstore.CertificateInfo;
import certstore.CertificatePurposeType;
import certstore.Client;
import certstore.CrlDistributionPoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class TestCertificateInfoBuilder {

    public static final String TEST_CLIENT = "testClient";

    private final CertificateInfo certInfo;

    private List<TestCertificateInfoBuilder> issued = new ArrayList<>();

    public TestCertificateInfoBuilder() {
        certInfo = new CertificateInfo();
        defaults();
    }

    public CertificateInfo getCertificateInfo() {
        return certInfo;
    }

    public TestCertificateInfoBuilder defaults() {
        return active(true)
                .client(TEST_CLIENT)
                .ski("" + nextSki++)
                .alias(certInfo.ski)
                .validFrom(new Date())
                .validTo(new Date());
    }

    private static int nextSki = 0;

    public TestCertificateInfoBuilder issued(TestCertificateInfoBuilder builder) {
        issued.add(builder);
        return this;
    }

    public TestCertificateInfoBuilder id(long id) {
        certInfo.id = id;
        return this;
    }

    public TestCertificateInfoBuilder thumbPrint(String thumbPrint) {
        certInfo.thumbPrint = thumbPrint;
        return this;
    }

    public TestCertificateInfoBuilder alias(String alias) {
        certInfo.alias = alias;
        return this;
    }

    public TestCertificateInfoBuilder ski(String ski) {
        certInfo.ski = ski;
        return this;
    }

    public TestCertificateInfoBuilder certificate(byte[] cert) {
        certInfo.certificate = cert;
        return this;
    }

    public TestCertificateInfoBuilder email(String email) {
        certInfo.email = email;
        return this;
    }

    public TestCertificateInfoBuilder active(boolean active) {
        certInfo.active = active;
        return this;
    }

    public TestCertificateInfoBuilder description(String description) {
        certInfo.description = description;
        return this;
    }

    public TestCertificateInfoBuilder state(String state) {
        certInfo.state = state;
        return this;
    }

    public TestCertificateInfoBuilder validFrom(Date validFrom) {
        certInfo.validFrom = validFrom;
        return this;
    }

    public TestCertificateInfoBuilder validTo(Date validTo) {
        certInfo.validTo = validTo;
        return this;
    }

    public TestCertificateInfoBuilder sliceFrom(Date sliceFrom) {
        certInfo.sliceFrom = sliceFrom;
        return this;
    }

    public TestCertificateInfoBuilder sliceTo(Date sliceTo) {
        certInfo.sliceTo = sliceTo;
        return this;
    }

    public TestCertificateInfoBuilder delete(boolean delete) {
        certInfo.delete = delete;
        return this;
    }

    public TestCertificateInfoBuilder update(boolean update) {
        certInfo.update = update;
        return this;
    }

    public TestCertificateInfoBuilder certificatePurposes(Set<CertificatePurposeType> certificatePurposes) {
        certInfo.certificatePurposes = certificatePurposes;
        return this;
    }

    public TestCertificateInfoBuilder certificatePurposes(CertificatePurposeType... certificatePurposes) {
        if (certificatePurposes != null) {
            for (CertificatePurposeType certificatePurpose : certificatePurposes) {
                certInfo.certificatePurposes.add(certificatePurpose);
            }
        }
        return this;
    }

    public TestCertificateInfoBuilder client(Client client) {
        certInfo.setClient(client);
        return this;
    }


    public TestCertificateInfoBuilder privateKey(boolean privateKey) {
        certInfo.setPrivateKey(privateKey);
        return this;
    }

    public TestCertificateInfoBuilder crlDistributionPoints(Set<CrlDistributionPoint> crlDistributionPoints) {
        certInfo.setCrlDistributionPoints(crlDistributionPoints);
        return this;
    }

    public TestCertificateInfoBuilder storedOnHsm(boolean storedOnHsm) {
        certInfo.setStoredOnHsm(storedOnHsm);
        return this;
    }

    public TestCertificateInfoBuilder publicKeyToPrivateKey(boolean publicKeyToPrivateKey) {
        certInfo.setPublicKeyToPrivateKey(publicKeyToPrivateKey);
        return this;
    }

    public CertificateInfo persist() {
        for (TestCertificateInfoBuilder builder : issued) {
            builder.aki(certInfo.ski).persist();
        }
        CertStoreFake.addCertificateInfo(certInfo);
        return certInfo;
    }

    public TestCertificateInfoBuilder client(String clientName) {
        Client client = new Client();
        client.setName(clientName);
        return client(client);
    }

    public TestCertificateInfoBuilder purpose(CertificatePurposeType purpose) {
        return certificatePurposes(purpose);
    }

    public TestCertificateInfoBuilder aki(String aki) {
        certInfo.aki = aki;
        return this;
    }

    public TestCertificateInfoBuilder gateway(String gatewayId) {
        return alias(gatewayId + ".smgw");
    }
}