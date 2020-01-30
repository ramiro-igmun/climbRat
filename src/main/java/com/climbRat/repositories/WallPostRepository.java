package com.climbRat.repositories;

import com.climbRat.domain.Account;
import com.climbRat.domain.WallPost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WallPostRepository extends JpaRepository<WallPost, Long> {

  @Query("SELECT w.id FROM WallPost w " +
          "WHERE w.author = :currentUser OR w.author IN " +
          "(SELECT f.following FROM FollowingFollower f WHERE f.follower = :currentUser)")
  List<Long> pageHomeWallPosts(@Param("currentUser") Account currentUser, Pageable pageable);

  @Query("SELECT DISTINCT w FROM WallPost w JOIN FETCH w.author LEFT JOIN FETCH w.picture LEFT JOIN FETCH w.likes" +
          " WHERE w.id IN (:wallPosts)")
  List<WallPost> getSortedWallPostsWithLikes(@Param("wallPosts") List<Long> wallPosts, Sort sort);

  @Query("SELECT DISTINCT w FROM WallPost w LEFT JOIN FETCH w.comments" +
          " WHERE w IN (:wallPosts)")
  List<WallPost> getSortedWallPostsWithComments(@Param("wallPosts") List<WallPost> wallPosts, Sort sort);

  @Modifying(flushAutomatically = true)
  @Query(value = "INSERT INTO WALL_POST_LIKES (LIKED_WALL_POSTS_ID, LIKES_ID) VALUES (?1,?2)", nativeQuery = true)
  void setWallPostLike(Long wallPostId, Long accountId);

  @Query(value = "SELECT COUNT(1) FROM WALL_POST_LIKES WHERE LIKED_WALL_POSTS_ID = ?1 AND LIKES_ID = ?2" ,nativeQuery = true)
  int checkIfLikeExists(Long wallPostId, Long accountId);

  List<WallPost> findByAuthor(Account author);
  }




