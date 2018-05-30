import java.util.Comparator;

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
