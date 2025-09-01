package com.algovisualizer.graph;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class GraphTest {
    
    private Graph graph;
    private Node node1;
    private Node node2;
    private Node node3;
    
    @BeforeEach
    void setUp() {
        graph = new Graph();
        node1 = new Node("A", 100, 100);
        node2 = new Node("B", 200, 200);
        node3 = new Node("C", 300, 150);
    }
    
    @Test
    void testAddNode() {
        graph.addNode(node1);
        assertTrue(graph.hasNode("A"));
        assertEquals(1, graph.getNodeCount());
        assertEquals(node1, graph.getNode("A"));
    }
    
    @Test
    void testAddEdge() {
        graph.addNode(node1);
        graph.addNode(node2);
        graph.addEdge(node1, node2);
        
        assertEquals(1, graph.getEdgeCount());
        assertTrue(graph.hasEdge(node1, node2));
    }
    
    @Test
    void testGetNeighbors() {
        graph.addNode(node1);
        graph.addNode(node2);
        graph.addNode(node3);
        graph.addEdge(node1, node2);
        graph.addEdge(node1, node3);
        
        var neighbors = graph.getNeighbors(node1);
        assertEquals(2, neighbors.size());
        assertTrue(neighbors.contains(node2));
        assertTrue(neighbors.contains(node3));
    }
    
    @Test
    void testRemoveNode() {
        graph.addNode(node1);
        graph.addNode(node2);
        graph.addEdge(node1, node2);
        
        graph.removeNode("A");
        assertFalse(graph.hasNode("A"));
        assertEquals(0, graph.getEdgeCount()); // Edge should be removed too
    }
    
    @Test
    void testFindNodeAt() {
        graph.addNode(node1); // at (100, 100)
        
        Node found = graph.findNodeAt(105, 95, 20); // Within radius
        assertEquals(node1, found);
        
        Node notFound = graph.findNodeAt(150, 150, 20); // Outside radius
        assertNull(notFound);
    }
}