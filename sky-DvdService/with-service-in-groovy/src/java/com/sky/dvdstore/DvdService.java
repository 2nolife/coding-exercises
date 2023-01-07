/*
 * Copyright © 2006-2010. BSkyB Ltd All Rights reserved
 */
package com.sky.dvdstore;

public interface DvdService {

	Dvd retrieveDvd(String dvdReference) throws DvdNotFoundException;

	String getDvdSummary(String dvdReference) throws DvdNotFoundException;
    
}
