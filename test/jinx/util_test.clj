(ns ^:auxiliary jinx.util-test
  (:require [jinx.util :refer :all]
            [clojure.test :refer :all]))

(println "*** [TEST] Loading jinx.util-test ***")

(defonce free-port
  ;; select an open and therefore inaccessible port
  (with-open [s (java.net.ServerSocket. 0)]
    (.getLocalPort s)))


(deftest ^:integration fetch-test
  (println "Running fetch-test")
  (testing "Accessible HTTP APIs return correct parsed data, exceptions when inaccessible"
    (is (map? (fetch "https://cat-fact.herokuapp.com/facts/random")))
    (is (string? (fetch "https://alexwohlbruck.github.io/cat-facts" :parser identity)))
    (is (thrown-with-msg? Exception
                          #"Connection refused"
                          (fetch (str "http://localhost:" free-port))))))
