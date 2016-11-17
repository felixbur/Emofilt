package com.felix.util;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

public class AndroidHelper {

	public static boolean isUsable;

	private static final Logger logger = Logger.getLogger( "AndroidHelper" );

	private static Class<?> ctxCls;
	private static Method mAssets;
	
	static {
		try {
			ctxCls = Class.forName( "android.content.Context" );
			mAssets = ctxCls.getMethod( "getAssets", (Class[])null );
			isUsable = true;
		}
		catch (ClassNotFoundException cnfe) {
			logger.fine( "This runtime is no dalvik - helper disabled" );
			isUsable = false;
		} catch (NoSuchMethodException e) {
			logger.fine( "Cannot get AssetManager from context class - helper disabled" );
			isUsable = false;
		}
	}
	
	public static InputStream getRessourceInputStream( Object context, String ressource ) {
		if ( ctxCls != null && ctxCls.isInstance( context ) ) {
			try {
				// what we wanne do is ...
				//android.content.Context ctx = (android.content.Context) context;
				//return ctx.getAssets().open( ressource );
				
				Object assetsCls = mAssets.invoke( context, (Object[])null );
				Method mOpen = assetsCls.getClass().getMethod( "open", String.class );
				return (InputStream)mOpen.invoke( assetsCls, ressource );
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (Exception e) {
				// may be an ioexception, but did not know it due to indeirect caling
				e.printStackTrace();
			}
		}
		return null; 
	}
}
