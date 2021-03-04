package com.djtechtools;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.callback.ShortMidiMessageReceivedCallback;
import com.bitwig.extension.controller.api.BooleanValue;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.Preferences;
import com.bitwig.extension.controller.api.Transport;
import com.bitwig.extension.controller.ControllerExtension;
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
//      BooleanValue enableShiftEncoders = prefs.getBooleanSetting("Enable Shift Encoders?", "General", false);

      mSession = new Session(host);
      mHardware = new TwisterHardware(host, mSession);

      mTransport = host.createTransport();
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
      // MMC Transport Controls:
      if (data.equals("f07f7f0605f7"))
            mTransport.rewind();
      else if (data.equals("f07f7f0604f7"))
            mTransport.fastForward();
      else if (data.equals("f07f7f0601f7"))
            mTransport.stop();
      else if (data.equals("f07f7f0602f7"))
            mTransport.play();
      else if (data.equals("f07f7f0606f7"))
            mTransport.record();
   }

   private Transport mTransport;
}
