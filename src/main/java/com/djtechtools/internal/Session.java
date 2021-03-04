package com.djtechtools.internal;

import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.MidiIn;
import com.bitwig.extension.controller.api.MidiOut;

public class Session {
    private MidiIn midiIn;
    private MidiOut midiOut;

    public Session(ControllerHost host) {
        midiIn = host.getMidiInPort(0);
        midiOut = host.getMidiOutPort(0);
    }

    public MidiIn getMidiIn() {
        return midiIn;
    }

    public MidiOut getMidiOut() {
        return midiOut;
    }

    public void sendMidi(int status, int data1, int data2) {
        midiOut.sendMidi(status, data1, data2);
    }
}
