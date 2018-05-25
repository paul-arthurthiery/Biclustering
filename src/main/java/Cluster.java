import java.util.ArrayList;
import java.util.List;

public class Cluster {
    private List<List<Flag>> listOfClusters;

    public Cluster() {
        this.listOfClusters = new ArrayList<List<Flag>>();
        for(int i=0; i<6; i++){
            ArrayList<Flag> cluster = new ArrayList<Flag>();
            listOfClusters.add(cluster);
        }
    }

    public List<List<Flag>> getLandmassClusters(Flag[] listOfFlags) {
        for(int i=0; i<listOfFlags.length; i++){
            this.listOfClusters.get(listOfFlags[i].landmass-1).add(listOfFlags[i]);
        }
        return this.listOfClusters;
    }
}
