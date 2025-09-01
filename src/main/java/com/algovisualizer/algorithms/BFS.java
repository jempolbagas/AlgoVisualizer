package com.algovisualizer.algorithms;

import com.algovisualizer.graph.Graph;
import com.algovisualizer.graph.Node;
import com.algovisualizer.graph.Edge;

import java.util.*;

/**
 * Breadth-First Search algorithm implementation with step-by-step visualization support.
 */
public class BFS {
    
    public static class BFSResult {
        private final List<BFSStep> steps;
        private final Map<Node, Node> parentMap;
        private final Map<Node, Integer> distanceMap;
        
        public BFSResult(List<BFSStep> steps, Map<Node, Node> parentMap, Map<Node, Integer> distanceMap) {
            this.steps = steps;
            this.parentMap = parentMap;
            this.distanceMap = distanceMap;
        }
        
        public List<BFSStep> getSteps() { return steps; }
        public Map<Node, Node> getParentMap() { return parentMap; }
        public Map<Node, Integer> getDistanceMap() { return distanceMap; }
        
        public List<Node> getPathTo(Node target) {
            if (!parentMap.containsKey(target)) {
                return new ArrayList<>(); // No path found
            }
            
            List<Node> path = new ArrayList<>();
            Node current = target;
            while (current != null) {
                path.add(0, current);
                current = parentMap.get(current);
            }
            return path;
        }
    }
    
    public static class BFSStep {
        public enum StepType {
            START,
            VISIT_NODE,
            EXPLORE_NEIGHBOR,
            ADD_TO_QUEUE,
            FINISH_NODE,
            COMPLETE
        }
        
        private final StepType type;
        private final Node currentNode;
        private final Node neighborNode;
        private final String description;
        private final Queue<Node> queueSnapshot;
        
        public BFSStep(StepType type, Node currentNode, Node neighborNode, String description, Queue<Node> queue) {
            this.type = type;
            this.currentNode = currentNode;
            this.neighborNode = neighborNode;
            this.description = description;
            this.queueSnapshot = new LinkedList<>(queue);
        }
        
        public StepType getType() { return type; }
        public Node getCurrentNode() { return currentNode; }
        public Node getNeighborNode() { return neighborNode; }
        public String getDescription() { return description; }
        public Queue<Node> getQueueSnapshot() { return queueSnapshot; }
    }
    
    /**
     * Performs BFS traversal starting from the given node.
     */
    public static BFSResult performBFS(Graph graph, Node startNode) {
        List<BFSStep> steps = new ArrayList<>();
        Map<Node, Node> parentMap = new HashMap<>();
        Map<Node, Integer> distanceMap = new HashMap<>();
        Set<Node> visited = new HashSet<>();
        Queue<Node> queue = new LinkedList<>();
        
        // Initialize
        queue.add(startNode);
        visited.add(startNode);
        distanceMap.put(startNode, 0);
        parentMap.put(startNode, null);
        
        steps.add(new BFSStep(BFSStep.StepType.START, startNode, null, 
                "Starting BFS from " + startNode.getLabel(), queue));
        
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            
            steps.add(new BFSStep(BFSStep.StepType.VISIT_NODE, current, null,
                    "Visiting node " + current.getLabel(), queue));
            
            // Explore neighbors
            List<Node> neighbors = graph.getNeighbors(current);
            for (Node neighbor : neighbors) {
                steps.add(new BFSStep(BFSStep.StepType.EXPLORE_NEIGHBOR, current, neighbor,
                        "Exploring neighbor " + neighbor.getLabel(), queue));
                
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parentMap.put(neighbor, current);
                    distanceMap.put(neighbor, distanceMap.get(current) + 1);
                    queue.add(neighbor);
                    
                    steps.add(new BFSStep(BFSStep.StepType.ADD_TO_QUEUE, current, neighbor,
                            "Adding " + neighbor.getLabel() + " to queue", queue));
                }
            }
            
            steps.add(new BFSStep(BFSStep.StepType.FINISH_NODE, current, null,
                    "Finished processing " + current.getLabel(), queue));
        }
        
        steps.add(new BFSStep(BFSStep.StepType.COMPLETE, null, null,
                "BFS traversal complete", queue));
        
        return new BFSResult(steps, parentMap, distanceMap);
    }
    
    /**
     * Performs BFS to find the shortest path between two nodes.
     */
    public static BFSResult findPath(Graph graph, Node startNode, Node targetNode) {
        BFSResult result = performBFS(graph, startNode);
        
        // Check if target was reached
        if (!result.getParentMap().containsKey(targetNode)) {
            // Add a step indicating no path found
            List<BFSStep> steps = new ArrayList<>(result.getSteps());
            steps.add(new BFSStep(BFSStep.StepType.COMPLETE, null, null,
                    "No path found to " + targetNode.getLabel(), new LinkedList<>()));
            return new BFSResult(steps, result.getParentMap(), result.getDistanceMap());
        }
        
        return result;
    }
}