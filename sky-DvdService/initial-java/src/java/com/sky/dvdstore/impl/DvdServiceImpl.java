package com.sky.dvdstore.impl;

import com.sky.dvdstore.*;

public class DvdServiceImpl implements DvdService {

  private DvdRepository dvdRepository;
  private static final int MAX_SUMMARY = 10;


  public DvdServiceImpl(DvdRepository dvdRepository) {
    if (dvdRepository == null) throw new IllegalStateException("DVD repository cannot be NULL");
    this.dvdRepository = dvdRepository;
  }


  public Dvd retrieveDvd(String dvdReference) throws DvdNotFoundException {
    if (dvdReference == null || !dvdReference.startsWith("DVD-"))
      throw new InvalidDvdReferenceException();

    Dvd dvd = dvdRepository.retrieveDvd(dvdReference);
    if (dvd == null) throw new DvdNotFoundException();
    return dvd;
  }


  public String getDvdSummary(String dvdReference) throws DvdNotFoundException {
    Dvd dvd = retrieveDvd(dvdReference);

    StringBuilder sb = new StringBuilder(dvd.getReference()).append(" ").append(dvd.getTitle()).append(" -");
    String[] tokens = dvd.getReview().split(" ");

    for (int z = 0; z < (tokens.length > MAX_SUMMARY ? MAX_SUMMARY : tokens.length); z++)
      sb.append(" ").append(tokens[z]);

    if (tokens.length > MAX_SUMMARY) {
      int len = sb.length();
      if (",.)!?".indexOf(sb.charAt(len-1)) != -1) sb.delete(len-1, len);
      sb.append("...");
    }

    return sb.toString();
  }
}
