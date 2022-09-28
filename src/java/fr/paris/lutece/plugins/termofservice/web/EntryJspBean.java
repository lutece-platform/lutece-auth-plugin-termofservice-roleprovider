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

import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.url.UrlItem;
import fr.paris.lutece.util.html.AbstractPaginator;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import fr.paris.lutece.plugins.termofservice.business.Entry;
import fr.paris.lutece.plugins.termofservice.business.EntryHome;

/**
 * This class provides the user interface to manage Entry features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageEntrys.jsp", controllerPath = "jsp/admin/plugins/termofservice/", right = "TERMOFSERVICE_MANAGEMENT" )
public class EntryJspBean extends AbstractManageTOSJspBean <Integer, Entry>
{
    // Templates
    private static final String TEMPLATE_MANAGE_ENTRYS = "/admin/plugins/termofservice/manage_entrys.html";
    private static final String TEMPLATE_CREATE_ENTRY = "/admin/plugins/termofservice/create_entry.html";
    private static final String TEMPLATE_MODIFY_ENTRY = "/admin/plugins/termofservice/modify_entry.html";

    // Parameters
    private static final String PARAMETER_ID_ENTRY = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_ENTRYS = "termofservice.manage_entrys.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_ENTRY = "termofservice.modify_entry.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_ENTRY = "termofservice.create_entry.pageTitle";

    // Markers
    private static final String MARK_ENTRY_LIST = "entry_list";
    private static final String MARK_ENTRY = "entry";

    private static final String JSP_MANAGE_ENTRYS = "jsp/admin/plugins/termofservice/ManageEntrys.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_ENTRY = "termofservice.message.confirmRemoveEntry";

    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "termofservice.model.entity.entry.attribute.";

    // Views
    private static final String VIEW_MANAGE_ENTRYS = "manageEntrys";
    private static final String VIEW_CREATE_ENTRY = "createEntry";
    private static final String VIEW_MODIFY_ENTRY = "modifyEntry";

    // Actions
    private static final String ACTION_CREATE_ENTRY = "createEntry";
    private static final String ACTION_MODIFY_ENTRY = "modifyEntry";
    private static final String ACTION_REMOVE_ENTRY = "removeEntry";
    private static final String ACTION_CONFIRM_REMOVE_ENTRY = "confirmRemoveEntry";

    // Infos
    private static final String INFO_ENTRY_CREATED = "termofservice.info.entry.created";
    private static final String INFO_ENTRY_UPDATED = "termofservice.info.entry.updated";
    private static final String INFO_ENTRY_REMOVED = "termofservice.info.entry.removed";
    
    // Errors
    private static final String ERROR_RESOURCE_NOT_FOUND = "Resource not found";
    
    // Session variable to store working values
    private Entry _entry;
    private List<Integer> _listIdEntrys;
    
    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_ENTRYS, defaultView = true )
    public String getManageEntrys( HttpServletRequest request )
    {
        _entry = null;
        
        if ( request.getParameter( AbstractPaginator.PARAMETER_PAGE_INDEX) == null || _listIdEntrys.isEmpty( ) )
        {
        	_listIdEntrys = EntryHome.getIdEntrysList(  );
        }
        
        Map<String, Object> model = getPaginatedListModel( request, MARK_ENTRY_LIST, _listIdEntrys, JSP_MANAGE_ENTRYS );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_ENTRYS, TEMPLATE_MANAGE_ENTRYS, model );
    }

	/**
     * Get Items from Ids list
     * @param listIds
     * @return the populated list of items corresponding to the id List
     */
	@Override
	List<Entry> getItemsFromIds( List<Integer> listIds ) 
	{
		List<Entry> listEntry = EntryHome.getEntrysListByIds( listIds );
		
		// keep original order
        return listEntry.stream()
                 .sorted(Comparator.comparingInt( notif -> listIds.indexOf( notif.getId())))
                 .collect(Collectors.toList());
	}
    
    /**
    * reset the _listIdEntrys list
    */
    public void resetListId( )
    {
    	_listIdEntrys = new ArrayList<>( );
    }

    /**
     * Returns the form to create a entry
     *
     * @param request The Http request
     * @return the html code of the entry form
     */
    @View( VIEW_CREATE_ENTRY )
    public String getCreateEntry( HttpServletRequest request )
    {
        _entry = ( _entry != null ) ? _entry : new Entry(  );

        Map<String, Object> model = getModel(  );
        model.put( MARK_ENTRY, _entry );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_CREATE_ENTRY ) );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_ENTRY, TEMPLATE_CREATE_ENTRY, model );
    }

    /**
     * Process the data capture form of a new entry
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_CREATE_ENTRY )
    public String doCreateEntry( HttpServletRequest request ) throws AccessDeniedException
    {
        populate( _entry, request, getLocale( ) );
        

        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_CREATE_ENTRY ) )
        {
            throw new AccessDeniedException ( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _entry, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_ENTRY );
        }

        EntryHome.create( _entry );
        addInfo( INFO_ENTRY_CREATED, getLocale(  ) );
        resetListId( );

        return redirectView( request, VIEW_MANAGE_ENTRYS );
    }

    /**
     * Manages the removal form of a entry whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_ENTRY )
    public String getConfirmRemoveEntry( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ENTRY ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_ENTRY ) );
        url.addParameter( PARAMETER_ID_ENTRY, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_ENTRY, url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a entry
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage entrys
     */
    @Action( ACTION_REMOVE_ENTRY )
    public String doRemoveEntry( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ENTRY ) );
        
        
        EntryHome.remove( nId );
        addInfo( INFO_ENTRY_REMOVED, getLocale(  ) );
        resetListId( );

        return redirectView( request, VIEW_MANAGE_ENTRYS );
    }

    /**
     * Returns the form to update info about a entry
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_ENTRY )
    public String getModifyEntry( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ENTRY ) );

        if ( _entry == null || ( _entry.getId(  ) != nId ) )
        {
            Optional<Entry> optEntry = EntryHome.findByPrimaryKey( nId );
            _entry = optEntry.orElseThrow( ( ) -> new AppException(ERROR_RESOURCE_NOT_FOUND ) );
        }


        Map<String, Object> model = getModel(  );
        model.put( MARK_ENTRY, _entry );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_MODIFY_ENTRY ) );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_ENTRY, TEMPLATE_MODIFY_ENTRY, model );
    }

    /**
     * Process the change form of a entry
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_MODIFY_ENTRY )
    public String doModifyEntry( HttpServletRequest request ) throws AccessDeniedException
    {   
        populate( _entry, request, getLocale( ) );
		
		
        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_MODIFY_ENTRY ) )
        {
            throw new AccessDeniedException ( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _entry, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_ENTRY, PARAMETER_ID_ENTRY, _entry.getId( ) );
        }

        EntryHome.update( _entry );
        addInfo( INFO_ENTRY_UPDATED, getLocale(  ) );
        resetListId( );

        return redirectView( request, VIEW_MANAGE_ENTRYS );
    }
}
