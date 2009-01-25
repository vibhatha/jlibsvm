package edu.berkeley.compbio.jlibsvm.regression;

import edu.berkeley.compbio.jlibsvm.ContinuousModel;
import edu.berkeley.compbio.jlibsvm.SvmException;
import edu.berkeley.compbio.jlibsvm.binary.AlphaModel;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class RegressionModel<P> extends AlphaModel<Float, P> implements ContinuousModel<P>
	{
	public static final float NO_LAPLACE_PARAMETER = -1;

	public float laplaceParameter = NO_LAPLACE_PARAMETER;

	public float r;// for Solver_NU.  I wanted to factor this out as SolutionInfoNu, but that was too much hassle

/*	public RegressionModel(BinaryModel<Float, P> binaryModel)
		{
		super(binaryModel);
		}*/


	public RegressionModel()
		{
		super();
		}

	public RegressionModel(Properties props)
		{
		super(props);
		laplaceParameter = Float.parseFloat(props.getProperty("laplace"));
		}

	public Float predictValue(P x)
		{
		float sum = 0;
		for (Map.Entry<P, Double> entry : supportVectors.entrySet())
			{
			sum += entry.getValue() * kernel.evaluate(x, entry.getKey());
			}
		sum -= rho;
		return sum;
		}


	public float getLaplaceParameter()
		{
		if (laplaceParameter == NO_LAPLACE_PARAMETER)
			{
			throw new SvmException("Model doesn't contain information for SVR probability inference\n");
			}
		return laplaceParameter;
		}

	public void writeToStream(DataOutputStream fp) throws IOException
		{
		super.writeToStream(fp);
		fp.writeBytes("probA " + laplaceParameter + "\n");

		//these must come after everything else
		writeSupportVectors(fp);

		fp.close();
		}


	public boolean supportsLaplace()
		{
		return laplaceParameter != NO_LAPLACE_PARAMETER;
		}
	}