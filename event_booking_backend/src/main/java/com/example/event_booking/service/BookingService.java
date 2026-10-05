package com.example.event_booking.service;

import com.example.event_booking.repository.BookingRepository;
import com.example.event_booking.repository.EventSeatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class BookingService {

    private final EventSeatRepository eventSeatRepository;
    private final BookingRepository bookingRepository;

    public BookingService(
            EventSeatRepository eventSeatRepository,
            BookingRepository bookingRepository) {

        this.eventSeatRepository = eventSeatRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public boolean bookSeat(
            long userId,
            long eventId,
            long eventSeatId) {

        int rows = eventSeatRepository.bookSeat(
                eventId,
                eventSeatId
        );

        if (rows == 0) {
            return false;
        }

        BigDecimal price =
                eventSeatRepository.getSeatPrice(
                        eventId,
                        eventSeatId
                );

        long bookingId =
                bookingRepository.createBooking(
                        userId,
                        eventId
                );

        bookingRepository.addBookingItem(
                bookingId,
                eventSeatId,
                price
        );

        return true;
    }
}