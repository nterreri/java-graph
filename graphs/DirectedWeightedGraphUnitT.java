package uk.ac.ucl.ucabter.graphs;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

public class DirectedWeightedGraphUnitT extends DirectedWeightedGraph {
	DirectedWeightedGraph<String, IntegerWeightEdge<String>> acyclicalGraph;
	DirectedWeightedGraph<String, IntegerWeightEdge<String>> generalGraph;
	
	@Before
	public void constructInstance() throws GraphException {
		//create instance
		acyclicalGraph = new DirectedWeightedGraph<String, IntegerWeightEdge<String>>();
		//add vertices
		acyclicalGraph.addVertex("A");
		acyclicalGraph.addVertex("B");
		acyclicalGraph.addVertex("C");
		acyclicalGraph.addVertex("D");
		acyclicalGraph.addVertex("E");
		acyclicalGraph.addVertex("F");//size = 7
		//add edges
		acyclicalGraph.setEdge("A", "B", 5);
		acyclicalGraph.setEdge("A", "C", 10);
		acyclicalGraph.setEdge("B", "C", 4);
		acyclicalGraph.setEdge("B", "E", 5);
		acyclicalGraph.setEdge("B", "D", 10);
		acyclicalGraph.setEdge("C", "E", 1);
		acyclicalGraph.setEdge("D", "F", 1);
		acyclicalGraph.setEdge("D", "E", 2);
		acyclicalGraph.setEdge("E", "F", 10);
		
		//create other instance
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
	public void testConstructor() {
		assertNotNull(acyclicalGraph);
	}
	
	@Test
	public void testGetEdges() {
		assertTrue(acyclicalGraph.edges("F").isEmpty());
		//TODO check edges
	}
	
	@Test
	public void testAddVertex() {
		acyclicalGraph.addVertex("Z");
		assertNotNull(acyclicalGraph.edges("Z"));
	}
	
	@Test
	public void testsetEdge() throws GraphException {
		acyclicalGraph.setEdge("C", "D", 5);
		List<IntegerWeightEdge<String>> edges = acyclicalGraph.edges("C");
		assertFalse(edges.isEmpty());
		
		//expect D to be the second element in list of edges from D
		Iterator<IntegerWeightEdge<String>> pointer = edges.iterator();
		pointer.next();
		assertEquals("D", pointer.next().terminal);
	}
	
	@Test(expected=GraphException.class)
	public void testsetEdgeExceptionInvalid() throws GraphException {

		acyclicalGraph.setEdge("A", "INVALID", 5);
	}
	
	@Test(expected=GraphException.class)
	public void testsetEdgeExceptionDuplicate() throws GraphException {

		acyclicalGraph.setEdge("C", "D", 5);
		acyclicalGraph.setEdge("C", "D", 5);
	}

	@Test
	public void testCostNeighbour() throws GraphException {
		assertEquals(5, acyclicalGraph.costNeighbour("A", "B"));
	}
	
	@Test(expected=GraphException.class)
	public void testCostNeighbourWithException() throws GraphException {
		assertEquals(5, acyclicalGraph.costNeighbour("A", "Z"));
	}
	
	@Test
	public void testCost() throws GraphException {
		String[] path1 = {"A", "B", "D"};
		assertEquals(15, acyclicalGraph.cost(path1));
		
		String[] path2 = {"B", "D", "E", "F"};
		assertEquals(22, acyclicalGraph.cost(path2));

		String[] path3 = {"A", "C", "E", "F"};
		assertEquals(21, acyclicalGraph.cost(path3));

		String[] path4 = {"A", "B", "E", "F"};
		assertEquals(20, acyclicalGraph.cost(path4));
		
		String[] path5 = {"A", "B", "C"};
		assertEquals(9, generalGraph.cost(path5));

		String[] path6 = {"A", "D"};
		assertEquals(5, generalGraph.cost(path6));
		
		String[] path7 = {"A", "D", "C"};
		assertEquals(13, generalGraph.cost(path7));
		
		String[] path8 = {"A", "E", "B", "C", "D"};
		assertEquals(21, generalGraph.cost(path8));
	}
	
	@Test(expected=GraphException.class)
	public void testCostNoSuchPath() throws GraphException {
//		String[] path1 = {"A", "B", "F"};
//		assertEquals(15, acyclicalGraph.cost(path1));
		
		String[] path2 = {"A", "E", "D"};
		assertEquals(77, generalGraph.cost(path2));
	}
	
	@Test(expected=GraphException.class)
	public void testCostTooShort() throws GraphException {
		String[] path = {"A"};
		assertEquals(15, acyclicalGraph.cost(path));
		
	}
	
	@Test
	public void testDFTraverse() {
//		Set<String> visited = new HashSet<String>();
		
		acyclicalGraph.doTraversal("A");
		assertTrue(acyclicalGraph.mark.contains("A"));
		assertTrue(acyclicalGraph.mark.contains("B"));
		assertTrue(acyclicalGraph.mark.contains("C"));
		assertTrue(acyclicalGraph.mark.contains("D"));
		assertTrue(acyclicalGraph.mark.contains("E"));
		assertTrue(acyclicalGraph.mark.contains("F"));

//		visited = new HashSet<String>();
		acyclicalGraph.doTraversal("D");
		assertFalse(acyclicalGraph.mark.contains("A"));
		assertFalse(acyclicalGraph.mark.contains("B"));
		assertFalse(acyclicalGraph.mark.contains("C"));
		assertTrue(acyclicalGraph.mark.contains("D"));
		assertTrue(acyclicalGraph.mark.contains("E"));
		assertTrue(acyclicalGraph.mark.contains("F"));
		
	}

	@Test
	public void testPathsToLessThan() throws GraphException {
		assertEquals(5, acyclicalGraph.pathsTo("A", "F", 10, Conditions.LESSTHAN));
		assertEquals(0, acyclicalGraph.pathsTo("A", "F", 1, Conditions.LESSTHAN));
		
		assertEquals(2, generalGraph.pathsTo("C", "C", 3, Conditions.LESSTHAN));
		
	}
	
	@Test
	public void testPathsToExact() throws GraphException {
		assertEquals(3, acyclicalGraph.pathsTo("A", "F", 3, Conditions.EXACT));
		assertEquals(3, generalGraph.pathsTo("A", "C", 4, Conditions.EXACT));
	}
	
	@Test(expected=GraphException.class)
	public void testPathsToException() throws GraphException {
		generalGraph.pathsTo("INVALID", "INVALID", -50 , Conditions.EXACT);
	}
	
	@Test
	public void testShortestPath() throws GraphException {
		assertEquals(16, acyclicalGraph.shortestPath("A", "F"));
		
		assertEquals(9, generalGraph.shortestPath("A", "C"));
		assertEquals(9, generalGraph.shortestPath("B", "B"));
	}
	
	@Test(expected=GraphException.class)
	public void testShortestPathNoPath() throws GraphException {
//		System.out.println(acyclicalGraph.shortestPath("F", "A"));
		assertNotEquals(Integer.MAX_VALUE, acyclicalGraph.shortestPath("F", "A"));
	}
	
	@Test
	public void testEdgeCount() {
		assertEquals(9, acyclicalGraph.edgeCount());
	}
	
	@Test
	public void testFirst() throws GraphException {
		assertEquals("B", acyclicalGraph.first("A").getTerminal());
	}
	
	@Test
	public void testNext() throws GraphException {
		assertEquals("E", acyclicalGraph.next("B", "C").getTerminal());
	}

	@Test
	public void testIsEdge() throws GraphException {
		assertTrue(acyclicalGraph.isEdge("A", "B"));
		assertFalse(acyclicalGraph.isEdge("A", "F"));
	}

	@Test
	public void testDelEdge() throws GraphException {
		acyclicalGraph.delEdge("A", "C");
		assertFalse(acyclicalGraph.isEdge("A", "C"));
	}
	
	@Test
	public void testWeight() throws GraphException {
		assertEquals(5, acyclicalGraph.weight("A", "B"));
	}
	
	@Test
	public void testMark() {
		acyclicalGraph.setMark("A", 999);
		assertEquals(1, acyclicalGraph.getMark("A"));
	}

	@Test
	public void testPathsToLessThanCost() throws GraphException {
		assertEquals(1, acyclicalGraph.pathsTo("A", "F", 20, Conditions.COST_LESSTHAN));
		assertEquals(3, acyclicalGraph.pathsTo("A", "F", 21, Conditions.COST_LESSTHAN));
		assertEquals(2, acyclicalGraph.pathsTo("B", "E", 9, Conditions.COST_LESSTHAN));
		assertEquals(2, generalGraph.pathsTo("C", "C", 16, Conditions.COST_LESSTHAN));
		assertEquals(3, generalGraph.pathsTo("C", "C", 19, Conditions.COST_LESSTHAN));
		assertEquals(4, generalGraph.pathsTo("C", "C", 21, Conditions.COST_LESSTHAN));
		assertEquals(6, generalGraph.pathsTo("C", "C", 25, Conditions.COST_LESSTHAN));
		assertEquals(9, generalGraph.pathsTo("C", "C", 30, Conditions.COST_LESSTHAN));
	}
}
