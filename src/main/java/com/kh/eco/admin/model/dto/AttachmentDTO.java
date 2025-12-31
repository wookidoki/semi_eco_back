package com.kh.eco.admin.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AttachmentDTO {
    
    private Long fileNo;             // FILE_NO
    private Long refBno;             // REF_BNO
    private String originalFileName; // ORIGINAL_FILE_NAME
    private String modifiedFileName; // MODIFIED_FILE_NAME
    private String attachmentPath;   // ATTACHMENT_PATH
    private String status;           // STATUS
}
