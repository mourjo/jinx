(ns jinx.api.time
  (:require [cheshire.core :as cc]
            [compojure.core :as c]
            [compojure.route :as cr]
            [jinx.api.core :as jac]
            [jinx.core :as jc]))

(println "*** [SRC] Loading jinx.api.time ***")

(c/defroutes app
  (c/GET "/" []
         (cc/generate-string {:message "Welcome!"}))

  (c/GET "/time/" []
         (cc/generate-string
          {:time (jc/remote-time "Asia/Kolkata")}))

  (cr/not-found
   (cc/generate-string
    {:message "Page not found."})))


(defn start-time-server
  [port]
  (jac/start-server app port))


(defn stop-time-server
  []
  (jac/stop-server))
