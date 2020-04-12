(ns ^:auxiliary jinx.util-test
  (:require [jinx.util :refer :all]
            [clojure.string :as cstring]
            [clojure.test :refer :all]))

(println "*** [TEST] Loading jinx.util-test ***")

(defonce free-port
  (with-open [s (java.net.ServerSocket. 0)]
    (.getLocalPort s)))


(deftest ^:integration fetch-test
  (println "Running fetch-test")
  (is (map? (fetch "https://cat-fact.herokuapp.com/facts/random")))
  (is (string? (fetch "https://alexwohlbruck.github.io/cat-facts" :parser identity)))
  (is (thrown-with-msg? Exception
                        #"Connection refused"
                        (fetch (str "http://localhost:" free-port)))))
