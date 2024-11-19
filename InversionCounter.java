import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Class with two different methods to count inversions in an array of integers.
 * @author Darius Fan df2842
 * @version 1.1.0 November 13, 2024
 */
public class InversionCounter
{

    /**
     * Returns the number of inversions in an array of integers.
     * This method uses nested loops to run in Theta(n^2) time.
     * @param array the array to process
     * @return the number of inversions in an array of integers
     */
    public static long countInversionsSlow(int[] array)
    {
        int[] arrayCopy = new int[array.length];
        System.arraycopy(array, 0, arrayCopy, 0, array.length);
        long inversions = 0;

        for (int i = 1; i < array.length; i++)
        {
            int count = 0;

            while (count < i && arrayCopy[i - 1 - count] > array[i])
            {
                arrayCopy[i - count] = arrayCopy[i - 1 - count];
                count++;
            }

            arrayCopy[i - count] = array[i];
            inversions += count;
        }

        return inversions;
    }

    /**
     * Returns the number of inversions in an array of integers.
     * This method uses mergesort to run in Theta(n lg n) time.
     * @param array the array to process
     * @return the number of inversions in an array of integers
     */
    public static long countInversionsFast(int[] array)
    {
        int[] arrayCopy = new int[array.length], scratch =  new int[array.length];
        System.arraycopy(array, 0, arrayCopy, 0, array.length);
        return countInversionsFastHelper(arrayCopy, scratch, 0, array.length - 1);
    }

    private static long countInversionsFastHelper(int[] array, int[] scratch, int low, int high)
    {
        long inversions = 0;

        if (low < high)
        {
            int mid = low + (high - low) / 2;
            inversions += countInversionsFastHelper(array, scratch, low, mid);
            inversions += countInversionsFastHelper(array, scratch, mid + 1, high);

            int i = low, j = mid + 1;
            for (int k = low; k <= high; k++)
            {
                if (i <= mid && (j > high || array[i] <= array[j]))
                {
                    scratch[k] = array[i++];
                }
                else
                {
                    scratch[k] = array[j++];
                    inversions += mid - i + 1;
                }
            }
            for (int k = low; k <= high; k++)
            {
                array[k] = scratch[k];
            }
        }

        return inversions;
    }

    /**
     * Reads an array of integers from stdin.
     * @return an array of integers
     * @throws IOException if data cannot be read from stdin
     * @throws NumberFormatException if there is an invalid character in the
     * input stream
     */
    private static int[] readArrayFromStdin() throws IOException,
                                                     NumberFormatException {
        List<Integer> intList = new LinkedList<>();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        int value = 0, index = 0, ch;
        boolean valueFound = false;
        while ((ch = reader.read()) != -1) {
            if (ch >= '0' && ch <= '9') {
                valueFound = true;
                value = value * 10 + (ch - 48);
            } else if (ch == ' ' || ch == '\n' || ch == '\r') {
                if (valueFound) {
                    intList.add(value);
                    value = 0;
                }
                valueFound = false;
                if (ch != ' ') {
                    break;
                }
            } else {
                throw new NumberFormatException(
                        "Invalid character '" + (char)ch +
                        "' found at index " + index + " in input stream.");
            }
            index++;
        }

        int[] array = new int[intList.size()];
        Iterator<Integer> iterator = intList.iterator();
        index = 0;
        while (iterator.hasNext()) {
            array[index++] = iterator.next();
        }
        return array;
    }

    public static void main(String[] args)
    {
        boolean slow = false;

        if (args.length > 1)
        {
            System.err.println("Usage: java InversionCounter [slow]");
            System.exit(1);
        }
        else if (args.length == 1 && !args[0].equals("slow"))
        {
            System.err.println("Error: Unrecognized option '" + args[0] + "'.");
            System.exit(1);
        }
        else if (args.length == 1)
        {
            slow = true;
        }

        System.out.print("Enter sequence of integers, each followed by a space: ");

        try
        {
            int[] array = readArrayFromStdin();

            if (array.length == 0)
            {
                System.err.println("Error: Sequence of integers not received.");
                System.exit(1);
            }

            if (slow)
            {
                System.out.println("Number of inversions: " + countInversionsSlow(array));
                System.exit(0);
            }

            System.out.println("Number of inversions: " + countInversionsFast(array));
            System.exit(0);
        }
        catch (Exception e)
        {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
