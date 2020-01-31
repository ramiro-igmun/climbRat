package com.climbr.services;

import com.climbr.domain.Account;
import com.climbr.domain.WallPost;
import com.climbr.repositories.WallPostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WallPostService {

  private final WallPostRepository wallPostRepository;
  private final Sort sort = Sort.by("postDateTime").descending();


  public WallPostService(WallPostRepository wallPostRepository) {
    this.wallPostRepository = wallPostRepository;
  }

  /*
  Gets the last wallPosts of the current user and the users being followed by him.
  JPA doesn't allow database pagination on queries that use more than one JOIN FETCH,
  so two Queries have to be done, one to page the wallPosts that returns only the ids
  and one to sort and get the wallPosts using more than one JOIN FETCH.
   */

  public WallPost getWallPost(Long wallPostId){
    return wallPostRepository.getById(wallPostId);
  }

  public List<WallPost> getHomePageWallPosts(Account currentUser){
    List<Long> wallPostsIds = wallPostRepository.getPageHomeWallPostIds(currentUser, getPage());
    return getWallPostsWithLikesAndComments(wallPostsIds);
  }

  public List<WallPost> getAccountWallPosts(Account account){
    List<Long> accountPostsIds = wallPostRepository.findByAuthor(account,getPage());
    return getWallPostsWithLikesAndComments(accountPostsIds);
  }

  public List<WallPost> getWallPostComments(WallPost wallPost){
    List<Long> commentPostsIds = wallPostRepository.findByParentPost(wallPost, getPage());
    return  getWallPostsWithLikesAndComments(commentPostsIds);
  }

  private List<WallPost> getWallPostsWithLikesAndComments(List<Long> wallPostsIds) {
    List<WallPost> wallPosts = wallPostRepository.getSortedWallPostsWithLikes(wallPostsIds, sort);
    return wallPostRepository.getSortedWallPostsWithComments(wallPosts,sort);
  }

  private Pageable getPage() {
    return PageRequest.of(0,25, sort);
  }

  public void saveWallPost(WallPost wallPost){
        wallPostRepository.save(wallPost);
  }

  @Transactional//TODO is this annotation necessary??
  public void addLikeToWallPost(Long wallPostId, Account currentUser){
    Long currentUserId = currentUser.getId();
    if(!likeExists(wallPostId,currentUserId)
            && isLikeOfCurrentUser(wallPostId, currentUserId)){
    wallPostRepository.setWallPostLike(wallPostId, currentUserId);
    }
  }

  private boolean isLikeOfCurrentUser(Long wallPostId, Long currentUserId) {
    return wallPostRepository.getOne(wallPostId).getAuthor().getId().intValue() != currentUserId.intValue();
  }

  private boolean likeExists(Long wallPostId, Long currentUserId) {
    return (wallPostRepository.checkIfLikeExists(wallPostId,currentUserId) == 1);
  }

}
