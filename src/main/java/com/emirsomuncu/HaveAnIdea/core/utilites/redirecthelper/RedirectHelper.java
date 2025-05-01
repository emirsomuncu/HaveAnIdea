package com.emirsomuncu.HaveAnIdea.core.utilites.redirecthelper;

import lombok.NoArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@NoArgsConstructor
public class RedirectHelper {

    public static String encodeUrlPathSegment(String value) {
        if (value == null) {
            return "";
        }
        try {
            return URLEncoder.encode(value, "UTF-8")
                    .replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 encoding not supported", e);
        }
    }
}
