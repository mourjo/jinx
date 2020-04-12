(ns jinx.util
  (:require [cheshire.core :as cc]
            [clj-http.client :as http]))

(println "*** [SRC] Loading jinx.util ***")

(defn fetch
  "Make HTTP GET requests with backoff retries in case of errors."
  [url & {:keys [parser]
          :or {parser #(cc/parse-string % keyword)}}]
  (loop [i 0]
    (Thread/sleep (* 500 i))
    (let [[code result] (try
                          [::success
                           (-> url http/get :body parser)]
                          (catch Exception e
                            [::failure
                             e]))]
      (cond
        (= code ::success) result
        (< i 5) (recur (inc i))
        :else (throw result)))))
