package fr.paris.lutece.plugins.termofservice.web;

import java.util.Optional;
import java.util.function.BiPredicate;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.automaticroleprovider.service.AutomaticRoleConfiguration;
import fr.paris.lutece.plugins.automaticroleprovider.service.ConfigurationPredicate;
import fr.paris.lutece.plugins.termofservice.business.UserAccepted;
import fr.paris.lutece.plugins.termofservice.business.UserAcceptedHome;
import fr.paris.lutece.portal.service.security.LuteceUser;

public class TOSPredicate implements ConfigurationPredicate{

	BiPredicate<LuteceUser,AutomaticRoleConfiguration>  _biPredicate;
	
	public TOSPredicate() {
		 
		_biPredicate = (aUser, roleConfiguration) -> {
			Optional<UserAccepted> userAccept = UserAcceptedHome.findByGuid( aUser.getName( ) );
			if (userAccept.isPresent( ) )
			{
	    	  return true  ;
			}
			else 
			{
			  return false;
			}
	      };
	}

	@Override
	public BiPredicate<LuteceUser, AutomaticRoleConfiguration> getPredicate() {

		return _biPredicate;
	}

}
