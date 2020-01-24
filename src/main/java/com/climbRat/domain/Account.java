package com.climbRat.domain;

import lombok.Data;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Account extends AbstractPersistable<Long> {

    private String userName;
    private String password;
    private String profileString;

    @OneToOne(fetch = FetchType.LAZY)
    private Picture profilePicture;

    @OneToMany(mappedBy = "account")
    private List<Picture> pictures;

    @OneToMany(mappedBy = "author")
    private List<WallPost> wallPosts;

    @OneToMany(mappedBy = "follower")
    private List<FollowingFollower> followers;
    @OneToMany(mappedBy = "following")
    private List<FollowingFollower> following;

    @ManyToMany(mappedBy = "likes")
    private List<WallPost> likedWallPosts;

}
