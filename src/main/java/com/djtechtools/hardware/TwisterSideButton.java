package com.djtechtools.hardware;

import com.bitwig.extension.controller.api.HardwareActionMatcher;
import com.bitwig.extension.controller.api.HardwareButton;
import com.bitwig.extension.controller.api.HardwareSurface;
import com.djtechtools.internal.Session;

public class TwisterSideButton {
    private HardwareButton mSideButton;

    public TwisterSideButton(Session session, HardwareSurface surface, int cc) {
        mSideButton = surface.createHardwareButton("SideButton" + cc);

        HardwareActionMatcher press = session.getMidiIn().createCCActionMatcher(3, cc, 127);
        HardwareActionMatcher release = session.getMidiIn().createCCActionMatcher(3, cc, 0);

        mSideButton.pressedAction().setActionMatcher(press);
        mSideButton.releasedAction().setActionMatcher(release);
    }

    public HardwareButton getSideButton() { return mSideButton; }
}
