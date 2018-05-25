import java.io.IOException;

public class Testing
{
    public static void main(String[] args) throws IOException
    {

        FileProcessor processor = new FileProcessor();
        Flag[] flags = processor.processFile("flag.txt");
        for(Flag flag : flags)
        {
            System.out.println(flag.name);
        }

        /*
        for(int i=0; i<30; i++)
        {
            System.out.print("Integer.parseInt(raw["+i+"]),");
            if(i%6==0){System.out.println();}
        }
        */

    }

}
