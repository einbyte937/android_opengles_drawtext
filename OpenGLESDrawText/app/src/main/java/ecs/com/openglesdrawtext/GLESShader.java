package ecs.com.openglesdrawtext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.util.Log;

public class GLESShader
{
   static public Context mContext;

   private static String readShader ( Context mContext, String sFileName )
   {
      String sShaderSource = null;

      if ( sFileName == null )
      {
         return sShaderSource;
      }

      InputStream mIS = null;

      byte [] bBuffer;

      try
      {
         mIS =  mContext.getAssets( ).open ( sFileName );

         bBuffer = new byte[ mIS.available( ) ];

         mIS.read( bBuffer );

         ByteArrayOutputStream os = new ByteArrayOutputStream( );

         os.write ( bBuffer );

         os.close( );

         mIS.close( );

         sShaderSource = os.toString( );
      }
      catch ( IOException ioe )
      {
         mIS = null;
      }

      if ( mIS == null )
      {
         return sShaderSource;
      }

      return sShaderSource;
   }

   public static int loadShader ( int dType, String sShaderSrc )
   {
      int dShader;

      int[] dCompiled = new int[1];

      dShader = GLES30.glCreateShader ( dType );

      if ( dShader == 0 )
      {
         return 0;
      }

      GLES30.glShaderSource ( dShader, sShaderSrc );

      GLES30.glCompileShader ( dShader );

      GLES30.glGetShaderiv(dShader, GLES30.GL_COMPILE_STATUS, dCompiled, 0);

      if ( dCompiled[0] == 0 )
      {
         Log.e ( "GLESShader", GLES30.glGetShaderInfoLog ( dShader ) );

         GLES30.glDeleteShader ( dShader );

         return 0;
      }

      return dShader;
   }

   public static int loadProgram ( String sVertShaderSrc, String sFragShaderSrc )
   {
      int dVertexShader;

      int dFragmentShader;

      int dProgramObject;

      int[] dLinked = new int[1];

      dVertexShader = loadShader ( GLES30.GL_VERTEX_SHADER, sVertShaderSrc );

      if ( dVertexShader == 0 )
      {
         return 0;
      }

      dFragmentShader = loadShader ( GLES30.GL_FRAGMENT_SHADER, sFragShaderSrc );

      if ( dFragmentShader == 0 )
      {
         GLES30.glDeleteShader ( dVertexShader );

         return 0;
      }

      dProgramObject = GLES30.glCreateProgram();

      if ( dProgramObject == 0 )
      {
         return 0;
      }

      GLES30.glAttachShader ( dProgramObject, dVertexShader );

      GLES30.glAttachShader ( dProgramObject, dFragmentShader );

      GLES30.glLinkProgram( dProgramObject );

      GLES30.glGetProgramiv(dProgramObject, GLES30.GL_LINK_STATUS, dLinked, 0);

      if ( dLinked[0] == 0 )
      {
         Log.e ( "GLESShader", "Error linking program:" );

         Log.e ( "GLESShader", GLES30.glGetProgramInfoLog ( dProgramObject ) );

         GLES30.glDeleteProgram ( dProgramObject );

         return 0;
      }

      GLES30.glDeleteShader(dVertexShader);

      GLES30.glDeleteShader(dFragmentShader);

      return dProgramObject;
   }

   public static int loadProgramFromAsset ( Context mContext, String sVertexShaderFileName, String sFragShaderFileName )
   {
      int dVertexShader;

      int dFragmentShader;

      int dProgramObject;

      int[] dLinked = new int[1];

      String sVertShaderSrc = null;

      String sFragShaderSrc = null;

      sVertShaderSrc = readShader ( mContext, sVertexShaderFileName );

      if ( sVertShaderSrc == null )
      {
         return 0;
      }

      sFragShaderSrc = readShader ( mContext, sFragShaderFileName );

      if ( sFragShaderSrc == null )
      {
         return 0;
      }

      dVertexShader = loadShader( GLES30.GL_VERTEX_SHADER, sVertShaderSrc );

      if ( dVertexShader == 0 )
      {
         return 0;
      }

      dFragmentShader = loadShader ( GLES30.GL_FRAGMENT_SHADER, sFragShaderSrc );

      if ( dFragmentShader == 0 )
      {
         GLES30.glDeleteShader ( dVertexShader );
         return 0;
      }

      dProgramObject = GLES30.glCreateProgram( );

      if ( dProgramObject == 0 )
      {
         return 0;
      }

      GLES30.glAttachShader ( dProgramObject, dVertexShader );

      GLES30.glAttachShader ( dProgramObject, dFragmentShader );

      GLES30.glLinkProgram(dProgramObject);

      GLES30.glGetProgramiv(dProgramObject, GLES30.GL_LINK_STATUS, dLinked, 0);

      if ( dLinked[0] == 0 )
      {
         Log.e ( "GLESShader", "Error linking program:" );

         Log.e ( "GLESShader", GLES30.glGetProgramInfoLog ( dProgramObject ) );

         GLES30.glDeleteProgram ( dProgramObject );

         return 0;
      }

      GLES30.glDeleteShader ( dVertexShader );

      GLES30.glDeleteShader ( dFragmentShader );

      return dProgramObject;
   }

   ///
   //  Load texture from asset
   //
   static public int loadTextureFromAsset ( String fileName )
   {
      int[] textureId = new int[1];
      Bitmap bitmap = null;
      InputStream is = null;

      try
      {
         is = mContext.getAssets().open ( fileName );
      }
      catch ( IOException ioe )
      {
         is = null;
      }

      if ( is == null )
      {
         return 0;
      }

      bitmap = BitmapFactory.decodeStream(is);

      GLES30.glGenTextures ( 1, textureId, 0 );
      GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId[0]);

      GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0);

      GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR);
      GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
      GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE);
      GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE);

      return textureId[0];
   }
}
