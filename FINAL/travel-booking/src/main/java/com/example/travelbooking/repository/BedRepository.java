package com.example.travelbooking.repository;

import com.example.travelbooking.entity.Bed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface BedRepository extends JpaRepository<Bed, Long> {

    @Query("select b from Bed b where b.room.id = :roomId")
    List<Bed> findByRoom(Long roomId);

    @Transactional
    @Modifying
    @Query("delete from Bed b where b.room.id = :roomId")
    void deleteAllByRoomId(Long roomId);
}
