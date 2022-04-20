package com.nhnacademy.mycurl;

import com.beust.jcommander.Parameter;
import java.util.ArrayList;
import java.util.List;

public class SCurlParm {
    private String requestMethod = "GET";
    private int count = 0;


    @Parameter(hidden = true)
    private List<String> parameters = new ArrayList<>();

    @Parameter(names = "scurl", description = "start scurl")
    private boolean scurl = false;

    @Parameter(names = {"-v"}, hidden = true)
    private boolean verbose = false;

    @Parameter(names = {"-X"}, hidden = true)
    private boolean method = false;

    public boolean isScurl() {
        return scurl;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public boolean isMethod() {
        return method;
    }

    public String getMethod() {
        if (isMethod()) {
            requestMethod = parameters.get(count);
            count++;
            return requestMethod;
        }
        return requestMethod;
    }

    public int getCount() {
        return count;
    }
}
