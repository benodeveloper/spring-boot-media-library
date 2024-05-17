package com.benodeveloper.medialibrary.query;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ImageQuery {
    private int w;
    private  int h;
    private String fit;
    private String f = "jpg";

}
