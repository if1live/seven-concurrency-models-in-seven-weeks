(ns wordcount.http
  (:require [clojure.core.async :as async :refer :all :exclude [map into reduce merge partition partition-by take]]
            [org.httpkit.client :as http])
  (:import [java.net URL]))

(defn report-error [response]
  (println "Error" (:status response) "retrieving URL:" (get-in response [:opts :url])))

(defn http-get [url]
  (let [ch (chan)]
    (http/get url (fn [response]
                    (if (or (= 200 (:status response))
                            (= 304 (:status response)))
                      (put! ch response)
                      (do (report-error response) (close! ch)))))
  ch))

