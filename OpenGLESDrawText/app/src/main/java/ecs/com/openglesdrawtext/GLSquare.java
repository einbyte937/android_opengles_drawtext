package ecs.com.openglesdrawtext;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.Arrays;

public class GLSquare {

    public float[] mVerticesData;

    public float[] mColourData;

    public float[] mTextureData;

    public int dCount = 0;

    public ShortBuffer mIndices;

    static int VERTEXSIZE = 3;

    static int COLOURSIZE = 4;

    static int TEXTURESIZE = 2;

    static int VERTEXSTRIDE = VERTEXSIZE * 4;

    static int COLOURSTRIDE = COLOURSIZE * 4;

    static int TEXTURESTRIDE = TEXTURESIZE * 4;

    public final short[] mIndicesData =
    {
            0, 1, 2, 0, 2, 3
    };

    GLSquare( float[] mVerticesDataParam, float[] mColourDataParam, float[] mTextureDataParam )
    {
        mVerticesData = new float[12];

        mColourData = new float[16];

        mTextureData = new float[8];

        dCount = 0;

        for( float f : mVerticesDataParam )
        {
            mVerticesData[dCount] = f;

            dCount++;
        }

        dCount = 0;

        for( float f : mColourDataParam )
        {
            mColourData[dCount] = f;

            dCount++;
        }

        dCount = 0;

        for( float f : mTextureDataParam )
        {
            mTextureData[dCount] = f;

            dCount++;
        }

        mIndices = ByteBuffer.allocateDirect ( mIndicesData.length * 2 )
                .order ( ByteOrder.nativeOrder( ) ).asShortBuffer( );

        mIndices.put ( mIndicesData ).position ( 0 );
    }

    public void setColourData( float[] mSingleColour )
    {
        for( int x = 0; x < mColourData.length; x += 4 )
        {
            mColourData[x] = mSingleColour[0];
            mColourData[x+1] = mSingleColour[1];
            mColourData[x+2] = mSingleColour[2];
            mColourData[x+3] = mSingleColour[3];
        }
    }

    GLSquare( float[] mVerticesDataParam, float[] mColourDataParam )
    {
        mVerticesData = new float[12];

        mColourData = new float[16];

        mTextureData = new float[8];

        dCount = 0;

        for( float f : mVerticesDataParam )
        {
            mVerticesData[dCount] = f;

            dCount++;
        }

        dCount = 0;

        for( float f : mColourDataParam )
        {
            mColourData[dCount] = f;

            dCount++;
        }

        Arrays.fill( mTextureData, 0 );

        mIndices = ByteBuffer.allocateDirect ( mIndicesData.length * 2 )
                .order ( ByteOrder.nativeOrder( ) ).asShortBuffer( );

        mIndices.put ( mIndicesData ).position ( 0 );
    }

    public int numVertices( )
    {
        return mVerticesData.length / 3;
    }

    public GLSquare offset( float ox, float oy )
    {
        //For future cloneable

        float[] mNewVerts = new float[12];

        for( int x = 0; x < mVerticesData.length; x += 3 )
        {
            mNewVerts[x] = mVerticesData[x] + ( ox );
            mNewVerts[x+1] = mVerticesData[x+1] + ( oy );
            mNewVerts[x+2] = mVerticesData[x+2];
        }

        return new GLSquare( mNewVerts, mColourData );
    }
}
