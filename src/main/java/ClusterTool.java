import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

import static java.lang.Math.abs;

public class ClusterTool
{
    private Flag[] listOfFlags;
    private int numberOfClusters;

    public ClusterTool(Flag[] listOfFlags, int numberOfClusters) {
        this.listOfFlags = listOfFlags;
        this.numberOfClusters = numberOfClusters;
    }

    // main method to pilot all the biclustering process
    public List<List<Flag>> biCluster()
    {
        Flag[] singleClusteredList = this.listOfFlags.clone();

        long millisStart = Calendar.getInstance().getTimeInMillis();
        // loops through all the clusters and all the flags intra-cluster to compare flags one by one,
        // and store an array with the distances between each

        int numberOfFlags = this.listOfFlags.length;
        int[] arrayAllPairDistances = new int[(numberOfFlags*(numberOfFlags-1))/2]; //one dimension array with all the distances of the possible pairs of flags
        int[][] flagPairs = new int[arrayAllPairDistances.length][2]; //all the possible pairs of flags
        int[][] distances = new int[singleClusteredList.length][singleClusteredList.length];//all pairs distances in 2D
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

        millisStart = Calendar.getInstance().getTimeInMillis();

        ArrayIndexComparator comparator = new ArrayIndexComparator(arrayAllPairDistances);
        Integer[] indexes = comparator.createIndexArray();
        Arrays.sort(indexes, comparator);

        int position = (indexes.length/2)-1; // position of medianIndex in indexes array
        int medianIndex = indexes[position]; // value of the index of the median
        float percentageToKeep = 60; // percentage to keep, centered on the median (half above and under)
        //Percentage : Max clusters / 40%:8 / 50%:10 / 60%:20 / 70%:22 / 80%:32
        // 60% and 15 clusters is nice, more tests to do
        int bottomIndex = (int)((position)-((percentageToKeep/200.0)*indexes.length));
        int topIndex = (int)((position)+((percentageToKeep/200.0)*indexes.length));
        Integer[] indexesOfFlagsToKeep = Arrays.copyOfRange(indexes, bottomIndex,topIndex+1);

        millisEnd = Calendar.getInstance().getTimeInMillis();
        System.out.println("Sorting and Median Done Time : " +(millisEnd-millisStart));

        //millisStart = Calendar.getInstance().getTimeInMillis();

        /*#### INFO : to know what pair of flags is at an index, just do :
        ########### --> flagPairs[index]  // returns int[flagOne,flagTwo] */

        List<List<Flag>> biClusteredFlags = new ArrayList<List<Flag>>();
        Stack remainingFlagsStack = new Stack<Flag>();
        remainingFlagsStack.addAll(Arrays.asList(listOfFlags));

        /*//########################### OLD ALGO #############################################
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
        //###################################################################################*/

        //########################### NEW ALGO #############################################
        millisStart = Calendar.getInstance().getTimeInMillis();
        Object[] firstClusteringResult = findFirstClustersFlags(remainingFlagsStack,indexesOfFlagsToKeep,flagPairs,distances);
        biClusteredFlags = (List<List<Flag>>) firstClusteringResult[0];
        remainingFlagsStack = (Stack) firstClusteringResult[1];
        millisEnd = Calendar.getInstance().getTimeInMillis();
        System.out.println("Custom Spicy First Clusters Done Time : " +(millisEnd-millisStart));
        //###################################################################################

        //for each cluster, it will add the closest flag to the first flag in the array
        millisStart = Calendar.getInstance().getTimeInMillis();
        biClusteredFlags = biClusterFromFirstElements(biClusteredFlags, distances, remainingFlagsStack);
        millisEnd = Calendar.getInstance().getTimeInMillis();
        System.out.println("Final Biclustering done time : " +(millisEnd-millisStart));

        printBicluster(biClusteredFlags);

        return biClusteredFlags;
    }

