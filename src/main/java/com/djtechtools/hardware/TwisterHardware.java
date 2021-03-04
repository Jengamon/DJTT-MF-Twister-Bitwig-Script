package com.djtechtools.hardware;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.controller.api.*;
import com.djtechtools.internal.Session;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class TwisterHardware {
    private TwisterKnob[] mKnobs;
    private HardwareSurface mSurface;
//    private AtomicInteger mBank;
    private UserControlBank mControls;
    private ArrayList<HardwareBinding> mBindings;

    public TwisterHardware(ControllerHost host, Session session) {
        mSurface = host.createHardwareSurface();
//        mBank = new AtomicInteger(0);

        mKnobs = new TwisterKnob[64];
        for (int i = 0; i < mKnobs.length; i++) {
            mKnobs[i] = new TwisterKnob(session, mSurface, i);
        }

        // Force device to bank 1
        session.sendMidi(0xB3, 0, 127);

        mBindings = new ArrayList<>();

        mControls = host.createUserControls(128);

        for(int i = 0; i < mKnobs.length; i++) {
            mControls.getControl(i).addBinding(mKnobs[i].getKnob());
            mControls.getControl(i + 64).addBinding(mKnobs[i].getShiftKnob());

            mControls.getControl(i).setIndication(true);
            mControls.getControl(i + 64).setIndication(true);
        }
    }

    public void updateHardware() {
        mSurface.updateHardware();
    }

    public void handleMidi(ShortMidiMessage msg) {
        if(msg.isControlChange() && msg.getChannel() == 3) {
//            int bank = msg.getData1();
//            if (msg.getData2() == 0xFF) {
////                bindBank(bank);
//            }
        }
    }
}
