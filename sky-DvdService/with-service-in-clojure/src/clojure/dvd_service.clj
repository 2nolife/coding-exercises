(ns dvd-service
  (:import (com.sky.dvdstore InvalidDvdReferenceException DvdNotFoundException))
  (:use [clojure.contrib.except :only (throw-if)]))


(def dvd-repo)
(def max-summary 10)


(defn- non-nil
  "Return first not nil value or nil.
   (non-nil x y z (if a? (non-nil b c) d))"
  [& x] (first (filter #(not (nil? %)) x)))


(defn init
  [dvd-repo]
    (throw-if (nil? dvd-repo) IllegalStateException "DVD repository cannot be NULL")
    (intern 'dvd-service 'dvd-repo dvd-repo))


(defn retrieve-dvd
  [ref]
    (throw-if (not (and ref (.startsWith ref "DVD-"))) InvalidDvdReferenceException)
    (let [dvd (.retrieveDvd dvd-repo ref)]
      (throw-if (nil? dvd) DvdNotFoundException)
      dvd))


(defn get-dvd-summary
  [ref]
    (let [dvd (retrieve-dvd ref)
          tokens (.split (.getReview dvd) " ")
          total (if (> (alength tokens) max-summary) max-summary (alength tokens))
          summary (str (.getReference dvd) " " (.getTitle dvd) " -"
                    (apply str (map #(str " " (aget tokens %)) (range total))))
          len (.length summary) last-char (.substring summary (dec len))
          dot-summary (when (> (alength tokens) max-summary)
                        (str (if (= (.indexOf ",.)!?" last-char) -1)
                               summary
                               (.substring summary 0 (dec len))) "..."))]
      (non-nil dot-summary summary)))

