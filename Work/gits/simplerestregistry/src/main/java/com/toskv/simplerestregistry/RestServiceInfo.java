/**
 * 
 */
package com.toskv.simplerestregistry;

import java.util.UUID;

public class RestServiceInfo {
	private String name;
	private String location;
	private UUID id;

	public RestServiceInfo() {
	}

	public RestServiceInfo(String name, String location, UUID id) {
		super();
		this.name = name;
		this.location = location;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

}
