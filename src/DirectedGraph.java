import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Henrik Akesson
 */
public final class DirectedGraph {
  /** Successor lists. */
  private final List<List<Integer>> successorLists;
  /** Number of edges. */
  private int nEdges;

  /**
   * Constructs a new directed graph with given number of vertices.
   *
   * @param vertices Number of vertices of the graph.
   */
  public DirectedGraph(final int vertices) {
    if (vertices < 1) {
      throw new IllegalArgumentException("Invalid number of vertices, must be >= 1");
    }
    successorLists = new ArrayList<>(vertices);
    for (int i = 0; i < vertices; i++) {
      successorLists.add(new ArrayList<>());
    }
  }

  /** @return Number of vertices. */
  public int getNVertices() {
    return successorLists.size();
  }

  /**
   * Add a directed edge between vertices with given ids. Indices must be between 0 and nbVertices - 1
   *
   * @param index1 Id of first vertex.
   * @param index2 Id of second vertex.
   */
  public void addEdge(final int index1, final int index2) {
    if (index1 < 0 || index1 >= successorLists.size() || index2 < 0 || index2 >= successorLists.size()) {
      throw new IllegalArgumentException("Invalid indices");
    }

    successorLists.get(index1).add(index2);

    nEdges++;
  }

  /** @return Current number of edges of this graph. */
  public int getNEdges() {
    return nEdges;
  }

  /**
   * @param vertex Vertex index.
   *
   * @return Successor list of given vertex.
   */
  public List<Integer> getSuccessorList(final int vertex) {
    return Collections.unmodifiableList(successorLists.get(vertex));
  }

}
