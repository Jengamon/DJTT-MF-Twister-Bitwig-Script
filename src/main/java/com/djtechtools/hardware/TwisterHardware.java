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

    public TwisterHardware(ControllerHost host, Session session, BooleanValue ese) {
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
        }
//        bindBank(0);
    }
//
//    private void bindBank(int bank) {
//        for(HardwareBinding binding : mBindings) {
//            binding.removeBinding();
//        }
//        mBindings.clear();
//        mBank.set(bank);
//        for(int i = 0; i < mKnobs.length; i++) {
//            mBindings.add(mControls.getControl(i + bank * 16).addBinding(mKnobs[i].getKnob()));
//            mBindings.add(mControls.getControl(64 + i + bank * 16).addBinding(mKnobs[i].getShiftKnob()));
////            Parameter buttonParam = mControls.getControl(128 + bank * 16);
////            mBindings.add(mKnobs[i].getButton().pressedAction().addBinding(buttonParam));
////            mBindings.add(mKnobs[i].getButton().releasedAction().addBinding(buttonParam));
//        }
//    }

    public void updateHardware() {
        mSurface.updateHardware();
    }

    public void handleMidi(ShortMidiMessage msg) {
        if(msg.isControlChange() && msg.getChannel() == 3) {
            int bank = msg.getData1();
            if (msg.getData2() == 0xFF) {
//                bindBank(bank);
            }
        }
    }
}
