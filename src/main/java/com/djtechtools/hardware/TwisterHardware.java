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
    private CursorRemoteControlsPage mRemote;
    private ControllerHost mHost;

    public TwisterHardware(ControllerHost host, Session session, EnumValue focusDevice) {
        mHost = host;
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
        CursorDevice device = host.createCursorTrack(0, 0).createCursorDevice("Primary", "Primary Device", 0, CursorDeviceFollowMode.FOLLOW_SELECTION);
        mRemote = device.createCursorRemoteControlsPage(128);

        for(int i = 0; i < mKnobs.length; i++) {
            // TODO Use bank number to determine who's indications get turned on.
            mControls.getControl(i).setIndication(true);
            mControls.getControl(i + 64).setIndication(true);
            mRemote.getParameter(i).setIndication(true);
            mRemote.getParameter(i + 64).setIndication(true);
        }

        mRemote.setHardwareLayout(HardwareControlType.ENCODER, 8);

        focusDevice.addValueObserver((fd) -> {
            for(int i = 0; i < mKnobs.length; i++) {
                mKnobs[i].getKnob().clearBindings();
                mKnobs[i].getShiftKnob().clearBindings();
                if(fd.equals("Device")) {
                    mKnobs[i].getKnob().addBinding(mRemote.getParameter(i));
                    mKnobs[i].getShiftKnob().addBinding(mRemote.getParameter(i + 64));
                } else {
                    mKnobs[i].getKnob().addBinding(mControls.getControl(i));
                    mKnobs[i].getShiftKnob().addBinding(mControls.getControl(i + 64));
                }
            }
        });
    }

    public void updateHardware() {
        mSurface.updateHardware();
    }

    public void handleMidi(ShortMidiMessage msg) {
        if(msg.isControlChange() && msg.getChannel() == 3) {
            int bank = msg.getData1();
            System.out.println("" + msg);
            if(msg.getData2() == 127) {
                mHost.showPopupNotification("Switched to Bank " + (bank + 1));
            }
        }
    }
}
