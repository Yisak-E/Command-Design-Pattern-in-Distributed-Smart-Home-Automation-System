package smarthome.client;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
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

// Client: wires receivers, commands, and invoker to demonstrate the Command pattern workflow.
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

        Map<String, String> menuActions = Map.ofEntries(
                Map.entry("1", "light:on"),
                Map.entry("2", "light:off"),
                Map.entry("3", "door:unlock"),
                Map.entry("4", "door:lock"),
                Map.entry("5", "ac:on"),
                Map.entry("6", "ac:off"),
                Map.entry("7", "ac:cool"),
                Map.entry("8", "music:play"),
                Map.entry("9", "music:stop"),
                Map.entry("10", "routine:morning"),
                Map.entry("11", "routine:night"));

        try (Scanner scanner = new Scanner(System.in);
                CommandScheduler scheduler = new CommandScheduler(invoker)) {
            boolean running = true;
            printWelcome();

            while (running) {
                printMenu();
                System.out.print("Select action: ");
                String input = scanner.nextLine().trim();
                String normalized = input.toLowerCase(Locale.ROOT);

                switch (normalized) {
                    case "u":
                    case "undo":
                        if (!invoker.undo()) {
                            System.out.println("Nothing to undo.");
                        }
                        break;
                    case "r":
                    case "redo":
                        if (!invoker.redo()) {
                            System.out.println("Nothing to redo.");
                        }
                        break;
                    case "s":
                    case "schedule":
                        scheduleStopMusic(scanner, scheduler, stopMusic);
                        break;
                    case "q":
                    case "quit":
                        running = false;
                        break;
                    default:
                        String commandKey = menuActions.get(normalized);
                        if (commandKey == null) {
                            System.out.println("Unknown option: " + input);
                        } else {
                            invoker.execute(commandKey);
                        }
                        break;
                }

                if (running) {
                    printDeviceState(light, airConditioner, doorLock, musicPlayer);
                }
            }
        }

        System.out.println("Shutting down Smart Home client.");
    }

    private static void printWelcome() {
        System.out.println("Smart Home Automation Console");
        System.out.println("Commands demonstrate the Command pattern with undo/redo support.\n");
    }

    private static void printMenu() {
                String border = "+" + "─".repeat(10) + "+" + "─".repeat(18) + "+" + "─".repeat(10) + "+" + "─".repeat(18) + "+";

                System.out.println(border);
                System.out.printf("|%-10s|%-18s|%-10s|%-18s|\n", " Command", " Action", " Command", " Action");
                System.out.println(border);

                System.out.printf("|%-10s|%-18s|%-10s|%-18s|\n", " 1", " Light On", " 2", " Light Off");
                System.out.printf("|%-10s|%-18s|%-10s|%-18s|\n", " 3", " Door Unlock", " 4", " Door Lock");
                System.out.printf("|%-10s|%-18s|%-10s|%-18s|\n", " 5", " AC On", " 6", " AC Off");
                System.out.printf("|%-10s|%-18s|%-10s|%-18s|\n", " 7", " AC Cool (20°C)", " 8", " Music Play");
                System.out.printf("|%-10s|%-18s|%-10s|%-18s|\n", " 9", " Music Stop", " 10", " Morning Routine");
                System.out.printf("|%-10s|%-18s|%-10s|%-18s|\n", " 11", " Night Routine", " u", " Undo");
                System.out.printf("|%-10s|%-18s|%-10s|%-18s|\n", " r", " Redo", " s", " Schedule Stop");
                System.out.printf("|%-10s|%-18s|%-10s|%-18s|\n", " q", " Quit", "", "");
                System.out.println(border);
    }

    private static void printDeviceState(Light light, AirConditioner ac, DoorLock lock, MusicPlayer player) {
        System.out.printf("Light: %s | Door: %s | AC: %s (%d°C) | Music: %s%n",
                light.isOn() ? "ON" : "OFF",
                lock.isLocked() ? "Locked" : "Unlocked",
                ac.isOn() ? "ON" : "OFF",
                ac.getTemperature(),
                player.isPlaying() ? "Playing" : "Stopped");
        System.out.println();
    }

    private static void scheduleStopMusic(Scanner scanner, CommandScheduler scheduler, Command stopMusic)
            throws InterruptedException {
        System.out.print("Enter delay in seconds (default 2): ");
        String value = scanner.nextLine().trim();
        long delay = 2L;
        if (!value.isEmpty()) {
            try {
                delay = Long.parseLong(value);
            } catch (NumberFormatException ex) {
                System.out.println("Invalid number. Using default delay of 2 seconds.");
            }
        }

        scheduler.schedule(stopMusic, delay, TimeUnit.SECONDS);
        System.out.println("Stop music command scheduled in " + delay + " second(s).");
    }
}
