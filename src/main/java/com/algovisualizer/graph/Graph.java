package com.algovisualizer.graph;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

/**
 * Represents a graph data structure with nodes and edges.
 */
public class Graph {
    private Map<String, Node> nodes;
    private List<Edge> edges;
    private boolean directed;
    
    @JsonCreator
    public Graph(@JsonProperty("directed") boolean directed) {
        this.directed = directed;
        this.nodes = new HashMap<>();
        this.edges = new ArrayList<>();
    }
    
    public Graph() {
        this(false); // Undirected by default
    }
    
    // Node management
    public void addNode(Node node) {
        nodes.put(node.getId(), node);
    }
    
    public void removeNode(String nodeId) {
        Node node = nodes.get(nodeId);
        if (node != null) {
            // Remove all edges connected to this node
            edges.removeIf(edge -> edge.getSource().equals(node) || edge.getTarget().equals(node));
            nodes.remove(nodeId);
        }
    }
    
    public Node getNode(String nodeId) {
        return nodes.get(nodeId);
    }
    
    public Collection<Node> getNodes() {
        return nodes.values();
    }
    
    public boolean hasNode(String nodeId) {
        return nodes.containsKey(nodeId);
    }
    
    // Edge management
    public void addEdge(Edge edge) {
        // Check if nodes exist
        if (!nodes.containsValue(edge.getSource()) || !nodes.containsValue(edge.getTarget())) {
            throw new IllegalArgumentException("Cannot add edge: one or both nodes not in graph");
        }
        
        // Check if edge already exists
        if (!hasEdge(edge.getSource(), edge.getTarget())) {
            edges.add(edge);
        }
    }
    
    public void addEdge(Node source, Node target) {
        addEdge(new Edge(source, target, 1.0, directed));
    }
    
    public void addEdge(Node source, Node target, double weight) {
        addEdge(new Edge(source, target, weight, directed));
    }
    
    public void removeEdge(Node source, Node target) {
        edges.removeIf(edge -> edge.connects(source, target));
    }
    
    public Edge getEdge(Node source, Node target) {
        return edges.stream()
                .filter(edge -> edge.connects(source, target))
                .findFirst()
                .orElse(null);
    }
    
    public List<Edge> getEdges() {
        return new ArrayList<>(edges);
    }
    
    public boolean hasEdge(Node source, Node target) {
        return getEdge(source, target) != null;
    }
    
    // Graph traversal helpers
    public List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.getSource().equals(node)) {
                neighbors.add(edge.getTarget());
            } else if (!directed && edge.getTarget().equals(node)) {
                neighbors.add(edge.getSource());
            }
        }
        return neighbors;
    }
    
    public List<Edge> getAdjacentEdges(Node node) {
        List<Edge> adjacentEdges = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.getSource().equals(node) || (!directed && edge.getTarget().equals(node))) {
                adjacentEdges.add(edge);
            }
        }
        return adjacentEdges;
    }
    
    // Graph properties
    public boolean isDirected() {
        return directed;
    }
    
    public void setDirected(boolean directed) {
        this.directed = directed;
        // Update all edges to match the graph's direction
        for (Edge edge : edges) {
            edge.setDirected(directed);
        }
    }
    
    public int getNodeCount() {
        return nodes.size();
    }
    
    public int getEdgeCount() {
        return edges.size();
    }
    
    public void clear() {
        nodes.clear();
        edges.clear();
    }
    
    // Reset all nodes and edges to their default state
    public void resetStates() {
        for (Node node : nodes.values()) {
            node.setState(Node.NodeState.UNVISITED);
        }
        for (Edge edge : edges) {
            edge.setState(Edge.EdgeState.NORMAL);
        }
    }
    
    // Find node at given coordinates (for UI interaction)
    public Node findNodeAt(double x, double y, double radius) {
        for (Node node : nodes.values()) {
            double distance = Math.sqrt(Math.pow(node.getX() - x, 2) + Math.pow(node.getY() - y, 2));
            if (distance <= radius) {
                return node;
            }
        }
        return null;
    }
    
    // Generate a unique node ID
    public String generateNodeId() {
        int counter = 1;
        String id;
        do {
            id = "Node" + counter++;
        } while (nodes.containsKey(id));
        return id;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph{\n");
        sb.append("  directed=").append(directed).append(",\n");
        sb.append("  nodes=").append(nodes.size()).append(",\n");
        sb.append("  edges=").append(edges.size()).append("\n");
        sb.append("}");
        return sb.toString();
    }
}