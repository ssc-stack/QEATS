package com.qeats.utils;

import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class FixtureHelpers {

    private FixtureHelpers() {
    }


    public static String fixture(String filename) {
        return fixture(filename, StandardCharsets.UTF_8);
    }


    private static String fixture(String filename, Charset charset) {
        final URL resource = Resources.getResource(filename);
        try {
            return Resources.toString(resource, charset).trim();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
