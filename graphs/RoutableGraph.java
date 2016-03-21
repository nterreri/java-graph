package uk.ac.ucl.ucabter.graphs;

/**Interface exposing methods to get information about the current global state 
 * of a graph.<p>
 * 
 * Aimed in particular at directed, weighted graphs.*/
public interface RoutableGraph<V, E> {

	/**@Return the cost (weight) of the edge between two directly connected 
	 * vertices (neighbours)
	 * @throws GraphException if the vertices are not directly connected*/
	int costNeighbour(V start, V destination) throws GraphException;

	/**@Return the cost of a path between two vertices.
	 * @throws GraphException if there is no such path*/
	int cost(V[] path) throws GraphException;

	/**@return number of paths from start to destination that meet the limit 
	 * condition. Conditions are defined within the Conditions enum in this 
	 * package.<p>
	 * 
	 * Such conditions are necessary in order to allow graph traversals to 
	 * revisit vertices after they have been visited (allows for cyclical 
	 * routes, that visit the same vertex multiple times) without looping 
	 * indefinitely. 
	 * @throws GraphException if start,destination do not exist*/
	int pathsTo(V start, V destination, int limit, Conditions c) throws GraphException;

	/**@Return the length of the shortest path between two vertices in the 
	 * graph
	 * @throws GraphException if there is no path*/
	int shortestPath(V start, V destination) throws GraphException;

}