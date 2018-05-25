import java.util.ArrayList;
import java.util.List;

public class ClusterTool {
    private List<List<Flag>> listOfClusters;
    private Flag[] listOfFlags;

    public ClusterTool(Flag[] listOfFlags) {
        this.listOfFlags = listOfFlags;
        this.listOfClusters = new ArrayList<List<Flag>>();
        for(int i=0; i<6; i++){
            ArrayList<Flag> cluster = new ArrayList<Flag>();
            listOfClusters.add(cluster);
        }
    }

    public List<List<Flag>> getLandmassClusters() {
        for(int i=0; i<this.listOfFlags.length; i++){
            this.listOfClusters.get(this.listOfFlags[i].landmass-1).add(this.listOfFlags[i]);
        }
        return this.listOfClusters;
    }

    //public List<List<Flag>> biCluster(List<List<Flag>> singleClusteredList) {
        //TODO : List<List<List<Integer>>> distances = new ArrayList de matrice pour chaque cluster;
        //TODO : fill matrix with distance between each element in cluster (start by doing the absolute value of the difference between each variable of each Flag in the cluster)
        //TODO : create ArrayList of ArrayLists of Flag
        //for each group of flags in cluster with a distance below the threshold, create a new cluster
        //add all these flags to the cluster
        //return this 2d arraylist
    //}
}
