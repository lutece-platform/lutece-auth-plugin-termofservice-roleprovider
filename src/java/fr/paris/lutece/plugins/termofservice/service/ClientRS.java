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
package fr.paris.lutece.plugins.termofservice.service;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.paris.lutece.plugins.termofservice.business.UserAccepted;
import fr.paris.lutece.plugins.termofservice.rs.Constants;
import fr.paris.lutece.plugins.termofservice.rs.dto.UserDTO;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.httpaccess.HttpAccess;

public class ClientRS {
	
	private static final String NODE_RESULT = "result";
	
	public static UserDTO doGet( String url )
	{
		
		ObjectMapper mapper = new ObjectMapper( );
        mapper.disable( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES );
        mapper.enable( DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT );
        
		HttpAccess clientHttp = new HttpAccess( );
        Map<String, String> mapHeadersRequest = new HashMap<String, String>( );
        mapHeadersRequest.put( Constants.HEADER_SECURITY, AppPropertiesService.getProperty( Constants.PROPERTY_HEADER_SECURITY ) );

        try
        {
        	String reponse = clientHttp.doGet(url, null, null, mapHeadersRequest);
        	JsonNode node = mapper.readTree( reponse );
        	UserDTO user = mapper.readValue( node.get( NODE_RESULT ).toPrettyString(), UserDTO.class );
        	
        	return user;
        }
        catch( Exception e )
        {
        	AppLogService.error( "Error when calling doGet on " + url + " " + e );
        }

        return null;
	}
	
	public static UserDTO doPost( String url, UserAccepted userAccepted )
	{
		ObjectMapper mapper = new ObjectMapper( );
        mapper.disable( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES );
        
		
        Map<String, String> mapParams = new HashMap<>( );
        mapParams.put( Constants.USERACCEPTED_ATTRIBUTE_GUID, userAccepted.getGuid( ) );
        mapParams.put( Constants.USERACCEPTED_ATTRIBUTE_ID_ENTRY_TOS, String.valueOf( userAccepted.getFkIdEntry( ) ) );
        
        Map<String, String> mapHeadersRequest = new HashMap<String, String>( );
        mapHeadersRequest.put( HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded" );
        mapHeadersRequest.put( Constants.HEADER_SECURITY, AppPropertiesService.getProperty( Constants.PROPERTY_HEADER_SECURITY ) );
		
		HttpAccess clientHttp = new HttpAccess( );
		
		try
        {
        	String reponse = clientHttp.doPost(url, mapParams, null, null, mapHeadersRequest);
        	JsonNode node = mapper.readTree(reponse);
        	UserDTO user = mapper.readValue(node.get( NODE_RESULT ).toPrettyString(), UserDTO.class);
        	
        	return user;
        }
        catch( Exception e )
        {
            AppLogService.error( "Error when calling doPost on " + url + " " + e );
        }
		
		return null;
	}

}
