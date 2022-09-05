package com.medicow.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
//이미지 파일을 업로드, 삭제처리를 수행해주는 Service 파일
public class FileService {
    // 이미지를 업로드합니다.
        public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception {
        // uploadPath) 업로드할 경로, originalFileName) 파일 이름 원본, fileData) 업로드할 데이터
        UUID uuid = UUID.randomUUID();
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")); // 파일 확장자

        String savedFileName = uuid.toString()+extension; // 저장될 파일 이름
        String fileUploadFullUrl = uploadPath+"/"+savedFileName; // 전체 경로 포함

        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData); // 네트워크를 통한 파일 쓰기(업로드)
        fos.close();

        return savedFileName;
    }

    // 특정 상품 이미지에 대한 수정 작업시 이전 이미지에 대한 삭제를 합니다.
    public void deleteFile(String filepath) throws Exception{
        // filepath는 삭제될 파일의 경로 및 이름 정보를 담고 있는 변수
        File deleteFile = new File(filepath); // 삭제될 파일 객체
        if(deleteFile.exists()) { // 파일이 존재하면
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        }else{
            log.info("해당 파일이 존재하지 않습니다.");

        }
    }
}
