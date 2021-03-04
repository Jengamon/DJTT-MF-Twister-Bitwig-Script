package com.djtechtools;
import java.util.UUID;

import com.bitwig.extension.api.PlatformType;
import com.bitwig.extension.controller.AutoDetectionMidiPortNamesList;
import com.bitwig.extension.controller.ControllerExtensionDefinition;
import com.bitwig.extension.controller.api.ControllerHost;

public class MidiFighterTwisterExtensionDefinition extends ControllerExtensionDefinition
{
   private static final UUID DRIVER_ID = UUID.fromString("f3383708-a3ec-4402-a991-1440bf7fa5d9");
   
   public MidiFighterTwisterExtensionDefinition()
   {
   }

   @Override
   public String getName()
   {
      return "Midi Fighter Twister";
   }
   
   @Override
   public String getAuthor()
   {
      return "Jengamon";
   }

   @Override
   public String getVersion()
   {
      return "0.1";
   }

   @Override
   public UUID getId()
   {
      return DRIVER_ID;
   }
   
   @Override
   public String getHardwareVendor()
   {
      return "DJ TechTools";
   }
   
   @Override
   public String getHardwareModel()
   {
      return "Midi Fighter Twister";
   }

   @Override
   public int getRequiredAPIVersion()
   {
      return 12;
   }

   @Override
   public int getNumMidiInPorts()
   {
      return 1;
   }

   @Override
   public int getNumMidiOutPorts()
   {
      return 1;
   }

   @Override
   public void listAutoDetectionMidiPortNames(final AutoDetectionMidiPortNamesList list, final PlatformType platformType)
   {
      if (platformType == PlatformType.WINDOWS)
      {
         list.add(new String[]{"Midi Fighter Twister"}, new String[]{"Midi Fighter Twister"});
         list.add(new String[]{"Midi Fighter Twister #2"}, new String[]{"Midi Fighter Twister #2"});
         list.add(new String[]{"Midi Fighter Twister #3"}, new String[]{"Midi Fighter Twister #3"});
         list.add(new String[]{"Midi Fighter Twister #4"}, new String[]{"Midi Fighter Twister #4"});
      }
      else if (platformType == PlatformType.MAC)
      {
         list.add(new String[]{"Midi Fighter Twister"}, new String[]{"Midi Fighter Twister"});
         list.add(new String[]{"Midi Fighter Twister #2"}, new String[]{"Midi Fighter Twister #2"});
         list.add(new String[]{"Midi Fighter Twister #3"}, new String[]{"Midi Fighter Twister #3"});
         list.add(new String[]{"Midi Fighter Twister #4"}, new String[]{"Midi Fighter Twister #4"});
      }
      else if (platformType == PlatformType.LINUX)
      {
         list.add(new String[]{"Midi Fighter Twister"}, new String[]{"Midi Fighter Twister"});
         list.add(new String[]{"Midi Fighter Twister #2"}, new String[]{"Midi Fighter Twister #2"});
         list.add(new String[]{"Midi Fighter Twister #3"}, new String[]{"Midi Fighter Twister #3"});
         list.add(new String[]{"Midi Fighter Twister #4"}, new String[]{"Midi Fighter Twister #4"});
      }
   }

   @Override
   public MidiFighterTwisterExtension createInstance(final ControllerHost host)
   {
      return new MidiFighterTwisterExtension(this, host);
   }
}
