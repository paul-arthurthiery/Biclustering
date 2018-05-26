import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static java.lang.Math.abs;

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

    public List<List<Flag>> biCluster(List<List<Flag>> singleClusteredList)
    {
        //TODO : List<List<List<Integer>>> distances = new ArrayList de matrice pour chaque cluster;
        //TODO : fill matrix with distance between each element in cluster (start by doing the absolute value of the difference between each variable of each Flag in the cluster)
        //TODO : create ArrayList of ArrayLists of Flag
        //for each group of flags in cluster with a distance below the threshold, create a new cluster
        //add all these flags to the cluster
        //return this 2d arraylist

        long millisStart = Calendar.getInstance().getTimeInMillis();

        List<List<List<Integer>>> distances = new ArrayList<List<List<Integer>>>();
        for(List<Flag> cluster : singleClusteredList)
        {
            List<List<Integer>> clusterMatrix = new ArrayList<List<Integer>>();
            for(int i=0; i<cluster.size();i++)
            {
                Flag flagOne = cluster.get(i);
                for(int j=i+1; j<cluster.size();j++)
                {
                    List<Integer> twoFlagsDistance = new ArrayList<Integer>();
                    Flag flagTwo = cluster.get(j);
                    clusterMatrix.add(compareTwoFlags(flagOne, flagTwo));
                }
            }
            distances.add(clusterMatrix);
        }

        long millisEnd = Calendar.getInstance().getTimeInMillis();

        System.out.println("Distance Done Time : "+(millisEnd-millisStart));

        return null;
    }

    private List<Integer> compareTwoFlags(Flag flagOne, Flag flagTwo)
    {
        List<Integer> distances = new ArrayList<Integer>();
        distances.add(abs(stringCompare(flagOne.name,flagTwo.name)));
        distances.add(abs(flagOne.landmass-flagTwo.landmass));
        distances.add(abs(flagOne.zone-flagTwo.zone));
        distances.add(abs(flagOne.area-flagTwo.area));
        distances.add(abs(flagOne.population-flagTwo.population));
        distances.add(abs(flagOne.language-flagTwo.language));
        distances.add(abs(flagOne.religion-flagTwo.religion));
        distances.add(abs(flagOne.bars-flagTwo.bars));
        distances.add(abs(flagOne.stripes-flagTwo.stripes));
        distances.add(abs(flagOne.colours-flagTwo.colours));
        distances.add(abs(flagOne.red-flagTwo.red));
        distances.add(abs(flagOne.green-flagTwo.green));
        distances.add(abs(flagOne.blue-flagTwo.blue));
        distances.add(abs(flagOne.gold-flagTwo.gold));
        distances.add(abs(flagOne.white-flagTwo.white));
        distances.add(abs(flagOne.black-flagTwo.black));
        distances.add(abs(flagOne.orange-flagTwo.orange));
        distances.add(abs(stringCompare(flagOne.mainhue,flagTwo.mainhue)));
        distances.add(abs(flagOne.circles-flagTwo.circles));
        distances.add(abs(flagOne.crosses-flagTwo.crosses));
        distances.add(abs(flagOne.saltires-flagTwo.saltires));
        distances.add(abs(flagOne.quarters-flagTwo.quarters));
        distances.add(abs(flagOne.sunstars-flagTwo.sunstars));
        distances.add(abs(flagOne.crescent-flagTwo.crescent));
        distances.add(abs(flagOne.triangle-flagTwo.triangle));
        distances.add(abs(flagOne.icon-flagTwo.icon));
        distances.add(abs(flagOne.animate-flagTwo.animate));
        distances.add(abs(flagOne.text-flagTwo.text));
        distances.add(abs(stringCompare(flagOne.topleft,flagTwo.topleft)));
        distances.add(abs(stringCompare(flagOne.botright,flagTwo.botright)));
        return distances;
    }


    // method to compare two String, return 0 if identic, 1 otherwise
    private int stringCompare(String a, String b)
    {
        return a.matches(b) ? 0 : 1;
    }



}
