(ns jinx.api.core
  (:require [ring.adapter.jetty :as jetty]))

(println "*** [SRC] Loading jinx.api.core ***")

(defonce server (atom nil))

(defn start-server
  ([port]
   (start-server (constantly {:body "Hello from Jinx!"})
                 port))
  ([handler port]
   (let [s (jetty/run-jetty handler
                            {:join? false
                             :port port})]
     (alter-var-root #'server (constantly s))
     s)))


(defn stop-server
  []
  (when server
    (.stop server)))
