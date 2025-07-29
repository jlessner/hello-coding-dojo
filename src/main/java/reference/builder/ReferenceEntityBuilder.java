/**
 * Copyright (C) Next Level Integration GmbH Germany - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package reference.builder;

import reference.entity.ReferenceEntity;

import static reference.builder.ReferenceEntityBuilderDefaults.defaults;

public class ReferenceEntityBuilder {

    private final ReferenceEntity referenceEntity;

    public ReferenceEntityBuilder() {
        referenceEntity = new ReferenceEntity();
        defaults(this);
    }

    public ReferenceEntityBuilder id(String id) {
        referenceEntity.setId(id);
        return this;
    }

    public ReferenceEntity build() {
        return referenceEntity;
    }

    public ReferenceEntity persist() {
        // Implement persistence logic here if needed
        // For example, save to a database or another storage
        return referenceEntity;
    }
}
