package com.djtechtools.hardware;

import com.bitwig.extension.controller.api.EnumDefinition;
import com.bitwig.extension.controller.api.EnumValueDefinition;

public class FocusGlobalEnumValue implements EnumValueDefinition {
    @Override
    public boolean equals(Object o) {
        return o.getClass() == FocusGlobalEnumValue.class;
    }

    @Override
    public EnumDefinition enumDefinition() {
        return new FocusEnumDefinition();
    }

    @Override
    public int getValueIndex() {
        return 1;
    }

    @Override
    public String getId() {
        return "Global";
    }

    @Override
    public String getDisplayName() {
        return "Focus Global";
    }

    @Override
    public String getLimitedDisplayName(int maxLength) {
        if(maxLength >= 3) {
            return getDisplayName().substring(6, maxLength - 2) + "..";
        } else if (maxLength >= 2) {
            return "FG";
        } else {
            return "G";
        }
    }
}
