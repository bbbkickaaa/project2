package com.API.Board.DTO;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class BoardImageUploadDTO {

    private List<MultipartFile> files;
}