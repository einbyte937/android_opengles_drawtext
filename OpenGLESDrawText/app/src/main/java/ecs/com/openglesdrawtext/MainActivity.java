package ecs.com.openglesdrawtext;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends Activity
implements
View.OnClickListener
{
    private final int CONTEXT_CLIENT_VERSION = 3;

    private GLSurfaceView mGLSurfaceView;

    final static int[] CLICKABLES = {
            R.id.button_start,
    };

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );

        setContentView( R.layout.main_activity );

        for ( int id : CLICKABLES ) findViewById( id ).setOnClickListener( this );
    }

    @Override
    public void onClick( View v )
    {
        switch ( v.getId( ) )
        {
            case R.id.button_start:

                LinearLayout llMain = ( LinearLayout ) findViewById( R.id.ll_main );

                llMain.setVisibility( View.GONE );

                LinearLayout ll_gls = ( LinearLayout ) findViewById( R.id.ll_gls );

                if( mGLSurfaceView == null ) mGLSurfaceView = new GLSurfaceView ( this );

                if ( detectOpenGLES30( ) )
                {
                    mGLSurfaceView.setEGLContextClientVersion ( CONTEXT_CLIENT_VERSION );

                    mGLSurfaceView.setRenderer ( new GLRenderer ( this ) );
                }
                else
                {
                    Log.e("SimpleTexture2D", "OpenGL ES 3.0 not supported on device.  Exiting...");

                    finish();

                }

                ll_gls.addView( mGLSurfaceView );

                ll_gls.setVisibility( View.VISIBLE );

                break;
        }
    }

    private boolean detectOpenGLES30( )
    {
        ActivityManager am =
                ( ActivityManager ) getSystemService ( getApplicationContext( ).ACTIVITY_SERVICE );

        ConfigurationInfo info = am.getDeviceConfigurationInfo( );

        return ( info.reqGlEsVersion >= 0x30000 );
    }
}
