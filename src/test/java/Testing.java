import java.io.IOException;
import java.util.List;

public class Testing
{
    public static void main(String[] args) throws IOException
    {

        FileProcessor processor = new FileProcessor();
        Flag[] flags = processor.processFile("flag.txt");
        ClusterTool clusterer = new ClusterTool();
        List<List<Flag>> result = clusterer.getLandmassClusters(flags);
        for (int i=0; i<result.size(); i++) {
            System.out.print("[");
            for(int j=0; j<result.get(i).size(); j++){
                System.out.print(result.get(i).get(j).landmass + ", ");
            }
            System.out.println("]");
        }

    }

}
