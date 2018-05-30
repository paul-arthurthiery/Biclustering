import java.io.IOException;
import java.util.List;

public class Testing
{
    public static void main(String[] args) throws IOException
    {

        // Disclaimer : as the dataset is from 1990, and the flags images more recent, the country names or the flags
        // may have changed, hence some pairs may not correspond.

        float percentageToKeep = 80; // percentage to keep, centered on the median (half above and under)
        //Percentage : Max clusters / 40%:8 / 50%:10 / 60%:20 / 70%:22 / 80%:32
        int numberOfClusters = 6;

        FileProcessor processor = new FileProcessor();
        Flag[] flags = processor.processFile("flag.txt");

        ClusterTool clusterer = new ClusterTool(flags, numberOfClusters, percentageToKeep);
        List<List<Flag>> result = clusterer.cluster();
        clusterer.displayClustersFlags(result);

    }

}
