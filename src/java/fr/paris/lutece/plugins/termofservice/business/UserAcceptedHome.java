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

import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.plugins.termofservice.rs.Constants;
import fr.paris.lutece.plugins.termofservice.rs.dto.UserDTO;
import fr.paris.lutece.plugins.termofservice.service.ClientRS;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

/**
 * This class provides instances management methods (create, find, ...) for UserAccepted objects
 */
public final class UserAcceptedHome
{
    // Static variable pointed at the DAO instance
    private static IUserAcceptedDAO _dao = SpringContextService.getBean( "termofservice.userAcceptedDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "termofservice" );
    
    private static final String PROPERTY_URL_TOS = "termofservice.urlexternaltos";

    /**
     * Private constructor - this class need not be instantiated
     */
    private UserAcceptedHome(  )
    {
    }

    /**
     * Create an instance of the userAccepted class
     * @param userAccepted The instance of the UserAccepted which contains the informations to store
     * @return The  instance of userAccepted which has been created with its primary key.
     */
    public static UserAccepted create( UserAccepted userAccepted )
    {
    	if ( AppPropertiesService.getProperty( PROPERTY_URL_TOS ) != null && !AppPropertiesService.getProperty( PROPERTY_URL_TOS ).isEmpty( ) )
    	{
	    	ClientRS.doPost( AppPropertiesService.getProperty( PROPERTY_URL_TOS ) + RestConstants.BASE_PATH + Constants.API_PATH + "/v1" + Constants.USERACCEPTED_PATH, userAccepted );	
    	}
    	else
    	{
    		createLocal( userAccepted );
    	}
        return userAccepted;
    }
    
    /**
     * Create an instance of the userAccepted class
     * @param userAccepted The instance of the UserAccepted which contains the informations to store
     * @return The  instance of userAccepted which has been created with its primary key.
     */
    public static UserAccepted createLocal( UserAccepted userAccepted )
    {
        _dao.insert( userAccepted, _plugin );
    	
        return userAccepted;
    }

    /**
     * Update of the userAccepted which is specified in parameter
     * @param userAccepted The instance of the UserAccepted which contains the data to store
     * @return The instance of the  userAccepted which has been updated
     */
    public static UserAccepted update( UserAccepted userAccepted )
    {
        _dao.store( userAccepted, _plugin );

        return userAccepted;
    }

    /**
     * Remove the userAccepted whose identifier is specified in parameter
     * @param nKey The userAccepted Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Returns an instance of a userAccepted whose identifier is specified in parameter
     * @param nKey The userAccepted primary key
     * @return an instance of UserAccepted
     */
    public static Optional<UserAccepted> findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Returns an instance of a userAccepted whose identifier is specified in parameter
     * @param strGuid The user GUID
     * @return an instance of UserAccepted
     */
    public static Optional<UserAccepted> findByGuid( String strGuid )
    {
    	Optional<UserAccepted> userAccept = _dao.loadByGuid( strGuid, _plugin );
    	if ( userAccept.isEmpty( ) && AppPropertiesService.getProperty( PROPERTY_URL_TOS ) != null && !AppPropertiesService.getProperty( PROPERTY_URL_TOS ).isEmpty( ) )
    	{
    		UserDTO user = ClientRS.doGet( AppPropertiesService.getProperty( PROPERTY_URL_TOS ) + RestConstants.BASE_PATH + Constants.API_PATH + "/v1" + Constants.USERACCEPTED_PATH +  "/" + strGuid);
    		if ( user != null )
    		{
    			UserAccepted useraccepted = new UserAccepted( );
    	    	useraccepted.setGuid( user.getGuid() );
    		    useraccepted.setFkIdEntry( user.getIdTermOfService());
    		    useraccepted.setDateAccepted( new Date( Calendar.getInstance( ).getTime( ).getTime( ) ) );
    	        UserAcceptedHome.createLocal( useraccepted );
    	        
    	        return Optional.of( useraccepted );
    		}
    	}
    	
    	return userAccept;
    }
    
    /**
     * Returns an instance of a userAccepted whose identifier is specified in parameter
     * @param strGuid The user GUID
     * @return an instance of UserAccepted
     */
    public static Optional<UserAccepted> findByGuidLocal( String strGuid )
    {
    	return _dao.loadByGuid( strGuid, _plugin );
    }
    
    /**
     * Load the data of all the userAccepted objects and returns them as a list
     * @return the list which contains the data of all the userAccepted objects
     */
    public static List<UserAccepted> getUserAcceptedsList( )
    {
        return _dao.selectUserAcceptedsList( _plugin );
    }
    
    /**
     * Load the id of all the userAccepted objects and returns them as a list
     * @return the list which contains the id of all the userAccepted objects
     */
    public static List<Integer> getIdUserAcceptedsList( )
    {
        return _dao.selectIdUserAcceptedsList( _plugin );
    }
    
    /**
     * Load the data of all the userAccepted objects and returns them as a referenceList
     * @return the referenceList which contains the data of all the userAccepted objects
     */
    public static ReferenceList getUserAcceptedsReferenceList( )
    {
        return _dao.selectUserAcceptedsReferenceList( _plugin );
    }
    
	
    /**
     * Load the data of all the avant objects and returns them as a list
     * @param listIds liste of ids
     * @return the list which contains the data of all the avant objects
     */
    public static List<UserAccepted> getUserAcceptedsListByIds( List<Integer> listIds )
    {
        return _dao.selectUserAcceptedsListByIds( _plugin, listIds );
    }

}

