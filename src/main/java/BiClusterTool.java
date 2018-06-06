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
    public List<List<Flag>> biCluster(List<List<Flag>> clusteredFlags) {

        long millisStart = Calendar.getInstance().getTimeInMillis();

        // analyze pairs in all the clusters, create and add sets where 2 flags have at least 3 criteria in common
        List<List<Flag>> sets = new ArrayList<List<Flag>>();
        List<List<Integer>> footPrints = new ArrayList<List<Integer>>();
        for(int a=0; a<clusteredFlags.size(); a++) {
            for(int i=0; i<clusteredFlags.get(a).size();i++) {
                Flag flagOne = clusteredFlags.get(a).get(i);
                for (int j = i + 1; j < clusteredFlags.size(); j++) {
                    Flag flagTwo = clusteredFlags.get(a).get(j);
                    ArrayList<Integer> footPrint = comparePairFlags(flagOne, flagTwo);
                    if(footPrint.size() >=20 && checkIfFootPrintGenerated(footPrint, footPrints)<0) {
                        footPrints.add(footPrint);
                        List<Flag> newSet = new ArrayList<Flag>();
                        newSet.add(flagOne);
                        newSet.add(flagTwo);
                        sets.add(newSet);
                    } else if(footPrint.size() >= 20 && checkIfFootPrintGenerated(footPrint, footPrints)>=0){
                        sets.get(checkIfFootPrintGenerated(footPrint, footPrints)).add(flagOne);
                        sets.get(checkIfFootPrintGenerated(footPrint, footPrints)).add(flagTwo);
                    }
                }
            }
        }
        for(Flag flag: listOfFlags){
            int counter = 0;
            for (List<Flag> set: sets) {
                ArrayList<Integer> footPrint = comparePairFlags(flag, set.get(0));
                if(footPrint == footPrints.get(counter)) set.add(flag);
                counter++;
            }
        }
        /*

        int numberOfFlags = this.listOfFlags.length;
        int[][] flagPairs = new int[(numberOfFlags*(numberOfFlags-1))/2][2]; //all the possible pairs of flags

        // first step is to create all the clusters
        for(List<Flag> cluster : clusteredFlags)
        {

            int counter = 0;
            for(int i=0; i<cluster.size();i++)
            {
                Flag flagOne = cluster.get(i);
                for(int j=i+1; j<singleClusteredList.length;j++)
                {
                    Flag flagTwo = cluster.get(j);
                    //pairsFootPrints[counter] = comparePairFlags(flagOne,flagTwo);
                    int[] matching = comparePairFlags(flagOne,flagTwo);
                    if(matching.length>=3)
                    {


                    }
                    //flagPairs[counter][0] = i;
                    //flagPairs[counter][1] = j;
                    counter++;
                }
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

        List<List<Flag>> biClusteredFlags = new ArrayList<List<Flag>>();
        Stack remainingFlagsStack = new Stack<Flag>();
        remainingFlagsStack.addAll(Arrays.asList(listOfFlags));

        return biClusteredFlags;
        */

        return sets;
    }

    private int checkIfFootPrintGenerated(ArrayList<Integer> footPrint, List<List<Integer>> footPrints) {
        if(footPrints.contains(footPrint)) return footPrints.indexOf(footPrint);
        else return -1;
    }


    private ArrayList<Integer> comparePairFlags(Flag flagOne, Flag flagTwo) {
        ArrayList<Integer> footPrint = new ArrayList<Integer>();
        if(stringCompare(flagOne.name,flagTwo.name)>0) footPrint.add(1);
        if(nonSubtractionCompare(flagOne.landmass,flagTwo.landmass) >0) footPrint.add(2);
        if(nonSubtractionCompare(flagOne.zone,flagTwo.zone)>0) footPrint.add(3);
        if(nonSubtractionCompare(flagOne.area, flagTwo.area)>0) footPrint.add(4);
        if(nonSubtractionCompare(flagOne.population, flagTwo.population)>0) footPrint.add(5);
        if(nonSubtractionCompare(flagOne.language,flagTwo.language)>0) footPrint.add(6);
        if(nonSubtractionCompare(flagOne.religion,flagTwo.religion)>0) footPrint.add(7);
        if(nonSubtractionCompare(flagOne.bars,flagTwo.bars)>0) footPrint.add(8);
        if(nonSubtractionCompare(flagOne.stripes, flagTwo.stripes)>0) footPrint.add(9);
        if(nonSubtractionCompare(flagOne.colours, flagTwo.colours)>0) footPrint.add(10);
        if(nonSubtractionCompare(flagOne.red, flagTwo.red)>0) footPrint.add(11);
        if(nonSubtractionCompare(flagOne.green, flagTwo.green)>0) footPrint.add(12);
        if(nonSubtractionCompare(flagOne.blue, flagTwo.blue)>0) footPrint.add(13);
        if(nonSubtractionCompare(flagOne.gold, flagTwo.gold)>0) footPrint.add(14);
        if(nonSubtractionCompare(flagOne.white, flagTwo.white)>0) footPrint.add(15);
        if(nonSubtractionCompare(flagOne.black, flagTwo.black)>0) footPrint.add(16);
        if(nonSubtractionCompare(flagOne.orange, flagTwo.orange)>0) footPrint.add(17);
        if(stringCompare(flagOne.mainhue,flagTwo.mainhue)>0) footPrint.add(18);
        if(nonSubtractionCompare(flagOne.circles, flagTwo.circles)>0) footPrint.add(19);
        if(nonSubtractionCompare(flagOne.crosses, flagTwo.crosses)>0) footPrint.add(20);
        if(nonSubtractionCompare(flagOne.saltires, flagTwo.saltires)>0) footPrint.add(21);
        if(nonSubtractionCompare(flagOne.quarters, flagTwo.quarters)>0) footPrint.add(22);
        if(nonSubtractionCompare(flagOne.sunstars, flagTwo.sunstars)>0) footPrint.add(23);
        if(nonSubtractionCompare(flagOne.crescent, flagTwo.crescent)>0) footPrint.add(24);
        if(nonSubtractionCompare(flagOne.triangle, flagTwo.triangle)>0) footPrint.add(25);
        if(nonSubtractionCompare(flagOne.icon, flagTwo.icon)>0) footPrint.add(26);
        if(nonSubtractionCompare(flagOne.animate, flagTwo.animate)>0) footPrint.add(27);
        if(nonSubtractionCompare(flagOne.text, flagTwo.text)>0) footPrint.add(28);
        if(stringCompare(flagOne.topleft,flagTwo.topleft)>0) footPrint.add(29);
        if(stringCompare(flagOne.botright,flagTwo.botright)>0) footPrint.add(30);
        return footPrint;
    }

    private int[] compareTwoFlags(Flag flagOne, Flag flagTwo) {
        int numberOfMatch = 0;
        int[] footPrint = new int[flagOne.getClass().getDeclaredFields().length];
        numberOfMatch += footPrint[0] = stringCompare(flagOne.name, flagTwo.name);
        numberOfMatch += footPrint[1] = nonSubtractionCompare(flagOne.landmass, flagTwo.landmass);
        numberOfMatch += footPrint[2] = nonSubtractionCompare(flagOne.zone, flagTwo.zone);
        numberOfMatch += footPrint[3] = nonSubtractionCompare(flagOne.area, flagTwo.area);
        numberOfMatch += footPrint[4] = nonSubtractionCompare(flagOne.population, flagTwo.population);
        numberOfMatch += footPrint[5] = nonSubtractionCompare(flagOne.language, flagTwo.language);
        numberOfMatch += footPrint[6] = nonSubtractionCompare(flagOne.religion, flagTwo.religion);
        numberOfMatch += footPrint[7] = nonSubtractionCompare(flagOne.bars, flagTwo.bars);
        numberOfMatch += footPrint[8] = nonSubtractionCompare(flagOne.stripes, flagTwo.stripes);
        numberOfMatch += footPrint[9] = nonSubtractionCompare(flagOne.colours, flagTwo.colours);
        numberOfMatch += footPrint[10] = nonSubtractionCompare(flagOne.red, flagTwo.red);
        numberOfMatch += footPrint[11] = nonSubtractionCompare(flagOne.green, flagTwo.green);
        numberOfMatch += footPrint[12] = nonSubtractionCompare(flagOne.blue, flagTwo.blue);
        numberOfMatch += footPrint[13] = nonSubtractionCompare(flagOne.gold, flagTwo.gold);
        numberOfMatch += footPrint[14] = nonSubtractionCompare(flagOne.white, flagTwo.white);
        numberOfMatch += footPrint[15] = nonSubtractionCompare(flagOne.black, flagTwo.black);
        numberOfMatch += footPrint[16] = nonSubtractionCompare(flagOne.orange, flagTwo.orange);
        numberOfMatch += footPrint[17] = stringCompare(flagOne.mainhue, flagTwo.mainhue);
        numberOfMatch += footPrint[18] = nonSubtractionCompare(flagOne.circles, flagTwo.circles);
        numberOfMatch += footPrint[19] = nonSubtractionCompare(flagOne.crosses, flagTwo.crosses);
        numberOfMatch += footPrint[20] = nonSubtractionCompare(flagOne.saltires, flagTwo.saltires);
        numberOfMatch += footPrint[21] = nonSubtractionCompare(flagOne.quarters, flagTwo.quarters);
        numberOfMatch += footPrint[22] = nonSubtractionCompare(flagOne.sunstars, flagTwo.sunstars);
        numberOfMatch += footPrint[23] = nonSubtractionCompare(flagOne.crescent, flagTwo.crescent);
        numberOfMatch += footPrint[24] = nonSubtractionCompare(flagOne.triangle, flagTwo.triangle);
        numberOfMatch += footPrint[25] = nonSubtractionCompare(flagOne.icon, flagTwo.icon);
        numberOfMatch += footPrint[26] = nonSubtractionCompare(flagOne.animate, flagTwo.animate);
        numberOfMatch += footPrint[27] = nonSubtractionCompare(flagOne.text, flagTwo.text);
        numberOfMatch += footPrint[28] = stringCompare(flagOne.topleft, flagTwo.topleft);
        numberOfMatch += footPrint[29] = stringCompare(flagOne.botright, flagTwo.botright);
        int[] matches = new int[numberOfMatch];
        int counter = 0;
        for (int i = 0; i < footPrint.length; i++)
        {
            if (footPrint[i] == 1)
            {
                matches[counter] = i;
                counter++;
            }
        }
        //return footPrint;
        return matches;
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

    //prints the result of the biclustering
    public void printBicluster(List<List<Flag>> biClusteredFlags){
        int num = 0;
        for(int i=0; i<biClusteredFlags.size();i++){
            num+=biClusteredFlags.get(i).size();
            System.out.println(biClusteredFlags.get(i));
        }
        System.out.println("Number of Countries : "+num);
    }


    //##########################

    // method to display the flags of the result of the bicluster
    public void displayClustersFlags(List<List<Flag>> clusters) {
        printBicluster(clusters);
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
    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image newimg = img.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(newimg);
    }
    // shows the frame with the flags
    private void showFrame(JPanel pane, String title) {
        JFrame mainframe = new JFrame(title);
        mainframe.getContentPane().add(pane);
        mainframe.setBounds(0, 0, 1000, 1000);
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.setVisible(true);
    }


}
