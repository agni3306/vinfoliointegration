package com.vinfolio.Logger;

public class Logger {
	 static public String LEVEL_INFO = "info";
	    static public String LEVEL_DEBUG = "debug";

	    private String _level;

	    /**
	     * Constructor
	     *
	     * @param level - level to log
	     */
	    public Logger( String level )
	    {
	        _level = level;
	    }

	    /**
	     * Helper method that writes a string to the console
	     *
	     * @param msg - message to log
	     */
	    private void log( String msg )
	    {
	        System.out.println(msg);
	    }

	    /**
	     * Logs if level is LEVEL_INFO
	     *
	     * @param msg - message to log
	     */
	    public void info( String msg )
	    {
	        if ( _level == LEVEL_INFO || _level == LEVEL_DEBUG )
	        {
	            log( msg );
	        }
	    }

	    /**
	     * Logs if level is LEVEL_DEBUG
	     *
	     * @param msg - message to log
	     */
	    public void debug( String msg )
	    {
	        if ( _level == LEVEL_DEBUG )
	        {
	            log( "[Debug]: " + msg );
	        }
	    }

	    /**
	     * Logs warnings
	     *
	     * @param msg - message to log
	     */
	    public void warning( String msg )
	    {
	        log( "[Warning]: " + msg );
	    }

	    /**
	     * Logs an error message
	     *
	     * @param msg - message to log
	     */
	    public void error( String msg )
	    {
	        log( "[Error]: " + msg );
	    }

	    /**
	     * Logs an error message with a new line before the message
	     *
	     * @param msg - message to log
	     * @param isNewLine - flag for newline
	     */
	    public void error( String msg, boolean isNewLine )
	    {
	        if (isNewLine)
	            log( "\n[Error]: " + msg );
	        else
	            log( "[Error]: " + msg );
	    }


	    /**
	     * Logs an error for list operations
	     *
	     * @param msg
	     */
	    public void errorForRecord( String msg )
	    {
	        log( "    [Error]: " + msg );
	    }

	    /**
	     * Logs SOAP faults
	     *
	     * @param msg
	     */
	    public void fault( String msg )
	    {
	        log( "\n[SOAP Fault]: " + msg );
	    }

	    /**
	     * Writes to the console
	     *
	     * @param text
	     */
	    public void write( String text )
	    {
	        System.out.print( text );
	    }

	    /**
	     * Writes line to the console
	     *
	     * @param text
	     */
	    public void writeLn( String text )
	    {
	        System.out.println( text );
	    }
}
