package java.com.cafe.crm.dao;

import java.com.cafe.crm.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Sasha ins on 17.04.2017.
 */
public interface RoleDao extends JpaRepository<Role, Long> {


    //this interface implements methods Jpa

}