package com.example.demo.DataBase.im;

import com.example.demo.DataBase.Notes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AddNote extends JpaRepository<Notes,Long> {

    @Query("select n from Notes n where n.parent_id=?1")
    Notes findByParent_id(String parent);
    @Query("select count(n.id) from Notes n where n.parent_id=?1")
    Integer findCount(String parent);
    @Query("update Notes n set n.content=:content where n.parent_id=:parent")
    @Modifying
    @Transactional
    int updateNotes(@Param("content") String content,@Param("parent")String parent);

    @Query(nativeQuery = true,value = ("select i.item_title,n.content,i.uuid from notes n left join items i on i.uuid = n.parent_id where i.uuid = ?1"))
    List downLoadFileByHtml(String uuid);
}
