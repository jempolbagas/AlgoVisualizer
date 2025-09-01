package com.algovisualizer.graph;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.scene.paint.Color;

/**
 * Represents a node (vertex) in a graph.
 */
public class Node {
    private String id;
    private double x;
    private double y;
    private String label;
    private NodeState state;
    private Color color;
    
    public enum NodeState {
        UNVISITED,
        VISITING,
        VISITED
    }
    
    @JsonCreator
    public Node(@JsonProperty("id") String id, 
                @JsonProperty("x") double x, 
                @JsonProperty("y") double y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.label = id;
        this.state = NodeState.UNVISITED;
        this.color = Color.LIGHTBLUE;
    }
    
    public Node(String id, double x, double y, String label) {
        this(id, x, y);
        this.label = label;
    }
    
    // Getters and setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public double getX() {
        return x;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public double getY() {
        return y;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    public String getLabel() {
        return label;
    }
    
    public void setLabel(String label) {
        this.label = label;
    }
    
    public NodeState getState() {
        return state;
    }
    
    public void setState(NodeState state) {
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
            case UNVISITED:
                color = Color.LIGHTBLUE;
                break;
            case VISITING:
                color = Color.YELLOW;
                break;
            case VISITED:
                color = Color.LIGHTGREEN;
                break;
        }
    }
    
    public double distanceTo(Node other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Node node = (Node) obj;
        return id.equals(node.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
    
    @Override
    public String toString() {
        return "Node{" +
                "id='" + id + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", label='" + label + '\'' +
                ", state=" + state +
                '}';
    }
}