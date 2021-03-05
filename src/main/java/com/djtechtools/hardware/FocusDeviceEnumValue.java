package com.djtechtools.hardware;

import com.bitwig.extension.controller.api.EnumDefinition;
import com.bitwig.extension.controller.api.EnumValueDefinition;

public class FocusDeviceEnumValue implements EnumValueDefinition {
    @Override
    public boolean equals(Object o) {
        return o.getClass() == FocusDeviceEnumValue.class;
    }

    @Override
    public EnumDefinition enumDefinition() {
        return new FocusEnumDefinition();
    }

    @Override
    public int getValueIndex() {
        return 0;
    }

    @Override
    public String getId() {
        return "Device";
    }

    @Override
    public String getDisplayName() {
        return "Focus Device";
    }

    @Override
    public String getLimitedDisplayName(int maxLength) {
        if(maxLength >= 3) {
            return getDisplayName().substring(6, maxLength - 2) + "..";
        } else if (maxLength >= 2) {
            return "FD";
        } else {
            return "D";
        }
    }
}
