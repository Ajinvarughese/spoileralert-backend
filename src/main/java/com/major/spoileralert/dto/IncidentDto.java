package com.major.spoileralert.dto;

import com.major.spoileralert.entity.Subcategory;
import com.major.spoileralert.entity.User;
import com.major.spoileralert.enumeration.Severity;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class IncidentDto {

    private String title;

    private String description;

    private Subcategory subcategory;

    private String latitude;

    private String longitude;

    private String location;

    private List<MultipartFile> files;

    private Severity severity;  // Selected by user

    private User reportedBy;

}
