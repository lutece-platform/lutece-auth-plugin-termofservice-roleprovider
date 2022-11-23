package fr.paris.lutece.plugins.termofservice.rs.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder( {
        "IdTermOfService", "guid", "id"
} )
public class UserDTO implements Serializable {
	
	private static final long serialVersionUID = 9019859058989234782L;
	private int nIdTermOfService;
	private String strGuid;
	private int nId;
    
	@JsonProperty( "IdTermOfService" )
    public int getIdTermOfService() {
		return nIdTermOfService;
	}
	@JsonProperty( "IdTermOfService" )
	public void setIdTermOfService(int nIdTermOfService) {
		this.nIdTermOfService = nIdTermOfService;
	}
	@JsonProperty( "guid" )
	public String getGuid() {
		return strGuid;
	}
	@JsonProperty( "guid" )
	public void setGuid(String strGuid) {
		this.strGuid = strGuid;
	}
	@JsonProperty( "id" )
    public int getId() {
		return nId;
	}
	@JsonProperty( "id" )
	public void setId(int nId) {
		this.nId = nId;
	}
	
	
    
    

}
