package uk.ac.ucl.ucabter.graphs;

/** Graph ADT<p>
 * 
 *  Slight adaptation of ADT in: 
 *  C. Shaffer, 2013 (Java Version, rev. 3.2.0.10), <em>Data Structures & 
 *  Algorithm Analysis</em>, page 377, <br>
 *  available at http://people.cs.vt.edu/~shaffer/Book/
 *  */
public interface WeightedGraph<V, E> { // Graph class ADT
	/** Initialize the graph
	 * @param n The number of vertices */
	public void Init(int n);
	
	/** @return The number of vertices */
	public int vertexCount();
	
	/** @return The current number of edges */
	public int edgeCount();
	
	/** @return v’s first neighbor 
	 * @throws GraphException if v does not exist*/
	public E first(V v) throws GraphException;
	
	/** @return v’s next neighbor after w 
	 * @throws GraphException if v,w does not exist*/
	public E next(V v, V w) throws GraphException;
	
	/** Set the weight for an edge
	 * @param v,w The vertices
	 * @param wght Edge weight 
	 * @throws GraphException if v,w do not exist */
	public void setEdge(V v, V w, int wght) throws GraphException;
	
	/** Delete an edge
	 * @param v,w The vertices 
	 * @throws GraphException if there is no such edge, or no such v,w */
	public void delEdge(V v, V w) throws GraphException;
	
	/** Determine if an edge is in the graph
	 * @param v,w The vertices
	 * @return true if there is an edge between v to w
	 * @throws GraphException if v,w do not exist*/
	public boolean isEdge(V v, V w) throws GraphException;
	
	/** @return The weight of edge v,w
	 * @param v,w The vertices
	 * @throws GraphException when the edge does not exist */
	public int weight(V v, V w) throws GraphException;
	
	/** Set the mark value for a vertex
	 * @param v The vertex
	 * @param val The value to set */
	public void setMark(V v, int val);
	
	/** Get the mark value for a vertex
	 * @param v The vertex
	 * @return The value of the mark */
	public int getMark(V v);
	
}