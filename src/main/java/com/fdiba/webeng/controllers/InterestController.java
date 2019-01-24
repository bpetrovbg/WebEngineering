package com.fdiba.webeng.controllers;

import com.fdiba.webeng.models.Interest;
import com.fdiba.webeng.models.InterestRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class InterestController {
    private final InterestRepository interestRepository;

    public InterestController(InterestRepository interestRepository) {
        this.interestRepository = interestRepository;
    }

    @GetMapping(path = "/interest/all")
    public @ResponseBody
    Iterable<Interest> getallInterests() {
        for (Interest interest : interestRepository.findAll()) {
            System.out.println(interest.getInterestname());
        }
        return interestRepository.findAll();
    }

    @PostMapping(path = "/interest/add", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    String saveInterest(@RequestBody Interest interestToAddJson) {
        if(interestRepository.findByInterestname(interestToAddJson.getInterestname()) == null) {
            interestRepository.save(interestToAddJson);
            return "Interest saved!";
        } else return "Interest is already saved!";
    }
}
