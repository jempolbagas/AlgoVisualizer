package com.algovisualizer.main;

import com.algovisualizer.algorithms.BFS;
import com.algovisualizer.graph.Graph;
import com.algovisualizer.graph.Node;

/**
 * Command-line demo of the AlgoVisualizer core functionality.
 * This demonstrates the graph creation and BFS algorithm without the GUI.
 */
public class ConsoleDemo {
    
    public static void main(String[] args) {
        System.out.println("=== AlgoVisualizer Console Demo ===\n");
        
        // Create a sample graph
        Graph graph = new Graph();
        
        // Add nodes
        Node nodeA = new Node("A", 0, 0, "Start");
        Node nodeB = new Node("B", 100, 0, "Node B");
        Node nodeC = new Node("C", 200, 0, "Node C");
        Node nodeD = new Node("D", 100, 100, "Target");
        Node nodeE = new Node("E", 200, 100, "Node E");
        
        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);
        graph.addNode(nodeD);
        graph.addNode(nodeE);
        
        // Add edges to create a connected graph
        graph.addEdge(nodeA, nodeB);
        graph.addEdge(nodeB, nodeC);
        graph.addEdge(nodeB, nodeD);
        graph.addEdge(nodeC, nodeE);
        graph.addEdge(nodeD, nodeE);
        
        System.out.println("Created graph with " + graph.getNodeCount() + " nodes and " + graph.getEdgeCount() + " edges");
        System.out.println("Graph structure:");
        System.out.println("A -- B -- C");
        System.out.println("     |    |");
        System.out.println("     D -- E");
        System.out.println();
        
        // Demonstrate BFS traversal
        System.out.println("=== BFS Traversal from node A ===");
        BFS.BFSResult result = BFS.performBFS(graph, nodeA);
        
        System.out.println("Distance from A to each node:");
        for (Node node : graph.getNodes()) {
            Integer distance = result.getDistanceMap().get(node);
            if (distance != null) {
                System.out.println("  " + node.getLabel() + ": " + distance);
            } else {
                System.out.println("  " + node.getLabel() + ": unreachable");
            }
        }
        System.out.println();
        
        // Demonstrate pathfinding
        System.out.println("=== BFS Pathfinding from A to E ===");
        BFS.BFSResult pathResult = BFS.findPath(graph, nodeA, nodeE);
        var path = pathResult.getPathTo(nodeE);
        
        if (!path.isEmpty()) {
            System.out.print("Path found: ");
            for (int i = 0; i < path.size(); i++) {
                System.out.print(path.get(i).getLabel());
                if (i < path.size() - 1) {
                    System.out.print(" -> ");
                }
            }
            System.out.println();
            System.out.println("Path length: " + (path.size() - 1) + " edges");
        } else {
            System.out.println("No path found!");
        }
        System.out.println();
        
        // Show algorithm steps
        System.out.println("=== BFS Algorithm Steps ===");
        var steps = result.getSteps();
        int stepCount = 0;
        for (BFS.BFSStep step : steps) {
            stepCount++;
            System.out.println("Step " + stepCount + ": " + step.getDescription());
            if (stepCount >= 10) { // Limit output for demo
                System.out.println("... (showing first 10 steps only)");
                break;
            }
        }
        
        System.out.println("\n=== Demo Complete ===");
        System.out.println("The AlgoVisualizer core functionality is working correctly!");
        System.out.println("In a GUI environment, you would see:");
        System.out.println("- Interactive graph creation by clicking");
        System.out.println("- Real-time algorithm visualization with colors");
        System.out.println("- Step-by-step animation controls");
        System.out.println("- Node and edge manipulation");
    }
}