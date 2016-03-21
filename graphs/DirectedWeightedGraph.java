package uk.ac.ucl.ucabter.graphs;

import java.util.*;

/**Concrete implementation of an adjacency-list based, weighted directed 
 * graph<p>
 * 
 * The implementation is inspired by 
 * C. Shaffer, 2013 (Java Version, rev. 3.2.0.10), <em>Data Structures & 
 * Algorithm Analysis</em>, available 
 * here: http://people.cs.vt.edu/~shaffer/Book/<p>
 * 
 * Many thanks also to Jens Krinke ( http://www0.cs.ucl.ac.uk/people/J.Krinke.html ),
 * who taught the author about data structures and algorithms.<p>
 * 
 * Given the choice of adjacency lists, retrieval, deletion and insertion will
 * come close to linear search as the graph gets larger, but since the required
 * capabilities do not relate to changing the state of the data structure, or 
 * querying the state of local parts of it, but rather querying different 
 * information about its state globally, the implementation should perform 
 * adequately, particularly with regards to the shortest path algorithm 
 * implemented (Dijkstra's). An adjacency list, in fact, contains no more than
 * the accessible nodes, unlike an adjacency matrix, where memory is reserved
 * a-priori to store an edge from one vertex to any other, and only the existing
 * edges are considered when looking for the shortest paths between the start 
 * vertex and every other vertex. This is more advantageous when the graph is 
 * sparse (as opposed to dense) see Shaffer, page 373-376. For an example of 
 * an adjacency matrix implementation, please refer to Shaffer, page 379.<p>
 * 
 * The current implementation uses HashMap, HashSet and LinkedList, but could 
 * in principle be extended to delegate to other classes implementing the 
 * Map, Set and List interface respectively.<p>
 * 
 * It provides facilities to:
 * <ol>
 * <li>Calculate the length (cost) of a given route (path)</li>
 * <li>The number of possible routes between two points</li>
 * <li>The shortest route between two points (NOTE: assumes total distance is less 
 * than Integer.MAX_VALUE)</li>
 * </ol><p>*/
