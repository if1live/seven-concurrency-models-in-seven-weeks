(ns wordcount.core
  (:require [clojure.core.async :as async :refer :all
             :exclude [map into reduce merge partition partition-by take]]
            [clojure.java.io :as io]
            [org.httpkit.client :as http]
            [wordcount.feed :refer :all]
            [wordcount.http :refer :all]
            [wordcount.words :refer :all]))

(defn handle-response [response]
  (let [url (get-in response [:opts :url])
        status (:status response)]
    (println "Fetched:" url "with status:" status)))

(defn get-counts [urls]
  (let [counts (chan)]
    (go (while true
        (let [url (<! urls)]
          (when-let [response (<! (http-get url))]
            (let [c (count (get-words (:body response)))]
              (>! counts [url c]))))))
    counts))

(defn -main [feeds-file]
  (with-open [rdr (io/reader feeds-file)]
    (let [feed-urls (line-seq rdr)
          article-urls (doall (map new-links feed-urls))
          article-counts (doall (map get-counts article-urls))
          counts (async/merge article-counts)]
      (while true
        (println (<!! counts))))))
