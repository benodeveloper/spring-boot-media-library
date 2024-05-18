package com.benodeveloper.medialibrary.query;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@RequiredArgsConstructor
@Setter
public class ImageQuery {
    private String w = "auto";
    private String h = "auto";
    @Getter
    private String fit;
    @Getter
    private String f = "jpg";

    public int getW() {
        int width = -1;
        if(!w.equals("auto")) {
            try {
                width = Integer.parseInt(w);
            } catch (Exception e) {
                width = 0;
            }
        }
        if(width > 1 && width < 50) {
            width = 50;
        }
        return width;
    }

    public int getH() {
        int height = -1;
        if(!h.equals("auto")) {
            try {
                height = Integer.parseInt(h);
            } catch (Exception e) {
                height = 0;
            }
        }
        if(height > 1 && height < 50) {
            height = 50;
        }
        return height;
    }
}
