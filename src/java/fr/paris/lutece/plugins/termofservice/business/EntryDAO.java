/*
 * Copyright (c) 2002-2022, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */


package fr.paris.lutece.plugins.termofservice.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class provides Data Access methods for Entry objects
 */
public final class EntryDAO implements IEntryDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id_entry, text, version FROM termofservice_entry WHERE id_entry = ?";
    private static final String SQL_QUERY_SELECT_LAST_VERSION = "SELECT id_entry, text, version FROM termofservice_entry WHERE version = (select max( version ) from termofservice_entry ) ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO termofservice_entry ( text, version ) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM termofservice_entry WHERE id_entry = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE termofservice_entry SET id_entry = ?, text = ?, version = ? WHERE id_entry = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_entry, text, version FROM termofservice_entry";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_entry FROM termofservice_entry";
    private static final String SQL_QUERY_SELECTALL_BY_IDS = "SELECT id_entry, text, version FROM termofservice_entry WHERE id_entry IN (  ";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( Entry entry, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++ , entry.getText( ) );
            daoUtil.setInt( nIndex++ , entry.getVersion( ) );
            
            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) ) 
            {
                entry.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
        
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Optional<Entry> load( int nKey, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
	        daoUtil.setInt( 1 , nKey );
	        daoUtil.executeQuery( );
	        Entry entry = null;
	
	        if ( daoUtil.next( ) )
	        {
	            entry = new Entry();
	            int nIndex = 1;
	            
	            entry.setId( daoUtil.getInt( nIndex++ ) );
			    entry.setText( daoUtil.getString( nIndex++ ) );
			    entry.setVersion( daoUtil.getInt( nIndex ) );
	        }
	
	        return Optional.ofNullable( entry );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
	        daoUtil.setInt( 1 , nKey );
	        daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( Entry entry, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
	        int nIndex = 1;
	        
	        daoUtil.setInt( nIndex++ , entry.getId( ) );
            	daoUtil.setString( nIndex++ , entry.getText( ) );
            	daoUtil.setInt( nIndex++ , entry.getVersion( ) );
	        daoUtil.setInt( nIndex , entry.getId( ) );
	
	        daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Entry> selectEntrysList( Plugin plugin )
    {
        List<Entry> entryList = new ArrayList<>(  );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
	        daoUtil.executeQuery(  );
	
	        while ( daoUtil.next(  ) )
	        {
	            Entry entry = new Entry(  );
	            int nIndex = 1;
	            
	            entry.setId( daoUtil.getInt( nIndex++ ) );
			    entry.setText( daoUtil.getString( nIndex++ ) );
			    entry.setVersion( daoUtil.getInt( nIndex ) );
	
	            entryList.add( entry );
	        }
	
	        return entryList;
        }
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdEntrysList( Plugin plugin )
    {
        List<Integer> entryList = new ArrayList<>( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin ) )
        {
	        daoUtil.executeQuery(  );
	
	        while ( daoUtil.next(  ) )
	        {
	            entryList.add( daoUtil.getInt( 1 ) );
	        }
	
	        return entryList;
        }
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectEntrysReferenceList( Plugin plugin )
    {
        ReferenceList entryList = new ReferenceList();
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
	        daoUtil.executeQuery(  );
	
	        while ( daoUtil.next(  ) )
	        {
	            entryList.addItem( daoUtil.getInt( 1 ) , daoUtil.getString( 2 ) );
	        }
	
	        return entryList;
    	}
    }
    
    /**
     * {@inheritDoc }
     */
	@Override
	public List<Entry> selectEntrysListByIds( Plugin plugin, List<Integer> listIds ) {
		List<Entry> entryList = new ArrayList<>(  );
		
		StringBuilder builder = new StringBuilder( );

		if ( !listIds.isEmpty( ) )
		{
			for( int i = 0 ; i < listIds.size(); i++ ) {
			    builder.append( "?," );
			}
	
			String placeHolders =  builder.deleteCharAt( builder.length( ) -1 ).toString( );
			String stmt = SQL_QUERY_SELECTALL_BY_IDS + placeHolders + ")";
			
			
	        try ( DAOUtil daoUtil = new DAOUtil( stmt, plugin ) )
	        {
	        	int index = 1;
				for( Integer n : listIds ) {
					daoUtil.setInt(  index++, n ); 
				}
	        	
	        	daoUtil.executeQuery(  );
	        	while ( daoUtil.next(  ) )
		        {
		        	Entry entry = new Entry(  );
		            int nIndex = 1;
		            
		            entry.setId( daoUtil.getInt( nIndex++ ) );
				    entry.setText( daoUtil.getString( nIndex++ ) );
				    entry.setVersion( daoUtil.getInt( nIndex ) );
		            
		            entryList.add( entry );
		        }
		
		        daoUtil.free( );
		        
	        }
	    }
		return entryList;
		
	}

	@Override
	public Optional<Entry> loadLastVersion(Plugin _plugin) {
		try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_LAST_VERSION, _plugin ) )
        {
	        daoUtil.executeQuery( );
	        Entry entry = null;
	
	        if ( daoUtil.next( ) )
	        {
	            entry = new Entry();
	            int nIndex = 1;
	            
	            entry.setId( daoUtil.getInt( nIndex++ ) );
			    entry.setText( daoUtil.getString( nIndex++ ) );
			    entry.setVersion( daoUtil.getInt( nIndex ) );
	        }
	
	        return Optional.ofNullable( entry );
        }
	}
}
