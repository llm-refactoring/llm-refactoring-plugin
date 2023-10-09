package ca.ualberta.cs.serl.wikidev.clustering;


import java.util.Random;

public class SammonsProjection {
	
	private int maxIteration;
	private double lambda = 1;
	private double[][] InputData;
	private int[] indicesI;
	private int[] indicesJ;
	private int OutputDimension;
	protected double[][] _distanceMatrix;
	private int Count;
	private double[][] Projection;
	private int Iteration;
	
	
	public SammonsProjection(
			double[][] inputData,
			int outputDimension,
			int maxIteration)
		{
			if (inputData == null || inputData.length == 0)
				throw new IllegalArgumentException("inputData");
			//-----------------------------------------------------------------
			
			this.InputData = inputData;
			this.Count = this.InputData.length;
			this.OutputDimension = outputDimension;
			this.maxIteration = maxIteration;

			// Initialize the projection:
			Initialize();
			
			// Create the indices-arrays:
			indicesI = createIndexArray(this.Count);
			indicesJ = createIndexArray(this.Count);

		}
	

	public double[][] getProjection() {
		return Projection;
	}

	
	private int[] createIndexArray(int count) {
		int[] indices = new int[count];
		for(int i=0; i<count; i++) {
			indices[i] = i;
		}
		return indices;
	}
	
	public void CreateMapping()
	{
		for (int i = maxIteration; i >= 0; i--)
			this.Iterate();
	}

	public void Iterate()
	{
		int[] indicesI = this.indicesI;
		int[] indicesJ = this.indicesJ;
		double[][] distanceMatrix = _distanceMatrix;
		double[][] projection = this.Projection;

		// Shuffle the indices-array for random pick of the points:
		Helper.FisherYatesShuffle(indicesI);
		Helper.FisherYatesShuffle(indicesJ);

		for (int i = 0; i < indicesI.length; i++)
		{
			double[] distancesI = distanceMatrix[indicesI[i]];
			double[] projectionI = projection[indicesI[i]];

			for (int j = 0; j < indicesJ.length; j++)
			{
				if (indicesI[i] == indicesJ[j])
					continue;

				double[] projectionJ = projection[indicesJ[j]];

				double dij = distancesI[indicesJ[j]];
				double Dij = Helper.ManhattenDistance(
						projectionI,
						projectionJ);

				// Avoid division by zero:
				if (Dij == 0)
					Dij = 1e-10;

				double delta = lambda * (dij - Dij) / Dij;

				for (int k = 0; k < projectionJ.length; k++)
				{
					double correction =
						delta * (projectionI[k] - projectionJ[k]);

					projectionI[k] += correction;
					projectionJ[k] -= correction;
				}
			}
		}

		// Reduce lambda monotonically:
		ReduceLambda();
	}

	private void Initialize()
	{
		_distanceMatrix = CalculateDistanceMatrix();

		// Initialize random points for the projection:
		Random rnd = new Random();
		double[][] projection = new double[this.Count][];
		
		for (int i = 0; i < projection.length; i++)
		{
			double[] projectionI = new double[this.OutputDimension];
			projection[i] = projectionI;
			for (int j = 0; j < projectionI.length; j++)
				projectionI[j] = rnd.nextDouble()*this.Count;
		}
		this.Projection = projection;
	}
	
	private double[][] CalculateDistanceMatrix()
	{
		double[][] distanceMatrix = new double[this.Count][];
		double[][] inputData = this.InputData;

		for (int i=0;i<distanceMatrix.length;i++)
		{
			double[] distances = new double[this.Count];
			

			double[] inputI = inputData[i];

			for (int j = 0; j < distances.length; j++)
			{
				if (j == i)
				{
					distances[j] = 0;
					continue;
				}

				distances[j] = Helper.ManhattenDistance(
					inputI,
					inputData[j]);
			}
			distanceMatrix[i] = distances;
		}

		return distanceMatrix;
	}
	
	private void ReduceLambda()
	{
		this.Iteration++;

		double ratio = (double)this.Iteration / maxIteration;

		lambda = Math.pow(0.01, ratio);
	}


}
