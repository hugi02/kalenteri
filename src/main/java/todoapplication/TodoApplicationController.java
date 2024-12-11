package todoapplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import todoapplication.domain.Event;
import todoapplication.domain.Category;
import todoapplication.repository.EventRepository;
import todoapplication.repository.CategoryRepository;
import todoapplication.service.EventService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class TodoApplicationController {

    @Autowired
    private EventService eventService;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @GetMapping("/")
    public String home(Model model) {
        List<Event> events = eventService.getAllEvents();
        System.out.println("Found events in home: " + events);
        model.addAttribute("events", events);
        model.addAttribute("categories", categoryRepository.findAll());
        return "index";
    }
    
    @GetMapping("/events")
    public String listEvents(Model model) {
        List<Event> events = eventService.getAllEvents();
        System.out.println("Found events in /events: " + events);
        model.addAttribute("events", events);
        model.addAttribute("categories", categoryRepository.findAll());
        return "events";
    }
    
    @PostMapping("/events")
    public String addEvent(@Valid @ModelAttribute Event event, 
                          BindingResult bindingResult,
                          @RequestParam(required = false) List<Long> categoryIds,
                          Model model) {
        System.out.println("Received event data: " + event);
        System.out.println("Raw dateTime value: " + event.getDateTime());
        System.out.println("Category IDs: " + categoryIds);

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> {
                System.out.println("Error: " + error.getDefaultMessage());
                System.out.println("Field: " + ((FieldError) error).getField());
                System.out.println("Rejected value: " + ((FieldError) error).getRejectedValue());
            });
            model.addAttribute("events", eventService.getAllEvents());
            model.addAttribute("error", "Please check the date format and ensure all required fields are filled");
            return "index";
        }

        try {
            Event savedEvent = eventService.saveEvent(event, categoryIds);
            System.out.println("Successfully saved event: " + savedEvent);
            return "redirect:/events";
        } catch (Exception e) {
            System.err.println("Error saving event: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Failed to save event: " + e.getMessage());
            return "index";
        }
    }
    
    @GetMapping("/events/{id}")
    public String viewEvent(@PathVariable Long id, Model model) {
        model.addAttribute("event", eventService.getEvent(id));
        return "event";
    }
    
    @DeleteMapping("/events/{id}")
    public String deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return "redirect:/events";
    }
    
    @GetMapping("/categories/{id}")
    public String viewCategory(@PathVariable Long id, Model model) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category != null) {
            model.addAttribute("category", category);
            model.addAttribute("events", category.getEvents());
        }
        return "category";
    }
    
    @GetMapping("/categories")
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "categories";
    }
    
    @PostMapping("/categories")
    public String addCategory(@Valid @ModelAttribute Category category,
                            BindingResult bindingResult,
                            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            return "categories";
        }
        categoryRepository.save(category);
        return "redirect:/categories";
    }
    
    @ModelAttribute("categories")
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }
}
