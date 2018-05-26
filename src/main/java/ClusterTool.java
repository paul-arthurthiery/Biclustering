import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static java.lang.Math.abs;

public class ClusterTool {
    private List<List<Flag>> listOfClusters, oneCluster;
    private Flag[] listOfFlags;

    public ClusterTool(Flag[] listOfFlags) {
        this.listOfFlags = listOfFlags;
        this.listOfClusters = new ArrayList<List<Flag>>();
        this.oneCluster = new ArrayList<List<Flag>>();
        this.oneCluster.add(new ArrayList<Flag>());
        for(int i=0; i<6; i++){
            ArrayList<Flag> cluster = new ArrayList<Flag>();
            listOfClusters.add(cluster);
        }
    }

    //generate a first list of clusters by grouping all flags from similar continents
    public List<List<Flag>> getLandmassClusters() {
        for(int i=0; i<this.listOfFlags.length; i++){
            this.listOfClusters.get(this.listOfFlags[i].landmass-1).add(this.listOfFlags[i]);
        }
        return this.listOfClusters;
    }

    // groups all the flags into one cluster
    public List<List<Flag>> getOneCluster()
    {
        for(Flag flag : this.listOfFlags) {
            this.oneCluster.get(0).add(flag);
        }
        return this.oneCluster;
    }

    public List<List<Flag>> biCluster(List<List<Flag>> singleClusteredList)
    {
        //TODO DONE : List<List<List<Integer>>> distances = new ArrayList de matrice pour chaque cluster;
        //TODO DONE : fill matrix with distance between each element in cluster (start by doing the absolute value of the difference between each variable of each Flag in the cluster)

        long millisStart = Calendar.getInstance().getTimeInMillis();
        // loops through all the clusters and all the flags intra-cluster to compare flags one by one,
        // and store an array with the distances between each
        List<int[][]> distances = new ArrayList<int[][]>();
        for(List<Flag> cluster : singleClusteredList)
        {
            int[][] clusterMatrix = new int[cluster.size()][cluster.size()];
            for(int i=0; i<cluster.size();i++)
            {
                Flag flagOne = cluster.get(i);
                clusterMatrix[i][i] = 0;
                for(int j=i+1; j<cluster.size();j++)
                {
                    Flag flagTwo = cluster.get(j);
                    clusterMatrix[i][j] = clusterMatrix[j][i] = compareTwoFlags(flagOne, flagTwo);
                }
            }
            distances.add(clusterMatrix);
        }
        long millisEnd = Calendar.getInstance().getTimeInMillis();
        System.out.println("Distance Done Time : "+(millisEnd-millisStart));

        //TODO DONE : create ArrayList of ArrayLists of Flag
        List<List<Flag>> reClusteredFlags = new ArrayList<List<Flag>>();

        //TODO : for each group of flags in cluster with a distance below the threshold, create a new cluster
        //TODO : add all these flags to the cluster
        //TODO : return this 2nd arraylist
        // Comment déterminer un threshold ?
        // Nombre de clusters minimal ? 2 maybe ?
        // Nombre de paramètres minimum pour former un cluster ?
        // Regrouper en catégorie "nbr de critères similaires" ?
        // Créer une note de "matching" et regrouper ensuite ?
        



        return reClusteredFlags;
    }

    // method to compare attributes of two flags, returns the difference in an array
    private int compareTwoFlags(Flag flagOne, Flag flagTwo)
    {
        int distance = 0;
        distance += (abs(stringCompare(flagOne.name,flagTwo.name)));
        distance += (abs(flagOne.landmass-flagTwo.landmass));
        distance += (abs(flagOne.zone-flagTwo.zone));
        distance += (abs(flagOne.area-flagTwo.area));
        distance += (abs(flagOne.population-flagTwo.population));
        distance += (abs(flagOne.language-flagTwo.language));
        distance += (abs(flagOne.religion-flagTwo.religion));
        distance += (abs(flagOne.bars-flagTwo.bars));
        distance += (abs(flagOne.stripes-flagTwo.stripes));
        distance += (abs(flagOne.colours-flagTwo.colours));
        distance += (abs(flagOne.red-flagTwo.red));
        distance += (abs(flagOne.green-flagTwo.green));
        distance += (abs(flagOne.blue-flagTwo.blue));
        distance += (abs(flagOne.gold-flagTwo.gold));
        distance += (abs(flagOne.white-flagTwo.white));
        distance += (abs(flagOne.black-flagTwo.black));
        distance += (abs(flagOne.orange-flagTwo.orange));
        distance += (abs(stringCompare(flagOne.mainhue,flagTwo.mainhue)));
        distance += (abs(flagOne.circles-flagTwo.circles));
        distance += (abs(flagOne.crosses-flagTwo.crosses));
        distance += (abs(flagOne.saltires-flagTwo.saltires));
        distance += (abs(flagOne.quarters-flagTwo.quarters));
        distance += (abs(flagOne.sunstars-flagTwo.sunstars));
        distance += (abs(flagOne.crescent-flagTwo.crescent));
        distance += (abs(flagOne.triangle-flagTwo.triangle));
        distance += (abs(flagOne.icon-flagTwo.icon));
        distance += (abs(flagOne.animate-flagTwo.animate));
        distance += (abs(flagOne.text-flagTwo.text));
        distance += (abs(stringCompare(flagOne.topleft,flagTwo.topleft)));
        distance += (abs(stringCompare(flagOne.botright,flagTwo.botright)));
        return distance;
    }


    // method to compare two String, return 0 if identic, 1 otherwise
    private int stringCompare(String a, String b)
    {
        return a.matches(b) ? 0 : 1;
    }



}
