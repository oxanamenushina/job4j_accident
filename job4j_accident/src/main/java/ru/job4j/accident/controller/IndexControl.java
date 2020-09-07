package ru.job4j.accident.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.accident.service.IAccidentService;

/**
 * IndexControl.
 *
 * @author Oxana Menushina (oxsm@mail.ru).
 * @version $Id$
 * @since 0.1
 */
@Controller
public class IndexControl {

    @Autowired
    @Qualifier("accidentService")
    private IAccidentService accidentService;

    public void setAccidentService(IAccidentService accidentService) {
        this.accidentService = accidentService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("accidents", this.accidentService.getList());
        return "index";
    }
}