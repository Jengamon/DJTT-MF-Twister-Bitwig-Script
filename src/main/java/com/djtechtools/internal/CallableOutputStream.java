package com.djtechtools.internal;

import com.bitwig.extension.controller.api.ControllerHost;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Consumer;

public class CallableOutputStream extends OutputStream {
    private String mBuffer = "";
    private Consumer<String> mPrintTarget;

    public CallableOutputStream(Consumer<String> printTarget) {
        mPrintTarget = printTarget;
    }

    @Override
    public void write(int b) throws IOException {
        if ((char) b == '\n') {
            mPrintTarget.accept(mBuffer);
            mBuffer = "";
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append(mBuffer);
            builder.append((char) b);
            mBuffer = builder.toString();
        }
    }
}
