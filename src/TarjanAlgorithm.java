import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Stack;

public class TarjanAlgorithm {
    private final DirectedGraph graph;
    private final int[] scc;
    private final int[] dfsnum;
    private final int[] low;
    private int dfsnumCounter;
    private int conectedComponentCounter;
    private Stack<Integer> visitedVertices;


    public TarjanAlgorithm(DirectedGraph graph) {
        this.graph = graph;
        this.scc = new int[graph.getNVertices()];
        this.dfsnum = new int[scc.length];
        this.low = new int[scc.length];
        visitedVertices = new Stack<>();
    }

    public int[] sccTarjan() {
        // For each vertex in graph
        for (int vertex = 0; vertex < scc.length; vertex++) { // TODO potentiellement intutile -> array init at 0
            scc[vertex] = 0;
            dfsnum[vertex] = 0;
        }

        dfsnumCounter = 0;
        conectedComponentCounter = 0;
        visitedVertices.empty();

        for (int vertex = 0; vertex < scc.length; vertex++) {
            if(scc[vertex] == 0)
                scc(vertex);
        }
        return scc.clone();
    }

    private void scc(Integer vertex) {
        dfsnum[vertex] = ++dfsnumCounter;
        low[vertex] = dfsnumCounter;

        visitedVertices.push(vertex);
        for (int successor: graph.getSuccessorList(vertex)) {
            if(dfsnum[successor] == 0) // Not yet visited
                scc(successor);
            if(scc[successor] == 0)
                low[vertex] = Math.min(low[vertex], low[successor]);
        }
        if(low[vertex] == dfsnum[vertex]){ // pop until we get to vertex
            conectedComponentCounter++;
            while(true){ // TODO maybe faut pas désempiller quand pop = vertex
                Integer removedVertex = visitedVertices.pop();
                scc[removedVertex] = conectedComponentCounter;
                if(removedVertex.equals(vertex)){
                    break;
                }
            }
        }
    }


    public static void main(String[] args) {
        if(args.length != 1){
            throw new IllegalArgumentException("Exactly 1 parameter (graphs folder path) is required !");
        }
        File folder = new File(args[0]);

        DirectedGraph graph;

        File[] files = folder.listFiles(new FilenameFilter() {
            public boolean accept(File directory, String fileName) {
                return fileName.endsWith(".txt");
            }
        });
        if(files != null){
            for (final File fileEntry : files) {
                try {

                    graph = DirectedGraphReader.fromFile(fileEntry.getPath());

                    System.out.println(graph + " in file " + fileEntry.getName());
                    TarjanAlgorithm tarj = new TarjanAlgorithm(graph);
                    int[] composants = tarj.sccTarjan();

                    int vertex = 0;
                    for (int composant: composants) {
                        System.out.println("Vertex " + vertex++ + " in composant -> " + composant);
                    }
                    break; // TODO remove
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
