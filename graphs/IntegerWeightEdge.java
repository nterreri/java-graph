package uk.ac.ucl.ucabter.graphs;

/**A terminal, value pair.<p> 
 * The terminal is the vertex in the graph to which the edge is connected 
 * (and/or directed), the integer is the cost or weight associated with the edge.*/
public class IntegerWeightEdge<V> implements GraphIntegerWeightedEdge<V> {
	protected V terminal;
	protected int weight;

	/**Constructs an edge to the terminal with the given weight.*/
	protected IntegerWeightEdge(V terminal, int weight) {
		this.terminal = terminal;
		this.weight = weight;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.ucabter.graphs.GraphEdge#getTerminal()
	 */
	@Override
	public V getTerminal() {
		return terminal;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.ucabter.graphs.GraphEdge#getWeight()
	 */
	@Override
	public int getWeight()  {
		return weight;
	}

}