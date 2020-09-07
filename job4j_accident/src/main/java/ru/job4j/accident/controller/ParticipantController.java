package ru.job4j.accident.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.Participant;
import ru.job4j.accident.service.IAccidentService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Comparator;

/**
 * ParticipantController.
 *
 * @author Oxana Menushina (oxsm@mail.ru).
 * @version $Id$
 * @since 0.1
 */
@Controller
public class ParticipantController {

    @Autowired
    @Qualifier("accidentService")
    private IAccidentService accidentIService;

    public void setAccidentIService(IAccidentService accidentIService) {
        this.accidentIService = accidentIService;
    }

    @GetMapping("/participant-page")
    public String getParticipantPage(@RequestParam int pid, @RequestParam int aid, Model model) {
        model.addAttribute("participant", this.accidentIService.getElementById(aid).getParticipants().stream()
                .filter(p -> pid == p.getId()).findFirst().get());
        return "/participantpage";
    }

    @GetMapping("/add-participant")
    public String addParticipant() {
        return "/addparticipant";
    }

    @GetMapping("/edit-participant")
    public String editParticipant(@RequestParam int id, HttpServletRequest req, Model model) {
        HttpSession session = req.getSession();
        Accident accident = (Accident) session.getAttribute("current");
        model.addAttribute("participant",
                accident.getParticipants().stream().filter(p -> id == p.getId()).findFirst().get());
        return "/editparticipant";
    }

    @PostMapping("/add-participant-complete")
    public String addParticipantComplete(@ModelAttribute Participant participant, HttpServletRequest req) {
        HttpSession session = req.getSession();
        Accident current = (Accident) session.getAttribute("current");
        Participant min = current.getParticipants().stream().min(Comparator.comparing(Participant::getId)).orElse(null);
        int id = current.getParticipants().isEmpty() || (min != null && min.getId() > 0) ? -1 : min.getId() - 1;
        participant.setId(id);
        current.getParticipants().add(participant);
        return "redirect:/intermediate-data";
    }

    @PostMapping("/edit-participant-complete")
    public String editParticipantComplete(@ModelAttribute Participant participant, HttpServletRequest req) {
        HttpSession session = req.getSession();
        Accident current = (Accident) session.getAttribute("current");
        Collection<Participant> participants = current.getParticipants();
        participants.remove(participants.stream().filter(p -> participant.getId() == p.getId()).findFirst().get());
        participants.add(participant);
        return "redirect:/intermediate-data";
    }

    @PostMapping("/delete-participant")
    public String deleteParticipant(@RequestParam int id, HttpServletRequest req) {
        HttpSession session = req.getSession();
        Accident current = (Accident) session.getAttribute("current");
        Collection<Participant> participants = current.getParticipants();
        participants.remove(participants.stream().filter(p -> id == p.getId()).findFirst().get());
        return "redirect:/intermediate-data";
    }
}