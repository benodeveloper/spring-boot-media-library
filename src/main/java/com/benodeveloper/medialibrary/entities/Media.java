package com.benodeveloper.medialibrary.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Media {
    private String uuid;
    private String name;
    private String fileName;
    private String fileType;
    private Long size;
}
