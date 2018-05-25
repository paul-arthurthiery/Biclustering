import java.io.*;
import java.util.Scanner;

public class FileProcessor
{
    public FileProcessor(){}

    public Flag[] processFile(String path) throws IOException
    {
        String data = "";
        // Read the file
        File file = new File(path);
        Scanner sc = new Scanner(file);
        sc.useDelimiter("\\Z");
        // Store the data in a String
        data += sc.next();
        // Stores lines into a String array
        String[] data_array = data.split("\n");
        // Create Flags container variable
        Flag[] flags = new Flag[data_array.length];
        // Loop over the lines to create Flags and store them into the container
        int counter = 0;
        for(String one_line : data_array)
        {
            String[] raw = one_line.split(",");
            Flag temp_flag = new Flag(raw[0],
                    Integer.parseInt(raw[1]),Integer.parseInt(raw[2]),Integer.parseInt(raw[3]),
                    Integer.parseInt(raw[4]),Integer.parseInt(raw[5]),Integer.parseInt(raw[6]),
                    Integer.parseInt(raw[7]),Integer.parseInt(raw[8]),Integer.parseInt(raw[9]),
                    Integer.parseInt(raw[10]),Integer.parseInt(raw[11]),Integer.parseInt(raw[12]),
                    Integer.parseInt(raw[13]),Integer.parseInt(raw[14]),Integer.parseInt(raw[15]),
                    Integer.parseInt(raw[16]),raw[17],Integer.parseInt(raw[18]),Integer.parseInt(raw[19]),
                    Integer.parseInt(raw[20]),Integer.parseInt(raw[21]),Integer.parseInt(raw[22]),
                    Integer.parseInt(raw[23]),Integer.parseInt(raw[24]),Integer.parseInt(raw[25]),
                    Integer.parseInt(raw[26]),Integer.parseInt(raw[27]),raw[28],raw[29]);

            flags[counter] = temp_flag;
            counter++;
        }
        return flags;
    }



}
