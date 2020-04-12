(ns jinx.core-test
  (:require [clojure.test :refer :all]
            [jinx.core :refer :all])
  (:import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
           com.fasterxml.jackson.dataformat.yaml.JacksonYAMLParseException))

(println "*** [TEST] Loading jinx.core-test ***")

(deftest ^:integration remote-timezones-test
  (println "Running remote-timezones-test")
  (are [tz] (contains? (remote-timezones) tz)
    "Africa/Johannesburg"
    "America/Argentina/Ushuaia"
    "America/New_York"
    "America/Los_Angeles"
    "Asia/Kolkata"
    "Europe/Paris"
    "Europe/Madrid"))


(deftest system-timezones-test
  (println "Running system-timezones-test")
  (are [tz] (contains? (system-timezones) tz)
    "Africa/Johannesburg"
    "America/Argentina/Ushuaia"
    "America/New_York"
    "America/Los_Angeles"
    "Asia/Kolkata"
    "Europe/Paris"
    "Europe/Madrid"))


(deftest ^:integration ^:flaky remote-system-time-difference-test
  (println "Running remote-system-time-difference-test")
  (are [tz] (= (remote-time tz) (system-time tz))
    "Africa/Johannesburg"
    "America/Argentina/Ushuaia"
    "America/New_York"
    "America/Los_Angeles"
    "Asia/Kolkata"
    "Europe/Paris"
    "Europe/Madrid"))


(defn valid-yaml?
  [s]
  (let [parser (.createParser (YAMLFactory.) s)]
    (try (while (.nextToken parser))
         true
         (catch JacksonYAMLParseException _
           false))))


(deftest ^:integration remote-api-info-test
  (println "Running remote-api-info-test")
  (is (not (valid-yaml? "saidfhaoif\n\t\t\t8324")))
  (is (valid-yaml? (remote-api-info))))


(deftest ^:auxiliary ^:integration estimate-remote-time-error-test
  (println "Running estimate-remote-time-error-test")
  (is (pos? (estimate-remote-time-error)))
  (is (pos? (estimate-remote-time-error 3)))
  (is (zero? (estimate-remote-time-error -4))))
