package com.my.retail.catalog.db.entities;

import com.my.retail.catalog.constants.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.Date;

@Document(collection = "price")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Price {

    @Id
    @GeneratedValue
    private long id;
    private double value;
    private String currency_code;
    @Temporal(TemporalType.TIMESTAMP)
    private Date insertTimestamp;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTimestamp;

    @PrePersist
    protected void onCreate() {
        updateTimestamp = insertTimestamp = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTimestamp = new Date();
    }

}
