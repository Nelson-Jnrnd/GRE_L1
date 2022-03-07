import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class TarjanAlgorithm {
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

                    System.out.println(graph);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
