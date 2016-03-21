package uk.ac.ucl.ucabter.graphs;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


/*First iteration, implements Dijkstra's algorithm to determine shortest path
 * between two vertices. Tightly coupled with hash map and uses an adjacency
 * matrix to store information about edges. Final solution should not be 
 * tightly coupled, considering using an adjacency list instead of a matrix
 * given that the focus is on shortest path rather than vertex/edge insertion
 * deletion and update. Unit not properly tested*/
public class CrudeConcreteGraph {
	
	private HashMap<String, Integer> vertices;
	private int[][] edges;
	
	public CrudeConcreteGraph() {}
	
	public CrudeConcreteGraph(int size) {
		vertices = new HashMap<String, Integer>(size);
		edges = new int[size][];
		
		for(int i = 0; i<size; i++) {
			edges[i] = new int[size];
			for(int j = 0; j < size; j++)
				edges[i][j] = 0;
		}
		
	}
	
	public int countVertices() {
		return vertices.size();
	}
	
	public int countEdges() {
		int result = 0;
		for(int i = 0; i<vertices.size(); i++)
			for(int j = 0; j < vertices.size(); j++)
				result += (edges[i][j] == 0 ? 0 : 1);
		
		return result;
	}
	
	public void setEdge(int i, int j, int value) throws IndexOutOfBoundsException {
			edges[i][j] = value;
	}
	
	public int getEdge(int i, int j) {
		return edges[i][j];
	}
	
	public double shortestPath(String v1, String v2) {
		//store hashed index for both start and end point
		int startingVertex = vertices.get(v1);
		int destinationVertex = vertices.get(v2);
		//reserve memory to store distances from start of each vertex, and to
		//keep track of which vertices have already been explored/exhausted/
		//visited
		boolean[] distanceFound = new boolean[vertices.size()];
		int[] distances = new int[vertices.size()];
		
		//Stage 1: initialize all distances based on immediate accessiblity 
		//from start (i.e. from weight of edge between start and vertex, 
		//if any)
		for(int i = 0; i < vertices.size(); i++) {
			int cost = edges[startingVertex][i];
				distances[i] = cost;
		}
		
		int vertexPointer = startingVertex; //initialize vertex pointer to 
		//start for next stage
		
		//outer loop for stages 2 and 3
		for(int i = 0; i < vertices.size(); i++) {
			int min = Integer.MAX_VALUE; //set minumum distance to positive 
			//infinity
			
			//Stage 2: determine next closest (shortest path from 
			//startingVertex) vertex, from the current vertex pointer
			for(int j = 0; j < vertices.size(); j++) {
				if(!distanceFound[j] && distances[j] < min) {
					vertexPointer = j;//update pointer
					min = distances[j];//update smallest distance record
				}
			}
			
			//Stage 3: update distances if element is reachable from current
			//vertex pointer and path from current vertex pointer to element is
			//shorter than recorded distance between startingVertex and element
			//That is, if there is a shorter path to the pointer element than
			//the one currently recorded
			for(int j = 0; j < vertices.size(); j++) {
				int cost = edges[vertexPointer][j];
				int costFromStartingVertex = cost + min;
				if(costFromStartingVertex < distances[j])
					distances[j] = costFromStartingVertex;
			}
		}
		//at the end of outer loop, return the shortest distance from 
		//startingVertex to destination vertex:
		return distances[destinationVertex];
	}

}
