(ns jinx.core
  (:require [jinx.util :as ju])
  (:import java.time.ZoneId
           java.time.LocalDateTime))

(println "*** [SRC] Loading jinx.core ***")


(defn remote-timezones
  "Fetch list of timezones from worldtimeapi.org."
  []
  (set (ju/fetch "https://worldtimeapi.org/api/timezone")))


(defn remote-api-info
  "Fetch API information from worldtimeapi.org."
  []
  (ju/fetch "http://worldtimeapi.org/api/" :parser identity))


(defn system-timezones
  "Set of timezones supported by the JVM."
  []
  (ZoneId/getAvailableZoneIds))


(defn remote-time
  "Check the time in a given timezone as reported by worldtimeapi.org
  (precision upto minutes)."
  [tz]
  (-> "https://worldtimeapi.org/api/timezone/"
      (str tz)
      ju/fetch
      :datetime
      (subs 0 16)))


(defn system-time
  "Current system time (precision upto minutes)."
  [tz]
  (-> tz
      ZoneId/of
      LocalDateTime/now
      str
      (subs 0 16)))


(defn estimate-remote-time-error
  "Estimate the time spent in making network call to worldtimeapi.org."
  ([]
   (estimate-remote-time-error 5))
  ([n]
   (let [rtts (for [tz (take n (shuffle (remote-timezones)))]
                (let [start (System/nanoTime)]
                  (try (remote-time tz)
                       (catch Exception _ 0))
                  (/ (double (- (System/nanoTime) start)) 1000000.0)))]
     (/ (double (/ (apply + rtts) n)) 2.0))))
