package ivanovvasil.u5d5w3Project.controllers;

import ivanovvasil.u5d5w3Project.entities.Event;
import ivanovvasil.u5d5w3Project.entities.Prenotation;
import ivanovvasil.u5d5w3Project.entities.User;
import ivanovvasil.u5d5w3Project.exceptions.BadRequestException;
import ivanovvasil.u5d5w3Project.payloadsDTO.EventDTO;
import ivanovvasil.u5d5w3Project.services.EventsService;
import ivanovvasil.u5d5w3Project.services.PrenotationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/events")
public class EventsController {
  @Autowired
  private EventsService eventsService;
  @Autowired
  private PrenotationsService prenotationsService;

  @PostMapping("")
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasAuthority('MANAGER')")
  public Event addEvent(@RequestBody @Validated EventDTO body, BindingResult validation) {
    if (validation.hasErrors()) {
      throw new BadRequestException("Empty or not respected fields", validation.getAllErrors());
    } else {
      try {
        return eventsService.save(body);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @PostMapping("/{event_id}/bookMe")
  public Prenotation bookEvent(@AuthenticationPrincipal User user, @PathVariable Long event_id) {
    return prenotationsService.bookEvent(user, event_id);
  }

  @GetMapping("")
  public Page<Event> getEvents(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "15") int size,
                               @RequestParam(defaultValue = "id") String orderBy) {
    return eventsService.findAll(page, size, orderBy);
  }

  @GetMapping("/{id}")
  public Event getEvent(@PathVariable Long id) {
    return eventsService.findById(id);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('MANAGER')")
  public Event editEvent(@PathVariable Long id, @RequestBody @Validated EventDTO body, BindingResult validation) {

    if (validation.hasErrors()) {
      throw new BadRequestException("Empty or not respected fields", validation.getAllErrors());
    } else {
      try {
        return eventsService.findByIdAndUpdate(id, body);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('MANAGER')")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void findByIdAndDelete(@PathVariable Long id) {
    eventsService.findByIdAndDelete(id);
  }
}
