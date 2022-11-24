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

/**
 * Rest Constants
 */
public final class Constants 
{
    public static final String API_PATH = "termofservice/api";
    public static final String VERSION_PATH = "/v{" + Constants.VERSION + "}";
    public static final String ID_PATH = "/{" + Constants.ID + "}";
    public static final String GUID_PATH = "/{" + Constants.USERACCEPTED_ATTRIBUTE_GUID + "}";
    public static final String VERSION = "version";
    public static final String ID = "id";
    
    public static final String HEADER_SECURITY = "headersecurity";
    public static final String PROPERTY_HEADER_SECURITY = "termofservice.headersecurity";
    
    public static final String SWAGGER_DIRECTORY_PATH = "/plugins/";
    public static final String SWAGGER_PATH = "/swagger";
    public static final String SWAGGER_VERSION_PATH = "/v";
    public static final String SWAGGER_REST_PATH = "rest/";
    public static final String SWAGGER_JSON = "/swagger.json";
    
    public static final String EMPTY_OBJECT = "{}";
    public static final String ERROR_NOT_FOUND_VERSION = "Version not found";
    public static final String ERROR_NOT_FOUND_RESOURCE = "Resource not found";
    public static final String ERROR_BAD_REQUEST_EMPTY_PARAMETER = "Empty parameter";
    public static final String ERROR_USER_ALREADY_ACCEPTED = "The user has already accepted the term of service";
    public static final String ERROR_UNAUTHORIZED = "Unauthorized use of the service";
    
    public static final String USERACCEPTED_PATH = "/useraccepteds";
    public static final String USERACCEPTED_ATTRIBUTE_GUID = "guid";
    public static final String USERACCEPTED_ATTRIBUTE_ID_ENTRY_TOS = "fk_id_entry";
    public static final String USERACCEPTED_ATTRIBUTE_DATE_ACCEPTED = "date_accepted";
    public static final String USERACCEPTED_ATTRIBUTE_VERSION = "version";
    
    //Properties
  	public static final String PROPERTY_USED_REMOTE = "termofservice.usedRemote";
  	public static final String PROPERTY_URL_TOS = "termofservice.urlexternaltos";
    
    /**
     * Private constructor
     */
    private Constants(  )
    {
    }
}
