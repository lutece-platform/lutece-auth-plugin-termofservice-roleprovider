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

package fr.paris.lutece.plugins.termofservice.rs;

import fr.paris.lutece.plugins.termofservice.business.UserAccepted;
import fr.paris.lutece.plugins.termofservice.business.UserAcceptedHome;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.util.json.ErrorJsonResponse;
import fr.paris.lutece.util.json.JsonResponse;
import fr.paris.lutece.util.json.JsonUtil;

import org.apache.commons.lang3.StringUtils;
import fr.paris.lutece.portal.service.util.AppLogService;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * UserAcceptedRest
 */
@Path( RestConstants.BASE_PATH + Constants.API_PATH + Constants.VERSION_PATH + Constants.USERACCEPTED_PATH )
public class UserAcceptedRest
{
    private static final int VERSION_1 = 1;
    private static final String ATTRIBUTE_ID_USER = "id";
    private static final String ATTRIBUTE_GUID_USER = "guid";
    private static final String ATTRIBUTE_ID_TOS = "id terms of service";
    
    /**
     * Get UserAccepted 
     * @param guid the guid
     * @param nVersion the API version
     * @return the UserAccepted List
     */
    @GET
    @Path( Constants.GUID_PATH)
    @Produces( MediaType.APPLICATION_JSON )
    public Response getUserAccepted( 
    		@PathParam( Constants.VERSION ) Integer nVersion, 
    		@PathParam( Constants.USERACCEPTED_ATTRIBUTE_GUID ) String guid )
    {
        if ( nVersion == VERSION_1 )
        {
        	return getUserAccepted( guid );
        }
        AppLogService.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) )
                .build( );
    }
    
    /**
     * Get UserAccepted V1
     * @param guid the guid
     * @return the UserAccepted List for the version 1
     */
    private Response getUserAccepted( String guid )
    {
    	Optional<UserAccepted> userAccept = UserAcceptedHome.findByGuid( guid );
		if ( userAccept.isPresent( ) )
		{
			Map<String, Object> mapResponse = new HashMap<>( );
			mapResponse.put(ATTRIBUTE_ID_TOS, userAccept.get( ).getFkIdEntry( ) );
			mapResponse.put(ATTRIBUTE_GUID_USER, userAccept.get( ).getGuid( ) );
			mapResponse.put(ATTRIBUTE_ID_USER, userAccept.get( ).getId( ) );
			return Response.status( Response.Status.OK )
	                .entity( JsonUtil.buildJsonResponse( new JsonResponse( mapResponse ) ) )
	                .build( );
		}
		
		return Response.status( Response.Status.NO_CONTENT )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( userAccept ) ) )
                .build( );
    }
    
    /**
     * Create UserAccepted
     * @param nVersion the API version
     * @param guid the guid
     * @param id_entry_tos the fk_id_entry
     * @return the UserAccepted if created
     */
    @POST
    @Path( StringUtils.EMPTY )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createUserAccepted(
    @FormParam( Constants.USERACCEPTED_ATTRIBUTE_GUID ) String guid,
    @FormParam( Constants.USERACCEPTED_ATTRIBUTE_ID_ENTRY_TOS ) String id_entry_tos,
    @PathParam( Constants.VERSION ) Integer nVersion )
    {
		if ( nVersion == VERSION_1 )
		{
		    return createUserAcceptedV1( guid, id_entry_tos );
		}
        AppLogService.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) )
                .build( );
    }
    
    /**
     * Create UserAccepted V1
     * @param guid the guid
     * @param fk_id_entry the fk_id_entry
     * @return the UserAccepted if created for the version 1
     */
    private Response createUserAcceptedV1( String guid, String fk_id_entry )
    {
        if ( StringUtils.isEmpty( guid ) || StringUtils.isEmpty( fk_id_entry ) )
        {
            AppLogService.error( Constants.ERROR_BAD_REQUEST_EMPTY_PARAMETER );
            return Response.status( Response.Status.BAD_REQUEST )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.BAD_REQUEST.name( ), Constants.ERROR_BAD_REQUEST_EMPTY_PARAMETER ) ) )
                    .build( );
        }
        
        UserAccepted useraccepted = new UserAccepted( );
    	useraccepted.setGuid( guid );
	    useraccepted.setFkIdEntry( Integer.parseInt( fk_id_entry ) );
	    useraccepted.setDateAccepted( new Date( Calendar.getInstance( ).getTime( ).getTime( ) ) );
        UserAcceptedHome.create( useraccepted );
        
        return Response.status( Response.Status.OK )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( useraccepted ) ) )
                .build( );
    }
    
}