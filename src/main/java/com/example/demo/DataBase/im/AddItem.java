package com.example.demo.DataBase.im;
import com.example.demo.DataBase.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.FetchProfile;
import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;
import java.util.zip.DataFormatException;

public interface AddItem extends JpaRepository<Items,Long> {

    @Query(nativeQuery = true,value = ("select * from items i where TO_DAYS(i.create_at) = TO_DAYS(:date) and i.delete_state = 0 order by create_at desc"))
    List<Items> findByAppDate(String date);
    @Query("select i from Items i where i.delete_state = 0 order by i.create_at desc ")
    List<Items> findAll();
    @Query("update Items i set i.delete_state = 1 where i.uuid = ?1")
    @Modifying
    @Transactional
    int deleteItem(String uuid);
    @Query(nativeQuery = true,value = ("select * from items i left join notes n on i.uuid=n.parent_id where i.item_title like %?1% or n.content like %?1% order by i.create_at desc"))
    List<Items> findLikeText(String text);

}
