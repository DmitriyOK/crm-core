package com.cafe.crm.service_impl.role;

import com.cafe.crm.dao.role.RoleRepository;

import com.cafe.crm.models.worker.Role;
import com.cafe.crm.service_abstract.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public void save(Role role) {
        roleRepository.saveAndFlush(role);
	}

	@Override
	public Role getRoleByName(String name) {
		return roleRepository.getRoleByName(name);
	}
}
