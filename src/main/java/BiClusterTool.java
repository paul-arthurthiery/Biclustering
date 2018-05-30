import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import static java.lang.Math.abs;


public class BiClusterTool
{
    private Flag[] listOfFlags;
    private int numberOfClusters;
    private float percentageToKeep;

    public BiClusterTool(Flag[] listOfFlags, int numberOfClusters, float percentageToKeep) {
        this.listOfFlags = listOfFlags;
        this.numberOfClusters = numberOfClusters;
        this.percentageToKeep = percentageToKeep;
    }

    // main method to pilot all the biclustering process
    public List<List<Flag>> biCluster()
    {
        Flag[] singleClusteredList = this.listOfFlags.clone();

        long millisStart = Calendar.getInstance().getTimeInMillis();

        int numberOfFlags = this.listOfFlags.length;
        int[][] flagPairs = new int[(numberOfFlags*(numberOfFlags-1))/2][2]; //all the possible pairs of flags

        // first step is to create all the clusters

        int[][] pairsFootPrints = new int[flagPairs.length][singleClusteredList[0].getClass().getDeclaredFields().length];
        int counter = 0;
        for(int i=0; i<singleClusteredList.length;i++)
        {
            Flag flagOne = singleClusteredList[i];
            for(int j=i+1; j<singleClusteredList.length;j++)
            {
                Flag flagTwo = singleClusteredList[j];
                pairsFootPrints[counter] = comparePairFlags(flagOne,flagTwo);
                flagPairs[counter][0] = i;
                flagPairs[counter][1] = j;
                counter++;
            }
        }

        ArrayList<int[]> patterns = new ArrayList<int[]>();
        ArrayList<ArrayList<Flag>> clusters = new ArrayList<ArrayList<Flag>>();

        for(int[] footPrint : pairsFootPrints)
        {
            if(!patterns.contains(footPrint))
            {
                patterns.add(footPrint);
                ArrayList<Flag> flags = new ArrayList<Flag>();
                Flag flagOne = this.listOfFlags[flagPairs[counter][0]];
                Flag flagTwo = this.listOfFlags[flagPairs[counter][1]];
                flags.add(flagOne);
                flags.add(flagTwo);
                clusters.add(flags);
            }
            else
            {
                int patternIndex = patterns.indexOf(footPrint);
                Flag flagOne = this.listOfFlags[flagPairs[counter][0]];
                Flag flagTwo = this.listOfFlags[flagPairs[counter][1]];
                if(!clusters.get(patternIndex).contains(flagOne)) {
                    clusters.get(patternIndex).add(flagOne);
                }
                if(!clusters.get(patternIndex).contains(flagTwo)) {
                    clusters.get(patternIndex).add(flagTwo);
                }
            }
        }

        long millisEnd = Calendar.getInstance().getTimeInMillis();
        System.out.println("Distance Done Time : "+(millisEnd-millisStart));



        millisStart = Calendar.getInstance().getTimeInMillis();

        ArrayIndexComparator comparator = new ArrayIndexComparator(arrayAllPairDistances);
        Integer[] indexes = comparator.createIndexArray();
        Arrays.sort(indexes, comparator);

        int position = (indexes.length/2)-1; // position of medianIndex in indexes array
        int medianIndex = indexes[position]; // value of the index of the median
        int bottomIndex = (int)((position)-((this.percentageToKeep/200.0)*indexes.length));
        int topIndex = (int)((position)+((this.percentageToKeep/200.0)*indexes.length));
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

    private int[] comparePairFlags(Flag flagOne, Flag flagTwo)
    {
        int[] footPrint = new int[flagOne.getClass().getDeclaredFields().length];
        footPrint[0] = stringCompare(flagOne.name,flagTwo.name);
        footPrint[1] = nonSubtractionCompare(flagOne.landmass,flagTwo.landmass);
        footPrint[2] = nonSubtractionCompare(flagOne.zone,flagTwo.zone);
        footPrint[3] = subtractionCompare(flagOne.area, flagTwo.area);
        footPrint[4] = subtractionCompare(flagOne.population, flagTwo.population);
        footPrint[5] = nonSubtractionCompare(flagOne.language,flagTwo.language);
        footPrint[6] = nonSubtractionCompare(flagOne.religion,flagTwo.religion);
        footPrint[7] = subtractionCompare(flagOne.bars,flagTwo.bars);
        footPrint[8] = subtractionCompare(flagOne.stripes, flagTwo.stripes);
        footPrint[9] = subtractionCompare(flagOne.colours, flagTwo.colours);
        footPrint[10] = nonSubtractionCompare(flagOne.red, flagTwo.red);
        footPrint[11] = nonSubtractionCompare(flagOne.green, flagTwo.green);
        footPrint[12] = nonSubtractionCompare(flagOne.blue, flagTwo.blue);
        footPrint[13] = nonSubtractionCompare(flagOne.gold, flagTwo.gold);
        footPrint[14] = nonSubtractionCompare(flagOne.white, flagTwo.white);
        footPrint[15] = nonSubtractionCompare(flagOne.black, flagTwo.black);
        footPrint[16] = nonSubtractionCompare(flagOne.orange, flagTwo.orange);
        footPrint[17] = stringCompare(flagOne.mainhue,flagTwo.mainhue);
        footPrint[18] = subtractionCompare(flagOne.circles, flagTwo.circles);
        footPrint[19] = subtractionCompare(flagOne.crosses, flagTwo.crosses);
        footPrint[20] = subtractionCompare(flagOne.saltires, flagTwo.saltires);
        footPrint[21] = subtractionCompare(flagOne.quarters, flagTwo.quarters);
        footPrint[22] = subtractionCompare(flagOne.sunstars, flagTwo.sunstars);
        footPrint[23] = nonSubtractionCompare(flagOne.crescent, flagTwo.crescent);
        footPrint[24] = nonSubtractionCompare(flagOne.triangle, flagTwo.triangle);
        footPrint[25] = nonSubtractionCompare(flagOne.icon, flagTwo.icon);
        footPrint[26] = nonSubtractionCompare(flagOne.animate, flagTwo.animate);
        footPrint[27] = nonSubtractionCompare(flagOne.text, flagTwo.text);
        footPrint[28] = stringCompare(flagOne.topleft,flagTwo.topleft);
        footPrint[29] = stringCompare(flagOne.botright,flagTwo.botright);
        return footPrint;
    }

    // method to compare two String, return 0 if identical, 1 otherwise
    private int stringCompare(String a, String b)
    {
        return a.matches(b) ? 1 : 0;
    }

    //method to compare 2 ints, when subtraction doesn't represent closeness. Same output as above
    private int nonSubtractionCompare(int a, int b){
        return a == b ? 1 : 0;
    }

    //method to compare 2 ints, when subtraction represents closeness.
    private int subtractionCompare(int a, int b){
        return a == b ? 1 : 0;
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
                if(vertical+1>=horizontal && vertical+1<=horizontal+horizontalMoving){}
                else
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
                        pairs = (ArrayList<int[]>) thepairs.clone();
                        possibilities.add(flags);
                        break;
                    }
                }
            }
            vertical++;
        }
        System.out.print("Selected pairs : ");
        for(int[] pair : pairs)
        {
            System.out.print(Arrays.toString(pair));
        }
        System.out.println();

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
