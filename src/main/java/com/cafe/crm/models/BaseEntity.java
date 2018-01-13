package com.cafe.crm.models;
import com.cafe.crm.models.company.Company;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@MappedSuperclass
@JsonIgnoreProperties({"company"})
public abstract class BaseEntity {

	@ManyToOne
	private Company company;

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
}