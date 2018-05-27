import java.lang.reflect.Array;
import java.util.*;

import static java.lang.Math.abs;

public class ClusterTool {
    private Flag[] listOfFlags;
    private int numberOfClusters;

    public ClusterTool(Flag[] listOfFlags, int numberOfClusters) {
        this.listOfFlags = listOfFlags;
        this.numberOfClusters = numberOfClusters;
    }


    public List<List<Flag>> biCluster()
    {
        Flag[] singleClusteredList = this.listOfFlags.clone();
        //TODO DONE : List<List<List<Integer>>> distances = new ArrayList de matrice pour chaque cluster;
        //TODO DONE : fill matrix with distance between each element in cluster (start by doing the absolute value of the difference between each variable of each Flag in the cluster)

        long millisStart = Calendar.getInstance().getTimeInMillis();
        // loops through all the clusters and all the flags intra-cluster to compare flags one by one,
        // and store an array with the distances between each

        int numberOfFlags = this.listOfFlags.length;
        int[] oneDimensionArray = new int[(numberOfFlags*(numberOfFlags-1))/2];
        int[][] flagPairs = new int[oneDimensionArray.length][2];
        //TODO : add average distance with every other flag for each flag
        int[][] distances = new int[singleClusteredList.length][singleClusteredList.length];
        int counter = 0;
        for(int i=0; i<singleClusteredList.length;i++)
        {
            Flag flagOne = singleClusteredList[i];
            distances[i][i] = 0;
            for(int j=i+1; j<singleClusteredList.length;j++)
            {
                Flag flagTwo = singleClusteredList[j];
                distances[i][j] = distances[j][i] = oneDimensionArray[counter] = compareTwoFlags(flagOne, flagTwo);
                flagPairs[counter][0] = i;
                flagPairs[counter][1] = j;
                counter++;
            }
        }

        long millisEnd = Calendar.getInstance().getTimeInMillis();
        System.out.println("Distance Done Time : "+(millisEnd-millisStart));

        //TODO DONE : create ArrayList of ArrayLists of Flag
        List<List<Flag>> biClusteredFlags = new ArrayList<List<Flag>>();

        //TODO : remove 5-10% of flags with the lowest/highest average distance (removes the aberrant values)
        //TODO : create n arralists in biClusteredFlags. Add to each one of the n remaining flags with the largest distance
        //TODO : create a method which takes in parameter the array of distances and the current list of clusters
        //for each cluster, it will add the closest flag to the first flag in the array. if two clusters have the same next closest flag, it will go to the cluster with the shortest distance (either for the first flag or the whole array if i can)

        ArrayIndexComparator comparator = new ArrayIndexComparator(oneDimensionArray);
        Integer[] indexes = comparator.createIndexArray();
        Arrays.sort(indexes, comparator);

        int position = (indexes.length/2)-1; // position of medianIndex in indexes array
        int medianIndex = indexes[position]; // value of the index of the median
        int medianDistance = oneDimensionArray[medianIndex];
        // percentage to keep, centered on the median (half above and under)
        float percentageToKeep = 50;
        int bottomIndex = (int)((position)-((percentageToKeep/200.0)*indexes.length));
        int topIndex = (int)((position)+((percentageToKeep/200.0)*indexes.length));
        int[] flagsToKeep = Arrays.copyOfRange(oneDimensionArray, bottomIndex,topIndex+1);
        
        //#### INFO : to know what pair of flags is at an index, just do :
        //########### --> flagPairs[index]  // returns int[flagOne,flagTwo]

        //TODO : next find the n best distances to create the clusters



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

    public class ArrayIndexComparator implements Comparator<Integer>
    {
        private final int[] array;

        public ArrayIndexComparator(int[] array)
        {
            this.array = array;
        }

        public Integer[] createIndexArray()
        {
            Integer[] indexes = new Integer[array.length];
            for (int i = 0; i < array.length; i++)
            {
                indexes[i] = i;
            }
            return indexes;
        }

        public int compare(Integer index1, Integer index2)
        {
            return array[index1] <= array[index2] ? (array[index1] < array[index2] ? -1 : 0) : 1;
        }
    }


}
