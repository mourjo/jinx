(ns jinx.core
  (:require [cheshire.core :as cc])
  (:import java.time.ZoneId
           java.time.LocalDateTime))


(defn fetch
  [url & {:keys [parser]
          :or {parser #(cc/parse-string % keyword)}}]
  (loop [i 0]
    (Thread/sleep (* 100 i))
    (let [[code result] (try
                        [::success
                         (-> url slurp parser)]
                        (catch Exception e
                          [::failure
                           e]))]
      (cond
        (= code ::success) result
        (< i 5) (recur (inc i))
        :else (throw result)))))


(defn remote-timezones
  []
  (set (fetch "https://worldtimeapi.org/api/timezone")))


(defn remote-api-info
  []
  (fetch "http://worldtimeapi.org/api/" :parser identity))


(defn system-timezones
  []
  (ZoneId/getAvailableZoneIds))


(defn remote-time
  [tz]
  (-> "https://worldtimeapi.org/api/timezone/"
      (str tz)
      fetch
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
