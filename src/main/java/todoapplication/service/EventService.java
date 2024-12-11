package todoapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import todoapplication.domain.Event;
import todoapplication.domain.Category;
import todoapplication.repository.EventRepository;
import todoapplication.repository.CategoryRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EventService {
    
    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    public List<Event> getAllEvents() {
        try {
            List<Event> events = eventRepository.findAll();
            System.out.println("Service found " + events.size() + " events");
            events.forEach(event -> {
                System.out.println("Event details:");
                System.out.println("  ID: " + event.getId());
                System.out.println("  Title: " + event.getTitle());
                System.out.println("  DateTime: " + event.getDateTime());
                System.out.println("  Categories: " + event.getCategories());
            });
            return events;
        } catch (Exception e) {
            System.err.println("Error fetching events: " + e.getMessage());
            e.printStackTrace();
            return List.of(); // Return empty list instead of null
        }
    }
    
    public Event saveEvent(Event event, List<Long> categoryIds) {
        try {
            System.out.println("Saving event with title: " + event.getTitle());
            System.out.println("Event date: " + event.getDateTime());
            
            if (categoryIds != null && !categoryIds.isEmpty()) {
                Set<Category> selectedCategories = categoryIds.stream()
                    .map(id -> categoryRepository.findById(id).orElse(null))
                    .filter(category -> category != null)
                    .collect(Collectors.toSet());
                event.setCategories(selectedCategories);
                System.out.println("Added categories: " + selectedCategories);
            }
            
            Event savedEvent = eventRepository.save(event);
            System.out.println("Successfully saved event with ID: " + savedEvent.getId());
            
            // Verify the save
            Event verifiedEvent = eventRepository.findById(savedEvent.getId()).orElse(null);
            if (verifiedEvent != null) {
                System.out.println("Verified event exists in database");
            } else {
                System.out.println("WARNING: Could not verify saved event!");
            }
            
            return savedEvent;
        } catch (Exception e) {
            System.err.println("Error saving event: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    public Event getEvent(Long id) {
        return eventRepository.findById(id).orElse(null);
    }
    
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
} 