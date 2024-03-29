package com.djtechtools.hardware;

import com.bitwig.extension.controller.api.EnumDefinition;
import com.bitwig.extension.controller.api.EnumValueDefinition;

public class FocusEnumDefinition implements EnumDefinition {
    @Override
    public boolean equals(Object o) {
        return o.getClass() == FocusEnumDefinition.class;
    }

    @Override
    public int getValueCount() {
        return 3;
    }

    @Override
    public EnumValueDefinition valueDefinitionAt(int valueIndex) {
        return (new EnumValueDefinition[] {
                new FocusDeviceEnumValue(),
                new FocusGlobalEnumValue(),
                new MIDIEnumValue(),
        })[valueIndex];
    }

    @Override
    public EnumValueDefinition valueDefinitionFor(String id) {
        if(id.equals("Device")) {
            return new FocusDeviceEnumValue();
        } else if (id.equals("Global")) {
            return new FocusGlobalEnumValue();
        } else if (id.equals("MIDI")) {
            return new MIDIEnumValue();
        } else {
            return null;
        }
    }
}
