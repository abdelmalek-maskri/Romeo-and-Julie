# Romeo and Juliet - Concurrent Programming Simulation

## Overview

The **Romeo and Juliet** project simulates a concurrent communication scenario between two characters—Romeo and Juliet—using a TCP/IP client-server architecture. Inspired by Shakespeare's classic play, this application models the exchange of love letters between the two characters, while also solving an ordinary differential equation (ODE) to simulate the dynamics of their love over time.

## Features

- **Client-Server Architecture**: The application consists of one client (the Playwriter) and two servers (Romeo and Juliet), enabling them to communicate over TCP/IP.
- **Concurrent Communication**: The Playwriter manages interactions with both Romeo and Juliet, requesting and receiving love letters in a multi-threaded environment.
- **ODE Simulation**: The love dynamics are represented through an ordinary differential equation, providing a unique twist to the traditional narrative.
- **Novel Creation**: The love letters exchanged between Romeo and Juliet are compiled into a `.csv` file, creating a novel-like output of their communication.

## Components

### 1. Playwriter
The client component that initiates communication between Romeo and Juliet. It handles:
- Creating characters.
- Establishing acquaintances.
- Requesting verses (love letters) from both characters.
- Compiling the love letters into a final output.

### 2. Romeo Server
Handles requests from the Playwriter, simulating Romeo's responses. Key functionalities include:
- Receiving love letters from the Playwriter.
- Processing the love dynamics and sending back responses.

### 3. Juliet Server
Similar to Romeo, but focuses on Juliet's perspective. It includes:
- Handling requests from the Playwriter.
- Sending responses based on the love dynamics.

## Usage

1. **Run the Servers**: Start the Juliet and Romeo servers to listen for incoming connections.
2. **Start the Playwriter**: Run the Playwriter client, which will connect to both servers, initiate the communication, and compile the love letters.
3. **Output**: The collected love letters will be saved in a file named `RomeoAndJuliet.csv`, and the execution output can be found in `RomeoAndJuliet_Execution.txt`.

## Compile the Java files:

```bash
javac PlayWriter.java Romeo.java Juliet.java
```

###Start the servers:

```bash
java Juliet <initialLoveValue>
java Romeo <initialLoveValue>
```
## Run the Playwriter:

```bash
java PlayWriter
```
