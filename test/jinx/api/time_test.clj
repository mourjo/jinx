(ns jinx.api.time-test
  (:require [clj-http.client :as http]
            [cheshire.core :as cc]
            [clojure.test :refer :all]
            [jinx.api.time :refer :all]
            [jinx.core :as jc])
  (:import java.net.ServerSocket))

(println "*** [TEST] Loading jinx.api.time-test ***")

(deftest ^:integration ^:flaky time-server-test
  (println "Running time-server-test")
  (try
    (let [ss (ServerSocket. 0)
          port (.getLocalPort ss)]
      (.close ss)

      (start-time-server port)

      (is (= {:time (jc/system-time "Asia/Kolkata")}
             (-> "http://localhost:%s/time/"
                 (format port)
                 http/get
                 :body
                 (cc/parse-string keyword)))
          "System time and remote time are same.")

      (is (= 404
             (:status (http/get (format "http://localhost:%s/arbitrary/" port)
                                {:throw-exceptions false})))
          "Non-existent routes return 404 status.")

      (stop-time-server)

      (is (thrown? Exception
                   (http/get (str "http://localhost:" port)))
          "After stopping the server, it is not accessible."))

    (finally (stop-time-server))))
