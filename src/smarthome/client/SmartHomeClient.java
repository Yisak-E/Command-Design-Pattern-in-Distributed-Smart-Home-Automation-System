package smarthome.client;

import java.util.List;
import java.util.concurrent.TimeUnit;

import smarthome.commands.Command;
import smarthome.commands.LockDoorCommand;
import smarthome.commands.MacroCommand;
import smarthome.commands.PlayMusicCommand;
import smarthome.commands.SetAirConditionerTemperatureCommand;
import smarthome.commands.StopMusicCommand;
import smarthome.commands.TurnOffAirConditionerCommand;
import smarthome.commands.TurnOffLightCommand;
import smarthome.commands.TurnOnAirConditionerCommand;
import smarthome.commands.TurnOnLightCommand;
import smarthome.commands.UnlockDoorCommand;
import smarthome.devices.AirConditioner;
import smarthome.devices.DoorLock;
import smarthome.devices.Light;
import smarthome.devices.MusicPlayer;
import smarthome.invoker.SmartHomeInvoker;
import smarthome.scheduler.CommandScheduler;

public final class SmartHomeClient {
    private SmartHomeClient() {
    }

    public static void main(String[] args) throws InterruptedException {
        Light light = new Light();
        AirConditioner airConditioner = new AirConditioner();
        DoorLock doorLock = new DoorLock();
        MusicPlayer musicPlayer = new MusicPlayer();

        TurnOnLightCommand lightOn = new TurnOnLightCommand(light);
        TurnOffLightCommand lightOff = new TurnOffLightCommand(light);
        TurnOnAirConditionerCommand acOn = new TurnOnAirConditionerCommand(airConditioner);
        TurnOffAirConditionerCommand acOff = new TurnOffAirConditionerCommand(airConditioner);
        SetAirConditionerTemperatureCommand acCool = new SetAirConditionerTemperatureCommand(airConditioner, 20);
        UnlockDoorCommand doorUnlock = new UnlockDoorCommand(doorLock);
        LockDoorCommand doorLockCommand = new LockDoorCommand(doorLock);
        PlayMusicCommand playMusic = new PlayMusicCommand(musicPlayer);
        StopMusicCommand stopMusic = new StopMusicCommand(musicPlayer);

        Command morningRoutine = new MacroCommand(List.of(
                doorUnlock,
                lightOn,
                acOn,
                acCool,
                playMusic));

        Command nightRoutine = new MacroCommand(List.of(
                lightOff,
                stopMusic,
                acOff,
                doorLockCommand));

        SmartHomeInvoker invoker = new SmartHomeInvoker();
        invoker.registerCommand("light:on", lightOn);
        invoker.registerCommand("light:off", lightOff);
        invoker.registerCommand("door:unlock", doorUnlock);
        invoker.registerCommand("door:lock", doorLockCommand);
        invoker.registerCommand("ac:on", acOn);
        invoker.registerCommand("ac:off", acOff);
        invoker.registerCommand("ac:cool", acCool);
        invoker.registerCommand("music:play", playMusic);
        invoker.registerCommand("music:stop", stopMusic);
        invoker.registerCommand("routine:morning", morningRoutine);
        invoker.registerCommand("routine:night", nightRoutine);

        invoker.execute("routine:morning");
        invoker.undo();
        invoker.redo();

        invoker.execute("light:off");
        invoker.undo();

        try (CommandScheduler scheduler = new CommandScheduler(invoker)) {
            scheduler.schedule(stopMusic, 2, TimeUnit.SECONDS);
            Thread.sleep(TimeUnit.SECONDS.toMillis(3));
        }
    }
}
