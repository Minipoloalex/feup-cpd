# CPD - Distributed Systems project

## How to run the project

```bash

```

## Project description

This project consists of an implementation of a simple game in a distributed system, taking into consideration concurrency and fault tolerance.

### Game description

We decided to implement a simple game similar to [Nim](https://en.wikipedia.org/wiki/Nim).

In our game, there a certain number of piles (or stacks), each with a certain number of rocks. The players take turns removing rocks from the piles. In each turn, a player can remove any number of rocks from a single pile. The player that removes the last rock from the last pile wins the game.

This is an example of a game with just 2 piles, and 2 players, for simplicity:

```
o
o o
o o
1 2
```

The numbers correspond to the number of each pile, and the `o` characters represent the rocks.

Here, if player 1 takes 3 rocks from the first pile, the second player can win by taking the last 2 rocks from the second pile, since he was the last player to remove rocks.

### Implemented features

- Game modes
  - Simple game mode, without ratings
  - Ranked game mode, where players are paired according to their ratings
- Fault tolerance
  - Clients that are disconnected in the queue can reconnect and not lose their spot
  <!--TODO ?: * Clients that are disconnected during a game can reconnect and continue playing-->
- Concurrency control
  - Using locks to control concurrent accesses (e.g. implementation of our own concurrent TreeSet)
- User registration and authentication
  - Secure storage of passwords using SHA-256 and salt
- Secure communication between players and server
  - Using SSL sockets

### Project Architecture

This is the project's directory structure:

```
src/main/pt/up/fe/cpd2324/
├── client
│   ├── Client.java
│   └── Player.java
├── common
│   ├── Connection.java
│   ├── Message.java
│   ├── TreeSet.java
│   └── Utils.java
├── game
│   └── Stones.java
├── queue
│   ├── NormalQueue.java
│   ├── Queue.java
│   ├── RankedQueue.java
│   └── Rateable.java
└── server
    ├── ClientAuthenticator.java
    ├── Database.java
    ├── Game.java
    ├── GameScheduler.java
    ├── QueueManager.java
    └── Server.java
```

The most important classes in regards to understanding our architecture are the inside the `Server` folder.

#### Client

---

- [`client/Client`](src/main/pt/up/fe/cpd2324/client/Client.java): main class that starts the client and connects to the server. It's responsible for sending messages to the server and receiving responses.

- [`client/Player`](src/main/pt/up/fe/cpd2324/client/Player.java): represents a player (a client) in the game, with a name, password, rating and the associated socket.

#### Common

---

- [`common/Message`](src/main/pt/up/fe/cpd2324/common/Message.java): represents a message that can be sent between the client and the server. It has a type and content.

- [`common/Connection`](src/main/pt/up/fe/cpd2324/common/Connection.java): class used to send and receive messages between the client and the server.

- [`common/TreeSet`](src/main/pt/up/fe/cpd2324/common/TreeSet.java): implementation of a simple TreeSet that is thread-safe.

- [`common/Utils`](src/main/pt/up/fe/cpd2324/common/Utils.java): simple class with general utility methods.

#### Queues

---

- [`queue/Queue`](src/main/pt/up/fe/cpd2324/queue/Queue.java): abstract class that represents a queue of players waiting to play.

- [`queue/NormalQueue`](src/main/pt/up/fe/cpd2324/queue/NormalQueue.java): implementation of a simple queue. Players are paired according to first come, first served.

- [`queue/Rateable`](src/main/pt/up/fe/cpd2324/queue/Rateable.java): simple interface that represents an object that can be rated.

- [`queue/RankedQueue`](src/main/pt/up/fe/cpd2324/queue/RankedQueue.java): implementation of a queue that pairs players according to their ratings. This queue consists in a series of'buckets' that gathers players inside some rating range, and two players can only match-up if they are in the same bucket. When a player joins the queue, it is placed in the bucket that corresponds to its rating range. To make sure no player waits forever in the queue because of rating disparity, when the queue has at least 2 players, the queue periodically enlarges bucket sizes, redistributing the players. This guarantees that after long enough time a game will be found. After a game starts, the buckets are reset to their original size.

#### Game

---

- [`game/Stones`](src/main/pt/up/fe/cpd2324/game/Stones.java): Holds the logic for building a game and making "moves" in the game, i.e. removing rocks.

#### Server

---

- [`server/Server`](src/main/pt/up/fe/cpd2324/server/Server.java): main class that starts the server and listens for connections on a certain port. It's responsible for creating threads to manage the queues and games, as well as handling connections from clients.

- [`server/ClientAuthenticator`](src/main/pt/up/fe/cpd2324/server/ClientAuthenticator.java): created by the `Server` for each client. It is responsible for authenticating clients.

- [`server/Database`](src/main/pt/up/fe/cpd2324/server/Database.java): Simulates a database, handles storing players and their information.

- [`server/Game`](src/main/pt/up/fe/cpd2324/server/Game.java): Represents a game in the server between 2 players. It handles the game logic and interactions with the players. In case of a ranked game, it also updates the ratings of the players at the end of the game.

- [`server/QueueManager`](src/main/pt/up/fe/cpd2324/server/QueueManager.java): Responsible for adding players to the normal or ranked queues, according to the client's choice.

- [`server/GameScheduler`](src/main/pt/up/fe/cpd2324/server/GameScheduler.java): Manages the games and the players in them. It is responsible for starting and ending games.
