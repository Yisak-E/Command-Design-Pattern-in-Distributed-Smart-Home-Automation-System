package smarthome.scheduler;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import smarthome.commands.Command;
import smarthome.invoker.SmartHomeInvoker;

// Scheduler: defers command execution using a single-threaded executor.
public class CommandScheduler implements AutoCloseable {
    private final SmartHomeInvoker invoker;
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        thread.setName("smart-home-scheduler");
        return thread;
    });

    // Creates a scheduler that delegates back to the given invoker.
    public CommandScheduler(SmartHomeInvoker invoker) {
        this.invoker = Objects.requireNonNull(invoker);
    }

    // Schedules a command after the requested delay.
    public ScheduledFuture<?> schedule(Command command, long delay, TimeUnit unit) {
        Objects.requireNonNull(command);
        Objects.requireNonNull(unit);
        return executor.schedule(() -> invoker.execute(command), delay, unit);
    }

    // Shuts down the underlying executor immediately.
    @Override
    public void close() {
        executor.shutdownNow();
    }
}
