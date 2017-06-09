package com.cafe.crm.service_abstract.property;


import com.cafe.crm.models.property.Property;

import java.util.List;

public interface PropertyService {

	void save(Property property);

	List<Property> findAll();

	Property getOne(Long id);

	void delete(Long id);

}