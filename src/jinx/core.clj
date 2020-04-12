(ns jinx.core
  (:require [jinx.util :as ju])
  (:import java.time.ZoneId
           java.time.LocalDateTime))

(println "*** [SRC] Loading jinx.core ***")


(defn remote-timezones
  []
  (set (ju/fetch "https://worldtimeapi.org/api/timezone")))


(defn remote-api-info
  []
  (ju/fetch "http://worldtimeapi.org/api/" :parser identity))


(defn system-timezones
  []
  (ZoneId/getAvailableZoneIds))


(defn remote-time
  [tz]
  (-> "https://worldtimeapi.org/api/timezone/"
      (str tz)
      ju/fetch
      :datetime
      (subs 0 16)))


(defn system-time
  [tz]
  (-> tz
      ZoneId/of
      LocalDateTime/now
      str
      (subs 0 16)))


(defn estimate-remote-time-error
  ([]
   (estimate-remote-time-error 5))
  ([n]
   (let [rtts (for [tz (take n (shuffle (remote-timezones)))]
                (let [start (System/nanoTime)]
                  (try (remote-time tz)
                       (catch Exception _ 0))
                  (/ (double (- (System/nanoTime) start)) 1000000.0)))]
     (/ (double (/ (apply + rtts) n)) 2.0))))
