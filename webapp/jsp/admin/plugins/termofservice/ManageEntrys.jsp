<jsp:useBean id="managetosEntry" scope="session" class="fr.paris.lutece.plugins.termofservice.web.EntryJspBean" />
<% String strContent = managetosEntry.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
