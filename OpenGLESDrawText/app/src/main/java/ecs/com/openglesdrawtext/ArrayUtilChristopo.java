package ecs.com.openglesdrawtext;

import java.util.ArrayList;

public class ArrayUtilChristopo {

	static float[] combFloat( ArrayList< float[] > aParamFloat )
	{
		int numFloats = 0;

		for( float[] f : aParamFloat )
		{
			numFloats += f.length;
		}
		
		float[] fReturnVal = new float[ numFloats ];
		
		int dCount = 0;
		
		for( float[] f : aParamFloat )
		{
			for( float ff : f )
			{
				fReturnVal[ dCount ] = ff;
				
				dCount++;
			}
		}
		
		return fReturnVal;
	}

	static short[] combShort( ArrayList< short[] > aParamFloat )
	{
		int numFloats = 0;

		for( short[] f : aParamFloat )
		{
			numFloats += f.length;
		}

		short[] fReturnVal = new short[ numFloats ];

		int dCount = 0;

		for( short[] f : aParamFloat )
		{
			for( short ff : f )
			{
				fReturnVal[ dCount ] = ff;

				dCount++;
			}
		}

		return fReturnVal;
	}
}
