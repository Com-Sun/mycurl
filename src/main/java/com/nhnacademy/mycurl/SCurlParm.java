package com.nhnacademy.mycurl;

import com.beust.jcommander.Parameter;
import java.util.ArrayList;
import java.util.List;

public class SCurlParm {
    private String requestMethod = "GET";

    @Parameter(hidden = true)
    private List<String> parameters = new ArrayList<>();

    @Parameter(names = "scurl", description = "start scurl")
    private boolean scurl = false;

    @Parameter(names = {"-v"}, hidden = true)
    private boolean verbose = false;


    public boolean isScurl() {
        return scurl;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public List<String> getParameters() {
        return parameters;
    }
}
