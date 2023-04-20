package com.unibuc.ismyblog.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    void saveImageFile(Long productId, MultipartFile[] images) throws IOException;

}
