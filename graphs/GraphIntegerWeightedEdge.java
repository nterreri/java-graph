package uk.ac.ucl.ucabter.graphs;

/**Interface of a graph edge with an Integer weight*/
public interface GraphIntegerWeightedEdge<V> {
	/**@return the end vertex of the edge*/
	V getTerminal();

	/**@return the value associated with the weight (or cost) of the edge*/
	int getWeight();

}