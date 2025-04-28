# Scratch Game

## Overview
The Scratch Game is a CLI-based game where users place a bet and generate a matrix of symbols. Based on the matrix and predefined winning combinations, the user either wins or loses. Bonus symbols can further enhance the reward if a winning combination is achieved.

## Features
- Generate a random matrix of symbols based on probabilities.
- Evaluate winning combinations and calculate rewards.
- Apply bonus symbols for additional rewards.
- Configurable game settings via a JSON file.

## Requirements
- JDK >= 17
- Maven

## Configuration
The game uses a JSON configuration file to define:
- Matrix dimensions (rows and columns).
- Symbols and their properties (standard and bonus).
- Probabilities for symbol generation.
- Winning combinations and their rewards.

Refer to the provided `config.json` file for detailed configuration options.

## Usage
1. Build the project:
   ```bash
   mvn clean package
   ```
2. Run the game:
   ```bash
   java -jar target/game-0.0.1-SNAPSHOT.jar --config config.json --betting-amount 100
   ```

### Parameters
- `--config`: Path to the configuration file.
- `--betting-amount`: The betting amount for the game.

## Testing
Run the test cases using Maven:
```bash
mvn test
```
