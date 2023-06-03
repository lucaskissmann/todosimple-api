import java.util.ArrayList;
import java.util.List;

public class FindOutlier{
    static int find(int[] integers)
    {
        List<Integer> oddNumbers = new ArrayList<>();
        List<Integer> evenNumbers = new ArrayList<>();

        for( int i = 0; i < integers.length; i++ )
        {
            if( integers[i] % 2 == 0 ) 
            {
                oddNumbers.add(integers[i]);
            }
            else
            {
                evenNumbers.add(integers[i]);
            }       
        } 

        return evenNumbers.size() > oddNumbers.size() ? oddNumbers.get(0) : evenNumbers.get(0);
    }
}