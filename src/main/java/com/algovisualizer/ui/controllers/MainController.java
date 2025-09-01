package com.algovisualizer.ui.controllers;

import com.algovisualizer.algorithms.BFS;
import com.algovisualizer.graph.Graph;
import com.algovisualizer.graph.Node;
import com.algovisualizer.graph.Edge;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Main controller for the AlgoVisualizer application.
 */
public class MainController implements Initializable {
    
    @FXML private Canvas graphCanvas;
    @FXML private Button playButton;
    @FXML private Button pauseButton;
    @FXML private Button resetButton;
    @FXML private Button clearButton;
    @FXML private ComboBox<String> algorithmComboBox;
    @FXML private Slider speedSlider;
    @FXML private Label statusLabel;
    @FXML private TextArea infoTextArea;
    @FXML private ToggleButton nodeMode;
    @FXML private ToggleButton edgeMode;
    
    private Graph graph;
    private GraphicsContext gc;
    private Node selectedNode;
    private Node startNode;
    private Node targetNode;
    private boolean isCreatingEdge;
    private Timeline animationTimeline;
    private BFS.BFSResult currentResult;
    private int currentStep;
    
    private static final double NODE_RADIUS = 20;
    private static final double EDGE_WIDTH = 2;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        graph = new Graph();
        gc = graphCanvas.getGraphicsContext2D();
        
