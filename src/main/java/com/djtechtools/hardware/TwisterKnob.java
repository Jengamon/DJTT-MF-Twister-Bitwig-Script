package com.djtechtools.hardware;

import com.bitwig.extension.api.Color;
import com.bitwig.extension.controller.api.*;
import com.djtechtools.internal.Session;

public class TwisterKnob {
    private HardwareButton mButton;
    private RelativeHardwareKnob mEncoder;
    private RelativeHardwareKnob mSEncoder;

    public TwisterKnob(Session session, HardwareSurface surface, int i) {
        mButton = surface.createHardwareButton("KnobB" + i);
        mEncoder = surface.createRelativeHardwareKnob("EncoderB" + i);
        mSEncoder = surface.createRelativeHardwareKnob("SEncoderB" + i);

        // TODO Provide hardware virtual surface that splits up the 128 virtual knobs
        HardwareActionMatcher encoderPress = session.getMidiIn().createCCActionMatcher(1, i, 127);
        HardwareActionMatcher encoderRelease = session.getMidiIn().createCCActionMatcher(1, i, 0);
        RelativeHardwareValueMatcher encoderMatcher = session.getMidiIn().createRelativeBinOffsetCCValueMatcher(0, i, 127);
        RelativeHardwareValueMatcher shiftEncoderMatcher = session.getMidiIn().createRelativeBinOffsetCCValueMatcher(4, i, 127);

        mButton.pressedAction().setActionMatcher(encoderPress);
        mButton.releasedAction().setActionMatcher(encoderRelease);
        mButton.isPressed().addValueObserver((pressed) -> session.sendMidi(0xB1, i, pressed ? 127 : 0));

        mEncoder.setAdjustValueMatcher(encoderMatcher);
        mEncoder.targetValue().addValueObserver((val) -> session.sendMidi(0xB0, i, (int)Math.round(val * 127)));

        mSEncoder.setAdjustValueMatcher(shiftEncoderMatcher);
        mSEncoder.targetValue().addValueObserver((val) -> session.sendMidi(0xB4, i, (int)Math.round(val * 127)));
    }

    public HardwareButton getButton() { return mButton; }
    public RelativeHardwareKnob getEncoder() { return mEncoder; }
    public RelativeHardwareKnob getShiftEncoder() { return mSEncoder; }
}
