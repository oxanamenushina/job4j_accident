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
import ru.job4j.accident.service.IAccidentService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * AccidentController.
 *
 * @author Oxana Menushina (oxsm@mail.ru).
 * @version $Id$
 * @since 0.1
 */
@Controller
public class AccidentController {

    private IAccidentService accidentService;

    @Autowired
    @Qualifier("accidentService")
    public void setAccidentService(IAccidentService accidentService) {
        this.accidentService = accidentService;
    }

    @GetMapping("/add-accident")
    public String addAccident(HttpServletRequest req) {
        HttpSession session = req.getSession();
        session.removeAttribute("current");
        return "/addaccident";
    }

    @GetMapping("/edit-accident")
    public String editAccident(@RequestParam int id, Model model, HttpServletRequest req) {
        Accident accident = this.accidentService.getElementById(id);
        HttpSession session = req.getSession();
        session.setAttribute("current", accident);

        model.addAttribute("accident", accident);
        return "/editaccident";
    }

    @PostMapping("/delete-accident")
    public String deleteAccident(@RequestParam int id) {
        this.accidentService.delete(id);
        return "redirect:/";
    }

    @PostMapping("/accident-intermediate-operation")
    public String intermediateOperation(@ModelAttribute Accident accident, HttpServletRequest req) {
        HttpSession session = req.getSession();
        Accident current = (Accident) session.getAttribute("current");
        if (current != null) {
            accident.setParticipants(current.getParticipants());
        }
        session.setAttribute("current", accident);
        return "redirect:/intermediate-data";
    }

    @GetMapping("/intermediate-data")
    public String intermediateData(Model model, HttpServletRequest req) {
        HttpSession session = req.getSession();
        model.addAttribute("accident", session.getAttribute("current"));
        return "/participantsoperations";
    }

    @PostMapping("/complete-operation")
    public String completeOperation(HttpServletRequest req) {
        HttpSession session = req.getSession();
        Accident accident = (Accident) session.getAttribute("current");
        if (accident.getId() == 0) {
            this.accidentService.add(accident);
        } else {
            this.accidentService.update(accident);
        }
        return "redirect:/";
    }
}
