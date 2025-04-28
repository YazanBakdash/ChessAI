# ChessAI
**Java-based Chess GUI and AI Opponent**

ChessAI is a Java application that offers a graphical user interface (GUI) for playing chess against an AI opponent. The project aims to provide an interactive platform for users to engage in chess games with computer-generated moves.

## Features
- **Graphical User Interface**: A user-friendly GUI that allows players to interact with the chessboard seamlessly.
- **AI Opponent**: Play against an AI that makes moves based on implemented algorithms.
- **Standard Chess Rules**: Supports all standard chess rules, including piece movements, check, checkmate, and stalemate conditions.

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- An IDE such as IntelliJ IDEA or Eclipse (optional but recommended)

### Installation

1. **Clone the Repository**
    ```bash
    git clone https://github.com/YazanBakdash/ChessAI.git
    ```

2. **Navigate to the Project Directory**
    ```bash
    cd ChessAI
    ```

3. **Compile and Run**

    - **Using Command Line:**
      ```bash
      javac -d bin src/*.java
      java -cp bin Main
      ```

    - **Using an IDE:**
        - Import the project as a Java project.
        - Locate the `Main.java` file.
        - Run the `Main` class.

## How to Play
1. **Launch the Application**: Run the `Main` class to start the game.
2. **Make Moves**: Click on a piece and then click on the destination square to move.
3. **AI Response**: After your move, the AI will calculate and make its move.
4. **Game End**: The game continues until checkmate, stalemate, or draw conditions are met.