    // method to display the flags of the result of the bicluster
    public void displayClustersFlags(List<List<Flag>> clusters)
    {
        int counter = 1;
        for(List<Flag> cluster : clusters)
        {
            String title = "Cluster "+counter;
            JPanel pane = new JPanel();
            for(Flag flag : cluster)
            {
                String flagName = flag.name;
                flagName = flagName.replaceAll("-"," ");
                String src = "flags/Flag of "+flagName+".gif";
                ImageIcon icon = new ImageIcon(src);
                icon = resizeIcon(icon, 150, 118);
                JLabel label = new JLabel(icon);
                label.setText(flagName);
                label.setHorizontalTextPosition(JLabel.CENTER);
                label.setVerticalTextPosition(JLabel.BOTTOM);
                pane.add(label);
            }
            showFrame(pane,title);
            counter++;
        }
    }
    // resizes an ImageIcon
    private ImageIcon resizeIcon(ImageIcon icon, int width, int height)
    {
        Image img = icon.getImage();
        Image newimg = img.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(newimg);
    }
    // shows the frame with the flags
    private void showFrame(JPanel pane, String title)
    {
        JFrame mainframe = new JFrame(title);
        mainframe.getContentPane().add(pane);
        mainframe.setBounds(0, 0, 1000, 1000);
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.setVisible(true);
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
    /* custom comparator to sort an array of indexes (generated from an initial array)
     instead of sorting the initial array */
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
    // dispatches the remaining flags into the clusters they are closest to
    public List<List<Flag>> biClusterFromFirstElements(List<List<Flag>> biclusters, int[][] distances, Stack<Flag> remainingFlagsStack){
        List<Flag> arrayListOfFlags = Arrays.asList(this.listOfFlags);
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
    //prints the result of the biclustering
    public void printBicluster(List<List<Flag>> biClusteredFlags){
        int num = 0;
        for(int i=0; i<biClusteredFlags.size();i++){
            num+=biClusteredFlags.get(i).size();
            System.out.println(biClusteredFlags.get(i));
        }
        System.out.println("Number of Countries : "+num);
    }
    // method to call to generate the clusters with a first flag inside
    private Object[] findFirstClustersFlags(Stack remainingFlagsStack, Integer[] indexesOfFlagsToKeep, int[][] flagPairs, int[][] distances)
    {
        Object[] toReturn = new Object[2];
        if(this.numberOfClusters==2)
        {
            toReturn = findFirstClustersForTwo(indexesOfFlagsToKeep, flagPairs, remainingFlagsStack, toReturn);
        }
        else
        {
            toReturn = findFirstClustersForMore(remainingFlagsStack, flagPairs, indexesOfFlagsToKeep, distances, toReturn);
        }
        return toReturn;
    }
    // called from upper method to do what said above, called if the number of clusters requested is > 2
    private Object[] findFirstClustersForMore(Stack remainingFlagsStack, int[][] flagPairs, Integer[] indexesOfFlagsToKeep, int[][] distances, Object[] toReturn)
    {
        int[][] matrixOfFlagsToKeep = new int[this.listOfFlags.length][this.listOfFlags.length];
        for(int index : indexesOfFlagsToKeep)
        {
            int[] pair = flagPairs[index];
            matrixOfFlagsToKeep[pair[0]][pair[1]] = 1;
        }

        List<List<Flag>> biClusteredFlags = new ArrayList<List<Flag>>();
        ArrayList<int[]> pairs = new ArrayList<int[]>();
        int horizontalMoving = this.numberOfClusters-2;
        horizontalMoving = this.numberOfClusters==3 ? 2 : (this.numberOfClusters==2 ? 1 : horizontalMoving);
        int verticalMoving = 2;
        verticalMoving = this.numberOfClusters==2 || this.numberOfClusters==3 ? 1 : verticalMoving;
        int vertical = 0;
        ArrayList<ArrayList<Integer>> possibilities = new ArrayList<ArrayList<Integer>>();
        while(vertical<matrixOfFlagsToKeep.length)
        {
            for(int horizontal=0; horizontal<matrixOfFlagsToKeep.length-horizontalMoving;horizontal++)
            {
                ArrayList<Integer> verticalFlags = new ArrayList<Integer>();
                ArrayList<Integer> horizontalFlags = new ArrayList<Integer>();
                ArrayList<int[]> thepairs = new ArrayList<int[]>();
                int cancelToken = 0;
                if(horizontal<2&&vertical<2){}
                else if(horizontal!=vertical)
                {
                    for (int i = 0; i < horizontalMoving; i++)
                    {
                        int breakToken = 0;
                        int horizontalIndex = horizontal + i;
                        for (int j = 0; j < verticalMoving; j++)
                        {
                            int verticalIndex = vertical + j;
                            int value = matrixOfFlagsToKeep[verticalIndex][horizontalIndex];
                            if (value == 0)
                            {
                                breakToken = 1;
                                verticalFlags.clear();
                                horizontalFlags.clear();
                                thepairs.clear();
                                break;
                            }
                            if (!verticalFlags.contains(verticalIndex)) {
                                verticalFlags.add(verticalIndex);
                            }
                            if(!horizontalFlags.contains(horizontalIndex)) {
                                horizontalFlags.add(horizontalIndex);
                            }
                            thepairs.add(new int[]{horizontalIndex, verticalIndex});
                        }
                        if (breakToken == 1) { break; }
                    }
                }
                if((verticalFlags.size()+horizontalFlags.size())==this.numberOfClusters)
                {
                    if(verticalFlags.size()==2)
                    {
                        if (matrixOfFlagsToKeep[verticalFlags.get(0)][verticalFlags.get(1)] == 0)
                        {
                            verticalFlags.clear();
                            horizontalFlags.clear();
                            thepairs.clear();
                            cancelToken = 1;
                        }
                    }
                    else
                    {
                        for(int a=0; a<horizontalFlags.size()-1;a++)
                        {
                            int count = 0;
                            int valueOne = horizontalFlags.get(a);
                            for(int b=a+1; b<horizontalFlags.size();b++)
                            {
                                int valueTwo = horizontalFlags.get(b);
                                count += matrixOfFlagsToKeep[valueOne][valueTwo];
                            }
                            if(count==0) { cancelToken = 1; }
                        }
                    }
                    if(cancelToken==0)
                    {
                        horizontalFlags.addAll(verticalFlags);
                        ArrayList<Integer> flags = new ArrayList<Integer>(horizontalFlags);
                        possibilities.add(flags);
                        break;

                    }
                }
            }
            vertical++;
        }
        int[] clustersDistances = new int[possibilities.size()];
        for(int i=0; i<possibilities.size();i++)
        {
            ArrayList<Integer> cluster = possibilities.get(i);
            int distance = 0;
            for(int firstIndex=0; firstIndex<cluster.size()-1; firstIndex++)
            {
                int firstFlagNumber = cluster.get(firstIndex);
                for(int secondIndex=firstIndex+1;secondIndex<cluster.size();secondIndex++)
                {
                    int secondFlagNumber = cluster.get(secondIndex);
                    distance += distances[firstFlagNumber][secondFlagNumber];
                }
            }
            clustersDistances[i] = distance;
        }
        ArrayIndexComparator comparator = new ArrayIndexComparator(clustersDistances);
        Integer[] indexes = comparator.createIndexArray();
        Arrays.sort(indexes, comparator);
        int bestCluster = indexes[indexes.length-1];
        System.out.println("Found Possibilities : "+possibilities.size()+" , Selected One : "+bestCluster);
        for(int flagNum : possibilities.get(bestCluster))
        {
            Flag flag = this.listOfFlags[flagNum];
            ArrayList<Flag> cluster = new ArrayList<Flag>();
            cluster.add(flag);
            remainingFlagsStack.remove(flag);
            biClusteredFlags.add(cluster);
        }
        toReturn[0] = biClusteredFlags;
        toReturn[1] = remainingFlagsStack;
        return toReturn;
    }
    // same as above but when number of clusters requested is 2
    private Object[] findFirstClustersForTwo(Integer[] indexesOfFlagsToKeep, int[][] flagPairs, Stack remainingFlagsStack, Object[] toReturn)
    {
        List<List<Flag>> biClusteredFlags = new ArrayList<List<Flag>>();
        int bestPairIndex = indexesOfFlagsToKeep[indexesOfFlagsToKeep.length-1];
        int[] bestPair = flagPairs[bestPairIndex];
        Flag flagOne = this.listOfFlags[bestPair[0]];
        Flag flagTwo = this.listOfFlags[bestPair[1]];
        List<Flag> clusterOne = new ArrayList<Flag>();
        clusterOne.add(flagOne);
        biClusteredFlags.add(clusterOne);
        remainingFlagsStack.remove(flagOne);
        List<Flag> clusterTwo = new ArrayList<Flag>();
        clusterTwo.add(flagTwo);
        remainingFlagsStack.remove(flagTwo);
        biClusteredFlags.add(clusterTwo);
        toReturn[0] = biClusteredFlags;
        toReturn[1] = remainingFlagsStack;
        System.out.println(Arrays.toString(bestPair));
        return toReturn;
    }

}
