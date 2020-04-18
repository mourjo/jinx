(ns jinx.api.core-test
  (:require [clj-http.client :as http]
            [clojure.test :refer :all]
            [jinx.api.core :refer :all])
  (:import java.net.ServerSocket))

(println "*** [TEST] Loading jinx.api.core-test ***")

(deftest ^:integration start-stop-server-test
  (println "Running start-stop-server-test")
  (try
    (let [ss (ServerSocket. 0)
          port (.getLocalPort ss)]
      (.close ss)

      (start-server port)

      (is (= "Hello from Jinx!"
             (:body (http/get (str "http://localhost:" port))))
          "The server started with the default handler is accessible.")

      (stop-server)

      (is (thrown? Exception
                   (http/get (str "http://localhost:" port)))
          "After stopping the server, it is not accessible.")


      (start-server (constantly {:body "This is a new body"}) port)

      (is (= "This is a new body"
             (:body (http/get (format "http://localhost:%s/somepath/"
                                      port))))
          "The server started with a non-default handler is accessible."))

    (finally (stop-server))))
