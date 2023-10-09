package ca.ualberta.cs.serl.wikidev.clustering;

import java.util.Random;

public class Helper {
	
	private static Random _rnd = new Random();
	//---------------------------------------------------------------------
	public static double ManhattenDistance(double[] vec1, double[] vec2)
	{
		double distance = 0;

		for (int i = 0; i < vec1.length; i++)
			distance += Math.abs(vec1[i] - vec2[i]);

		return distance;
	}
	//---------------------------------------------------------------------
	public static void FisherYatesShuffle(int[] array)
	{
		for (int i = array.length - 1; i > 0; i--)
		{
			// Pick random positoin:
			int pos = _rnd.nextInt(i + 1);

			// Swap:
			int tmp = array[i];
			array[i] = array[pos];
			array[pos] = tmp;
		}
	}


}
