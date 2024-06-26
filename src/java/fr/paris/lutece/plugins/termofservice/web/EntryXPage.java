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
 
package fr.paris.lutece.plugins.termofservice.web;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.termofservice.business.Entry;
import fr.paris.lutece.plugins.termofservice.business.EntryHome;
import fr.paris.lutece.plugins.termofservice.business.UserAccepted;
import fr.paris.lutece.plugins.termofservice.business.UserAcceptedHome;
import fr.paris.lutece.plugins.termofservice.rs.Constants;
import fr.paris.lutece.plugins.verifybackurl.service.AuthorizedUrlService;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.xpages.XPage; 


/**
 * This class provides the user interface to manage Entry xpages ( manage, create, modify, remove )
 */
@Controller( xpageName = "entry" , pageTitleI18nKey = "termofservice.xpage.entry.pageTitle" , pagePathI18nKey = "termofservice.xpage.entry.pagePathLabel" )
public class EntryXPage extends MVCApplication
{
  
	
	// Templates
    private static final String TEMPLATE_MANAGE_TOS = "/skin/plugins/termofservice/manage_entrys.html";
    
    // Parameters
    private static final String PARAMETER_ID_ENTRY = "id";
    private static final String PARAMETER_ID_ACCEPTED = "accepted";
    
    // Markers
    private static final String MARK_ENTRY_LIST = "entry_list";
    private static final String MARK_ENTRY = "entry";
    private static final String MARK_BACK_URL = "back_url";
    
    // Views
    private static final String VIEW_MANAGE_TOS = "manageEntrys";

    // Actions
    private static final String ACTION_MODIFY_TOS = "modifyEntry";

    // Infos
    private static final String INFO_ENTRY_UPDATED = "termofservice.info.entry.updated";
    
    // Errors
    private static final String ERROR_RESOURCE_NOT_FOUND = "Resource not found";
    
    // Session variable to store working values
    private Entry _entry;
    
    /**
     * return the form to manage entrys
     * @param request The Http request
     * @return the html code of the list of entrys
     */
    @View( value = VIEW_MANAGE_TOS, defaultView = true )
    public XPage getManageTOS( HttpServletRequest request ) throws UserNotSignedException
    {
    	
    	
    	 
        _entry = ( _entry != null ) ? _entry : new Entry(  );
        List<Entry> listEntrys = EntryHome.getEntrysList(  );
        Optional<Entry> entryLastVersion = EntryHome.findByLastVersion( );
        
        Map<String, Object> model = getModel(  );
        model.put( MARK_ENTRY_LIST, listEntrys );
        if ( entryLastVersion.isPresent( ) )
        {
        	model.put( MARK_ENTRY, entryLastVersion.get( ) );
        }
        else
        {
        	model.put( MARK_ENTRY, null );
        }
        
        
        //check back url
        String strBackUrl = AuthorizedUrlService.getInstance().getServiceBackUrl(request );
        
        if ( !StringUtils.isEmpty( strBackUrl ) )
        {
            model.put ( MARK_BACK_URL, AuthorizedUrlService.getInstance().getServiceBackUrlEncoded(request) );
        
        }
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_MODIFY_TOS ) );
        

        LuteceUser luteceUser = SecurityService.getInstance( ).getRegisteredUser( request );
        if ( luteceUser == null)
        {
        	throw new UserNotSignedException( );
        }
        
        Optional<UserAccepted> userAccept = UserAcceptedHome.findByGuid( luteceUser.getName( ),AppPropertiesService.getPropertyBoolean(Constants.PROPERTY_USED_REMOTE, false) );
		if (userAccept.isPresent( ) )
		{
			  
	        if ( !StringUtils.isEmpty( strBackUrl ) )
	        {
	            return redirect(request, strBackUrl);
	        }
			
    	   return redirect(request, AppPathService.getBaseUrl(request));
		}

        
        return getXPage( TEMPLATE_MANAGE_TOS, getLocale( request ), model );
    }

    /**
     * Process the change form of a entry
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_MODIFY_TOS )
    public XPage doModifyTOS( HttpServletRequest request ) throws AccessDeniedException, UserNotSignedException
    {     
    	LuteceUser luteceUser = SecurityService.getInstance( ).getRegisteredUser( request );

    	if ( luteceUser == null)
        {
        	throw new UserNotSignedException( );
        }
    	
    	int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ENTRY ) );

        if ( _entry == null || ( _entry.getId(  ) != nId ) )
        {
            Optional<Entry> optEntry = EntryHome.findByPrimaryKey( nId );
            _entry = optEntry.orElseThrow( ( ) -> new AppException(ERROR_RESOURCE_NOT_FOUND ) );
        }
        
        boolean accepted = request.getParameter( PARAMETER_ID_ACCEPTED ) != null;
		

        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_MODIFY_TOS ) )
        {
            throw new AccessDeniedException ( "Invalid security token" );
        }
        
        if ( accepted ) 
        {
	        UserAccepted userAccepted = new UserAccepted( );
	        userAccepted.setGuid( luteceUser.getName( ) );
	        userAccepted.setFkIdEntry( nId );
	        userAccepted.setVersion( _entry.getVersion( ) );
	        userAccepted.setDateAccepted( new Date( Calendar.getInstance().getTime().getTime() ) );
	        
	        
	        
	        
	        UserAcceptedHome.create( userAccepted,AppPropertiesService.getPropertyBoolean(Constants.PROPERTY_USED_REMOTE, false) );
	        addInfo( INFO_ENTRY_UPDATED, getLocale( request ) );
	        //check back url
	        String strBackUrl = AuthorizedUrlService.getInstance().getServiceBackUrl(request );
	        
	        if ( !StringUtils.isEmpty( strBackUrl ) )
	        {
	            return redirect(request, strBackUrl);
	        }
	        	
	        	return redirect(request, AppPathService.getBaseUrl(request));
        }
        return redirectView( request, VIEW_MANAGE_TOS );
    }
}
