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
