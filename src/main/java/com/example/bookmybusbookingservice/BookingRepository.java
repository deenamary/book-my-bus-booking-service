package com.example.bookmybusbookingservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface BookingRepository extends JpaRepository<Booking, String> {

    @Transactional
    @Modifying
    @Query("update Booking b set b.status = ?1 where b.bookingId = ?2")
    int updateStatusByBookingId(String status, String bookingId);
}
