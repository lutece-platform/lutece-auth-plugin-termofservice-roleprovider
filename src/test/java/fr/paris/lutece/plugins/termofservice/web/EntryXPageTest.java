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
 * SUBSTITUTE GOODS OR SERVICES LOSS OF USE, DATA, OR PROFITS OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */

package fr.paris.lutece.plugins.termofservice.web;

import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.test.LuteceTestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import java.io.IOException;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import java.util.List;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.web.LocalVariables;
import fr.paris.lutece.plugins.termofservice.business.Entry;
import fr.paris.lutece.plugins.termofservice.business.EntryHome;
/**
 * This is the business class test for the object Entry
 */
public class EntryXPageTest extends LuteceTestCase
{
    private static final String TEXT1 = "Text1";
    private static final String TEXT2 = "Text2";
	private static final boolean ACCEPTED1 = true;
    private static final boolean ACCEPTED2 = false;

public void testXPage(  ) throws AccessDeniedException, IOException
	{
        // Xpage create test
        MockHttpServletRequest request = new MockHttpServletRequest( );
		MockHttpServletResponse response = new MockHttpServletResponse( );
		MockServletConfig config = new MockServletConfig( );

		EntryXPage xpage = new EntryXPage( );
		assertNotNull( xpage.getCreateEntry( request ) );
		
		request = new MockHttpServletRequest();
		request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "createEntry" ));
		
		LocalVariables.setLocal(config, request, response);
		
		request.addParameter( "action" , "createEntry" );
        request.addParameter( "text" , TEXT1 );
        request.addParameter( "accepted" , String.valueOf( ACCEPTED1) );
		request.setMethod( "POST" );
		
		assertNotNull( xpage.doCreateEntry( request ) );
		
		
		//modify Entry	
		List<Integer> listIds = EntryHome.getIdEntrysList(); 

		assertTrue( !listIds.isEmpty( ) );

		request = new MockHttpServletRequest();
		request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );

		assertNotNull( xpage.getModifyEntry( request ) );

		response = new MockHttpServletResponse();
		request = new MockHttpServletRequest();
		LocalVariables.setLocal(config, request, response);
		
        request.addParameter( "text" , TEXT2 );
        request.addParameter( "accepted" , String.valueOf( ACCEPTED2) );

		request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "modifyEntry" ));
		request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
		
		assertNotNull( xpage.doModifyEntry( request ) );

		//do confirm remove Entry
		request = new MockHttpServletRequest();
		request.addParameter( "action" , "confirmRemoveEntry" );
		request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
		request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "confirmRemoveEntry" ));;
		request.setMethod("GET");

		try
		{
			xpage.getConfirmRemoveEntry( request );
		}
		catch(Exception e)
		{
			assertTrue(e instanceof SiteMessageException);
		}

		//do remove Entry
		response = new MockHttpServletResponse();
		request = new MockHttpServletRequest();
		LocalVariables.setLocal(config, request, response);
		request.addParameter( "action" , "removeEntry" );
		request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "removeEntry" ));
		request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
		assertNotNull( xpage.doRemoveEntry( request ) );

    }
    
}
