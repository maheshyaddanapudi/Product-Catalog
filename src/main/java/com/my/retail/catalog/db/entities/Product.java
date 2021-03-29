package com.my.retail.catalog.db.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.Date;

@Document(collection = "product")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

	@Id
	private long id;

	private String name;
	@OneToOne
	@MapsId
	private Price current_price;
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
