package com.algovisualizer.algorithms;

import com.algovisualizer.graph.Graph;
import com.algovisualizer.graph.Node;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class BFSTest {
    
    private Graph graph;
    private Node nodeA;
    private Node nodeB;
    private Node nodeC;
    private Node nodeD;
    
    @BeforeEach
    void setUp() {
        /*
         * Create a simple graph:
         * A -- B -- D
         * |    |
         * C ----
         */
        graph = new Graph();
        nodeA = new Node("A", 0, 0);
        nodeB = new Node("B", 100, 0);
        nodeC = new Node("C", 0, 100);
        nodeD = new Node("D", 200, 0);
        
        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);
        graph.addNode(nodeD);
        
        graph.addEdge(nodeA, nodeB);
        graph.addEdge(nodeA, nodeC);
        graph.addEdge(nodeB, nodeC);
        graph.addEdge(nodeB, nodeD);
    }
    
    @Test
    void testBFSTraversal() {
        BFS.BFSResult result = BFS.performBFS(graph, nodeA);
        
        assertNotNull(result);
        assertFalse(result.getSteps().isEmpty());
        
        // All nodes should be reachable from A
        assertEquals(4, result.getDistanceMap().size());
        assertEquals(0, (int) result.getDistanceMap().get(nodeA));
        assertEquals(1, (int) result.getDistanceMap().get(nodeB));
        assertEquals(1, (int) result.getDistanceMap().get(nodeC));
        assertEquals(2, (int) result.getDistanceMap().get(nodeD));
    }
    
    @Test
    void testPathFinding() {
        BFS.BFSResult result = BFS.findPath(graph, nodeA, nodeD);
        
        List<Node> path = result.getPathTo(nodeD);
        assertFalse(path.isEmpty());
        assertEquals(nodeA, path.get(0)); // Start node
        assertEquals(nodeD, path.get(path.size() - 1)); // End node
        assertEquals(3, path.size()); // A -> B -> D
    }
    
    @Test
    void testDisconnectedNodes() {
        // Create a disconnected node
        Node nodeE = new Node("E", 300, 300);
        graph.addNode(nodeE);
        
        BFS.BFSResult result = BFS.findPath(graph, nodeA, nodeE);
        
        List<Node> path = result.getPathTo(nodeE);
        assertTrue(path.isEmpty()); // No path should exist
        assertFalse(result.getDistanceMap().containsKey(nodeE));
    }
    
    @Test
    void testBFSSteps() {
        BFS.BFSResult result = BFS.performBFS(graph, nodeA);
        
        List<BFS.BFSStep> steps = result.getSteps();
        assertFalse(steps.isEmpty());
        
        // First step should be START
        assertEquals(BFS.BFSStep.StepType.START, steps.get(0).getType());
        assertEquals(nodeA, steps.get(0).getCurrentNode());
        
        // Last step should be COMPLETE
        BFS.BFSStep lastStep = steps.get(steps.size() - 1);
        assertEquals(BFS.BFSStep.StepType.COMPLETE, lastStep.getType());
    }
}