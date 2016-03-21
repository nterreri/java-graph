package uk.ac.ucl.ucabter.graphs;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DirectedWeightedGraphDemoJUnit {
	DirectedWeightedGraph<String, IntegerWeightEdge<String>> generalGraph;

	@Before
	public void constructInstance() throws GraphException {
		//create instance
		generalGraph = new DirectedWeightedGraph<String, IntegerWeightEdge<String>>();
		//add vertices
		generalGraph.addVertex("A");
		generalGraph.addVertex("B");
		generalGraph.addVertex("C");
		generalGraph.addVertex("D");
		generalGraph.addVertex("E");//size = 6
		//add edges
		generalGraph.setEdge("A", "B", 5);
		generalGraph.setEdge("B", "C", 4);
		generalGraph.setEdge("C", "D", 7);
		generalGraph.setEdge("D", "C", 8);
		generalGraph.setEdge("D", "E", 6);
		generalGraph.setEdge("A", "D", 5);
		generalGraph.setEdge("C", "E", 2);
		generalGraph.setEdge("E", "B", 3);
		generalGraph.setEdge("A", "E", 7);
	}
	
	@Test
	public void testCost() throws GraphException {
		
		//TC1
		String[] path5 = {"A", "B", "C"};
		assertEquals(9, generalGraph.cost(path5));
		//TC2
		String[] path6 = {"A", "D"};
		assertEquals(5, generalGraph.cost(path6));
		//TC3
		String[] path7 = {"A", "D", "C"};
		assertEquals(13, generalGraph.cost(path7));
		//TC4
		String[] path8 = {"A", "E", "B", "C", "D"};
		assertEquals(21, generalGraph.cost(path8));
	}
	
	@Test(expected=GraphException.class)
	public void testCostNoSuchPath() throws GraphException {
		//TC5
		String[] path2 = {"A", "E", "D"};
		assertEquals(77, generalGraph.cost(path2));
	}
	
	@Test
	public void testPathsToLessThan() throws GraphException {
		//TC6
		assertEquals(2, generalGraph.pathsTo("C", "C", 3, Conditions.LESSTHAN));
		
	}
	
	@Test
	public void testPathsToExact() throws GraphException {
		//TC7
		assertEquals(3, generalGraph.pathsTo("A", "C", 4, Conditions.EXACT));
	}
	
	@Test
	public void testShortestPath() throws GraphException {
		//TC8
		assertEquals(9, generalGraph.shortestPath("A", "C"));
		//TC9
		assertEquals(9, generalGraph.shortestPath("B", "B"));
	}
	
	@Test
	public void testPathsToLessThanCost() throws GraphException {
		//TC10
		assertEquals(9, generalGraph.pathsTo("C", "C", 30, Conditions.COST_LESSTHAN));
	}

}
