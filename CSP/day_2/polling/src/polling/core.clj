(ns polling.core
  (:require [clojure.core.async :as async :refer :all
             :exclude [map into reduce merge partition partition-by take]]))

(defn poll-fn [interval action]
  (let [seconds (* interval 1000)]
    (go (while true
          (action)
          (<! (timeout seconds))))))

(defmacro poll [interval & body]
  `(let [seconds# (* ~interval 1000)]
    (go (while true
          (do ~@body)
          (<! (timeout seconds#))))))

(defn -main [& args]
  (println "main"))
