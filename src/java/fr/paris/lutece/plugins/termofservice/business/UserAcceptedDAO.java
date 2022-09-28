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
 * This class provides Data Access methods for UserAccepted objects
 */
public final class UserAcceptedDAO implements IUserAcceptedDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id_user_accepted, guid, fk_id_entry, date_accepted, version FROM termofservice_user_accepted WHERE id_user_accepted = ?";
    private static final String SQL_QUERY_SELECT_BY_GUID = "SELECT id_user_accepted, guid, fk_id_entry, date_accepted, version FROM termofservice_user_accepted WHERE guid = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO termofservice_user_accepted ( guid, fk_id_entry, date_accepted, version ) VALUES ( ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM termofservice_user_accepted WHERE id_user_accepted = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE termofservice_user_accepted SET id_user_accepted = ?, guid = ?, fk_id_entry = ?, date_accepted = ?, version = ? WHERE id_user_accepted = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_user_accepted, guid, fk_id_entry, date_accepted, version FROM termofservice_user_accepted";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_user_accepted FROM termofservice_user_accepted";
    private static final String SQL_QUERY_SELECTALL_BY_IDS = "SELECT id_user_accepted, guid, fk_id_entry, date_accepted, version FROM termofservice_user_accepted WHERE id_user_accepted IN (  ";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( UserAccepted userAccepted, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++ , userAccepted.getGuid( ) );
            daoUtil.setInt( nIndex++ , userAccepted.getFkIdEntry( ) );
            daoUtil.setDate( nIndex++ , userAccepted.getDateAccepted( ) );
            daoUtil.setInt( nIndex++ , userAccepted.getVersion( ) );
            
            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) ) 
            {
                userAccepted.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
        
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Optional<UserAccepted> load( int nKey, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
	        daoUtil.setInt( 1 , nKey );
	        daoUtil.executeQuery( );
	        UserAccepted userAccepted = null;
	
	        if ( daoUtil.next( ) )
	        {
	            userAccepted = new UserAccepted();
	            int nIndex = 1;
	            
	            userAccepted.setId( daoUtil.getInt( nIndex++ ) );
			    userAccepted.setGuid( daoUtil.getString( nIndex++ ) );
			    userAccepted.setFkIdEntry( daoUtil.getInt( nIndex++ ) );
			    userAccepted.setDateAccepted( daoUtil.getDate( nIndex++ ) );
			    userAccepted.setVersion( daoUtil.getInt( nIndex ) );
	        }
	
	        return Optional.ofNullable( userAccepted );
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
    public void store( UserAccepted userAccepted, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
	        int nIndex = 1;
	        
	        daoUtil.setInt( nIndex++ , userAccepted.getId( ) );
            	daoUtil.setString( nIndex++ , userAccepted.getGuid( ) );
            	daoUtil.setInt( nIndex++ , userAccepted.getFkIdEntry( ) );
            	daoUtil.setDate( nIndex++ , userAccepted.getDateAccepted( ) );
            	daoUtil.setInt( nIndex++ , userAccepted.getVersion( ) );
	        daoUtil.setInt( nIndex , userAccepted.getId( ) );
	
	        daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<UserAccepted> selectUserAcceptedsList( Plugin plugin )
    {
        List<UserAccepted> userAcceptedList = new ArrayList<>(  );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
	        daoUtil.executeQuery(  );
	
	        while ( daoUtil.next(  ) )
	        {
	            UserAccepted userAccepted = new UserAccepted(  );
	            int nIndex = 1;
	            
	            userAccepted.setId( daoUtil.getInt( nIndex++ ) );
			    userAccepted.setGuid( daoUtil.getString( nIndex++ ) );
			    userAccepted.setFkIdEntry( daoUtil.getInt( nIndex++ ) );
			    userAccepted.setDateAccepted( daoUtil.getDate( nIndex++ ) );
			    userAccepted.setVersion( daoUtil.getInt( nIndex ) );
	
	            userAcceptedList.add( userAccepted );
	        }
	
	        return userAcceptedList;
        }
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdUserAcceptedsList( Plugin plugin )
    {
        List<Integer> userAcceptedList = new ArrayList<>( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin ) )
        {
	        daoUtil.executeQuery(  );
	
	        while ( daoUtil.next(  ) )
	        {
	            userAcceptedList.add( daoUtil.getInt( 1 ) );
	        }
	
	        return userAcceptedList;
        }
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectUserAcceptedsReferenceList( Plugin plugin )
    {
        ReferenceList userAcceptedList = new ReferenceList();
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
	        daoUtil.executeQuery(  );
	
	        while ( daoUtil.next(  ) )
	        {
	            userAcceptedList.addItem( daoUtil.getInt( 1 ) , daoUtil.getString( 2 ) );
	        }
	
	        return userAcceptedList;
    	}
    }
    
    /**
     * {@inheritDoc }
     */
	@Override
	public List<UserAccepted> selectUserAcceptedsListByIds( Plugin plugin, List<Integer> listIds ) {
		List<UserAccepted> userAcceptedList = new ArrayList<>(  );
		
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
		        	UserAccepted userAccepted = new UserAccepted(  );
		            int nIndex = 1;
		            
		            userAccepted.setId( daoUtil.getInt( nIndex++ ) );
				    userAccepted.setGuid( daoUtil.getString( nIndex++ ) );
				    userAccepted.setFkIdEntry( daoUtil.getInt( nIndex++ ) );
				    userAccepted.setDateAccepted( daoUtil.getDate( nIndex++ ) );
				    userAccepted.setVersion( daoUtil.getInt( nIndex ) );
		            
		            userAcceptedList.add( userAccepted );
		        }
		
		        daoUtil.free( );
		        
	        }
	    }
		return userAcceptedList;
		
	}

	@Override
	public Optional<UserAccepted> loadByGuid(String strGuid, Plugin plugin) {
		try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_GUID, plugin ) )
        {
	        daoUtil.setString( 1 , strGuid );
	        daoUtil.executeQuery( );
	        UserAccepted userAccepted = null;
	
	        if ( daoUtil.next( ) )
	        {
	            userAccepted = new UserAccepted();
	            int nIndex = 1;
	            
	            userAccepted.setId( daoUtil.getInt( nIndex++ ) );
			    userAccepted.setGuid( daoUtil.getString( nIndex++ ) );
			    userAccepted.setFkIdEntry( daoUtil.getInt( nIndex++ ) );
			    userAccepted.setDateAccepted( daoUtil.getDate( nIndex++ ) );
			    userAccepted.setVersion( daoUtil.getInt( nIndex ) );
	        }
	
	        return Optional.ofNullable( userAccepted );
        }
	}
}
