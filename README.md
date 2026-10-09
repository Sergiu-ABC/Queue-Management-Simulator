# Queue Management Simulator

A real-time simulation of clients arriving at several service queues (think checkout lanes). Each
queue runs on its own thread, a scheduler sends every new client to a queue using the selected
strategy, and at the end the simulation reports waiting-time statistics.

## What it does

- Generates N clients with random arrival and service times inside the ranges you set
- Runs Q queues in parallel, one thread per queue, ticking once per second
- Dispatches each arriving client with one of two strategies:
  - **Shortest time:** the queue with the smallest total remaining service time
  - **Shortest queue:** the queue with the fewest clients
- Shows the live state of every queue in a Swing window and writes the same log to
  `log_of_events_<timestamp>.txt`
- Reports average waiting time, average service time and the peak moment (most clients in queues)

## Design

```
gui/      Viewer (Swing form + live log), Controller (reads inputs, starts the simulation)
logic/    SimulationManager   main loop: dispatches arrivals, logs state, computes statistics
          Scheduler           owns the queues and their threads, applies the chosen strategy
          Strategy            interface, implemented by ConcreteStrategyTime and ConcreteStrategyQueue
model/    Server              one queue: a BlockingQueue of clients, run on its own thread
          Task                a client (id, arrival time, service time)
```

- **Strategy pattern** for the dispatch policy, so a new policy is one new class.
- **Observer** (`SimulationObserver`) so the simulation logic doesn't depend on Swing.
- **Thread safety:** each queue is a `LinkedBlockingQueue`, its remaining work is an `AtomicInteger`,
  and the stop flag is `volatile`, so the scheduler thread can read queue state while queue threads
  are serving clients.

UML and use-case diagrams are in `UmlDiagram.drawio.html` and `Diagramausecase.drawio.html`.

## Running it

Requirements: Java 25 and Maven.

```bash
mvn compile
mvn exec:java -Dexec.mainClass=com.borcasergiu.queuesim.Main
```

Fill in the number of clients and queues, the simulation time, the arrival and service time ranges,
pick a strategy and press start.
