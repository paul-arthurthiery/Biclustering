import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Testing
{
    public static void main(String[] args) throws IOException
    {

        FileProcessor processor = new FileProcessor();
        Flag[] flags = processor.processFile("flag.txt");
        ClusterTool clusterer = new ClusterTool(flags, 6);
        clusterer.biCluster();

    }

}
