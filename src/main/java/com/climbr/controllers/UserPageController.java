package com.climbr.controllers;

import com.climbr.domain.Account;
import com.climbr.services.AccountService;
import com.climbr.services.WallPostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class UserPageController {

  private final AccountService accountService;
  private final WallPostService wallPostService;

  public UserPageController(WallPostService wallPostService, AccountService accountService) {
    this.wallPostService = wallPostService;
    this.accountService = accountService;
  }

  @GetMapping("/{userProfileString}")
  public String climbookUserPage(Model model, HttpSession httpSession, @PathVariable String userProfileString) {
    Account account = accountService.findByProfileString(userProfileString);
    Account currentUser = (Account) httpSession.getAttribute("currentUser");
    if (account == null){
      return "redirect:/home";
    }
    model.addAttribute("account", account);
    model.addAttribute("currentUser", currentUser);
    model.addAttribute("wallPosts", wallPostService.getAccountWallPosts(account));
    model.addAttribute("followers", accountService.getFollowers(account));
    model.addAttribute("followed", accountService.getFollowing(account));

    return "userpage";
  }

}