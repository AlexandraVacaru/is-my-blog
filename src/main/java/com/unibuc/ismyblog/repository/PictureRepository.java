package com.unibuc.ismyblog.repository;

import com.unibuc.ismyblog.model.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureRepository extends JpaRepository<Picture, Long> {
}
