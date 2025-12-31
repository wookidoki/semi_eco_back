package com.kh.eco.file;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kh.eco.exception.InvalidRequestException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

/**
 * AWS S3 파일 업로드 서비스
 * 모든 파일 업로드는 이 서비스를 통해 S3에 저장
 */
@Slf4j
@Service("s3FileUploadService")
@RequiredArgsConstructor
public class S3Service implements FileUploadService {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;

    /**
     * 파일 업로드
     * @param file 업로드할 파일
     * @return S3 파일 URL
     */
    @Override
    public String upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidRequestException("업로드할 파일이 없습니다.");
        }

        try {
            String fileName = generateFileName(file.getOriginalFilename());

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            String fileUrl = getFileUrl(fileName);
            log.info("S3 파일 업로드 완료 - {}", fileUrl);

            return fileUrl;

        } catch (IOException e) {
            log.error("S3 파일 업로드 실패: {}", e.getMessage());
            throw new InvalidRequestException("파일 업로드에 실패했습니다.");
        }
    }

    /**
     * 파일 업로드 (폴더 지정)
     * @param file 업로드할 파일
     * @param folder 저장할 폴더명 (예: "profile", "board")
     * @return S3 파일 URL
     */
    @Override
    public String upload(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw new InvalidRequestException("업로드할 파일이 없습니다.");
        }

        try {
            String fileName = folder + "/" + generateFileName(file.getOriginalFilename());

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            String fileUrl = getFileUrl(fileName);
            log.info("S3 파일 업로드 완료 - {}", fileUrl);

            return fileUrl;

        } catch (IOException e) {
            log.error("S3 파일 업로드 실패: {}", e.getMessage());
            throw new InvalidRequestException("파일 업로드에 실패했습니다.");
        }
    }

    /**
     * 파일 삭제
     * @param fileUrl 삭제할 파일의 S3 URL
     * @return 삭제 성공 여부
     */
    @Override
    public boolean delete(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return false;
        }

        try {
            String fileName = extractFileName(fileUrl);

            DeleteObjectRequest request = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            s3Client.deleteObject(request);
            log.info("S3 파일 삭제 완료 - {}", fileName);

            return true;

        } catch (Exception e) {
            log.error("S3 파일 삭제 실패: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 고유 파일명 생성
     * 형식: Eco_yyyyMMddHHmmss_UUID.확장자
     */
    private String generateFileName(String originalFilename) {
        String extension = getExtension(originalFilename);
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        return "Eco_" + timestamp + "_" + uuid + extension;
    }

    /**
     * 파일 확장자 추출
     */
    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }

    /**
     * S3 파일 URL 생성
     */
    private String getFileUrl(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileName);
    }

    /**
     * URL에서 파일명 추출
     */
    private String extractFileName(String fileUrl) {
        String prefix = String.format("https://%s.s3.%s.amazonaws.com/", bucketName, region);
        if (fileUrl.startsWith(prefix)) {
            return fileUrl.substring(prefix.length());
        }
        return fileUrl;
    }
}
