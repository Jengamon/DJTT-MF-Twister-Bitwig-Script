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
//    private ArrayList<HardwareBinding> mBindings;
    private CursorRemoteControlsPage mTopRemote;
    private CursorRemoteControlsPage mBottomRemote;
    private ControllerHost mHost;

    private NoteInput mNoteInput;

    private TwisterSideButton mSideLeft1;
    private TwisterSideButton mSideLeft2;
    private TwisterSideButton mSideRight1;
    private TwisterSideButton mSideRight2;

    public TwisterHardware(ControllerHost host, Session session, EnumValue focusDevice) {
        mHost = host;
        mSurface = host.createHardwareSurface();
//        mBank = new AtomicInteger(0);

        mKnobs = new TwisterKnob[64];
        for (int i = 0; i < mKnobs.length; i++) {
            mKnobs[i] = new TwisterKnob(session, mSurface, i);
        }

        mSideLeft1 = new TwisterSideButton(session, mSurface, 8);
        mSideLeft2 = new TwisterSideButton(session, mSurface, 10);
        mSideRight1 = new TwisterSideButton(session, mSurface, 11);
        mSideRight2 = new TwisterSideButton(session, mSurface, 13);

        // Force device to bank 1
        session.sendMidi(0xB3, 0, 127);

        //mBindings = new ArrayList<>();
        mControls = host.createUserControls(128);
        CursorDevice device = host.createCursorTrack(0, 0).createCursorDevice("Primary", "Primary Device", 0, CursorDeviceFollowMode.FOLLOW_SELECTION);
        mTopRemote = device.createCursorRemoteControlsPage("Top", 8, "");
        mBottomRemote = device.createCursorRemoteControlsPage("Bottom", 8, "");
        mNoteInput = session.getMidiIn().createNoteInput("MF Twister");

        HardwareBindable topPrev = mTopRemote.selectPreviousAction();
        HardwareBindable botPrev = mBottomRemote.selectPreviousAction();
        HardwareBindable topNext = mTopRemote.selectNextAction();
        HardwareBindable botNext = mBottomRemote.selectNextAction();


        for(int i = 0; i < mKnobs.length; i++) {
            // TODO Use bank number to determine who's indications get turned on.
            mControls.getControl(i).setIndication(true);
            mControls.getControl(i + 64).setIndication(true);
        }

        for(int i = 0; i < 8; i++) {
            mTopRemote.getParameter(i).setIndication(true);
            mBottomRemote.getParameter(i).setIndication(true);
        }

        mTopRemote.setHardwareLayout(HardwareControlType.ENCODER, 4);
        mBottomRemote.setHardwareLayout(HardwareControlType.ENCODER, 4);

        focusDevice.addValueObserver((fd) -> {
            for(int i = 0; i < mKnobs.length; i++) {
                mKnobs[i].getEncoder().clearBindings();
                mKnobs[i].getShiftEncoder().clearBindings();
                mSideLeft1.getSideButton().pressedAction().clearBindings();
                mSideLeft2.getSideButton().pressedAction().clearBindings();
                mSideRight1.getSideButton().pressedAction().clearBindings();
                mSideRight2.getSideButton().pressedAction().clearBindings();
                switch (fd) {
                    case "Device":
                        if(i < 8) {
                            mKnobs[i].getEncoder().addBinding(mTopRemote.getParameter(i));
                        } else if (i < 16) {
                            mKnobs[i].getEncoder().addBinding(mBottomRemote.getParameter(i - 8));
                        }
                        mSideLeft1.getSideButton().pressedAction().addBinding(topPrev);
                        mSideLeft2.getSideButton().pressedAction().addBinding(botPrev);
                        mSideRight1.getSideButton().pressedAction().addBinding(topNext);
                        mSideRight2.getSideButton().pressedAction().addBinding(botNext);
                        break;
                    case "Global":
                        mKnobs[i].getEncoder().addBinding(mControls.getControl(i));
                        mKnobs[i].getShiftEncoder().addBinding(mControls.getControl(i + 64));
                        break;
                    case "MIDI":
//                        mKnobs[i].getEncoder().addBinding(midiActions[i]);
//                        mKnobs[i].getShiftEncoder().addBinding(sMidiActions[i]);
                        break;
                    default:
                        throw new RuntimeException("Invalid focus mode: " + fd);
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
