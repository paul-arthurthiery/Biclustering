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
    public List<List<Flag>> biCluster(List<List<Flag>> clusteredFlags)
    {
        Flag[] singleClusteredList = this.listOfFlags.clone();

        long millisStart = Calendar.getInstance().getTimeInMillis();

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
    }



    private int[] comparePairFlags(Flag flagOne, Flag flagTwo)
    {
        int numberOfMatch = 0;
        int[] footPrint = new int[flagOne.getClass().getDeclaredFields().length];
        numberOfMatch += footPrint[0] = stringCompare(flagOne.name,flagTwo.name);
        numberOfMatch += footPrint[1] = nonSubtractionCompare(flagOne.landmass,flagTwo.landmass);
        numberOfMatch += footPrint[2] = nonSubtractionCompare(flagOne.zone,flagTwo.zone);
        numberOfMatch += footPrint[3] = nonSubtractionCompare(flagOne.area, flagTwo.area);
        numberOfMatch += footPrint[4] = nonSubtractionCompare(flagOne.population, flagTwo.population);
        numberOfMatch += footPrint[5] = nonSubtractionCompare(flagOne.language,flagTwo.language);
        numberOfMatch += footPrint[6] = nonSubtractionCompare(flagOne.religion,flagTwo.religion);
        numberOfMatch += footPrint[7] = nonSubtractionCompare(flagOne.bars,flagTwo.bars);
        numberOfMatch += footPrint[8] = nonSubtractionCompare(flagOne.stripes, flagTwo.stripes);
        numberOfMatch += footPrint[9] = nonSubtractionCompare(flagOne.colours, flagTwo.colours);
        numberOfMatch += footPrint[10] = nonSubtractionCompare(flagOne.red, flagTwo.red);
        numberOfMatch += footPrint[11] = nonSubtractionCompare(flagOne.green, flagTwo.green);
        numberOfMatch += footPrint[12] = nonSubtractionCompare(flagOne.blue, flagTwo.blue);
        numberOfMatch += footPrint[13] = nonSubtractionCompare(flagOne.gold, flagTwo.gold);
        numberOfMatch += footPrint[14] = nonSubtractionCompare(flagOne.white, flagTwo.white);
        numberOfMatch += footPrint[15] = nonSubtractionCompare(flagOne.black, flagTwo.black);
        numberOfMatch += footPrint[16] = nonSubtractionCompare(flagOne.orange, flagTwo.orange);
        numberOfMatch += footPrint[17] = stringCompare(flagOne.mainhue,flagTwo.mainhue);
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
        numberOfMatch += footPrint[28] = stringCompare(flagOne.topleft,flagTwo.topleft);
        numberOfMatch += footPrint[29] = stringCompare(flagOne.botright,flagTwo.botright);
        int[] matches = new int[numberOfMatch];
        int counter = 0;
        for(int i=0; i<footPrint.length; i++)
        {
            if(footPrint[i]==1)
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


}
