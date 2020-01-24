package com.climbRat.controllers;

import com.climbRat.domain.Account;
import com.climbRat.repositories.WallPostRepository;
import com.climbRat.services.AccountService;
import com.climbRat.services.PictureService;
import com.climbRat.services.WallPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/home")
public class HomeController {

  private final WallPostService wallPostService;
  private final AccountService accountService;
  private final PictureService pictureService;

  public HomeController(WallPostService wallPostService, AccountService accountService, PictureService pictureService) {
    this.wallPostService = wallPostService;
    this.accountService = accountService;
    this.pictureService = pictureService;
  }

  @GetMapping
  public String climbookHome(Model model) {
    Account currentUser = accountService.getCurrentUserAccount();
    model.addAttribute("currentUser", currentUser);
    model.addAttribute("wallPosts", wallPostService.getHomePageWallPosts(currentUser));
    model.addAttribute("followers",accountService.getFollowers(currentUser));
    model.addAttribute("followed",accountService.getFollowing(currentUser));

    return "home";
  }

  @GetMapping(value = "/picture/{pictureId}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
  @ResponseBody
  public byte[] profilePicture(@PathVariable Long pictureId) {
    return pictureService.getPicture(pictureId);
  }

  @PostMapping
  public String newWallPost(@RequestParam String message) {
    wallPostService.saveWallPost(message, accountService.getCurrentUserAccount());
    return "redirect:/home";
  }

  @PostMapping("/like")
  public String addLike(@RequestParam Long likedWallPost){
    wallPostService.addLikeToWallPost(likedWallPost, accountService.getCurrentUserAccount());
    return "redirect:/home";
  }

  @ResponseBody
  @GetMapping("/test")
  public String test(){
    return "test";
  }
}
