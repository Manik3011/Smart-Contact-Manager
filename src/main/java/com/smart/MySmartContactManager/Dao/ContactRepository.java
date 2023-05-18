package com.smart.MySmartContactManager.Dao;

import com.smart.MySmartContactManager.Entity.Contact;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
//import org.w3c.dom.stylesheets.LinkStyle;

public interface ContactRepository extends JpaRepository<Contact,Integer> {

    @Query("from Contact as c where c.user.id=:userId")
    public Page<Contact> getAllContactsByUserId(@Param("userId") int userId, Pageable pageable);


//    @Query("delete from Contact c where c.cid=:cid")
//    public void deleteContactByCid(@Param("cid")Integer cid);

}
