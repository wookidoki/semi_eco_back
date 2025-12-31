package com.kh.eco.file;

import org.springframework.web.multipart.MultipartFile;

/**
 * 파일 업로드 서비스 인터페이스
 * S3 또는 로컬 저장소를 추상화하여 일관된 파일 업로드 기능 제공
 */
public interface FileUploadService {

    /**
     * 파일 업로드
     * @param file 업로드할 파일
     * @return 저장된 파일의 접근 URL
     */
    String upload(MultipartFile file);

    /**
     * 폴더를 지정하여 파일 업로드
     * @param file 업로드할 파일
     * @param folder 저장할 폴더명 (예: "profile", "board", "notice")
     * @return 저장된 파일의 접근 URL
     */
    String upload(MultipartFile file, String folder);

    /**
     * 파일 삭제
     * @param fileUrl 삭제할 파일의 URL
     * @return 삭제 성공 여부
     */
    boolean delete(String fileUrl);
}
