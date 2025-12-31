package com.kh.eco.api.news.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsDTO {
	
    private Long id; 
    private String title;   
    private String link;     
    private String description; 
    private String date;       
    private String imageUrl;    
    private String category;   
    private int views;      

}
