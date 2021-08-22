package com.djtechtools.hardware;

import com.bitwig.extension.controller.api.EnumDefinition;
import com.bitwig.extension.controller.api.EnumValueDefinition;

public class MIDIEnumValue implements EnumValueDefinition {
    @Override
    public boolean equals(Object o) {
        return o.getClass() == MIDIEnumValue.class;
    }

    @Override
    public EnumDefinition enumDefinition() {
        return new FocusEnumDefinition();
    }

    @Override
    public int getValueIndex() {
        return 2;
    }

    @Override
    public String getId() {
        return "MIDI";
    }

    @Override
    public String getDisplayName() {
        return "MIDI Mode";
    }

    @Override
    public String getLimitedDisplayName(int maxLength) {
        if(maxLength >= 4) {
            return "MIDI";
        } else if(maxLength >= 3) {
            return "MID";
        } else if (maxLength >= 2) {
            return "MD";
        } else {
            return "M";
        }
    }
}
