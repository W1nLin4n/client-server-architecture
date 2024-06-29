package com.w1nlin4n.project.utility;

import java.util.HashMap;

public class ArgsParser {
    public static HashMap<String, String> parse(String[] args) {
        HashMap<String, String> parsedArgs = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            parsedArgs.put(args[i], args[i + 1]);
        }
        return parsedArgs;
    }
}
