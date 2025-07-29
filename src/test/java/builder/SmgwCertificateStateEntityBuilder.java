package builder; /**
 * Copyright (C) Next Level Integration GmbH Germany - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

import gwa.SmgwCertificateStateEntity;

import java.time.LocalDateTime;

/**
 * Builder for SmgwCertificateStateEntity
 */
public class SmgwCertificateStateEntityBuilder {

    private final gwa.SmgwCertificateStateEntity entity;

    public SmgwCertificateStateEntityBuilder() {
        entity = new SmgwCertificateStateEntity();
        SmgwCertificateStateEntityBuilderDefaults.defaults(this);
    }

    public SmgwCertificateStateEntityBuilder id(Long id) {
        entity.setId(id);
        return this;
    }

    public SmgwCertificateStateEntityBuilder smgwId(String smgwId) {
        entity.setSmgwId(smgwId);
        return this;
    }

    public SmgwCertificateStateEntityBuilder certificateSki(String certificateSki) {
        entity.setCertificateSki(certificateSki);
        return this;
    }

    public SmgwCertificateStateEntityBuilder active(Boolean active) {
        entity.setActive(active);
        return this;
    }

    public SmgwCertificateStateEntityBuilder factoryCert(Boolean factoryCert) {
        entity.setFactoryCert(factoryCert);
        return this;
    }

    public SmgwCertificateStateEntityBuilder missing(Boolean missing) {
        entity.setMissing(missing);
        return this;
    }

    public SmgwCertificateStateEntityBuilder unknown(Boolean unknown) {
        entity.setUnknown(unknown);
        return this;
    }

    public SmgwCertificateStateEntityBuilder lastUpdated(LocalDateTime lastUpdated) {
        entity.setLastUpdated(lastUpdated);
        return this;
    }

    public SmgwCertificateStateEntityBuilder headendClient(String headendClient) {
        entity.setHeadendClient(headendClient);
        return this;
    }

    public SmgwCertificateStateEntityBuilder client(String client) {
        entity.setClient(client);
        return this;
    }

    public SmgwCertificateStateEntityBuilder gatewayGeneration(Integer gatewayGeneration) {
        entity.setGatewayGeneration(gatewayGeneration);
        return this;
    }

    public SmgwCertificateStateEntityBuilder certificateValidTo(LocalDateTime certificateValidTo) {
        entity.setCertificateValidTo(certificateValidTo);
        return this;
    }

    public SmgwCertificateStateEntity build() {
        return entity;
    }

    public SmgwCertificateStateEntity persist() {
        // Implement persistence logic here if needed
        // For example, save to a database or another storage
        return entity;
    }
}