        setupUI();
        setupEventHandlers();
        updateDisplay();
    }
    
    private void setupUI() {
        // Initialize algorithm combo box
        algorithmComboBox.getItems().addAll("BFS - Breadth-First Search", "DFS - Depth-First Search");
        algorithmComboBox.setValue("BFS - Breadth-First Search");
        
        // Initialize speed slider
        speedSlider.setMin(0.5);
        speedSlider.setMax(3.0);
        speedSlider.setValue(1.0);
        
        // Set initial mode
        nodeMode.setSelected(true);
        
        // Set initial status
        statusLabel.setText("Ready - Click to add nodes, select Edge Mode to connect them");
        infoTextArea.setText("Welcome to AlgoVisualizer!\n\n" +
                "Instructions:\n" +
                "1. Click 'Node Mode' and click on canvas to add nodes\n" +
                "2. Click 'Edge Mode' and click two nodes to connect them\n" +
                "3. Right-click a node to set as start (green) or target (red)\n" +
                "4. Select an algorithm and click Play to visualize\n" +
                "5. Use controls to pause, reset, or adjust speed");
    }
    
    private void setupEventHandlers() {
        graphCanvas.setOnMouseClicked(this::handleCanvasClick);
        graphCanvas.setOnMousePressed(this::handleMousePressed);
        graphCanvas.setOnMouseDragged(this::handleMouseDragged);
        graphCanvas.setOnMouseReleased(this::handleMouseReleased);
        
        nodeMode.setOnAction(e -> {
            if (nodeMode.isSelected()) {
                edgeMode.setSelected(false);
                isCreatingEdge = false;
                statusLabel.setText("Node Mode - Click to add nodes");
            }
        });
        
        edgeMode.setOnAction(e -> {
            if (edgeMode.isSelected()) {
                nodeMode.setSelected(false);
                statusLabel.setText("Edge Mode - Click two nodes to connect them");
            }
        });
    }
    
    @FXML
    private void handleCanvasClick(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();
        
        Node clickedNode = graph.findNodeAt(x, y, NODE_RADIUS);
        
        if (event.getButton() == MouseButton.SECONDARY) { // Right click
            handleRightClick(clickedNode, event);
        } else { // Left click
            if (nodeMode.isSelected()) {
                if (clickedNode == null) {
                    addNode(x, y);
                }
            } else if (edgeMode.isSelected()) {
                handleEdgeCreation(clickedNode);
            }
        }
        
        updateDisplay();
    }
    
    private void handleRightClick(Node node, MouseEvent event) {
        if (node == null) return;
        
        ContextMenu contextMenu = new ContextMenu();
        
        MenuItem setStart = new MenuItem("Set as Start Node");
        setStart.setOnAction(e -> {
            startNode = node;
            statusLabel.setText("Start node set to " + node.getLabel());
        });
        
        MenuItem setTarget = new MenuItem("Set as Target Node");
        setTarget.setOnAction(e -> {
            targetNode = node;
            statusLabel.setText("Target node set to " + node.getLabel());
        });
        
        MenuItem delete = new MenuItem("Delete Node");
        delete.setOnAction(e -> {
            graph.removeNode(node.getId());
            if (startNode == node) startNode = null;
            if (targetNode == node) targetNode = null;
            updateDisplay();
        });
        
        contextMenu.getItems().addAll(setStart, setTarget, new SeparatorMenuItem(), delete);
        contextMenu.show(graphCanvas, event.getScreenX(), event.getScreenY());
    }
    
    private void addNode(double x, double y) {
        String nodeId = graph.generateNodeId();
        Node node = new Node(nodeId, x, y);
        graph.addNode(node);
        statusLabel.setText("Added node " + nodeId);
    }
    
    private void handleEdgeCreation(Node clickedNode) {
        if (clickedNode == null) return;
        
        if (selectedNode == null) {
            selectedNode = clickedNode;
            statusLabel.setText("Selected " + selectedNode.getLabel() + " - click another node to create edge");
        } else {
            if (!selectedNode.equals(clickedNode) && !graph.hasEdge(selectedNode, clickedNode)) {
                graph.addEdge(selectedNode, clickedNode);
                statusLabel.setText("Created edge between " + selectedNode.getLabel() + " and " + clickedNode.getLabel());
            }
            selectedNode = null;
        }
    }
    
    private void handleMousePressed(MouseEvent event) {
        if (nodeMode.isSelected()) {
            Node clickedNode = graph.findNodeAt(event.getX(), event.getY(), NODE_RADIUS);
            if (clickedNode != null) {
                selectedNode = clickedNode;
            }
        }
    }
    
    private void handleMouseDragged(MouseEvent event) {
        if (selectedNode != null && nodeMode.isSelected()) {
            selectedNode.setX(event.getX());
            selectedNode.setY(event.getY());
            updateDisplay();
        }
    }
    
    private void handleMouseReleased(MouseEvent event) {
        selectedNode = null;
    }
    
    @FXML
    private void playAlgorithm() {
        if (startNode == null) {
            statusLabel.setText("Please set a start node (right-click on a node)");
            return;
        }
        
        graph.resetStates();
        
        String selectedAlgorithm = algorithmComboBox.getValue();
        if (selectedAlgorithm.startsWith("BFS")) {
            if (targetNode != null) {
                currentResult = BFS.findPath(graph, startNode, targetNode);
            } else {
                currentResult = BFS.performBFS(graph, startNode);
            }
            
            currentStep = 0;
            startAnimation();
        }
    }
    
    private void startAnimation() {
        if (animationTimeline != null) {
            animationTimeline.stop();
        }
        
        double speed = speedSlider.getValue();
        Duration stepDuration = Duration.seconds(1.0 / speed);
        
        animationTimeline = new Timeline(new KeyFrame(stepDuration, e -> animateStep()));
        animationTimeline.setCycleCount(Timeline.INDEFINITE);
        animationTimeline.play();
        
        statusLabel.setText("Animation playing...");
    }
    
    private void animateStep() {
        if (currentResult == null || currentStep >= currentResult.getSteps().size()) {
            pauseAnimation();
            statusLabel.setText("Animation complete");
            return;
        }
        
        BFS.BFSStep step = currentResult.getSteps().get(currentStep);
        executeAnimationStep(step);
        currentStep++;
        updateDisplay();
    }
    
    private void executeAnimationStep(BFS.BFSStep step) {
        switch (step.getType()) {
            case START:
                step.getCurrentNode().setState(Node.NodeState.VISITING);
                break;
            case VISIT_NODE:
                step.getCurrentNode().setState(Node.NodeState.VISITING);
                break;
            case EXPLORE_NEIGHBOR:
                if (step.getNeighborNode().getState() == Node.NodeState.UNVISITED) {
                    // Highlight the edge being explored
                    Edge edge = graph.getEdge(step.getCurrentNode(), step.getNeighborNode());
                    if (edge != null) {
                        edge.setState(Edge.EdgeState.HIGHLIGHTED);
                    }
                }
                break;
            case ADD_TO_QUEUE:
                step.getNeighborNode().setState(Node.NodeState.VISITING);
                break;
            case FINISH_NODE:
                step.getCurrentNode().setState(Node.NodeState.VISITED);
                break;
        }
        
        infoTextArea.setText(step.getDescription() + "\n" +
                "Queue: " + formatQueue(step.getQueueSnapshot()));
    }
    
    private String formatQueue(java.util.Queue<Node> queue) {
        if (queue.isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Node node : queue) {
            if (!first) sb.append(", ");
            sb.append(node.getLabel());
            first = false;
        }
        sb.append("]");
        return sb.toString();
    }
    
    @FXML
    private void pauseAnimation() {
        if (animationTimeline != null) {
            animationTimeline.pause();
            statusLabel.setText("Animation paused");
        }
    }
    
    @FXML
    private void resetAnimation() {
        if (animationTimeline != null) {
            animationTimeline.stop();
        }
        
        graph.resetStates();
        currentStep = 0;
        currentResult = null;
        updateDisplay();
        statusLabel.setText("Animation reset");
        infoTextArea.setText("Animation reset. Ready to start again.");
    }
    
    @FXML
    private void clearGraph() {
        graph.clear();
        startNode = null;
        targetNode = null;
        selectedNode = null;
        resetAnimation();
        updateDisplay();
        statusLabel.setText("Graph cleared");
    }
    
    private void updateDisplay() {
        gc.clearRect(0, 0, graphCanvas.getWidth(), graphCanvas.getHeight());
        
        // Draw edges first (so they appear behind nodes)
        for (Edge edge : graph.getEdges()) {
            drawEdge(edge);
        }
        
        // Draw nodes
        for (Node node : graph.getNodes()) {
            drawNode(node);
        }
    }
    
    private void drawNode(Node node) {
        double x = node.getX();
        double y = node.getY();
        
        // Set color based on node state and special roles
        Color fillColor = node.getColor();
        if (node == startNode) {
            fillColor = Color.GREEN;
        } else if (node == targetNode) {
            fillColor = Color.RED;
        }
        
        // Draw node circle
        gc.setFill(fillColor);
        gc.fillOval(x - NODE_RADIUS, y - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);
        
        // Draw border
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeOval(x - NODE_RADIUS, y - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);
        
        // Draw label
        gc.setFill(Color.BLACK);
        gc.fillText(node.getLabel(), x - 10, y + 5);
    }
    
    private void drawEdge(Edge edge) {
        Node source = edge.getSource();
        Node target = edge.getTarget();
        
        double x1 = source.getX();
        double y1 = source.getY();
        double x2 = target.getX();
        double y2 = target.getY();
        
        // Calculate edge endpoints (so line doesn't overlap with node circles)
        double dx = x2 - x1;
        double dy = y2 - y1;
        double length = Math.sqrt(dx * dx + dy * dy);
        
        if (length > 0) {
            double unitX = dx / length;
            double unitY = dy / length;
            
            double startX = x1 + unitX * NODE_RADIUS;
            double startY = y1 + unitY * NODE_RADIUS;
            double endX = x2 - unitX * NODE_RADIUS;
            double endY = y2 - unitY * NODE_RADIUS;
            
            // Set edge color and width
            gc.setStroke(edge.getColor());
            gc.setLineWidth(EDGE_WIDTH);
            
            // Draw edge
            gc.strokeLine(startX, startY, endX, endY);
            
            // Draw arrow for directed edges
            if (edge.isDirected()) {
                drawArrow(endX, endY, unitX, unitY);
            }
            
            // Draw weight if not 1.0
            if (edge.getWeight() != 1.0) {
                double midX = (startX + endX) / 2;
                double midY = (startY + endY) / 2;
                gc.setFill(Color.BLACK);
                gc.fillText(String.format("%.1f", edge.getWeight()), midX, midY);
            }
        }
    }
    
    private void drawArrow(double x, double y, double unitX, double unitY) {
        double arrowLength = 10;
        double arrowAngle = Math.PI / 6; // 30 degrees
        
        double leftX = x - arrowLength * (unitX * Math.cos(arrowAngle) + unitY * Math.sin(arrowAngle));
        double leftY = y - arrowLength * (unitY * Math.cos(arrowAngle) - unitX * Math.sin(arrowAngle));
        double rightX = x - arrowLength * (unitX * Math.cos(arrowAngle) - unitY * Math.sin(arrowAngle));
        double rightY = y - arrowLength * (unitY * Math.cos(arrowAngle) + unitX * Math.sin(arrowAngle));
        
        gc.strokeLine(x, y, leftX, leftY);
        gc.strokeLine(x, y, rightX, rightY);
    }
}