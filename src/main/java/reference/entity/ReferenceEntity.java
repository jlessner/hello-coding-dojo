/**
 * Copyright (C) Next Level Integration GmbH Germany - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package reference.entity;

import java.io.Serial;
import java.io.Serializable;

public class ReferenceEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    public ReferenceEntity() {
        // Default constructor
    }

    public ReferenceEntity(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
