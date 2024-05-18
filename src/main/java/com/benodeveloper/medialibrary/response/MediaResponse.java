package com.benodeveloper.medialibrary.response;


//public class MediaResponse {
//    // json response { file_url: "http://localhost:8080/api/images/dir/filename.png", file_path: "uploads/dir/filename.png", content_type: "image/png", size: 15454 , filename: "filename.png"}
//    String fileUrl;
//}

import com.fasterxml.jackson.annotation.JsonProperty;

public record MediaResponse(String filename, @JsonProperty("file_url") String fileUrl, @JsonProperty("file_path") String filePath, String contentType, Long size) {}