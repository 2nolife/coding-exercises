package com.sky.dvdstore.impl;

import clojure.lang.Compiler;
import clojure.lang.RT;
import com.sky.dvdstore.Dvd;
import com.sky.dvdstore.DvdRepository;
import com.sky.dvdstore.DvdService;

import java.io.InputStreamReader;

public class DvdServiceImpl implements DvdService {

  private static final String NS = "dvd-service";


  public DvdServiceImpl(DvdRepository dvdRepository) throws Exception {
    Compiler.load(new InputStreamReader(getClass().getResourceAsStream("/"+NS.replaceAll("-", "_")+".clj"), "UTF-8"));
    RT.var("dvd-service", "init").invoke(dvdRepository);
  }


  public Dvd retrieveDvd(String dvdReference) throws Exception {
    return (Dvd) RT.var(NS, "retrieve-dvd").invoke(dvdReference);
  }


  public String getDvdSummary(String dvdReference) throws Exception {
    return (String) RT.var(NS, "get-dvd-summary").invoke(dvdReference);
  }
  
}
