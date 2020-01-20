package com.climbRat.domain;

import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import java.time.LocalDateTime;

@Entity
@Data
public class FollowingFollower {

  @EmbeddedId
  AccountFollowerKey id;

  @ManyToOne
  @MapsId("accountId")
  Account following;
  @ManyToOne
  @MapsId("followerId")
  Account follower;

  LocalDateTime followingStartingDate = LocalDateTime.now();
}
