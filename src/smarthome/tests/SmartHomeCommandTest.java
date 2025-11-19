package smarthome.tests;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import smarthome.commands.Command;
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

public final class SmartHomeCommandTest {
    public static void main(String[] args) throws Exception {
        testLightCommands();
        testMacroCommand();
        testScheduler();
        testAirConditionerTemperatureCommand();
        System.out.println("All SmartHome command tests passed. (Run with -ea to enable assertions.)");
    }

    private static void testLightCommands() {
        SmartHomeInvoker invoker = new SmartHomeInvoker();
        Light light = new Light();
        Command turnOn = new TurnOnLightCommand(light);
        Command turnOff = new TurnOffLightCommand(light);

        invoker.execute(turnOn);
        assert light.isOn() : "Light should be on after TurnOnLightCommand";

        invoker.execute(turnOff);
        assert !light.isOn() : "Light should be off after TurnOffLightCommand";

        invoker.undo();
        assert light.isOn() : "Undo on TurnOffLightCommand should turn the light back on";

        invoker.undo();
        assert !light.isOn() : "Undo on TurnOnLightCommand should turn the light off";

        boolean redoResult = invoker.redo();
        assert redoResult : "Redo should succeed when history exists";
        assert light.isOn() : "Redo should reapply TurnOnLightCommand";
    }

    private static void testMacroCommand() {
        SmartHomeInvoker invoker = new SmartHomeInvoker();
        AirConditioner ac = new AirConditioner();
        DoorLock doorLock = new DoorLock();

        MacroCommand morningRoutine = new MacroCommand(List.of(
                new TurnOnAirConditionerCommand(ac),
                new UnlockDoorCommand(doorLock)
        ));

        invoker.execute(morningRoutine);
        assert ac.isOn() : "AC should be on after macro execution";
        assert !doorLock.isLocked() : "Door should be unlocked after macro execution";

        invoker.undo();
        assert !ac.isOn() : "AC should be off after macro undo";
        assert doorLock.isLocked() : "Door should be locked after macro undo";
    }

    private static void testScheduler() throws InterruptedException, TimeoutException {
        SmartHomeInvoker invoker = new SmartHomeInvoker();
        MusicPlayer player = new MusicPlayer();
        Command play = new PlayMusicCommand(player);
        Command stop = new StopMusicCommand(player);

        try (CommandScheduler scheduler = new CommandScheduler(invoker)) {
            scheduler.schedule(play, 150, TimeUnit.MILLISECONDS).get(1, TimeUnit.SECONDS);
        } catch (java.util.concurrent.ExecutionException e) {
            throw new AssertionError("Scheduled command threw unexpectedly", e);
        }

        assert player.isPlaying() : "Scheduled play command should have started music";
        boolean undoResult = invoker.undo();
        assert undoResult : "Undo should succeed after scheduled execution";
        assert !player.isPlaying() : "Undo of PlayMusicCommand should stop music";

        invoker.execute(stop);
        assert !player.isPlaying() : "StopMusicCommand should stop music";
        invoker.undo();
        assert player.isPlaying() : "Undo of StopMusicCommand should resume music";
    }

    private static void testAirConditionerTemperatureCommand() {
        SmartHomeInvoker invoker = new SmartHomeInvoker();
        AirConditioner ac = new AirConditioner();
        Command turnOn = new TurnOnAirConditionerCommand(ac);
        Command setTemp = new SetAirConditionerTemperatureCommand(ac, 20);
        Command turnOff = new TurnOffAirConditionerCommand(ac);

        invoker.execute(turnOn);
        invoker.execute(setTemp);
        assert ac.getTemperature() == 20 : "AC temperature should update after command";

        invoker.undo();
        assert ac.getTemperature() == 24 : "Undo should restore previous temperature";

        invoker.execute(turnOff);
        assert !ac.isOn() : "AC should be off after TurnOffAirConditionerCommand";
        invoker.undo();
        assert ac.isOn() : "Undo should turn AC back on";
    }

    private SmartHomeCommandTest() {
        // utility class
    }
}
