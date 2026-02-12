# Tachyon

Tachyon is a fast Rubik’s Cube solver written in Java.

The goal of this project is to build a powerful and extensible cube solving engine, while exploring different solving methods and algorithms.

## Current Features

- Fast cube state representation
- Scramble input support
- Solver based on the **Kociemba 2-Phase algorithm**
- Efficient pruning tables
- Deterministic solving

## Algorithm

At the moment, Tachyon only supports:

- **Kociemba’s Two-Phase Algorithm**

This method splits the solving process into two stages:
1. Reduce the cube to the G1 subgroup.
2. Solve the cube completely from G1.

This allows fast solutions, typically under 20 moves.

More solving methods may be added in the future.

## Goals

Tachyon is designed to become more than just a solver.  
Future plans may include:

- Additional solving methods (CFOP-style, IDA*, etc.)
- Performance improvements
- Analysis tools
- Experimental solving research

## Requirements

- Java 17+ (or compatible version)

## Usage

(Instructions will be added as the project evolves.)

---

Solved in milliseconds using **Tachyon**.
