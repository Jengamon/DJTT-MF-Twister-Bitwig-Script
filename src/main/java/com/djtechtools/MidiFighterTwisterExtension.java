package com.djtechtools;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.callback.ShortMidiMessageReceivedCallback;
import com.bitwig.extension.controller.api.*;
import com.bitwig.extension.controller.ControllerExtension;
import com.djtechtools.hardware.FocusDeviceEnumValue;
import com.djtechtools.hardware.FocusGlobalEnumValue;
import com.djtechtools.hardware.TwisterHardware;
import com.djtechtools.internal.CallableOutputStream;
import com.djtechtools.internal.Session;

import java.io.PrintStream;

public class MidiFighterTwisterExtension extends ControllerExtension
{
   private Session mSession;
   private TwisterHardware mHardware;

   protected MidiFighterTwisterExtension(final MidiFighterTwisterExtensionDefinition definition, final ControllerHost host)
   {
      super(definition, host);
   }

   @Override
   public void init()
   {
      final ControllerHost host = getHost();
      System.setOut(new PrintStream(new CallableOutputStream(host::println)));
      System.setErr(new PrintStream(new CallableOutputStream(host::errorln)));

      Preferences prefs = host.getPreferences();
      DocumentState state = host.getDocumentState();
      SettableEnumValue focusDevice = state.getEnumSetting("Focus Device", "General", new FocusGlobalEnumValue());
//      BooleanValue enableShiftEncoders = prefs.getBooleanSetting("Enable Shift Encoders?", "General", false);

      mSession = new Session(host);
      mHardware = new TwisterHardware(host, mSession, focusDevice);

      mSession.getMidiIn().setMidiCallback((ShortMidiMessageReceivedCallback) this::onMidi0);
      mSession.getMidiIn().setSysexCallback(this::onSysex0);

      // TODO: Perform your driver initialization here.
      // For now just show a popup notification for verification that it is running.
      host.showPopupNotification("Midi Fighter Twister Initialized");
   }

   @Override
   public void exit()
   {
      // TODO: Perform any cleanup once the driver exits
      // For now just show a popup notification for verification that it is no longer running.
      getHost().showPopupNotification("Midi Fighter Twister Exited");
   }

   @Override
   public void flush()
   {
      mHardware.updateHardware();
   }

   /** Called when we receive short MIDI message on port 0. */
   private void onMidi0(ShortMidiMessage msg) 
   {
      // Forward NoteOn / NoteOff messages
      if(msg.isNoteOn() || msg.isNoteOff()) {
         mSession.sendMidi(msg.getStatusByte(), msg.getData1(), msg.getData2());
      } else {
         mHardware.handleMidi(msg);
      }
   }

   /** Called when we receive sysex MIDI message on port 0. */
   private void onSysex0(final String data) 
   {
      // Do Nothing
   }
}
