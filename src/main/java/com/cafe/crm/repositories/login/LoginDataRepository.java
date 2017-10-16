package com.cafe.crm.repositories.login;


import com.cafe.crm.models.loginData.LoginData;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LoginDataRepository extends JpaRepository<LoginData, Long> {
	LoginData findByRemoteAddress(String remoteAddress);
}
