package com.safezones.safezones;

import com.safezones.safezones.Model.Point;
import com.safezones.safezones.Controller.PointController;
import com.safezones.safezones.Dto.PointRequest;
import com.safezones.safezones.Model.User;
import com.safezones.safezones.Repository.PointRepository;
import com.safezones.safezones.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PointControllerTest {

    @Mock
    private PointRepository pointRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PointController pointController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAddNewPoint() {
        PointRequest pointRequest = new PointRequest("40.7128", "-74.0060", "Description", "Hard", "1", "Harassment");
        User user = new User();
        user.setId("1");

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(pointRepository.save(any(Point.class))).thenReturn(new Point());

        String response = pointController.addNewPoint(pointRequest);

        assertEquals("Point added successfully", response);
        verify(userRepository, times(1)).findById("1");
        verify(pointRepository, times(1)).save(any(Point.class));
    }

    @Test
    void testGetAllPoints() {
        List<Point> points = new ArrayList<>();
        Point point1 = new Point();
        point1.setId(1L);
        point1.setLatitude("40.7128");
        point1.setLongitude("-74.0060");
        point1.setDescription("Description");
        point1.setCategory("Hard");
        point1.setUserId("1");
        point1.setTimestamp(LocalDateTime.now());
        point1.setEvent("Harassment");
        Point point2 = new Point();
        point2.setLatitude("140.6899");
        point2.setLongitude("-74.0060");
        point2.setDescription("Description");
        point2.setCategory("Hard");
        point2.setUserId("2");
        point2.setTimestamp(LocalDateTime.now());
        point2.setId(2L);
        point2.setEvent("Harassment");

        points.add(point1);
        points.add(point2);

        when(pointRepository.findAll()).thenReturn(points);

        Iterable<Point> result = pointController.getAllPoints();

        assertEquals(2, ((List<Point>) result).size());
    }

    @Test
    void testGetPointsByUserId() {
        List<Point> points = new ArrayList<>();
        points.add(new Point());
        points.add(new Point());

        when(userRepository.findById("1")).thenReturn(Optional.of(new User()));
        when(pointRepository.findByUserId("1")).thenReturn(points);

        List<Point> result = pointController.getPointsByUserId("1");

        assertEquals(2, result.size());
    }
}
