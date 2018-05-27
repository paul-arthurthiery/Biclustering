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
        int[] arrayAllPairDistances = new int[(numberOfFlags*(numberOfFlags-1))/2];
        int[][] flagPairs = new int[arrayAllPairDistances.length][2];
        //TODO DONE: add average distance with every other flag for each flag
        int[][] distances = new int[singleClusteredList.length][singleClusteredList.length];
        int[] averageDistancePerFlag = new int[listOfFlags.length];
        int counter = 0;
        for(int i=0; i<singleClusteredList.length;i++)
        {
            Flag flagOne = singleClusteredList[i];
            distances[i][i] = 0;
            for(int j=i+1; j<singleClusteredList.length;j++)
            {
                Flag flagTwo = singleClusteredList[j];
                distances[i][j] = distances[j][i] = arrayAllPairDistances[counter] = compareTwoFlags(flagOne, flagTwo);
                averageDistancePerFlag[i] += distances[i][j];
                averageDistancePerFlag[j] += distances[i][j];
                flagPairs[counter][0] = i;
                flagPairs[counter][1] = j;
                counter++;
            }
            averageDistancePerFlag[i] /= listOfFlags.length;
        }

        long millisEnd = Calendar.getInstance().getTimeInMillis();
        System.out.println("Distance Done Time : "+(millisEnd-millisStart));

        //TODO DONE: remove 20% of flags with the lowest/highest average distance compared to median value (removes the aberrant values)
        millisStart = Calendar.getInstance().getTimeInMillis();

        ArrayIndexComparator comparator = new ArrayIndexComparator(arrayAllPairDistances);
        Integer[] indexes = comparator.createIndexArray();
        Arrays.sort(indexes, comparator);

        int position = (indexes.length/2)-1; // position of medianIndex in indexes array
        int medianIndex = indexes[position]; // value of the index of the median
        int medianDistance = arrayAllPairDistances[medianIndex];
        // percentage to keep, centered on the median (half above and under)
        float percentageToKeep = 80;
        int bottomIndex = (int)((position)-((percentageToKeep/200.0)*indexes.length));
        int topIndex = (int)((position)+((percentageToKeep/200.0)*indexes.length));
        Integer[] indexesOfFlagsToKeep = Arrays.copyOfRange(indexes, bottomIndex,topIndex+1);

        millisEnd = Calendar.getInstance().getTimeInMillis();
        System.out.println("Sorting and Median Done Time : " +(millisEnd-millisStart));

        millisStart = Calendar.getInstance().getTimeInMillis();

        //#### INFO : to know what pair of flags is at an index, just do :
        //########### --> flagPairs[index]  // returns int[flagOne,flagTwo]
        //TODO DONE : create ArrayList of ArrayLists of Flag
        List<List<Flag>> biClusteredFlags = new ArrayList<List<Flag>>();
        //TODO DONE: next find the n best distances to create the clusters
        //TODO DONE: create n ArrayLists in biClusteredFlags. Add to each one of the n remaining flags with the largest pair distance but the smallest average distance
        Stack remainingFlagsStack = new Stack<Flag>();
        remainingFlagsStack.addAll(Arrays.asList(listOfFlags));
        for(int k=indexesOfFlagsToKeep.length-1; k>indexesOfFlagsToKeep.length-(1+numberOfClusters); k--){
            ArrayList<Flag> cluster = new ArrayList<Flag>();
            int[] largeDistancePair = flagPairs[indexesOfFlagsToKeep[k]];
            Flag flagToAdd = averageDistancePerFlag[largeDistancePair[0]] <= averageDistancePerFlag[largeDistancePair[1]] ? listOfFlags[largeDistancePair[0]] : listOfFlags[largeDistancePair[1]];
            cluster.add(flagToAdd);
            remainingFlagsStack.remove(flagToAdd);
            biClusteredFlags.add(cluster);
        }

        millisEnd = Calendar.getInstance().getTimeInMillis();
        System.out.println("First Element Clusters Done Time : " +(millisEnd-millisStart));


        //TODO DONE : create a method which takes in parameter the array of distances, the current list of clusters, and a stack of remaining Flags
        //for each cluster, it will add the closest flag to the first flag in the array

        millisStart = Calendar.getInstance().getTimeInMillis();
        biClusteredFlags = biClusterFromFirstElements(biClusteredFlags, distances, remainingFlagsStack);
        millisEnd = Calendar.getInstance().getTimeInMillis();
        System.out.println("Final Biclustering done time : " +(millisEnd-millisStart));

        printBicluster(biClusteredFlags);

        return biClusteredFlags;
    }

    // method to compare attributes of two flags, returns the difference in an array
    private int compareTwoFlags(Flag flagOne, Flag flagTwo) {
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

    public class ArrayIndexComparator implements Comparator<Integer> {
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

    public List<List<Flag>> biClusterFromFirstElements(List<List<Flag>> biclusters, int[][] distances, Stack<Flag> remainingFlagsStack){
        List<Flag> arrayListOfFlags = Arrays.asList(listOfFlags);
        while(!remainingFlagsStack.empty()){
            Flag currentFlag = remainingFlagsStack.pop();
            int clusterToAddFlagIndex = numberOfClusters-1;
            int currentFlagIndex = arrayListOfFlags.indexOf(currentFlag);
            int currentDistance = distances[arrayListOfFlags.indexOf(biclusters.get(numberOfClusters-1).get(0))][currentFlagIndex];
            for(int j=numberOfClusters-1; j>-1; j--){
                int newDistance = distances[arrayListOfFlags.indexOf(biclusters.get(j).get(0))][currentFlagIndex];
                if(currentDistance > newDistance) {
                    currentDistance = newDistance;
                    clusterToAddFlagIndex = j;
                }
            }
            biclusters.get(clusterToAddFlagIndex).add(currentFlag);
        }
        return biclusters;
    }

    public void printBicluster(List<List<Flag>> biClusteredFlags){
        for(int i=0; i<biClusteredFlags.size();i++){
            System.out.println(biClusteredFlags.get(i));
        }
    }


}
