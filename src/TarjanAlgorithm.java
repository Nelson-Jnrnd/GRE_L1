import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Stack;

/**
 * Object that calculates the connected components of a given connected graph using the tarjan algorithm
 * @author Nelson Jeanrenaud
 * @version 1.0
 */
public class TarjanAlgorithm {
    /**
     * Graph to process
     */
    private DirectedGraph graph;
    /**
     * Successor array
     */
    private int[] scc;
    /**
     * Order in the search of each vertex (index)
     */
    private int[] dfsnum;
    /**
     * Oldest ancester we can reach from each vertex (index)
     */
    private int[] low;
    /**
     * Counter of the search
     */
    private int dfsnumCounter;
    /**
     * Number of connected components found
     */
    private int connectedComponentCounter;
    private Stack<Integer> visitedVertices;

    public TarjanAlgorithm() {}

    /**
     * Initialize the variables for a particular graph
     * @param graph graph on which to apply the tarjan algorithm
     */
    private void init(DirectedGraph graph){
        this.graph = graph;
        this.scc = new int[graph.getNVertices()];
        this.dfsnum = new int[scc.length];
        this.low = new int[scc.length];
        this.visitedVertices = new Stack<>();
    }

    /**
     * Calculate the number of connected components for a given directed graph
     * @param graph the input graph on which we count the connected components
     * @return Number of connected components
     */
    public int nbConnectedComponents(DirectedGraph graph){
        sccTarjan(graph);
        return connectedComponentCounter;
    }

    /**
     * Returns the connected components of a given directed graph
     * @param graph the input graph on which we calculate the connected components
     * @return array containning the number of the component each vertex (index) is part of.
     */
    public int[] conectedComponents(DirectedGraph graph){
        return sccTarjan(graph).clone();
    }

    /**
     * Apply the tarjan algorithm on a directed graph
     * @param graph given graph on which we apply the algorithm
     * @return array containning the number of the component each vertex (index) is part of.
     */
    private int[] sccTarjan(DirectedGraph graph) {
        init(graph);

        dfsnumCounter = 0;
        connectedComponentCounter = 0;
        visitedVertices.empty();

        for (int vertex = 0; vertex < scc.length; vertex++) {
            if(scc[vertex] == 0)
                scc(vertex);
        }
        return scc;
    }

    /**
     * Depth search to find all the other vertices in the same connected component
     * @param vertex vertex we start the depth search from
     */
    private void scc(Integer vertex) {
        dfsnum[vertex] = ++dfsnumCounter;
        low[vertex] = dfsnumCounter;

        visitedVertices.push(vertex);
        for (int successor: graph.getSuccessorList(vertex)) {
            if(dfsnum[successor] == 0) // is vertex not yet visited
                scc(successor);
            if(scc[successor] == 0) // does vertex allow us to go further up the tree
                low[vertex] = Math.min(low[vertex], low[successor]);
        }
        if(low[vertex] == dfsnum[vertex]){ // pop until we get to vertex
            connectedComponentCounter++;
            // mark all the vertices of the tree with the connected component number
            while(true){
                Integer removedVertex = visitedVertices.pop();
                scc[removedVertex] = connectedComponentCounter;
                if(removedVertex.equals(vertex)){
                    break;
                }
            }
        }
    }


    /**
     * Calculate the number of connected components of directed graphs
     *
     * Each graph is stored in a text file whose first line contains two integers.
     * The first is positive and corresponds to the number n of vertices of the graph, the latter being
     * numbered from 0 to n − 1. The second is positive or null and corresponds to the number m of arcs
     * of the graph. The following m rows each define an arc and contain two integers,
     * the first corresponding to the number of the initial end of the arc and the second to the number
     * from its final end
     * 
     * @param args path to the directory containing all the graphs
     */
    public static void main(String[] args) {
        if(args.length != 1){
            throw new IllegalArgumentException("Exactly 1 parameter (graphs folder path) is required !");
        }
        File folder = new File(args[0]);

        DirectedGraph graph;

        // A filter that will only accept files ending with `.txt`
        File[] files = folder.listFiles(new FilenameFilter() {
            public boolean accept(File directory, String fileName) {
                return fileName.endsWith(".txt");
            }
        });
        // A way to avoid null pointer exception.
        if(files != null){
            TarjanAlgorithm tarj = new TarjanAlgorithm();
            for (final File fileEntry : files) {
                try {
                    graph = DirectedGraphReader.fromFile(fileEntry.getPath());

                    System.out.println(graph + " in file " + fileEntry.getName());

                    System.out.println("Number of components : " + tarj.nbConnectedComponents(graph));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
