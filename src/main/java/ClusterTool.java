import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static java.lang.Math.abs;

public class ClusterTool {
    private Flag[] listOfFlags;

    public ClusterTool(Flag[] listOfFlags) {
        this.listOfFlags = listOfFlags;
    }


    public List<List<Flag>> biCluster()
    {
        Flag[] singleClusteredList = this.listOfFlags;
        //TODO DONE : List<List<List<Integer>>> distances = new ArrayList de matrice pour chaque cluster;
        //TODO DONE : fill matrix with distance between each element in cluster (start by doing the absolute value of the difference between each variable of each Flag in the cluster)

        long millisStart = Calendar.getInstance().getTimeInMillis();
        // loops through all the clusters and all the flags intra-cluster to compare flags one by one,
        // and store an array with the distances between each
        int[][] distances = new int[singleClusteredList.length][singleClusteredList.length];
        for(int i=0; i<singleClusteredList.length;i++)
        {
            Flag flagOne = singleClusteredList[i];
            distances[i][i] = 0;
            for(int j=i+1; j<singleClusteredList.length;j++)
            {
                Flag flagTwo = singleClusteredList[j];
                distances[i][j] = distances[j][i] = compareTwoFlags(flagOne, flagTwo);
            }
        }

        long millisEnd = Calendar.getInstance().getTimeInMillis();
        System.out.println("Distance Done Time : "+(millisEnd-millisStart));

        //TODO DONE : create ArrayList of ArrayLists of Flag
        List<List<Flag>> biClusteredFlags = new ArrayList<List<Flag>>();


        //TODO : for each group of flags in cluster with a distance below the threshold, create a new cluster
        //TODO : add all these flags to the cluster
        //TODO : return this 2nd arraylist
        // Comment déterminer un threshold ?
        // Nombre de clusters minimal ? 2 maybe ?
        // Nombre de paramètres minimum pour former un cluster ?
        // Regrouper en catégorie "nbr de critères similaires" ?
        // Créer une note de "matching" et regrouper ensuite ?


        return biClusteredFlags;
    }

    // method to compare attributes of two flags, returns the difference in an array
    private int compareTwoFlags(Flag flagOne, Flag flagTwo)
    {
        int distance = 0;
        distance += stringCompare(flagOne.name,flagTwo.name);
        distance += nonSubtractionCompare(flagOne.landmass,flagTwo.landmass);
        distance += nonSubtractionCompare(flagOne.zone,flagTwo.zone);
        distance += subtractionCompare(flagOne.area, flagTwo.area);
        distance += subtractionCompare(flagOne.population, flagTwo.population);
        distance += nonSubtractionCompare(flagOne.language,flagTwo.language);
        distance += nonSubtractionCompare(flagOne.religion,flagTwo.religion);
        distance += subtractionCompare(flagOne.bars,flagTwo.bars);
        distance += subtractionCompare(flagOne.stripes, flagTwo.stripes);
        distance += subtractionCompare(flagOne.colours, flagTwo.colours);
        distance += nonSubtractionCompare(flagOne.red, flagTwo.red);
        distance += nonSubtractionCompare(flagOne.green, flagTwo.green);
        distance += nonSubtractionCompare(flagOne.blue, flagTwo.blue);
        distance += nonSubtractionCompare(flagOne.gold, flagTwo.gold);
        distance += nonSubtractionCompare(flagOne.white, flagTwo.white);
        distance += nonSubtractionCompare(flagOne.black, flagTwo.black);
        distance += nonSubtractionCompare(flagOne.orange, flagTwo.orange);
        distance += stringCompare(flagOne.mainhue,flagTwo.mainhue);
        distance += subtractionCompare(flagOne.circles, flagTwo.circles);
        distance += subtractionCompare(flagOne.crosses, flagTwo.crosses);
        distance += subtractionCompare(flagOne.saltires, flagTwo.saltires);
        distance += subtractionCompare(flagOne.quarters, flagTwo.quarters);
        distance += subtractionCompare(flagOne.sunstars, flagTwo.sunstars);
        distance += nonSubtractionCompare(flagOne.crescent, flagTwo.crescent);
        distance += nonSubtractionCompare(flagOne.triangle, flagTwo.triangle);
        distance += nonSubtractionCompare(flagOne.icon, flagTwo.icon);
        distance += nonSubtractionCompare(flagOne.animate, flagTwo.animate);
        distance += nonSubtractionCompare(flagOne.text, flagTwo.text);
        distance += stringCompare(flagOne.topleft,flagTwo.topleft);
        distance += stringCompare(flagOne.botright,flagTwo.botright);
        return distance;
    }


    // method to compare two String, return 0 if identical, 1 otherwise
    private int stringCompare(String a, String b)
    {
        return a.matches(b) ? 0 : 100;
    }

    //method to compare 2 ints, when subtraction doesn't represent closeness. Same output as above
    private int nonSubtractionCompare(int a, int b){
        return a == b ? 0: 100;
    }

    //method to compare 2 ints, when subtraction represents closeness.
    private int subtractionCompare(int a, int b){
        if(a == b) return 0;
        int max = a>b ? a : b;
        return (abs(a-b)/max)*100;
    }


}
