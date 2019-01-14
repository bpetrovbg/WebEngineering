package com.fdiba.webeng.controllers;

import com.fdiba.webeng.models.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {
    private final UserRepository userRepository;
    private final InterestRepository interestRepository;

    public MainController(UserRepository userRepository, InterestRepository interestRepository) {
        this.userRepository = userRepository;
        this.interestRepository = interestRepository;
    }

    @GetMapping("/main")
    public ModelAndView getMainpage(HttpServletRequest request) {
        if (request.getCookies() != null) {
            User currentUser = getCurrentUserWithCookies(request.getCookies());
            if (currentUser != null) {
                ModelAndView modelAndView = new ModelAndView("main");
                modelAndView.addObject("username", currentUser.getUserName());


                List<Interest> currentUserInterestList = currentUser.getUserInterestsList();
                Iterable<User> allUsers = userRepository.findAll();

                ArrayList<User> userListBasedOnSameInterests = new ArrayList<User>();

                for (Interest userInterest : currentUserInterestList) {
                    for (User user : allUsers) {
                        if (!userListBasedOnSameInterests.contains(user)) {
                            List<Interest> singleUserInterestsList = user.getUserInterestsList();
                            if (!user.getUserName().equals(currentUser.getUserName()) && singleUserInterestsList.contains(userInterest)) {
                                userListBasedOnSameInterests.add(user);
                            }
                        }
                    }
                }
                modelAndView.addObject("similarUsers", userListBasedOnSameInterests);
                return modelAndView;
            } else {
                return plsLoginFirst();
            }
        } else {
            return plsLoginFirst();
        }
    }

    @GetMapping("/main/account")
    public ModelAndView getMyAccountSettings(HttpServletRequest request) {
        if (request.getCookies() != null) {
            User currentUser = getCurrentUserWithCookies(request.getCookies());
            if (currentUser != null) {
                ModelAndView model = new ModelAndView("account");
                model.addObject("username", currentUser.getUserName());
                model.addObject("firstname", currentUser.getUserFirstName());
                model.addObject("lastname", currentUser.getUserLastName());
                model.addObject("phone", currentUser.getUserPhone());
                model.addObject("socialnetwork", currentUser.getUserSocialNetwork());
                return model;
            } else {
                return plsLoginFirst();
            }
        } else {
            return plsLoginFirst();
        }
    }

    @PostMapping(value = "/main/searchbyinterest", consumes = "application/x-www-form-urlencoded")
    public ModelAndView searchByInterest(@RequestParam Map<String, String> body, HttpServletRequest request) {
        if (request.getCookies() != null) {
            User currentUser = getCurrentUserWithCookies(request.getCookies());
            if (currentUser != null) {
                ModelAndView modelAndView = new ModelAndView("searchbyinterest");
                modelAndView.addObject("username", currentUser.getUserName());

                String userInterest = body.get("interest");
                Iterable<User> allUsers = userRepository.findAll();

                ArrayList<User> userListBasedOnSameInterests = new ArrayList<User>();

                for (User user : allUsers) {
                    if (!user.getUserName().equals(currentUser.getUserName()) && user.getUserInterestsList().stream().anyMatch(userDBInterest -> userDBInterest.getInterestName().contains(userInterest))) {
                        userListBasedOnSameInterests.add(user);
                    }
                }
                modelAndView.addObject("similarUsers", userListBasedOnSameInterests);
                modelAndView.addObject("userqty", userListBasedOnSameInterests.size());
                modelAndView.addObject("searchedinterest", userInterest);
                return modelAndView;
            } else {
                return plsLoginFirst();
            }
        } else {
            return plsLoginFirst();
        }
    }

    @PostMapping(value = "/main/account/updatefn", consumes = "application/x-www-form-urlencoded")
    public ModelAndView updateFirstName(@RequestParam Map<String, String> body, HttpServletRequest request) {
        if (request.getCookies() != null) {
            User user = getCurrentUserWithCookies(request.getCookies());
            if (user != null && body.get("firstname").trim().length() > 0) {
                user.setFirstName(body.get("firstname"));
                userRepository.save(user);
            }
            return new ModelAndView("redirect:/main/account");
        } else {
            return plsLoginFirst();
        }
    }

    @PostMapping(value = "/main/account/updateln", consumes = "application/x-www-form-urlencoded")
    public ModelAndView updateLastName(@RequestParam Map<String, String> body, HttpServletRequest request) {
        if (request.getCookies() != null) {
            User user = getCurrentUserWithCookies(request.getCookies());
            if (user != null && body.get("lastname").trim().length() > 0) {
                user.setLastName(body.get("lastname"));
                userRepository.save(user);
            }
            return new ModelAndView("redirect:/main/account");
        } else {
            return plsLoginFirst();
        }
    }

    @PostMapping(value = "/main/account/updatephone", consumes = "application/x-www-form-urlencoded")
    public ModelAndView updatePhone(@RequestParam Map<String, String> body, HttpServletRequest request) {
        if (request.getCookies() != null) {
            User user = getCurrentUserWithCookies(request.getCookies());
            if (user != null && body.get("phone").trim().length() > 0) {
                user.setUserPhone(body.get("phone"));
                userRepository.save(user);
            }
            return new ModelAndView("redirect:/main/account");
        } else {
            return plsLoginFirst();
        }
    }

    @PostMapping(value = "/main/account/updatesocial", consumes = "application/x-www-form-urlencoded")
    public ModelAndView updateSocial(@RequestParam Map<String, String> body, HttpServletRequest request) {
        if (request.getCookies() != null) {
            User user = getCurrentUserWithCookies(request.getCookies());
            if (user != null && body.get("socialnetwork").trim().length() > 0) {
                user.setUserSocialNetwork(body.get("socialnetwork"));
                userRepository.save(user);
            }
            return new ModelAndView("redirect:/main/account");
        } else {
            return plsLoginFirst();
        }
    }

    @GetMapping("/main/interests")
    public ModelAndView getAccountInterests(HttpServletRequest request) {
        if (request.getCookies() != null) {
            User user = getCurrentUserWithCookies(request.getCookies());
            if (user != null) {
                List<Interest> userInterests = user.getUserInterestsList();
                Iterable<Interest> interestsLists = interestRepository.findAll();
                ModelAndView model = new ModelAndView("interests");
                model.addObject("interests", interestsLists);
                model.addObject("userinterests", userInterests);
                return model;
            } else {
                return plsLoginFirst();
            }
        } else {
            return plsLoginFirst();
        }
    }

    @PostMapping(value = "/main/interests/add", consumes = "application/x-www-form-urlencoded")
    public ModelAndView addInterest(@RequestParam Map<String, String> body, HttpServletRequest request) {
        if (request.getCookies() != null) {
            User user = getCurrentUserWithCookies(request.getCookies());
            if (user != null) {
                List<Interest> userInterestsList = user.getUserInterestsList();
                if (!body.get("interestname").trim().equals("")) {
                    if (!userInterestsList.isEmpty()) {
                        Interest interest = new Interest();
                        interest.setInterestname(body.get("interestname"));

                        if (userInterestsList.stream().noneMatch(userOldInterest -> userOldInterest.getInterestName().equals(interest.getInterestName()))) {
                            for (Interest interestFromDB : interestRepository.findAll()) {
                                if (interestFromDB.getInterestName().equals(interest.getInterestName())) {
                                    interest.setInterestid(interestFromDB.getInterestid());
                                }
                            }
                            if (interest.getInterestid() == null) {
                                interestRepository.save(interest);
                            }
                            userInterestsList.add(interest);
                            user.setUserInterests(userInterestsList);
                            userRepository.save(user);
                            return new ModelAndView("redirect:/main/interests");
                        }
                    } else {
                        Interest interest = new Interest();
                        interest.setInterestname(body.get("interestname"));

                        for (Interest interestFromDB : interestRepository.findAll()) {
                            if (interestFromDB.getInterestName().equals(interest.getInterestName())) {
                                interest.setInterestid(interestFromDB.getInterestid());
                            }
                        }

                        if (interest.getInterestid() == null) {
                            interestRepository.save(interest);
                        }

                        userInterestsList.add(interest);
                        user.setUserInterests(userInterestsList);
                        userRepository.save(user);
                    }
                }
            }
            return new ModelAndView("redirect:/main/interests");
        } else {
            return plsLoginFirst();
        }
    }

    @PostMapping(value = "/main/interests/remove", consumes = "application/x-www-form-urlencoded")
    public ModelAndView removeInterest(@RequestParam Map<String, Integer> body, HttpServletRequest request) {
        if (request.getCookies() != null) {
            User user = getCurrentUserWithCookies(request.getCookies());
            if (user != null) {
                List<Interest> userOldInterests = user.getUserInterestsList();
                try {
                    if (body.get("interestid") != null && !String.valueOf(body.get("interestid")).trim().equals("")) {
                        userOldInterests.removeIf(interest -> interest.getInterestid() == Integer.parseInt(String.valueOf(body.get("interestid"))));
                        userRepository.save(user);
                    }
                    return new ModelAndView("redirect:/main/interests");
                } catch (Exception e) {
                    return new ModelAndView("redirect:/main/interests");
                }
            } else {
                return plsLoginFirst();
            }
        } else {
            return plsLoginFirst();
        }
    }

    @GetMapping("/main/logout")
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        if (request.getCookies() != null) {
            User user = getCurrentUserWithCookies(request.getCookies());
            if (user != null) {
                Cookie[] cookies = request.getCookies();
                for (Cookie singleCookie : cookies) {
                    singleCookie.setMaxAge(0);
                    singleCookie.setValue(null);
                    singleCookie.setPath("/");
                    response.addCookie(singleCookie);
                }
                return new ModelAndView("redirect:/login");
            } else {
                return new ModelAndView("redirect:/login");
            }
        } else {
            return plsLoginFirst();
        }
    }

    private ModelAndView plsLoginFirst() {
        ModelAndView loginmodel = new ModelAndView("login");
        return loginmodel.addObject("error", "Please login first!");
    }

    private User getCurrentUserWithCookies(Cookie[] cookies) {
        User user = new User();
        String userNameFromCookie = "";
        String isLoggedIn = "";

        for (Cookie singleCookie : cookies) {
            switch (singleCookie.getName()) {
                case "username":
                    userNameFromCookie = singleCookie.getValue();
                    break;
                case "isloggedin":
                    isLoggedIn = singleCookie.getValue();
            }
        }

        if (isLoggedIn.equals("true")) {
            user = userRepository.findUserByUserName(userNameFromCookie);
            return user;
        } else {
            return null;
        }
    }
}
