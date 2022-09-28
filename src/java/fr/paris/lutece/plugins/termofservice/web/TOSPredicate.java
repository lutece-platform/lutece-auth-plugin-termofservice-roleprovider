package fr.paris.lutece.plugins.termofservice.web;

import java.util.function.BiPredicate;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.automaticroleprovider.service.AutomaticRoleConfiguration;
import fr.paris.lutece.plugins.automaticroleprovider.service.ConfigurationPredicate;
import fr.paris.lutece.portal.service.security.LuteceUser;

public class TOSPredicate implements ConfigurationPredicate{

	BiPredicate<LuteceUser,AutomaticRoleConfiguration>  _biPredicate;
	
	public TOSPredicate() {
		 
		_biPredicate = (aUser, roleConfiguration) -> {
	    	  return aUser.getName()!=null?true:false  ;
	      };
	}

	@Override
	public BiPredicate<LuteceUser, AutomaticRoleConfiguration> getPredicate() {

		return _biPredicate;
	}

}
