package com.algovisualizer.graph;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.scene.paint.Color;

/**
 * Represents an edge connecting two nodes in a graph.
 */
public class Edge {
    private Node source;
    private Node target;
    private double weight;
    private boolean directed;
    private EdgeState state;
    private Color color;
    
    public enum EdgeState {
        NORMAL,
        HIGHLIGHTED,
        IN_PATH
    }
    
    @JsonCreator
    public Edge(@JsonProperty("source") Node source, 
                @JsonProperty("target") Node target,
                @JsonProperty("weight") double weight,
                @JsonProperty("directed") boolean directed) {
        this.source = source;
        this.target = target;
        this.weight = weight;
        this.directed = directed;
        this.state = EdgeState.NORMAL;
        this.color = Color.BLACK;
    }
    
    public Edge(Node source, Node target) {
        this(source, target, 1.0, false);
    }
    
    public Edge(Node source, Node target, double weight) {
        this(source, target, weight, false);
    }
    
    public Edge(Node source, Node target, boolean directed) {
        this(source, target, 1.0, directed);
    }
    
    // Getters and setters
    public Node getSource() {
        return source;
    }
    
    public void setSource(Node source) {
        this.source = source;
    }
    
    public Node getTarget() {
        return target;
    }
    
    public void setTarget(Node target) {
        this.target = target;
    }
    
    public double getWeight() {
        return weight;
    }
    
    public void setWeight(double weight) {
        this.weight = weight;
    }
    
    public boolean isDirected() {
        return directed;
    }
    
    public void setDirected(boolean directed) {
        this.directed = directed;
    }
    
    public EdgeState getState() {
        return state;
    }
    
    public void setState(EdgeState state) {
        this.state = state;
        updateColor();
    }
    
    public Color getColor() {
        return color;
    }
    
    public void setColor(Color color) {
        this.color = color;
    }
    
    private void updateColor() {
        switch (state) {
            case NORMAL:
                color = Color.BLACK;
                break;
            case HIGHLIGHTED:
                color = Color.ORANGE;
                break;
            case IN_PATH:
                color = Color.RED;
                break;
        }
    }
    
    /**
     * Returns the other node connected by this edge.
     * @param node One of the nodes connected by this edge
     * @return The other node, or null if the given node is not connected by this edge
     */
    public Node getOtherNode(Node node) {
        if (source.equals(node)) {
            return target;
        } else if (target.equals(node)) {
            return source;
        } else {
            return null;
        }
    }
    
    /**
     * Checks if this edge connects the given nodes.
     */
    public boolean connects(Node node1, Node node2) {
        if (directed) {
            return source.equals(node1) && target.equals(node2);
        } else {
            return (source.equals(node1) && target.equals(node2)) ||
                   (source.equals(node2) && target.equals(node1));
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Edge edge = (Edge) obj;
        
        if (directed) {
            return source.equals(edge.source) && target.equals(edge.target);
        } else {
            return (source.equals(edge.source) && target.equals(edge.target)) ||
                   (source.equals(edge.target) && target.equals(edge.source));
        }
    }
    
    @Override
    public int hashCode() {
        if (directed) {
            return source.hashCode() * 31 + target.hashCode();
        } else {
            // For undirected edges, use a commutative hash
            return source.hashCode() + target.hashCode();
        }
    }
    
    @Override
    public String toString() {
        String arrow = directed ? " -> " : " -- ";
        return "Edge{" + source.getId() + arrow + target.getId() + 
               ", weight=" + weight + '}';
    }
}