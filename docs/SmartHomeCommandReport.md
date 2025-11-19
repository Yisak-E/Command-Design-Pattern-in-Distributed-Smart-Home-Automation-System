# Smart Home Automation System – Command Pattern Refactoring

## 1. Introduction
This report documents the refactoring of the Smart Home Automation System to the Command design pattern. The original implementation tightly coupled the controller with concrete device classes and their operations, resulting in rigid and non-extensible code. The redesigned solution decouples command invocation from execution, enabling late binding of device actions, uniform undo/redo, macro commands, and basic scheduling while preserving single-responsibility boundaries.

## 2. Design Objectives
- Replace direct receiver invocations with a Command abstraction.
- Support dynamic registration of commands with an invoker.
- Track command history to provide undo/redo.
- Group commands into reusable macro routines.
- Provide an optional scheduler to execute commands after a simulated delay.
- Conform to SOLID design principles to ease future extension, such as adding new devices.

## 3. System Overview
The refactored solution introduces four primary layers:

1. **Receivers** (`Light`, `AirConditioner`, `DoorLock`, `MusicPlayer`): encapsulate concrete device behaviors. Receivers remain unchanged except for minimal state tracking to assist undo operations.
2. **Commands**: implement the `Command` interface (`execute`/`undo`). Each concrete command holds a reference to its receiver and delegates the operation. `MacroCommand` composes multiple commands, while `NoCommand` provides a null-object fallback.
3. **Invoker** (`SmartHomeInvoker`): manages command registration, invocation, and history stacks. Undo/redo use two deques to unwind or reapply commands consistently.
4. **Infrastructure** (`CommandScheduler`): wraps a single-threaded scheduled executor and delegates scheduled executions back to the invoker, keeping scheduling concerns separate from command logic.

`SmartHomeClient` demonstrates the wiring of these components by configuring routines and issuing commands.

## 4. SOLID Compliance
- **Single Responsibility**: Each class has one reason to change (e.g., `MacroCommand` only coordinates child commands, `CommandScheduler` only handles timing).
- **Open/Closed**: Adding a new device or operation introduces a new command class without touching existing code. The invoker stores commands polymorphically.
- **Liskov Substitution**: All commands conform to `Command` and can substitute for one another in histories or macros.
- **Interface Segregation**: The command interface contains only the essential `execute`/`undo` pair. Receivers expose minimal operations required by their commands.
- **Dependency Inversion**: `SmartHomeInvoker` depends on the abstraction `Command` rather than concrete device classes.

## 5. Undo/Redo Strategy
`SmartHomeInvoker` records every executed command on an `undoHistory` stack. Undo pops the most recent command, invokes `undo`, and pushes it onto `redoHistory`. Redo replays commands from `redoHistory`, restoring `undoHistory`. Clearing redo history on new executions ensures command sequences remain consistent. Commands impacting persistent state (e.g., `SetAirConditionerTemperatureCommand`) capture previous values before modification to reverse their effects accurately.

## 6. Macro Commands
`MacroCommand` aggregates a list of commands. `execute` iterates sequentially, while `undo` iterates in reverse to restore prior state safely. Routines such as `MorningRoutine` and `NightRoutine` are defined by composing concrete commands; users can register macros with the invoker like any single command.

## 7. Scheduling Support
`CommandScheduler` uses `ScheduledExecutorService` to trigger commands after a delay. The scheduler delegates execution to `SmartHomeInvoker` to reuse history handling. For simplicity and clarity within the assignment scope, the scheduler is single-threaded and intended for short-lived demonstrations (it implements `AutoCloseable` for deterministic shutdown in try-with-resources blocks).

## 8. Usage Walkthrough
1. Instantiate receivers and corresponding command objects.
2. Register commands with `SmartHomeInvoker` under descriptive keys.
3. Execute commands via keys or by passing command instances directly.
4. Call `undo()` / `redo()` to traverse history.
5. Build macro commands for scenarios like morning or night routines.
6. Optionally, schedule commands with `CommandScheduler.schedule` while the invoker records history once the delay elapses.

The demo `SmartHomeClient` exercises these steps: it triggers a morning routine, performs undo/redo cycles, and schedules a delayed music stop.

## 9. Extensibility Notes
- **Adding Devices**: Introduce a new receiver class and companion command classes; register them with the invoker. No existing code requires modification.
- **Stateful Undo**: Commands needing richer undo behavior can store snapshots or mementos. The current implementation captures only lightweight state.
- **Concurrency**: Thread-safety could be enhanced by wrapping history access in synchronized blocks or using concurrent stacks if multiple schedulers or GUIs interact concurrently.
- **Persistence**: Histories could be serialized to support system restarts or remote coordination.

## 10. Testing Guidelines
- Exercise each command individually and verify that undo reverses side effects.
- Validate macro commands undo correctly by observing output order.
- Confirm redo only operates after an undo and that new executions clear redo history.
- Use scheduled commands in `SmartHomeClient`; ensure commands execute after the delay and appear in undo history.

## 11. Conclusion
The refactored architecture demonstrates the Command pattern’s advantages for distributed automation: it decouples invokers from receivers, centralizes undo/redo logic, and supports composition and scheduling. The solution is ready for further enhancements such as device discovery or remote invocation without structural changes to the core command framework.
