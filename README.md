# AlgoVisualizer

A Java-based interactive tool for graph theory and algorithm exploration, built with JavaFX for educational purposes.

## Overview

AlgoVisualizer is a desktop application that provides an interactive, educational platform for visualizing graph data structures and algorithms. Users can dynamically create, modify, and interact with graphs, and view step-by-step visualizations of algorithms such as BFS, DFS, Dijkstra's, and A*.

## Features

### ✅ Implemented (MVP)
- **Interactive Graph Creation**: Add and remove nodes by clicking
- **Edge Management**: Connect nodes to create edges
- **Node Manipulation**: Drag and drop nodes to rearrange the graph
- **BFS Algorithm Visualization**: Step-by-step breadth-first search with animation
- **Algorithm Controls**: Play, pause, reset functionality
- **Visual Feedback**: Color-coded node states (unvisited, visiting, visited)
- **Pathfinding**: Find shortest paths between nodes using BFS
- **Console Mode**: Command-line demo for testing core functionality

### 🚧 Planned Features
- DFS (Depth-First Search) algorithm
- Dijkstra's shortest path algorithm
- A* search algorithm
- Minimum Spanning Tree algorithms (Prim's, Kruskal's)
- Graph save/load functionality (JSON format)
- Weighted edges support
- Directed/undirected graph modes

## Technical Stack

- **Language**: Java 17
- **UI Framework**: JavaFX 19
- **Build Tool**: Maven
- **Testing**: JUnit 5
- **JSON Processing**: Jackson (for future save/load functionality)

## Project Structure

```
src/main/java/com/algovisualizer/
├── main/                 # Application entry points
│   ├── MainApp.java     # JavaFX GUI application
│   └── ConsoleDemo.java # Command-line demo
├── ui/                  # JavaFX components
│   └── controllers/     # FXML controllers
├── graph/               # Graph data structures
│   ├── Graph.java       # Main graph class
│   ├── Node.java        # Graph node/vertex
│   └── Edge.java        # Graph edge
├── algorithms/          # Algorithm implementations
│   └── BFS.java         # Breadth-First Search
└── io/                  # File I/O (future)
```

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Building the Project
```bash
# Clone the repository
git clone https://github.com/jempolbagas/AlgoVisualizer.git
cd AlgoVisualizer

# Compile the project
mvn compile

# Run tests
mvn test

# Run console demo (works in any environment)
mvn exec:java -Dexec.mainClass="com.algovisualizer.main.ConsoleDemo"

# Run GUI application (requires graphical environment)
mvn javafx:run
```

### Using the Application

#### GUI Mode (when available)
1. **Node Mode**: Click on the canvas to add nodes
2. **Edge Mode**: Click two nodes to connect them with an edge
3. **Right-click** on nodes to set as start/target nodes
4. **Select Algorithm**: Choose BFS from the dropdown
5. **Play**: Start the visualization
6. **Controls**: Use play/pause/reset buttons to control animation

#### Console Mode (for testing)
Run the console demo to see the core algorithm functionality:
```bash
mvn exec:java -Dexec.mainClass="com.algovisualizer.main.ConsoleDemo"
```

## Example Output

The console demo creates a sample graph and demonstrates BFS:

```
=== AlgoVisualizer Console Demo ===

Created graph with 5 nodes and 5 edges
Graph structure:
A -- B -- C
     |    |
     D -- E

=== BFS Traversal from node A ===
Distance from A to each node:
  Start: 0
  Node B: 1
  Node C: 2
  Target: 2
  Node E: 3

=== BFS Pathfinding from A to E ===
Path found: Start -> Node B -> Node C -> Node E
Path length: 3 edges
```

## Development Roadmap

Following the original project proposal milestones:

- [x] **Milestone 1: Foundation** - JavaFX project setup, basic UI, core data structures
- [x] **Milestone 2: Interactive Graph Drawing** - Node/edge creation, drag-and-drop
- [x] **Milestone 3: First Algorithm (BFS)** - BFS implementation with visualization
- [ ] **Milestone 4: Weighted Algorithms** - Dijkstra's algorithm support
- [ ] **Milestone 5: Heuristic Algorithms** - A* search implementation
- [ ] **Milestone 6: Polish & Features** - Save/load, additional algorithms, UI refinements

## Testing

The project includes comprehensive unit tests:

```bash
# Run all tests
mvn test

# Test results show core functionality working:
# - Graph creation and manipulation
# - BFS algorithm correctness
# - Pathfinding capabilities
```

## Contributing

This project implements the design specified in `Project.-Idea-Proposal.md`. Core architecture follows the proposed structure with clean separation between:

- **Data Layer**: Graph, Node, Edge classes
- **Algorithm Layer**: BFS and future algorithm implementations  
- **UI Layer**: JavaFX controllers and views
- **Application Layer**: Main application entry points

## License

This project is developed for educational purposes as specified in the original project proposal.

## Acknowledgments

Built following the specifications in the project proposal document, implementing a clean, modular architecture suitable for educational graph algorithm visualization.