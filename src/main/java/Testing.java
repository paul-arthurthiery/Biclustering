import java.io.IOException;
import java.util.List;

public class Testing
{
    public static void main(String[] args) throws IOException
    {

        FileProcessor processor = new FileProcessor();
        Flag[] flags = processor.processFile("flag.txt");
        ClusterTool clusterer = new ClusterTool(flags);
        List<List<Flag>> result = clusterer.getLandmassClusters();
        List<List<Flag>> oneCluster = clusterer.getOneCluster();
        clusterer.biCluster(result);
        System.out.println("###");
        clusterer.biCluster(oneCluster);


    }

}
