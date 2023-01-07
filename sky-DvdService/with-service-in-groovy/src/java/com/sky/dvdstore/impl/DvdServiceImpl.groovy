/*
 * Copyright © 2006-2010. BSkyB Ltd All Rights reserved
 */

package com.sky.dvdstore.impl

import com.sky.dvdstore.*

class DvdServiceImpl implements DvdService {

  DvdRepository dvdRepository
  static final int MAX_SUMMARY = 10


  DvdServiceImpl(DvdRepository dvdRepository) {
    if (!dvdRepository) throw new IllegalStateException("DVD repository cannot be NULL")
    this.dvdRepository = dvdRepository
  }


  Dvd retrieveDvd(String dvdReference) {
    if (!dvdReference?.startsWith("DVD-")) throw new InvalidDvdReferenceException()

    Dvd dvd = dvdRepository.retrieveDvd(dvdReference)
    if (!dvd) throw new DvdNotFoundException()
    dvd
  }


  String getDvdSummary(String dvdReference) {
    def dvd = retrieveDvd(dvdReference)

    def sb = new StringBuilder("$dvd.reference $dvd.title -")
    def tokens = dvd.review.split(" ")
    (0..<(tokens.length > MAX_SUMMARY ? MAX_SUMMARY : tokens.length)).each { sb.append " ${tokens[it]}" }

    if (tokens.length > MAX_SUMMARY) {
      def len = sb.length()
      if (",.)!?".indexOf((int)sb.charAt(len-1)) != -1) sb.delete len-1, len
      sb.append "..."
    }

    sb
  }
}
