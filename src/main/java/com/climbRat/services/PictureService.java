package com.climbRat.services;

import com.climbRat.domain.Account;
import com.climbRat.domain.Picture;
import com.climbRat.domain.WallPost;
import com.climbRat.repositories.PictureRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class PictureService {

  private final PictureRepository pictureRepository;

  public PictureService(PictureRepository pictureRepository) {
    this.pictureRepository = pictureRepository;
  }

  public byte[] getPicture(Long id){
    Optional<Picture> picture = pictureRepository.findById(id);

    return picture.map(Picture::getContent).orElse(null);
  }

  public void savePicture(MultipartFile image, WallPost post, Account currentUser) throws IOException {
    Picture picture = new Picture();
    picture.setAccount(currentUser);
    picture.setContent(image.getBytes());
    picture.setMediaType(image.getContentType());
    picture.setName(image.getOriginalFilename());
    picture.setSize(image.getSize());
    picture.setParentPost(post);
    pictureRepository.save(picture);
  }

}