public class DirectedWeightedGraph<V, E extends IntegerWeightEdge<V>> 
implements RoutableGraph<V, E>, WeightedGraph<V, E> {
	
	/**Record of vertices and reachable from each*/
	protected Map<V, List<E>> vertices;
	/**Record of visited vertices (used in traversal and shortest path)*/
	protected Set<V> mark;
	
	/**Constructs a default instance with an initial capacity of 10 that 
	 * increases automatically*/
	public DirectedWeightedGraph() {
		Init(10);
	}
	
	/**ConstructorS auxilliary method*/
	public void Init(int capacity) {
		InitHashMap(capacity);
	}
	
	/**Delegated to by Init, other similar methods may be added to change the 
	 * underlying structures so long as they conform to the Map and Set 
	 * interfaces*/
	private void InitHashMap(int capacity) {
		vertices = new HashMap<V, List<E>>(capacity);
		mark = new HashSet<V>(capacity);
	}
		
	/**Adds a new vertex to the graph*/
	public void addVertex(V vertex) {
		addVertexLinkedList(vertex);
	}
	
	/**method delegated to by addVertex()*/
	private void addVertexLinkedList(V vertex) {
		vertices.put(vertex, new LinkedList<E>());
	}
	
	/**Returns a list of all vertices reachable from this vertex*/
	public List<E> edges(V vertex) {
		List<E> edges = vertices.get(vertex);
		return edges;
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.ucl.ucabter.graphs.RoutableGraph#costNeighbour(V, V)
	 */
	@Override
	public int costNeighbour(V start, V destination) 
			throws GraphException {
		int result = 0;
		boolean found = false;
		Iterator<E> edgePointer = vertices.get(start).iterator();
		
		//destination must be in list of vertices reachable from the start
		while(edgePointer.hasNext()) {
			IntegerWeightEdge<V> current = edgePointer.next();
			if(current.terminal == destination) {
				result = current.weight;
				found = true;
			}
		}
		
		if(!found)
			throw new GraphException("no such destination vertex");
		return result;	
	}
		
	/* (non-Javadoc)
	 * @see uk.ac.ucl.ucabter.graphs.RoutableGraph#cost(V[])
	 */
	@Override
	public int cost(V[] path) throws GraphException {
		if(path.length <= 1)
			throw new GraphException("path too short");
		
		int accumulator = 0;
		for(int i = 0; i < (path.length - 1); i++) 
			accumulator += costNeighbour(path[i], path[i+1]);
		
		return accumulator;
	}
	
	/**Visits the graph depth-first from currentVertex parameter, returns a 
	 * record of vertices visited as a map from vertices labes to true boolean
	 * objects */
	private void dfTraverse(V currentVertex, 
			Set<V> visited) {
		
		//visit action
		visited.add(currentVertex);
		
		Iterator<E> edgePointer = vertices.get(currentVertex).iterator();
		while(edgePointer.hasNext()) {
			IntegerWeightEdge<V> current = edgePointer.next();
			if(!visited.contains(current.terminal))
				dfTraverse(current.terminal, visited);
		}
		
	}
	
	/**Traverses the graph from the argument starting vertex*/
	public void doTraversal(V start) {
		//initialize mark record, record of visited vertices
		mark = new HashSet<V>(vertices.size() + 1, 1.0f);
		dfTraverse(start, mark);
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.ucabter.graphs.RoutableGraph#pathsTo(V, V, int, uk.ac.ucl.ucabter.graphs.Conditions)
	 */
	@Override
	public int pathsTo(V start, V destination, int limit, 
			Conditions c) throws GraphException {
		if(vertices.get(start) == null || vertices.get(destination) == null)
			throw new GraphException("No such vertex");
		
		switch(c) {
		case LESSTHAN:
			return pathsToLessThan(start, destination, limit - 1);
		case EXACT:
			return pathsToExact(start, destination, limit - 1);
		case COST_LESSTHAN:
			return pathsToLessThanCost(start, destination, limit - 1); //limit -1?
		default:
			return 0;
		}
	}
	
	/**Computes recursively all available paths from start to destination that 
	 * take less than or exactly the limit parameter number of junctures. 
	 * Further conditions may be added as other delegates*/
	protected int pathsToLessThan(V start, V destination, int limit) {
		//each recursion step uses its own local accumulator
		int accumulator = 0;

		//iterate through available edges from current node
		for(IntegerWeightEdge<V> edge : edges(start)) {
			
			//stop if no of junctures is strictly larger than limit
			if(limit < 0)
				break;
			
			//increase path No accumulator if destination is reached
			if(edge.terminal == destination) 
				++accumulator;
			
			//recurse over next available non-terminal edge
			else if(!edges(edge.terminal).isEmpty())
				accumulator += 
				pathsToLessThan(edge.terminal, destination, limit - 1);
		}

		return accumulator;
	}
	
	/**Computes recursively all available paths from start vertex to destination
	 * that exactly match the limit on the number of junctures taken.
	 * Further conditions may be added as other delegates*/
	protected int pathsToExact(V start, V destination, int limit) {
		//each recursion step uses its own local accumulator
		int accumulator = 0;
		
		//iterate through available edges from current node
		for(IntegerWeightEdge<V> edge : edges(start)) {
			
			//stop if no of junctures is strictly larger than limit
			if(limit < 0)
				break;
			
			//increase path No accumulator if destination is reached AND No of
			//junctures matches condition
			if(limit == 0 && edge.terminal == destination) 
				++accumulator;
			
			//recurse over next available non-terminal edge
			else if(!edges(edge.terminal).isEmpty())
				accumulator += 
				pathsToExact(edge.terminal, destination, limit - 1);
		}
		
		return accumulator;
	}
	
	/**Computes the number of paths available from the start node to the 
	 * destination node such that they cost strictly less than the limit 
	 * parameter in terms of path length/cost/weight*/
	protected int pathsToLessThanCost(V start, V destination, int limit) {
		//each recursion step uses its own local accumulator, accumulator 
		//stores No of discovered routes meeting the condition on the limit
		int accumulator = 0;

		//iterate through available edges from current node
		for(IntegerWeightEdge<V> edge : edges(start)) {

			//stop if cost is strictly larger than limit
			if(limit < 0)
				break;

			//increase cost accumulator if destination is reached, limit must 
			//also be checked as final step may be too costly to count. Using
			//costNeighbour may cause an exception, but should not occur since 
			//at this stage is known that destination is a terminal in at least
			//one edge from the current start vertex
			try {
				if(edge.terminal == destination) {
					if(!(limit - costNeighbour(start, destination) < 0))
						++accumulator;
						//skip to next iteration if current edge to destination
						//is too costly
					else
						continue;
				}

				//recurse over next available non-terminal edge, since loops are
				//allowed, the recursion is not an alternative, but something to
				//always do whenever possible, until limit is reached!
				//removing the "else" in the same section of the other methods
				//such as pathsToLessThan() and pathsToExact() does not break
				//the unit tests
				if(!edges(edge.terminal).isEmpty())
					accumulator += 
					pathsToLessThanCost(edge.terminal, destination, 
							limit - costNeighbour(start, edge.terminal));
			} catch (GraphException e) {
				//costNeighbour will throw an exception if the relevant edge 
				//has been deleted between the time that the current recursion 
				//and iteration over the edges steps began 
				//should not occur unless edges are deleted concurrently while
				//the present method is in progress
				throw new ConcurrentModificationException("Edge deleted from "
						+ this.getClass().getName() + " graph during global "
								+ "graph instance state read");
			}
		}

		return accumulator;
	}
	/* (non-Javadoc)
	 * @see uk.ac.ucl.ucabter.graphs.RoutableGraph#shortestPath(V, V)
	 */
	@Override
	public int shortestPath(V start, V destination) 
			throws GraphException {
		
		return dijkstraShortestPath(start, destination);
	}
	
	/**Implementation of Dijkstra's algorithm based on Map to Lists graph 
	 * structure. Delegated to by shortestPath() interface method*/
	protected int dijkstraShortestPath(V start, V destination)
			throws GraphException {
		if(vertices.get(start) == null || vertices.get(destination) == null)
			throw new GraphException("No such vertex");
		
		//reserve memory to store distances from start to each vertex, and to
		//keep track of which vertices have already been explored/exhausted/
		//visited
		mark = new HashSet<V>(vertices.size() + 1, 1.0f);
		Set<V> verticesSet = vertices.keySet();
		
		//initialize record of all distances
		//setting the initial capacity to 1 more than the size of the vertex map
		//setting load factor to 100% in order to ensure the hashmap will never
		//be rehashed while it exists 
		// see https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html
		HashMap<V, Integer> distances = 
				new HashMap<V, Integer>(vertices.size() + 1, 1.0f);
		
		//Stage 1: initialize all distances based on immediate accessiblity 
		//from start (i.e. from weight of edge between start and vertex, 
		//if any)
		
		//Initialize all distances to positive infinity
		for(V vertex : verticesSet) 
			distances.put(vertex, Integer.MAX_VALUE);
		//then compute actual distances from starting vertex to immediately
		//accessible vertices
		for(IntegerWeightEdge<V> edge : vertices.get(start)) {
			int cost = costNeighbour(start, edge.terminal);
			distances.put(edge.terminal, cost);
		}
		
		mark.add(start);
		V vertexPointer = start;
		
		//outer loop for stages 2 and 3
		for(int i = 0; i < vertices.size(); i++) {
			//set minimum distance variable to positive infinity
			int min = Integer.MAX_VALUE; 
			
			//Stage 2: determine next vertex closest to starting vertex 
			//(shortest path from startingVertex), from the current vertex 
			//pointer
			for(V vertex : verticesSet) {
				int distanceRecord = distances.get(vertex);
				//if the vertex has not been visited, 
				if(!mark.contains(vertex) && distanceRecord < min) {
					vertexPointer = vertex;//update pointer
					min = distanceRecord;//update smallest distance record for
					//target
				}
			}
			
			//mark current vertex as visited
			mark.add(vertexPointer);
			
			//Stage 3: update distances if element is reachable from current
			//vertex pointer and path from current vertex pointer to element is
			//shorter than recorded distance between start and element
			//That is, if there is a shorter path to the pointer element than
			//the one currently recorded
			for(IntegerWeightEdge<V> edge : vertices.get(vertexPointer)) {
				int cost = costNeighbour(vertexPointer, edge.terminal);
				int costFromStartingVertex = cost + min;
				if(costFromStartingVertex < distances.get(edge.terminal))
					distances.put(edge.terminal, costFromStartingVertex);
			}
			
		}
		int result = distances.get(destination);
		if(result == Integer.MAX_VALUE)
			throw new GraphException("No such path");
		return result;
	}

	//WeightedGraph interface
	
	@Override
	public int vertexCount() {
		
		return vertices.size();
	}

	@Override
	public int edgeCount() {
		int accumulator = 0;
		for(V vertex : vertices.keySet()) {
			accumulator += vertices.get(vertex).size();
		}
		return accumulator;
	}

	@Override
	public E first(V v) throws GraphException {
		List<E> edges = vertices.get(v);
		if(edges == null)
			throw new GraphException("No such vertex");
		
		return (edges.isEmpty() ? null : edges.get(0));
	}

	@Override
	public E next(V v, V w) throws GraphException {
		List<E> edges = vertices.get(v);
		
		if(edges == null || vertices.get(w) == null)
			throw new GraphException("No such vertex");
		
		E result = null;
		for(int i = 0; i < edges.size(); i++){
			if(edges.get(i).getTerminal() == w) {
				if(i < edges.size())
					result = edges.get(i + 1);
				else
					throw new GraphException("No more vertices accessible after"
							+ w);
			}
		}
		
		return result;
	}

	@Override
	public void setEdge(V start, V destination, int cost) 
			throws GraphException{
		
		//destination vertex must exist in graph
		if(vertices.get(start) == null || vertices.get(destination) == null) 
			throw new GraphException("Vertex not in graph");
		
		List<E> edges = vertices.get(start);
		Iterator<E> edgePointer = edges.iterator();
		
		while(edgePointer.hasNext()) {
			IntegerWeightEdge<V> current = edgePointer.next();
			//edge must not already exist
			if(current.terminal == destination)
				throw new 
				GraphException("edge already exists between vertices");
		}
		
		
		@SuppressWarnings("unchecked")
		//should be safe as long as E extends WeightedEdge
		E edge = (E) new IntegerWeightEdge<V>(destination, cost);
		edges.add(edge);
	}

	@Override
	public void delEdge(V v, V w) throws GraphException {
		if(vertices.get(v) == null || vertices.get(w) == null) 
			throw new GraphException("Vertex not in graph");
		
		boolean found = false;
		List<E> edges = vertices.get(v);
		for(int i = 0; i < edges.size(); i++) {
			if(edges.get(i).getTerminal() == w) {
				edges.remove(i);
				found = true;
				break;
			}
		}
		
		if(!found)
			throw new GraphException("No such edge");
	}

	@Override
	public boolean isEdge(V v, V w) throws GraphException {
		if(vertices.get(v) == null || vertices.get(w) == null) 
			throw new GraphException("Vertex not in graph");
		
		List<E> edges = vertices.get(v);
		boolean result = false;
		
		for(int i = 0; i < edges.size(); i++){
			if(edges.get(i).getTerminal() == w) {
				result = true;
			}
		}

		return result;
	}

	@Override
	public int weight(V v, V w) throws GraphException {
		if(vertices.get(v) == null || vertices.get(w) == null) 
			throw new GraphException("Vertex not in graph");
		
		int result = 0;
		boolean found = false;
		List<E> edges = vertices.get(v);
		for(int i = 0; i < edges.size(); i++) {
			E edge = edges.get(i);
			if(edge.getTerminal() == w) {
				result = edge.getWeight();
				found = true;
				break;
			}
		}
		
		if(!found)
			throw new GraphException("No such edge");
		
		return result;
	}

	@Override
	public void setMark(V v, int val) {
		mark.add(v);
	}
	@Override
	
	public int getMark(V v) {
		if(mark.contains(v))
			return 1;
		else
			return 0;
	}
	
}
