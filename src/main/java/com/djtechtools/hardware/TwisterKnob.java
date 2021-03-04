package com.djtechtools.hardware;

import com.bitwig.extension.controller.api.*;
import com.djtechtools.internal.Session;

import java.util.concurrent.atomic.AtomicInteger;

public class TwisterKnob {
    private AbsoluteHardwareKnob mKnob;
    private AbsoluteHardwareKnob mSKnob;
    private HardwareButton mButton;

    public TwisterKnob(Session session, HardwareSurface surface, int i) {
        mButton = surface.createHardwareButton("KnobB" + i);
        mKnob = surface.createAbsoluteHardwareKnob("Knob" + i);
        mSKnob = surface.createAbsoluteHardwareKnob("SKnob" + i);

        // TODO Provide hardware virtual surface that splits up the 128 virtual knobs

        AbsoluteHardwareValueMatcher bankMatcher = session.getMidiIn().createAbsoluteCCValueMatcher(0, i);
        AbsoluteHardwareValueMatcher sBankMatcher = session.getMidiIn().createAbsoluteCCValueMatcher(4, i);
        HardwareActionMatcher encoderPress = session.getMidiIn().createCCActionMatcher(1, i, 127);
        HardwareActionMatcher encoderRelease = session.getMidiIn().createCCActionMatcher(1, i, 0);

        mButton.pressedAction().setActionMatcher(encoderPress);
        mButton.releasedAction().setActionMatcher(encoderRelease);
        mButton.isPressed().addValueObserver((pressed) -> {
            session.sendMidi(0xB1, i, pressed ? 127 : 0);
        });

        mKnob.setAdjustValueMatcher(bankMatcher);
        mKnob.targetValue().addValueObserver((val) -> {
            session.sendMidi(0xB0, i, (int)Math.round(val * 127));
        });

        mSKnob.setAdjustValueMatcher(sBankMatcher);
        mSKnob.targetValue().addValueObserver((val) -> {
            session.sendMidi(0xB4, i, (int)Math.round(val * 127));
        });
    }

    public AbsoluteHardwareKnob getKnob() { return mKnob; }
    public AbsoluteHardwareKnob getShiftKnob() { return mSKnob; }
    public HardwareButton getButton() { return mButton; }
}
