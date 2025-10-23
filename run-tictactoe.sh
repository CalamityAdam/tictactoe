#!/bin/bash

# Tic Tac Toe Runner Script
# This script compiles and runs the Java tic-tac-toe game

# Set the project directory
PROJECT_DIR="/Users/adam.sisk/dev/repos/adamsisk/cli"
TICTACTOE_DIR="$PROJECT_DIR/tictactoe"

# Change to the project directory
cd "$PROJECT_DIR" || exit 1

# Compile the Java file(s) with the package structure
javac tictactoe/*.java 2>/dev/null

# Check if compilation was successful
if [ $? -ne 0 ]; then
    echo "Compilation failed. Please check your Java files for errors."
    exit 1
fi

# Run the Main class with the package name, passing any arguments
java tictactoe.Main "$@"